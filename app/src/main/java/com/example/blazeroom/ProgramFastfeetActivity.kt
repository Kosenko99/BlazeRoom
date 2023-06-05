package com.example.myapplication

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import java.sql.DriverManager
import kotlin.concurrent.thread

class ProgramFastfeetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_program_fastfeet)

        val textViewProgramFastfeetTitle = findViewById<TextView>(R.id.textViewProgramFastfeetTitle)
        val imageViewProgramFastfeet = findViewById<ImageView>(R.id.imageViewProgramFastfeet)
        val textViewProgramFastfeetDescriptionText = findViewById<TextView>(R.id.textViewProgramFastfeetDescriptionText)

        val listProgram = connectProgramm()
        for (program in listProgram){
            //!//!//!//!//!//!//!//!//!//!//!//!//!//!//!//!//!//!//
            imageViewProgramFastfeet.setImageResource(resources.getIdentifier("${program.photo}", "drawable", getPackageName()))
            textViewProgramFastfeetTitle.text = program.name
            textViewProgramFastfeetDescriptionText.text = program.descriptor

            addRow(program.exercise1, program.repeat1, program.exercise2, program.repeat2, program.exercise3, program.repeat3, program.exercise4, program.repeat4)
        }
    }

    fun addRow(exercise1: String, repeat1: String, exercise2: String, repeat2: String, exercise3: String, repeat3: String, exercise4: String, repeat4: String) {
        val tableLayout = findViewById<TableLayout>(R.id.tableProgramFastfeet) //Обозначаем Tablelayout для использования

        if (exercise1 !="" || repeat1 != "") {
            val tableRow =
                TableRow(this) //Добавление переменной типа TableRow, отвечает за целую строку
            tableRow.setBackgroundDrawable(getResources().getDrawable(R.drawable.tablerowstyle)) //Установка фона в новый TextView
            tableRow.layoutParams = TableLayout.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT
            )
            (tableRow.layoutParams as TableLayout.LayoutParams).apply { //Блок настройки отображения виджета
                marginStart = 4 // отступ слева
                topMargin = 4 // отступ сверху
                marginEnd = 4 // отступ справа
                bottomMargin = 4 // отступ снизу
            }

            val textView1 =
                TextView(this)//Добавление переменной типа TextView, находится в TableRow и отвечает ха ячейку
            textView1.setText(exercise1) //установка текста в новый TextView
            textView1.setTextSize(15F) // установка размера текста в новый TextView
            textView1.setTextColor(Color.parseColor("#031DA7"))
            textView1.setTypeface(
                ResourcesCompat.getFont(
                    this.applicationContext,
                    R.font.gilroy_bold
                )
            )
            textView1.gravity = Gravity.CENTER // привязка по центру для нового TextView
            textView1.layoutParams = TableRow.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            (textView1.layoutParams as TableRow.LayoutParams).apply { //Блок настройки отображения виджета
                column = 0 // столбец в котором будет находиться новый TextView
                marginStart = 4 // отступ слева
                topMargin = 4 // отступ сверху
                marginEnd = 4 // отступ справа
                weight = 2f
                bottomMargin = 4 // отступ снизу
            }

            val textView2 = TextView(this) //создание следующего textview, аналогично первому
            textView2.setText(repeat1)
            textView2.setTextSize(15F)
            textView2.setTextColor(Color.parseColor("#031DA7"))
            textView2.setTypeface(
                ResourcesCompat.getFont(
                    this.applicationContext,
                    R.font.gilroy_bold
                )
            )
            textView2.gravity = Gravity.CENTER
            textView2.layoutParams = TableRow.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            (textView2.layoutParams as TableRow.LayoutParams).apply {
                column = 1
                weight = 1f
                marginStart = 4
                topMargin = 4
                marginEnd = 4
                bottomMargin = 4
            }

            tableRow.addView(textView1) //Добавление textView1 в tableRow
            tableRow.addView(textView2) //Добавление textView2 в tableRow
            tableLayout?.addView(tableRow) //Добавление tableRow в TableLayout
        }

        if (exercise2 !="" || repeat2 != "") {
            val tableRow2 =
                TableRow(this) //Добавление переменной типа TableRow, отвечает за целую строку
            tableRow2.setBackgroundDrawable(getResources().getDrawable(R.drawable.tablerowstyle)) //Установка фона в новый TextView
            tableRow2.layoutParams = TableLayout.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT
            )
            (tableRow2.layoutParams as TableLayout.LayoutParams).apply { //Блок настройки отображения виджета
                marginStart = 4 // отступ слева
                topMargin = 4 // отступ сверху
                marginEnd = 4 // отступ справа
                bottomMargin = 4 // отступ снизу
            }

            val textView12 =
                TextView(this)//Добавление переменной типа TextView, находится в TableRow и отвечает ха ячейку
            textView12.setText(exercise2) //установка текста в новый TextView
            textView12.setTextSize(15F) // установка размера текста в новый TextView
            textView12.setTextColor(Color.parseColor("#031DA7"))
            textView12.setTypeface(
                ResourcesCompat.getFont(
                    this.applicationContext,
                    R.font.gilroy_bold
                )
            )
            textView12.gravity = Gravity.CENTER // привязка по центру для нового TextView
            textView12.layoutParams = TableRow.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            (textView12.layoutParams as TableRow.LayoutParams).apply { //Блок настройки отображения виджета
                column = 0 // столбец в котором будет находиться новый TextView
                marginStart = 4 // отступ слева
                weight = 2f
                topMargin = 4 // отступ сверху
                marginEnd = 4 // отступ справа
                bottomMargin = 4 // отступ снизу
            }

            val textView22 = TextView(this) //создание следующего textview, аналогично первому
            textView22.setText(repeat2)
            textView22.setTextSize(15F)
            textView22.setTextColor(Color.parseColor("#031DA7"))
            textView22.setTypeface(
                ResourcesCompat.getFont(
                    this.applicationContext,
                    R.font.gilroy_bold
                )
            )
            textView22.gravity = Gravity.CENTER
            textView22.layoutParams = TableRow.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            (textView22.layoutParams as TableRow.LayoutParams).apply {
                column = 1
                marginStart = 4
                topMargin = 4
                weight = 1f
                marginEnd = 4
                bottomMargin = 4
            }

            tableRow2.addView(textView12) //Добавление textView1 в tableRow
            tableRow2.addView(textView22) //Добавление textView2 в tableRow
            tableLayout?.addView(tableRow2) //Добавление tableRow в TableLayout
        }

        if (exercise3 !="" || repeat3 != "") {
            val tableRow3 =
                TableRow(this) //Добавление переменной типа TableRow, отвечает за целую строку
            tableRow3.setBackgroundDrawable(getResources().getDrawable(R.drawable.tablerowstyle)) //Установка фона в новый TextView
            tableRow3.layoutParams = TableLayout.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT
            )
            (tableRow3.layoutParams as TableLayout.LayoutParams).apply { //Блок настройки отображения виджета
                marginStart = 4 // отступ слева
                topMargin = 4 // отступ сверху
                marginEnd = 4 // отступ справа
                bottomMargin = 4 // отступ снизу
            }

            val textView13 =
                TextView(this)//Добавление переменной типа TextView, находится в TableRow и отвечает ха ячейку
            textView13.setText(exercise3) //установка текста в новый TextView
            textView13.setTextSize(15F) // установка размера текста в новый TextView
            textView13.setTextColor(Color.parseColor("#031DA7"))
            textView13.setTypeface(
                ResourcesCompat.getFont(
                    this.applicationContext,
                    R.font.gilroy_bold
                )
            )
            textView13.gravity = Gravity.CENTER // привязка по центру для нового TextView
            textView13.layoutParams = TableRow.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            (textView13.layoutParams as TableRow.LayoutParams).apply { //Блок настройки отображения виджета
                column = 0 // столбец в котором будет находиться новый TextView
                weight = 2f
                marginStart = 4 // отступ слева
                topMargin = 4 // отступ сверху
                marginEnd = 4 // отступ справа
                bottomMargin = 4 // отступ снизу
            }

            val textView23 = TextView(this) //создание следующего textview, аналогично первому
            textView23.setText(repeat3)
            textView23.setTextSize(15F)
            textView23.setTextColor(Color.parseColor("#031DA7"))
            textView23.setTypeface(
                ResourcesCompat.getFont(
                    this.applicationContext,
                    R.font.gilroy_bold
                )
            )
            textView23.gravity = Gravity.CENTER
            textView23.layoutParams = TableRow.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            (textView23.layoutParams as TableRow.LayoutParams).apply {
                column = 1
                marginStart = 4
                topMargin = 4
                weight = 1f
                marginEnd = 4
                bottomMargin = 4
            }

            tableRow3.addView(textView13) //Добавление textView1 в tableRow
            tableRow3.addView(textView23) //Добавление textView2 в tableRow
            tableLayout?.addView(tableRow3) //Добавление tableRow в TableLayout
        }


        if (exercise4 !="" || repeat4 != ""){
            val tableRow4 = TableRow(this) //Добавление переменной типа TableRow, отвечает за целую строку
            tableRow4.setBackgroundDrawable(getResources().getDrawable(R.drawable.tablerowstyle)) //Установка фона в новый TextView
            tableRow4.layoutParams = TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT)
            (tableRow4.layoutParams as TableLayout.LayoutParams).apply { //Блок настройки отображения виджета
                marginStart=4 // отступ слева
                topMargin=4 // отступ сверху
                marginEnd=4 // отступ справа
                bottomMargin=4 // отступ снизу
            }

            val textView14 = TextView(this)//Добавление переменной типа TextView, находится в TableRow и отвечает ха ячейку
            textView14.setText(exercise4) //установка текста в новый TextView
            textView14.setTextSize(15F) // установка размера текста в новый TextView
            textView14.setTextColor(Color.parseColor("#031DA7"))
            textView14.setTypeface(ResourcesCompat.getFont(this.applicationContext, R.font.gilroy_bold))
            textView14.gravity = Gravity.CENTER // привязка по центру для нового TextView
            textView14.layoutParams = TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
            (textView14.layoutParams as TableRow.LayoutParams).apply { //Блок настройки отображения виджета
                column=0 // столбец в котором будет находиться новый TextView
                marginStart=4 // отступ слева
                weight = 2f
                topMargin=4 // отступ сверху
                marginEnd=4 // отступ справа
                bottomMargin=4 // отступ снизу
            }

            val textView24 = TextView(this) //создание следующего textview, аналогично первому
            textView24.setText(repeat4)
            textView24.setTextSize(15F)
            textView24.setTextColor(Color.parseColor("#031DA7"))
            textView24.setTypeface(ResourcesCompat.getFont(this.applicationContext, R.font.gilroy_bold))
            textView24.gravity = Gravity.CENTER
            textView24.layoutParams = TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
            (textView24.layoutParams as TableRow.LayoutParams).apply {
                column = 1
                weight = 1f
                marginStart=4
                topMargin=4
                marginEnd=4
                bottomMargin=4
            }

            tableRow4.addView(textView14) //Добавление textView1 в tableRow
            tableRow4.addView(textView24) //Добавление textView2 в tableRow
            tableLayout?.addView(tableRow4) //Добавление tableRow в TableLayout
        }

    }

    fun connectProgramm() :MutableList<dataclassProgram>{ //Функция записи результатов функции подключения к БД
        val nameProgram = intent.getStringExtra("nameProgram")
        val listProgram: MutableList<dataclassProgram> = mutableListOf() //создание списка
        thread { //Создание потока
            try { //попробовать
                Class.forName("com.mysql.jdbc.Driver")
                val connection = DriverManager.getConnection(
                    "jdbc:mysql://141.8.195.65:3306/a0774975_blazeroom",
                    "a0774975_blazeroom",
                    "87052547870Jeka"
                )
                val search = "SELECT * FROM Programms " //Составленеи запроса к БД к определенной таблице
                val searchWhere = "WHERE name LIKE ('%$nameProgram%')" //сортировка с условием
                val searchQuery = connection.prepareStatement(search+searchWhere+";")  // выборка всей таблицы
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
                    listProgram.add(stringProgramm) //Добавление переменной в список
                }
                connection.close()
            } catch (e: Exception) { //Если не получилось то отлавливаем ошибки и выводим их в консоль
                print(e.message)
            }
        }.join() //Отвечает за приостановку основного потока до выполнения этого
        return listProgram //Вернуть список с значениями
    }
}