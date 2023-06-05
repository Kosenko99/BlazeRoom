package com.example.myapplication

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Base64
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.dataClasses.Exercise
import java.io.ByteArrayOutputStream
import java.net.URL
import java.sql.Blob
import java.sql.DriverManager
import kotlin.concurrent.thread


class ExerciseopsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exerciseops)
        val listExercise = connectExercise()
        val textViewExerciseopsTitle = findViewById<TextView>(R.id.textViewExerciseopsTitle)
        val imageViewExerciseops = findViewById<ImageView>(R.id.imageViewExerciseops)
        val textViewExerciseopsNumberBlaze = findViewById<TextView>(R.id.textViewExerciseopsNumberBlaze)
        val textViewExerciseopsDescriptionText = findViewById<TextView>(R.id.textViewExerciseopsDescriptionText)
        for (exercise in listExercise){
            //!//!//!//!//!//!//!//!//!//!//!//!//!//!//!//!//!//!//
            imageViewExerciseops.setImageResource(resources.getIdentifier("${exercise.photo}", "drawable", getPackageName()))
            textViewExerciseopsTitle.text = exercise.name
            textViewExerciseopsNumberBlaze.text = exercise.blaze.toString()
            textViewExerciseopsDescriptionText.text = exercise.description
        }

    }

    fun connectExercise() :MutableList<Exercise>{ //Функция записи результатов функции подключения к БД
        val nameExercises = intent.getStringExtra("nameExercises")
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
                val searchWhere = "WHERE name LIKE ('%$nameExercises%')" //сортировка с условием
                val searchQuery = connection.prepareStatement(search+searchWhere+";")  // выборка всей таблицы
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
                    println(stringExercise)
                }
                connection.close()
            } catch (e: Exception) { //Если не получилось то отлавливаем ошибки и выводим их в консоль
                print(e.message)
            }
        }.join() //Отвечает за приостановку основного потока до выполнения этого
        return listExercise //Вернуть список с значениями
    }
}