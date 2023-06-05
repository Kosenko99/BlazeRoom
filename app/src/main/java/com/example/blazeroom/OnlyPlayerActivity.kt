package com.example.myapplication

import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.dataClasses.Skills
import com.example.myapplication.dataClasses.Users
import com.example.myapplication.helpFunctions.SharedPreference
import com.test.InputFilterMinMax
import java.sql.DriverManager
import kotlin.concurrent.thread


class OnlyPlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_only_player)
        val speed = findViewById<View>(R.id.textViewSpeedLoad) as EditText
        speed.filters = arrayOf<InputFilter>(InputFilterMinMax("1", "100"))
        val agility = findViewById<View>(R.id.textViewAgilityLoad) as EditText
        agility.filters = arrayOf<InputFilter>(InputFilterMinMax("1", "100"))
        val coordination = findViewById<View>(R.id.textViewCoordLoad) as EditText
        coordination.filters = arrayOf<InputFilter>(InputFilterMinMax("1", "100"))
        val reaction = findViewById<View>(R.id.textViewReactionLoad) as EditText
        reaction.filters = arrayOf<InputFilter>(InputFilterMinMax("1", "100"))

        skillLoad()
    }


    var loginUser = ""

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
        val numberUser = intent.getStringExtra("goToPlayer")
        for (user in users){
            if (numberUser == user.number.toString() && user.status == "Клиент"){
                fio.text = user.fio
                data.text = user.data
                number.text = user.number.toString()
                loginUser = user.login
            }
        }
        for (skill in skills){
            if (loginUser == skill.login){
                speed.text = skill.speed.toString()
                agility.text = skill.agility.toString()
                coordination.text = skill.coordination.toString()
                reaction.text = skill.reaction.toString()

            }
        }
    }



    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    fun updateSpeed(view: View){
        val upskill =findViewById<TextView>(R.id.upskill)
        upskill.visibility = View.INVISIBLE
        val editTextSpeed =findViewById<EditText>(R.id.textViewSpeedLoad)
        val textViewAgilityLoad =findViewById<EditText>(R.id.textViewAgilityLoad)
        val editTextCoordination =findViewById<EditText>(R.id.textViewCoordLoad)
        val editTextReaction =findViewById<EditText>(R.id.textViewReactionLoad)
        val numberUser =findViewById<TextView>(R.id.textViewNumberLoad)
        val listUsers = connectUser()
        val listSkills = connectSkill()
        var login =""
        var newSpeed = ""
        var newAgility = ""
        var newCoordination = ""
        var newReaction = ""
        var id = 0
        for (user in listUsers){
            if (user.number.toString() == numberUser.text.toString()){
                login = user.login
            }
        }
        for (skill in listSkills){
            if (login == skill.login){
                id = skill.id
                newSpeed = editTextSpeed.text.toString()
                newAgility = textViewAgilityLoad.text.toString()
                newCoordination = editTextCoordination.text.toString()
                newReaction = editTextReaction.text.toString()
                updateSpeedSkill(id,newSpeed,newAgility,newCoordination,newReaction)
                upskill.visibility = View.VISIBLE
            }
        }

    }

    fun updateSpeedSkill(id:Int,newSpeed:String, newAgility:String,newCoordination:String,newReaction:String){
        thread {
            try {
                Class.forName("com.mysql.jdbc.Driver")
                val connection = DriverManager.getConnection(
                    "jdbc:mysql://141.8.195.65:3306/a0774975_blazeroom",
                    "a0774975_blazeroom",
                    "87052547870Jeka"
                )
                println("Подключение успешно выполнено")
                val speedToInt = newSpeed.toInt()
                val newAgilityToInt = newAgility.toInt()
                val newCoordinationToInt = newCoordination.toInt()
                val newReactionToInt = newReaction.toInt()
                val insert ="UPDATE Skills SET speed = $speedToInt, agility = $newAgilityToInt, coordination = $newCoordinationToInt, reaction = $newReactionToInt WHERE id = $id"
                println(insert)
                val querynIsertAccount = connection.prepareStatement(insert)
                querynIsertAccount.executeUpdate()
                connection.close()
            } catch (ex: Exception) {
                println("Connection failed...")
                println(ex)
            }
        }.join()
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

}