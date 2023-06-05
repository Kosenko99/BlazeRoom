package com.example.myapplication

import android.app.DatePickerDialog
import android.graphics.Color
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import com.example.myapplication.dataClasses.Users
import java.lang.Exception
import java.sql.DriverManager
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class CoachPickActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coach_pick)
        val listRaspisanie = connectBD()
        spinnerPick(listRaspisanie)


        val dateButton = findViewById<Button>(R.id.buttonCalendarDataCoach)
        dateButton.setOnClickListener {
            val cal = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                dateButton.setText(SimpleDateFormat("dd.MM.yyyy").format(cal.time))
                sorter(listRaspisanie)
            }
            val dialog = DatePickerDialog(this,R.style.MySpinnerDatePickerStyle, dateSetListener, cal.get(
                Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
            dialog.datePicker.minDate = Calendar.getInstance().timeInMillis
            dialog.show()

        }


    }
    var coach:String = ""


    fun sorter(listRaspisanie: MutableList<Raspisanie>){
        val imageButton =findViewById<ImageButton>(R.id.imageButton)
        imageButton.visibility = View.VISIBLE
        val buttonDate =findViewById<Button>(R.id.buttonCalendarDataCoach)
        cleanTable()
        for(raspisanie in listRaspisanie){
            if (raspisanie.coach == coach && buttonDate.text == raspisanie.data) {
                addRow(raspisanie.FIO, raspisanie.data, raspisanie.time, raspisanie.location)
            }
        }

    }


    fun deleteDate(view: View){
        cleanTable()
        val buttonDate =findViewById<Button>(R.id.buttonCalendarDataCoach)
        val imageButton =findViewById<ImageButton>(R.id.imageButton)
        buttonDate.text = "Выбрать"
        imageButton.visibility = View.INVISIBLE
        val listRaspisanie = connectBD()
        for(raspisanie in listRaspisanie){
            if (raspisanie.coach == coach) {
                addRow(raspisanie.FIO, raspisanie.data, raspisanie.time, raspisanie.location)
            }
        }

    }

    fun spinnerPick(listRaspisanie:MutableList<Raspisanie>){
        val buttonDate =findViewById<Button>(R.id.buttonCalendarDataCoach)
        val spinner = findViewById<Spinner>(R.id.CalendarCoachPicker) //Обозначаем выпадающий список для использования
        val mList = dataCoach()
        val mArrayAdapter = ArrayAdapter<Any?>(this, R.layout.spinnerpick, mList)
        mArrayAdapter.setDropDownViewResource(R.layout.spinnerpick)
        spinner.adapter = mArrayAdapter

        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener { // если изменяется выбранное

            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                coach = parentView?.getItemAtPosition(position).toString()
                cleanTable()
                if (buttonDate.text == "Выбрать"){
                    for(raspisanie in listRaspisanie){
                        if (raspisanie.coach == coach) {
                            addRow(raspisanie.FIO, raspisanie.data, raspisanie.time, raspisanie.location)
                        }
                    }
                }
                else {
                    for(raspisanie in listRaspisanie){
                        if (raspisanie.coach == coach && buttonDate.text == raspisanie.data) {
                            addRow(raspisanie.FIO, raspisanie.data, raspisanie.time, raspisanie.location)
                        }
                    }
                }
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {
                return
            }
        })
    }
    fun dataCoach():Array<String?> {
        val listUser: MutableList<Users> = mutableListOf()
        thread {
            try {
                Class.forName("com.mysql.jdbc.Driver")
                val connection = DriverManager.getConnection(
                    "jdbc:mysql://141.8.195.65:3306/a0774975_blazeroom",
                    "a0774975_blazeroom",
                    "87052547870Jeka"
                )
                println("Подключение успешно выполнено")

                val searchAccounts = "SELECT * FROM Users;"
                val searchAccountsQuery = connection.prepareStatement(searchAccounts)
                val result = searchAccountsQuery.executeQuery()
                while (result.next()) {
                    val id: Int = result.getInt(1)
                    val fio: String = result.getString(2)
                    val status:String = result.getString(3)
                    val login: String = result.getString(4)
                    val password: String = result.getString(5)
                    val number: Long = result.getLong(6)
                    val data: String = result.getString(7)
                    val user = Users(id, fio, status, login, password, number, data)
                    listUser.add(user)
                }
            } catch (e: Exception) {
                println(e.printStackTrace())
                println("не удалось подключиться к базе данных")

            }
        }.join()
        var check = false
        var coachList = arrayOf<String?>()
        for (user in listUser)
        {
            if(user.status == "Тренер") {
                coachList += user.fio
            }
        }
        return coachList

    }

    fun connectBD():MutableList<Raspisanie> {
        val listRaspisanie: MutableList<Raspisanie> = mutableListOf()
        thread {
            try {
                Class.forName("com.mysql.jdbc.Driver")
                val connection = DriverManager.getConnection(
                    "jdbc:mysql://141.8.195.65:3306/a0774975_blazeroom",
                    "a0774975_blazeroom",
                    "87052547870Jeka"
                )
                println("Подключение успешно выполнено")

                val searchAccounts = "SELECT * FROM Schedule"
                val searchAccountsQuery = connection.prepareStatement(searchAccounts)
                val result = searchAccountsQuery.executeQuery()
                while (result.next()) {
                    val id: Int = result.getInt(1)
                    val coach:String = result.getString(2)
                    val FIO: String = result.getString(3)
                    val login: String = result.getString(4)
                    val phone: Long = result.getLong(5)
                    val data: String = result.getString(6)
                    val time: String = result.getString(7)
                    val location: String = result.getString(8)
                    val raspisanie = Raspisanie(id, coach, FIO, login, phone, data, time, location)
                    listRaspisanie.add(raspisanie)
                    println("счёт $raspisanie")
                }
            } catch (e: Exception) {
                println(e.printStackTrace())
                println("не удалось подключиться к базе данных")

            }
        }.join()

        return listRaspisanie
    }


    fun cleanTable() {
        val tableLayout = findViewById<TableLayout>(R.id.tableCalendar)
        val childCount = tableLayout.childCount
        if (childCount > 1) {
            tableLayout.removeViews(1, childCount - 1)
        }
    }

    fun addRow(Fio: String, data: String, time: String, location: String) {
        val tableLayout = findViewById<TableLayout>(R.id.tableCalendar) //Обозначаем Tablelayout для использования
        val tableRow = TableRow(this) //Добавление переменной типа TableRow, отвечает за целую строку
        //tableRow.setBackgroundColor(Color.parseColor("#FFFFFF")) //Установка фона в новый TextView
        tableRow.setBackgroundDrawable(getResources().getDrawable(R.drawable.tablerowstyle))
        tableRow.layoutParams = TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT)
        (tableRow.layoutParams as TableLayout.LayoutParams).apply {
            marginStart=4 // отступ слева
            topMargin=4 // отступ сверху
            marginEnd=4 // отступ справа
            bottomMargin=4 // отступ снизу
        }



        val textView1 = TextView(this)//Добавление переменной типа TextView, находится в TableRow и отвечает ха ячейку
        textView1.setText("$data/\n$time") //установка текста в новый TextView
        textView1.setTextSize(15F) // установка размера текста в новый TextView
        textView1.setTextColor(Color.parseColor("#031DA7"))
        textView1.setTypeface(ResourcesCompat.getFont(this.applicationContext, R.font.gilroy_bold))
        textView1.gravity = Gravity.CENTER // привязка по центру для нового TextView
        textView1.layoutParams = TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
        (textView1.layoutParams as TableRow.LayoutParams).apply { //Блок настройки отображения виджета
            weight = 1f
            column=0 // столбец в котором будет находиться новый TextView
            marginStart=4 // отступ слева
            topMargin=4 // отступ сверху
            marginEnd=4 // отступ справа
            bottomMargin=4 // отступ снизу
        }

        val textView2 = TextView(this) //создание следующего textview, аналогично первому
        val list1: List<String> = listOf(*Fio.split(" ").toTypedArray())
        var count1 = 0
        for (i in list1){
            println(list1)
            if (count1 == 0){
                textView2.setText(i)
                count1 ++
            }else{
                textView2.setText(textView2.text.toString() + "\n" + i)
            }
        }
        textView2.setTextSize(15F)
        textView2.setTextColor(Color.parseColor("#031DA7"))
        textView2.setTypeface(ResourcesCompat.getFont(this.applicationContext, R.font.gilroy_bold))
        textView2.gravity = Gravity.CENTER
        textView2.layoutParams = TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
        (textView2.layoutParams as TableRow.LayoutParams).apply {
            weight = 4f
            column = 1
            marginStart=4
            topMargin=4
            marginEnd=4
            bottomMargin=4
        }

        val textView3 = TextView(this) //создание следующего textview, аналогично первому
        val list: List<String> = listOf(*location.split(",").toTypedArray())
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
        textView3.setTextColor(Color.parseColor("#031DA7"))
        textView3.setTypeface(ResourcesCompat.getFont(this.applicationContext, R.font.gilroy_bold))
        textView3.gravity = Gravity.CENTER
        textView3.layoutParams = TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
        (textView3.layoutParams as TableRow.LayoutParams).apply {
            weight = 5f
            column = 2
            marginStart=4
            topMargin=4
            marginEnd=4
            bottomMargin=4
        }

        tableRow.addView(textView1) //Добавление textView1 в tableRow
        tableRow.addView(textView2) //Добавление textView2 в tableRow
        tableRow.addView(textView3) //Добавление textView1 в tableRow
        tableLayout?.addView(tableRow) //Добавление tableRow в TableLayout

    }
}