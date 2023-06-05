package com.example.myapplication

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.styleimage.Constant.AESEncyption
import com.google.android.material.textfield.TextInputLayout
import java.lang.Exception
import java.security.SecureRandom
import java.sql.DriverManager
import java.text.SimpleDateFormat
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import kotlin.concurrent.thread


class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val dateButton= findViewById<Button>(R.id.dateBirthEditTextRegistration)
        dateButton.setOnClickListener {
            val cal = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                dateButton.setText(SimpleDateFormat("dd.MM.yyyy").format(cal.time))
            }
            val dialog = DatePickerDialog(this,R.style.MySpinnerDatePickerStyle, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
            dialog.datePicker.maxDate = Calendar.getInstance().timeInMillis
            dialog.show()
        }
    }



    fun sendMail(mail: String, code: Int) {
        thread {
            try {
                val props = Properties()
                props.put("mail.smtp.host", "smtp.yandex.ru")
                props.put("mail.smtp.auth", "true")
                props.put("mail.smtp.port", "465")
                props.put("mail.smtp.ssl.enable", "true")
                val session: Session = Session.getInstance(props,
                    object : Authenticator() {
                        override fun getPasswordAuthentication(): PasswordAuthentication {
                            return PasswordAuthentication("blaze.room@yandex.ru", "nmnpfqhibgknlbxz")
                        }
                    })
                val message = MimeMessage(session)
                message.setFrom(InternetAddress("blaze.room@yandex.ru", "BlazeRoom"))
                message.setRecipient(
                    Message.RecipientType.TO,
                    InternetAddress(mail))
                message.subject = "Регистрация в приложении BlazeRoom"
                message.setText("\nВаш проверочный код - $code.\n\nВведите этот код для того, чтобы активировать свою учетную запись в мобильном приложении BlazeRoom.\n")
                Transport.send(message)
                println("Sent message successfully....")
            } catch (mex: Exception) {
                println("******* ERROR sending Email *******")
                mex.printStackTrace()
            }
        }.join()
    }



    private lateinit var alertDialog: AlertDialog
    @SuppressLint("MissingInflatedId")
    private fun showCustomDialog(code: Int, login:String, password:String, fio:String , number: String, data: String) {
        val inflater: LayoutInflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.dialog_custom_view_auth, null)
        val textViewError = findViewById<TextView>(R.id.errorTextViewRegistration)
        val etAddPrice = dialogView.findViewById<TextView>(R.id.etAddPrice)
        val bAddPrice: Button = dialogView.findViewById(R.id.bAddPrice)


        bAddPrice.setOnClickListener {
            val pass = AESEncyption.encrypt(password)

            if (etAddPrice.text.toString() == code.toString()){
                thread {
                    try {
                        Class.forName("com.mysql.jdbc.Driver")
                        val connection = DriverManager.getConnection(
                            "jdbc:mysql://141.8.195.65:3306/a0774975_blazeroom",
                            "a0774975_blazeroom",
                            "87052547870Jeka"
                        )
                        val numberInt = number
                        val insert = "INSERT INTO Users (id, fio, Status, login, password, number, data) VALUES " +
                                "(NULL, '${fio}', 'Клиент', '${login}', '${pass}', '${numberInt.toLong()}', '${data}');"
                        val querynIsertAccount = connection.prepareStatement(insert)
                        querynIsertAccount.execute()

                        val insert1 = "INSERT INTO Skills (id, login, speed, agility, coordination, reaction) VALUES " +
                                "(NULL, '${login}', 0, 0, 0, 0);"
                        val querynIsertAccount1 = connection.prepareStatement(insert1)
                        querynIsertAccount1.execute()
                        connection.close()
                    } catch (e: Exception) { //Если не получилось то отлавливаем ошибки и выводим их в консоль
                        print(e.message)
                    }
                }.join()

                val transitionIntent = Intent(this, JoinActivity::class.java)
                transitionIntent.putExtra("CheckExit", "CheckExit")
                startActivity(transitionIntent)
                textViewError.text = "Аккаунт успешно создан!"
                alertDialog.cancel()
            }
            else{
                textViewError.text = "Код введен не верно!"
            }
        }

        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        dialogBuilder.setOnDismissListener { }
        dialogBuilder.setView(dialogView)

        alertDialog = dialogBuilder.create()
        alertDialog.show()
    }




    fun registration(view: View) {
        val loginEditText = findViewById<EditText>(R.id.loginEditTextRegistration)
        val fioEditText = findViewById<EditText>(R.id.fioEditTextRegistration)
        val passwordEditText = findViewById<EditText>(R.id.passwrodEditTextRegistration1)
        val numberEditText = findViewById<EditText>(R.id.numberEditTextRegistration)
        val dataButton = findViewById<Button>(R.id.dateBirthEditTextRegistration)
        val textViewError = findViewById<TextView>(R.id.errorTextViewRegistration)


        var check = true
        thread {
            try {
                Class.forName("com.mysql.jdbc.Driver")
                val connection = DriverManager.getConnection(
                    "jdbc:mysql://141.8.195.65:3306/a0774975_blazeroom",
                    "a0774975_blazeroom",
                    "87052547870Jeka"
                )

                val searchAccounts = "SELECT * FROM Users;"
                val searchAccountsQuery = connection.prepareStatement(searchAccounts)
                val result = searchAccountsQuery.executeQuery()
                val pass = AESEncyption.encrypt(passwordEditText.text.toString())
                while (result.next()) {
                    val login: String = result.getString(4)
                    val number: Long = result.getLong(6)
                    if (login == loginEditText.text.toString() ||number == numberEditText.text.toString().toLong()) {
                        check = false
                    }
                }

                connection.close()
            } catch (e: Exception) { //Если не получилось то отлавливаем ошибки и выводим их в консоль
                print(e.message)
            }
        }.join()
        if (check == true) {
            if (numberEditText.length() == 11 && loginEditText.text.toString().contains("@") && loginEditText.text.toString().contains("."))
            {
                val rand = SecureRandom()
                var code = rand.nextInt(10000)
                while (code < 1000){
                    code = rand.nextInt(10000)
                }
                sendMail(loginEditText.text.toString(), code)
                showCustomDialog(code,loginEditText.text.toString(), passwordEditText.text.toString(), fioEditText.text.toString(), numberEditText.text.toString(), dataButton.text.toString())
            }
            else {
                textViewError.text = "Номер телефона или адрес электронной почты введен неверно!"
            }
        }
        else{
            textViewError.text = "Ошибка. Такой аккаунт уже существует"
        }

    }
}


