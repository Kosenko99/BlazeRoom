package com.example.myapplication

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.myapplication.dataClasses.Users
import com.example.myapplication.helpFunctions.SharedPreference
import java.sql.DriverManager
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class AddRecord : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_record)
        spinnerPick()


        val dateEditText = findViewById<Button>(R.id.editTextTextDate)
        dateEditText.setOnClickListener {
            val cal = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                dateEditText.setText(SimpleDateFormat("dd.MM.yyyy").format(cal.time))
            }
            val dialog = DatePickerDialog(this,R.style.MySpinnerDatePickerStyle, dateSetListener, cal.get(
                Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
            dialog.datePicker.minDate = Calendar.getInstance().timeInMillis
            dialog.show()
        }

        val spinner1 = findViewById<Spinner>(R.id.sTimeAddRecord) //Обозначаем выпадающий список для использования
        val mList1 = arrayOf<String?>("08:00","10:00", "12:00","14:00","16:00", "18:00","20:00","22:00")
        val mArrayAdapter1 = ArrayAdapter<Any?>(this, R.layout.spinnerpick, mList1)
        mArrayAdapter1.setDropDownViewResource(R.layout.spinnerpick)
        spinner1.adapter = mArrayAdapter1

        spinner1.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener { // если изменяется выбранное

            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                time = parentView?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {
                return
            }
        })

        val userList = loadUsers()
        val fio = findViewById<EditText>(R.id.editTextTextPersonFIO)
        val phone = findViewById<EditText>(R.id.editTextPhone)
        val spinnerCoach = findViewById<ConstraintLayout>(R.id.dateSpinnerNewRecordsConstraintLayout)
        val checkUser = ""
        val sharedPreference: SharedPreference = SharedPreference(this)
        for (user in userList) {
            if (sharedPreference.getValueString("login").toString()!! == user.login && user.status == "Клиент") {
                checkUserStatus = "Клиент"
                fioUser = user.fio
                phoneClient = user.number
                fio.visibility = View.INVISIBLE
                phone.visibility = View.INVISIBLE
                val constraintLayout: ConstraintLayout = findViewById<ConstraintLayout>(R.id.constraintMain)
                val constraintSet = ConstraintSet()
                constraintSet.clone(constraintLayout)
                constraintSet.connect(findViewById<Button>(R.id.editTextTextDate).id,ConstraintSet.TOP,findViewById<ConstraintLayout>(R.id.dateSpinnerNewRecordsPlaceConstraintLayout).id,ConstraintSet.BOTTOM,0)
                constraintSet.applyTo(constraintLayout)
                (findViewById<Button>(R.id.editTextTextDate).layoutParams as ConstraintLayout.LayoutParams).apply { //Блок настройки отображения виджета
                    topMargin=40 // отступ сверху
                }
            }
            if (sharedPreference.getValueString("login").toString()!! == user.login && user.status == "Тренер") {
                checkUserStatus = "Тренер"
                fioUser = user.fio
                spinnerCoach.visibility = View.INVISIBLE
                val constraintLayout: ConstraintLayout = findViewById<ConstraintLayout>(R.id.constraintMain)
                val constraintSet = ConstraintSet()
                constraintSet.clone(constraintLayout)
                constraintSet.connect(findViewById<ConstraintLayout>(R.id.dateSpinnerNewRecordsPlaceConstraintLayout).id,ConstraintSet.TOP,findViewById<TextView>(R.id.textView).id,ConstraintSet.BOTTOM,0)
                constraintSet.applyTo(constraintLayout)
                (findViewById<ConstraintLayout>(R.id.dateSpinnerNewRecordsPlaceConstraintLayout).layoutParams as ConstraintLayout.LayoutParams).apply { //Блок настройки отображения виджета
                    topMargin=100 // отступ сверху
                }
            }
        }
    }
    var checkTrain = true
    var checkUserStatus = ""
    var fioUser = ""
    var phoneClient:Long = 1

    fun clickRecord(view: View){
        val fio = findViewById<EditText>(R.id.editTextTextPersonFIO)
        val sharedPreference: SharedPreference = SharedPreference(this)
        val login = sharedPreference.getValueString("login")!!
        val data = findViewById<Button>(R.id.editTextTextDate)
        val textError = findViewById<TextView>(R.id.textView13)
        val listTraining = connectBD()

        checkTrain = true

        if (checkUserStatus == "Клиент"){
            for (training in listTraining){
                if (training.data == data.text.toString() && training.time == time && training.location == location){ ///////Проверка занятости локации///////////////////////
                    checkTrain = false
                }
                if (training.data == data.text.toString() && training.time == time && training.coach == coach){ ////////Проверка занятости тренера//////////////////////
                    checkTrain = false
                }
            }
            if(checkTrain) {
                if (coach != ""  && login != "" && data.text.toString() != "Выбор даты" && time != "" && location != ""){
                    recordsAddContent("Schedule", coach , fioUser, login, phoneClient.toString(), data.text.toString(), time, location)
                    textError.text = "Запись успешно добавлена!"
                }
                else {
                    textError.text = "Необходимо заполнить все поля!"
                }
            }
            else if(!checkTrain) {
                textError.text = "Данная дата и время уже заняты!"
            }
        }
        if (checkUserStatus == "Тренер"){
            val listUser = loadUsers()
            val phone = findViewById<EditText>(R.id.editTextPhone)
            var loginUser = ""
            for (user in listUser){
                if (user.number.toString() == phone.text.toString()){
                    loginUser = user.login
                }
            }
            for (training in listTraining){
                if (training.data == data.text.toString() && training.time == time && training.location == location){ /////////Проверка занятости локации////////////00
                    checkTrain = false
                }
                if (training.data == data.text.toString() && training.time == time && training.coach == fioUser){ ///////////////Проверка занятости тренера//////////////////
                    checkTrain = false
                }
            }
            if (checkTrain){
                    if (fio.text.toString() != "" && loginUser != "" && phone.text.toString() != "" && phone.text.length == 11 && data.text.toString() != "Выбор даты" && time != "" && location != ""){
                        recordsAddContent("Schedule", fioUser , fio.text.toString(), loginUser, phone.text.toString(),  data.text.toString(), time, location)
                        textError.text = "Запись успешно добавлена!"
                    }
                    else {
                        textError.text = "Необходимо заполнить все поля!"
                    }
                }

            else if(!checkTrain) {
                textError.text = "Данная дата и время уже заняты!"
            }
        }


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
                }
            } catch (e: Exception) {
                println(e.printStackTrace())
            }
        }.join()

        return listRaspisanie
    }


    var coach:String = ""
    var location:String = ""
    var time:String = ""

    fun spinnerPick(){
        val spinner = findViewById<Spinner>(R.id.dateNewRecordsSpinner) //Обозначаем выпадающий список для использования
        val mList = dataCoach()
        val mArrayAdapter = ArrayAdapter<Any?>(this, R.layout.spinnerpick, mList)
        mArrayAdapter.setDropDownViewResource(R.layout.spinnerpick)
        spinner.adapter = mArrayAdapter

        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener { // если изменяется выбранное

            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                coach = parentView?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {
                return
            }
        })
        val spinner1 = findViewById<Spinner>(R.id.dateNewRecordsSpinnerPlace) //Обозначаем выпадающий список для использования
        val mList1 = arrayOf<String?>("SportBox, Большевитская 125", "X-FIT, Кирова 28","BESTGYMGOLD, Писарева 36")
        val mArrayAdapter1 = ArrayAdapter<Any?>(this, R.layout.spinnerpick, mList1)
        mArrayAdapter1.setDropDownViewResource(R.layout.spinnerpick)
        spinner1.adapter = mArrayAdapter1

        spinner1.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener { // если изменяется выбранное

            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                location = parentView?.getItemAtPosition(position).toString()
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
            } catch (e: java.lang.Exception) {
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

    fun loadUsers(): MutableList<Users> {
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
                    val status: String = result.getString(3)
                    val login: String = result.getString(4)
                    val password: String = result.getString(5)
                    val number: Long = result.getLong(6)
                    val data: String = result.getString(7)
                    val user = Users(id, fio, status, login, password, number, data)
                    listUser.add(user)
                }
            } catch (e: java.lang.Exception) {
                println(e.printStackTrace())
                println("не удалось подключиться к базе данных")

            }

        }.join()
        return listUser
    }



    fun recordsAddContent(nameTable: String, coach: String, Fio: String, login: String, phone:String, data: String, time: String, location: String){
        thread { //Создание потока
            try { //попробовать
                val user = "a0774975_blazeroom" //Имя пользователя БД
                val pass = "87052547870Jeka" //Пароль к БД
                val url = "jdbc:mysql://141.8.195.65:3306/a0774975_blazeroom" //Строка подключения (БиблиотекадляподключениякБД://ип:порт/Имябд)
                Class.forName("com.mysql.jdbc.Driver")
                val connection = DriverManager.getConnection(url, user, pass)
                val newPhone = phone.toLong()
                val insert ="INSERT INTO $nameTable (id, coach, Fio, login, Number, data, time, location) VALUES " +
                        "(NULL, '$coach', '$Fio', '$login', '$newPhone', '$data', '$time', '$location');"
                val querynIsertAccount = connection.prepareStatement(insert)
                querynIsertAccount.execute()
            }
            catch (e: Exception) { //Если не получилось то отлавливаем ошибки и выводим их в консоль
                print(e.message)
            }
        }.join()
    }


}