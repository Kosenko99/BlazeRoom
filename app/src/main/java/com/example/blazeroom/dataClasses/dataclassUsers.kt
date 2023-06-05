package com.example.myapplication.dataClasses

import java.sql.Blob

class Users(
    val id: Int,
    val fio: String,
    val status: String,
    val login: String,
    val password: String,
    val number: Long,
    val data: String,
        )