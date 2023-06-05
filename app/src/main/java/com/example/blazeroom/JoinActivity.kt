package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.dataClasses.Users
import com.example.myapplication.helpFunctions.SharedPreference
import com.example.styleimage.Constant.AESEncyption
import java.lang.Exception
import java.sql.DriverManager
import kotlin.concurrent.thread

class JoinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)
        val checkExit = intent.getStringExtra("CheckExit")
        val sharedPreference: SharedPreference = SharedPreference(this)
        if (sharedPreference.getValueString("password") != null && checkExit != "CheckExit") {
            val transitionIntent = Intent(this, MainActivity::class.java)
            startActivity(transitionIntent)
            finish()
        }
    }

    fun checkDataFunction(view: View) {
        val sharedPreference: SharedPreference = SharedPreference(this)
        val loginEditText = findViewById<EditText>(R.id.editTextLoginJoin)
        val passwordEditText = findViewById<EditText>(R.id.editTextTextPasswordAuthorization)
        val checkBoxUser = findViewById<CheckBox>(R.id.checkBoxUserDataRememberAuthorization)
        val checkBoxData = findViewById<CheckBox>(R.id.checkBoxData)
        val textError: TextView = findViewById<TextView>(R.id.textViewErrorOutputAuthorization)
        //Проверяем заполняемость EditText
        textError.text = ""

        sharedPreference.save("login", loginEditText.editableText.toString())

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
        var check = false
        for (user in listUser)
        {
            if (user.login.toString() == loginEditText.text.toString() && AESEncyption.decrypt(user.password.toString()) == passwordEditText.text.toString()){
                check = true
            }
        }

        if (check == true) {
            if(checkBoxUser.isChecked){
                sharedPreference.save("password", passwordEditText.editableText.toString())
            }
            else{
                sharedPreference.removeValue("password")
            }
            if(checkBoxData.isChecked){
                val transitionIntent = Intent(this, MainActivity::class.java)
                startActivity(transitionIntent)
                finish()
            }
            else{
                textError.text = "Пользователь не согласен с обработкой данных!"
            }
        }
        else {
            textError.text = "Логин или пароль введен неверно!"
        }
    }

    fun registration(view: View) {
        val transitionIntent = Intent(this, RegistrationActivity::class.java)
        startActivity(transitionIntent)
    }

}
