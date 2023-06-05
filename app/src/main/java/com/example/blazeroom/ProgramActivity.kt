package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.marginEnd
import java.sql.DriverManager
import kotlin.concurrent.thread

class ProgramActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_program)

        var dataPhoto = ""
        var dataName = ""
        var count = 0

        val listProgram = connectProgram()
        cleanTable()
        for(program in listProgram){
            if (count != 0){
                addRow(dataPhoto, dataName, program.photo, program.name)
                dataPhoto = program.photo
                dataName = program.name
                count = 0
            }
            else{
                count ++
                dataPhoto = program.photo
                dataName = program.name
            }
        }
    }

    fun connectProgram() :MutableList<dataclassProgram>{ //Функция записи результатов функции подключения к БД
        val listExercise: MutableList<dataclassProgram> = mutableListOf() //создание списка
        thread { //Создание потока
            try { //попробовать
                Class.forName("com.mysql.jdbc.Driver")
                val connection = DriverManager.getConnection(
                    "jdbc:mysql://141.8.195.65:3306/a0774975_blazeroom",
                    "a0774975_blazeroom",
                    "87052547870Jeka"
                )
                val search = "SELECT * FROM Programms" //Составленеи запроса к БД к определенной таблице
                val searchQuery = connection.prepareStatement(search+";")  // выборка всей таблицы
                val result = searchQuery.executeQuery() // Запись выборки БД в переменную result

                while (result.next()) { //цикл пока есть записи в result (построчно)
                    val id: Int = result.getInt(1)
                    val name: String = result.getString(2)
                    val photo: String = result.getString(3)
                    val exercise1: String = result.getString(4)
                    val repeat1: String = result.getString(5)
                    val exercise2: String = result.getString(6)
                    val repeat2: String = result.getString(7)
                    val exercise3: String = result.getString(8)
                    val repeat3: String = result.getString(9)
                    val exercise4: String = result.getString(10)
                    val repeat4: String = result.getString(11)
                    val description: String = result.getString(12)
                    val stringProgramm = dataclassProgram(id, name, photo, exercise1, repeat1, exercise2, repeat2, exercise3, repeat3, exercise4, repeat4, description) //Добавление переменной типа DataClassRegistr с значениями из result
                    listExercise.add(stringProgramm) //Добавление переменной в список
                }
                connection.close()
            } catch (e: Exception) { //Если не получилось то отлавливаем ошибки и выводим их в консоль
                print(e.message)
            }
        }.join() //Отвечает за приостановку основного потока до выполнения этого
        return listExercise //Вернуть список с значениями
    }

    fun cleanTable() {
        val tableLayout = findViewById<TableLayout>(R.id.TablePrograms)
        val childCount = tableLayout.childCount
        if (childCount > 1) {
            tableLayout.removeViews(1, childCount - 1)
        }
    }

    fun addRow(photo: String, name: String, photo2: String, name2: String) {
        val tableLayout = findViewById<TableLayout>(R.id.TablePrograms) //Обозначаем Tablelayout для использования
        val tableRow = TableRow(this) //Добавление переменной типа TableRow, отвечает за целую строку
        tableRow.layoutParams = TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT)
        (tableRow.layoutParams as TableLayout.LayoutParams).apply {
            topMargin=45
        }

        val linear1 = LinearLayout(this) //Добавление переменной типа TableRow, отвечает за целую строку
        linear1.orientation = LinearLayout.VERTICAL
        linear1.layoutParams = TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
        (linear1.layoutParams as TableRow.LayoutParams).apply {
            column = 0
            marginStart=20
            topMargin=5
            marginEnd=20
            bottomMargin=5
            weight = 1f
        }

        val textView1 = TextView(this) //создание следующего textview, аналогично первому
        textView1.id = View.generateViewId()
        textView1.setText(name)
        textView1.setTextSize(15F)
        textView1.setTextColor(Color.parseColor("#FFFFFF"))
        textView1.setTypeface(ResourcesCompat.getFont(this.applicationContext, R.font.gilroy_bold))
        textView1.gravity = Gravity.CENTER
        textView1.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)


        val imageView1 = ImageView(this)//Добавление переменной типа TextView, находится в TableRow и отвечает ха ячейку
        imageView1.setImageResource(resources.getIdentifier("$photo", "drawable", getPackageName()))
        imageView1.layoutParams = LinearLayout.LayoutParams(400, 400)
        imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP)
        (imageView1.layoutParams as LinearLayout.LayoutParams).apply {
            gravity = Gravity.CENTER
            topMargin = 30
            bottomMargin = 30
        }

        val button1 = Button(this) //создание следующего textview, аналогично первому
        button1.setText("ПОСМОТРЕТЬ")
        button1.gravity = Gravity.CENTER
        button1.setBackgroundResource(R.drawable.fonbutton2)
        button1.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        button1.setOnClickListener {
            val textViewProgramm = findViewById<TextView>(linear1.getChildAt(0).id)
            val nameProgram = textViewProgramm.text.toString()
            goToOneProgramm(nameProgram)
        }

        val linear2 = LinearLayout(this) //Добавление переменной типа TableRow, отвечает за целую строку
        linear2.orientation = LinearLayout.VERTICAL
        linear2.layoutParams = TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
        (linear2.layoutParams as TableRow.LayoutParams).apply {
            column = 0
            marginStart=5
            topMargin=5
            marginEnd=5
            bottomMargin=10
            weight = 1f
        }

        val textView2 = TextView(this) //создание следующего textview, аналогично первому
        textView2.id = View.generateViewId()
        textView2.setText(name2)
        textView2.setTextSize(15F)
        textView2.setTextColor(Color.parseColor("#FFFFFF"))
        textView2.setTypeface(ResourcesCompat.getFont(this.applicationContext, R.font.gilroy_bold))
        textView2.gravity = Gravity.CENTER
        textView2.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)


        val imageView2 = ImageView(this)//Добавление переменной типа TextView, находится в TableRow и отвечает ха ячейку
        imageView2.setImageResource(resources.getIdentifier("$photo2", "drawable", getPackageName()))
        imageView2.layoutParams = LinearLayout.LayoutParams(400, 400)
        imageView2.setScaleType(ImageView.ScaleType.CENTER_CROP)
        (imageView2.layoutParams as LinearLayout.LayoutParams).apply {
            gravity = Gravity.CENTER
            topMargin = 30
            bottomMargin = 30
        }

        val button2 = Button(this) //создание следующего textview, аналогично первому
        button2.setText("ПОСМОТРЕТЬ")
        button2.gravity = Gravity.CENTER
        button2.setBackgroundResource(R.drawable.fonbutton2)
        button2.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        button2.setOnClickListener {
            val textViewProgramm = findViewById<TextView>(linear2.getChildAt(0).id)
            val nameProgram = textViewProgramm.text.toString()
            goToOneProgramm(nameProgram)
        }

        linear2.addView(textView2)
        linear2.addView(imageView2)
        linear2.addView(button2)

        linear1.addView(textView1)
        linear1.addView(imageView1)
        linear1.addView(button1)
        tableRow.addView(linear1)
        tableRow.addView(linear2)
        tableLayout.addView(tableRow) //Добавление tableRow в TableLayout
    }

    fun goToOneProgramm(nameProgram: String){
        val transitionIntent = Intent(this, ProgramFastfeetActivity::class.java)
        transitionIntent.putExtra("nameProgram", nameProgram)
        startActivity(transitionIntent)
    }






}


