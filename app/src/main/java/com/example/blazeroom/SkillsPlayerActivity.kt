package com.example.myapplication

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.example.myapplication.dataClasses.Skills
import com.example.myapplication.dataClasses.Users
import com.example.myapplication.helpFunctions.SharedPreference
import java.lang.Exception
import java.sql.DriverManager
import kotlin.concurrent.thread

class SkillsPlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skills_player)
        skillLoad()
    }


    fun connectUser():MutableList<Users> {
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



    fun connectSkill():MutableList<Skills> {
        val skillsUser: MutableList<Skills> = mutableListOf()
        thread {
            try {
                Class.forName("com.mysql.jdbc.Driver")
                val connection = DriverManager.getConnection(
                    "jdbc:mysql://141.8.195.65:3306/a0774975_blazeroom",
                    "a0774975_blazeroom",
                    "87052547870Jeka"
                )
                println("Подключение успешно выполнено")

                val searchAccounts = "SELECT * FROM Skills;"
                val searchAccountsQuery = connection.prepareStatement(searchAccounts)
                val result = searchAccountsQuery.executeQuery()
                while (result.next()) {
                    val id: Int = result.getInt(1)
                    val login: String = result.getString(2)
                    val speed: Int = result.getInt(3)
                    val agility: Int = result.getInt(4)
                    val coordination: Int = result.getInt(5)
                    val reaction: Int = result.getInt(6)
                    val userSkills = Skills(id,login,speed,agility, coordination, reaction)
                    skillsUser.add(userSkills)
                }
                connection.close()
            } catch (e: Exception) {
                println(e.printStackTrace())
                println("не удалось подключиться к базе данных")

            }
        }.join()

        return skillsUser

    }

    fun skillLoad(){
        val users = connectUser()
        val skills = connectSkill()
        val sharedPreference: SharedPreference = SharedPreference(this)
        sharedPreference.getValueString("login")!!
        val fio = findViewById<TextView>(R.id.textViewFioLoad)
        val data = findViewById<TextView>(R.id.textViewAgeLoad)
        val number = findViewById<TextView>(R.id.textViewNumberLoad)
        val speed = findViewById<TextView>(R.id.textViewSpeedLoad)
        val agility = findViewById<TextView>(R.id.textViewAgilityLoad)
        val coordination = findViewById<TextView>(R.id.textViewCoordLoad)
        val reaction = findViewById<TextView>(R.id.textViewReactionLoad)
        for (user in users){
            if (sharedPreference.getValueString("login").toString()!! == user.login && user.status == "Клиент"){
                fio.text = user.fio
                data.text = user.data
                number.text = user.number.toString()
            }
        }
        for (skill in skills){
            if (sharedPreference.getValueString("login").toString()!! == skill.login){
                speed.text = skill.speed.toString()
                agility.text = skill.agility.toString()
                coordination.text = skill.coordination.toString()
                reaction.text = skill.reaction.toString()

            }
        }
    }


}




