package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.myapplication.dataClasses.Exercise
import java.sql.Blob
import java.sql.DriverManager
import kotlin.concurrent.thread

class ExercisesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercises)

        val spinner = findViewById<Spinner>(R.id.skillsSpinner) //Обозначаем выпадающий список для использования
        val mList = arrayOf<String?>("Все","Ловкость", "Реакция", "Координация", "Сила", "Чувство мяча", "Скорость")
        val mArrayAdapter = ArrayAdapter<Any?>(this, R.layout.spinnerpick, mList)
        mArrayAdapter.setDropDownViewResource(R.layout.spinnerpick)
        spinner.adapter = mArrayAdapter

        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener { // если изменяется выбранное

            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) { //то
                cleanTable() //функция очистки таблицы
                val listExercise: MutableList<Exercise> = connectExercise() //Запуск функции подключения к БД и таблицы price, который возвращает лист с данными из таблицы
                for (exercise in listExercise) //Цикл перебора списка с данными
                {// для каждой записи таблицы запускается функция добавления строки таблицы и загрузки в нее данных
                    if (parentView?.getItemAtPosition(position).toString() == "Все" ){
                        addRow(exercise.photo, exercise.name, exercise.skill)
                    }
                    else  if (exercise.skill.contains(parentView?.getItemAtPosition(position).toString())) {
                        addRow(exercise.photo, exercise.name, exercise.skill)
                    }
                }
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {//хз
                return
            }
        })
    }

    fun goToExerciseops(nameExercises: String){
        val transitionIntent = Intent(this, ExerciseopsActivity::class.java)
        transitionIntent.putExtra("nameExercises", nameExercises)
        startActivity(transitionIntent)
    }

    fun connectExercise() :MutableList<Exercise>{ //Функция записи результатов функции подключения к БД
        val listExercise: MutableList<Exercise> = mutableListOf() //создание списка
        thread { //Создание потока
            try { //попробовать
                Class.forName("com.mysql.jdbc.Driver")
                val connection = DriverManager.getConnection(
                    "jdbc:mysql://141.8.195.65:3306/a0774975_blazeroom",
                    "a0774975_blazeroom",
                    "87052547870Jeka"
                )
                val search = "SELECT * FROM Exercises " //Составленеи запроса к БД к определенной таблице
                val searchQuery = connection.prepareStatement(search+";")  // выборка всей таблицы
                val result = searchQuery.executeQuery() // Запись выборки БД в переменную result

                while (result.next()) { //цикл пока есть записи в result (построчно)
                    val id: Int = result.getInt(1) // запис"ь значения id в переменную
                    val name: String = result.getString(2) // запись значения даты в переменную
                    val photo: String = result.getString(3) // запись значения времени в переменнуюe)
                    val skill: String = result.getString(4) // запись значения фио клиента в переменную
                    val blaze: Int = result.getInt(5) // запись значения стоимости в переменную
                    val description: String = result.getString(6) // запись значения фио мастера в переменную
                    val programs: String = result.getString(7) // запись значения услуги в переменную
                    val stringExercise = Exercise(id, name, photo, skill, blaze, description, programs) //Добавление переменной типа DataClassRegistr с значениями из result
                    listExercise.add(stringExercise) //Добавление переменной в список
                }
                connection.close()
            } catch (e: Exception) { //Если не получилось то отлавливаем ошибки и выводим их в консоль
                print(e.message)
            }
        }.join() //Отвечает за приостановку основного потока до выполнения этого
        return listExercise //Вернуть список с значениями
    }

    fun cleanTable() {
        val tableLayout = findViewById<TableLayout>(R.id.tableExercises)
        val childCount = tableLayout.childCount
        if (childCount > 1) {
            tableLayout.removeViews(0, childCount )
        }
    }

    @SuppressLint("SetTextI18n")
    fun addRow(photo: String, name: String, skill: String) {
        val cl = findViewById<ScrollView>(R.id.clExs) //!/!/!/!/!/!/!/!/!/!/!/!/!/!/!/!/!/!/!/!//!/!/!//!/!/!/!!/!/!/!/!//!/1!/!//!/!
        val tableLayout = findViewById<TableLayout>(R.id.tableExercises) //Обозначаем Tablelayout для использования
        val tableRow = TableRow(this) //Добавление переменной типа TableRow, отвечает за целую строку
        tableRow.layoutParams = TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT)

        val imageView1 = ImageView(this)//Добавление переменной типа TextView, находится в TableRow и отвечает ха ячейку
        imageView1.setImageResource(resources.getIdentifier("$photo", "drawable", getPackageName()))
        imageView1.scaleType = ImageView.ScaleType.FIT_XY
        imageView1.layoutParams = TableRow.LayoutParams(cl.height/6, cl.height/6)
        (imageView1.layoutParams as TableRow.LayoutParams).apply {
            column = 0
            gravity = Gravity.CENTER
            topMargin = 20
        }

        val textView2 = TextView(this) //создание следующего textview, аналогично первому
        textView2.id = View.generateViewId()
        textView2.setText(name)
        textView2.setTextSize(15F)
        textView2.setTextColor(Color.parseColor("#FFFFFF"))
        textView2.setTypeface(ResourcesCompat.getFont(this.applicationContext, R.font.gilroy_bold))
        textView2.gravity = Gravity.CENTER
        textView2.layoutParams = TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
        (textView2.layoutParams as TableRow.LayoutParams).apply {
            column = 1
            marginStart=4
            topMargin=4
            marginEnd=4
            bottomMargin=4
        }

        val textView3 = TextView(this) //создание следующего textview, аналогично первому
        val list: List<String> = listOf(*skill.split(",").toTypedArray())
        var count = 0
        for (i in list){
            println(list)
            if (count == 0){
                textView3.setText(i)
                count ++
            }else{
                textView3.setText(textView3.text.toString() + "\n" + i)
            }
        }
        textView3.setTextSize(15F)
        textView3.setTextColor(Color.parseColor("#FFFFFF"))
        textView3.setTypeface(ResourcesCompat.getFont(this.applicationContext, R.font.gilroy_bold))
        textView3.gravity = Gravity.CENTER
        textView3.layoutParams = TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
        (textView3.layoutParams as TableRow.LayoutParams).apply {
            column = 2
            marginStart=4
            topMargin=4
            marginEnd=4
            bottomMargin=4
        }

        tableRow.addView(imageView1) //Добавление textView1 в tableRow
        tableRow.addView(textView2) //Добавление textView2 в tableRow
        tableRow.addView(textView3) //Добавление textView1 в tableRow

        tableRow.setOnClickListener {
            val textViewEx = findViewById<TextView>(tableRow.getChildAt(1).id)
            val nameExercises = textViewEx.text.toString()
            goToExerciseops(nameExercises)
        }

        tableLayout.addView(tableRow) //Добавление tableRow в TableLayout
    }


}