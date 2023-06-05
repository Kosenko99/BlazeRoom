package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
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

class PlayersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_players)
        spinnerPick()
    }

    var check = ""
    var ot = 0
    var dot = 100
    fun spinnerPick(){
        val spinner = findViewById<Spinner>(R.id.dataPlayerPicker) //Обозначаем выпадающий список для использования
        val mList = arrayOf<String?>("Все возраста", "от 5 до 18", "от 19 до 27", "от 28 до 35", "старше 35")
        val mArrayAdapter = ArrayAdapter<Any?>(this, R.layout.spinnerpick, mList)
        mArrayAdapter.setDropDownViewResource(R.layout.spinnerpick)
        spinner.adapter = mArrayAdapter

        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener { // если изменяется выбранное

            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                cleanTable()
                check = parentView?.getItemAtPosition(position).toString()
                if (check == "Все возраста"){
                    ot = 0
                    dot = 100
                    sort()
                }
                else if (check == "от 5 до 18"){
                    ot = 5
                    dot = 18
                    sort()
                }
                else if (check == "от 19 до 27" ){
                    ot = 19
                    dot = 27
                    sort()
                }
                else if (check == "от 28 до 35"){
                    ot = 28
                    dot = 35
                    sort()
                }
                else if (check == "старше 35"){
                    ot = 35
                    dot = 100
                    sort()
                }

            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {
                return
            }
        })
    }

    @SuppressLint("SimpleDateFormat")
    fun sort(){
        cleanTable()
        var nonCheck = 0
        val nonTV = findViewById<TextView>(R.id.non)
        nonTV.visibility = View.INVISIBLE
        val sortFio = findViewById<EditText>(R.id.editTextFIO)
        val listUsers = dataUser()
        if (sortFio.text.toString() == ""){
            for (user in listUsers){
                if (user.status == "Клиент"){
                    val year = user.data.get(6).toString() + user.data.get(7).toString() +  user.data.get(8).toString() + user.data.get(9).toString()
                    val time = Calendar.getInstance().time
                    val formatter = SimpleDateFormat("dd.MM.yyyy")
                    val current = formatter.format(time)
                    val yearNow = current.get(6).toString() + current.get(7).toString() + current.get(8).toString() + current.get(9).toString()
                    val age = yearNow.toInt() - year.toInt()
                    if(age >=ot && age <= dot){
                        addRow(user.data, user.fio, user.number.toString())
                        nonCheck++
                    }

                }
            }
            if (nonCheck == 0){
                nonTV.visibility = View.VISIBLE
            }
        }
        else{
            for (user in listUsers){
                if (user.status == "Клиент"){
                    val year = user.data.get(6).toString() + user.data.get(7).toString() +  user.data.get(8).toString() + user.data.get(9).toString()
                    val time = Calendar.getInstance().time
                    val formatter = SimpleDateFormat("dd.MM.yyyy")
                    val current = formatter.format(time)
                    val yearNow = current.get(6).toString() + current.get(7).toString() + current.get(8).toString() + current.get(9).toString()
                    val age = yearNow.toInt() - year.toInt()
                    if(age >=ot && age <= dot && user.fio.contains(sortFio.text.toString())){
                        addRow(user.data, user.fio, user.number.toString())
                    }

                }
            }
        }
    }

    fun sortFio(view: View){
        sort()
    }


    fun goToPlayer(numberUser: String){
        val transitionIntent = Intent(this, OnlyPlayerActivity::class.java)
        transitionIntent.putExtra("goToPlayer", numberUser)
        startActivity(transitionIntent)
    }


    fun addRow(data: String, fio: String, number:String) {
        val tableLayout = findViewById<TableLayout>(R.id.tb) //Обозначаем Tablelayout для использования
        val tableRow = TableRow(this) //Добавление переменной типа TableRow, отвечает за целую строку
        tableRow.setBackgroundDrawable(getResources().getDrawable(R.drawable.tablerowstyle))
        tableRow.layoutParams = TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT)
        (tableRow.layoutParams as TableLayout.LayoutParams).apply { //Блок настройки отображения виджета
            topMargin=4 // отступ сверху
            bottomMargin=4 // отступ снизу
        }


        val textView1 = TextView(this)//Добавление переменной типа TextView, находится в TableRow и отвечает ха ячейку
        textView1.setText("$data") //установка текста в новый TextView
        textView1.setTextSize(15F) // установка размера текста в новый TextView
        textView1.setTextColor(Color.parseColor("#031DA7"))
        textView1.setTypeface(ResourcesCompat.getFont(this.applicationContext, R.font.gilroy_bold))
        textView1.gravity = Gravity.CENTER // привязка по центру для нового TextView
        textView1.layoutParams = TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
        (textView1.layoutParams as TableRow.LayoutParams).apply { //Блок настройки отображения виджета
            column=0 // столбец в котором будет находиться новый TextView
            weight=1f
            marginStart=4 // отступ слева
            topMargin=4 // отступ сверху
            marginEnd=4 // отступ справа
            bottomMargin=4 // отступ снизу
        }

        val textView2 = TextView(this) //создание следующего textview, аналогично первому
        val list1: List<String> = listOf(*fio.split(" ").toTypedArray())
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
            column = 1
            weight=6f
            marginStart=4
            topMargin=4
            marginEnd=4
            bottomMargin=4
        }

        val textView3 = TextView(this) //создание следующего textview, аналогично первому
        textView3.id = View.generateViewId()
        textView3.setText(number)
        textView3.setTextSize(15F)
        textView3.setTextColor(Color.parseColor("#031DA7"))
        textView3.setTypeface(ResourcesCompat.getFont(this.applicationContext, R.font.gilroy_bold))
        textView3.gravity = Gravity.CENTER
        textView3.layoutParams = TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
        (textView3.layoutParams as TableRow.LayoutParams).apply {
            column = 2
            marginStart=4
            weight=4f
            topMargin=4
            marginEnd=4
            bottomMargin=4
        }

        tableRow.addView(textView1) //Добавление textView1 в tableRow
        tableRow.addView(textView2) //Добавление textView2 в tableRow
        tableRow.addView(textView3) //Добавление textView1 в tableRow


        tableRow.setOnClickListener {
            val textViewEx = findViewById<TextView>(tableRow.getChildAt(2).id)
            val numberUser = textViewEx.text.toString()
            goToPlayer(numberUser)
        }


        tableLayout?.addView(tableRow) //Добавление tableRow в TableLayout

    }



    fun dataUser():MutableList<Users> {
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
                connection.close()
            } catch (e: Exception) {
                println(e.printStackTrace())
                println("не удалось подключиться к базе данных")

            }
        }.join()

        return listUser

    }

    fun cleanTable() {
        val tableLayout = findViewById<TableLayout>(R.id.tb)
        val childCount = tableLayout.childCount
        if (childCount > 1) {
            tableLayout.removeViews(1, childCount - 1)
        }
    }
}