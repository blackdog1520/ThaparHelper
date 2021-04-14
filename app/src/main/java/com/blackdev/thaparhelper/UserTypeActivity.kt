package com.blackdev.thaparhelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.blackdev.thaparhelper.allutils.Constants

class UserTypeActivity : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_type)

            val studentUserRadioButton = findViewById<RadioButton>(R.id.studentUser)
            val facultyUserRadioButton = findViewById<RadioButton>(R.id.facultyUser)
            val administrationUserRadioButton = findViewById<RadioButton>(R.id.administrationUser)
            val proceedButton = findViewById<Button>(R.id.proceedButton)
            var user : Boolean = false
            when(user)
            {
                studentUserRadioButton.isChecked -> proceedButton.setOnClickListener { switchToSignUpActivity(Constants.USER_STUDENT) }
                facultyUserRadioButton.isChecked -> proceedButton.setOnClickListener { switchToSignUpActivity(Constants.USER_FACULTY)}
                administrationUserRadioButton.isChecked -> proceedButton.setOnClickListener { switchToSignUpActivity(Constants.USER_ADMINISTRATION) }
            }
    }

    private fun switchToSignUpActivity(userType: Int)
    {
        startActivity(Intent(this,SignUpActivity::class.java).apply { putExtra("userType",userType) })
    }
}