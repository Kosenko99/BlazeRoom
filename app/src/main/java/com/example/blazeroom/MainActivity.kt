package com.example.myapplication

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.myapplication.dataClasses.Users
import com.example.myapplication.helpFunctions.SharedPreference
import java.lang.Exception
import java.sql.DriverManager
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadUsers()
        checkStatus()

    }




    fun checkStatus(){
        val buttonPlayers = findViewById<Button>(R.id.buttonMainPlayers)
        val buttonDataPlayer = findViewById<Button>(R.id.buttonMainDataPlayer)
        val listUsers = loadUsers()
        val sharedPreference: SharedPreference = SharedPreference(this)
        for (user in listUsers){
            if (sharedPreference.getValueString("login").toString()!! == user.login ) {
                if (user.status == "Тренер"){
                    buttonPlayers.visibility = View.VISIBLE
                    buttonDataPlayer.visibility = View.INVISIBLE
                }
                else {
                    buttonDataPlayer.visibility = View.VISIBLE
                    buttonPlayers.visibility = View.INVISIBLE
                }
            }
        }

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
                connection.close()
            } catch (e: Exception) {
                println(e.printStackTrace())
                println("не удалось подключиться к базе данных")

            }

        }.join()
        return listUser
    }


        fun exit(view: View) {
            val transitionIntent = Intent(this, JoinActivity::class.java)
            transitionIntent.putExtra("CheckExit", "CheckExit")
            startActivity(transitionIntent)
            val sharedPreference = SharedPreference(this)
            sharedPreference.clearSharedPreference()
            finish()
        }



        fun rasPer(view: View) {
            val transitionIntent = Intent(this, CalendarActivity::class.java)
            startActivity(transitionIntent)
        }

        fun exerciseAll(view: View) {
            val transitionIntent = Intent(this, ExercisesActivity::class.java)
            startActivity(transitionIntent)
        }

        fun programAll(view: View) {
            val transitionIntent = Intent(this, ProgramActivity::class.java)
            startActivity(transitionIntent)
        }

        fun playersAll(view: View) {
            val transitionIntent = Intent(this, PlayersActivity::class.java)
            startActivity(transitionIntent)
        }

        fun playersData(view: View) {
            val transitionIntent = Intent(this, SkillsPlayerActivity::class.java)
            startActivity(transitionIntent)
        }



}

