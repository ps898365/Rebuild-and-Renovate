package com.example.renovate_.staff

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.renovate_.R
import com.example.renovate_.model.staff
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_join_now.*

class join_now : AppCompatActivity() {


    private lateinit var phone :String
    private lateinit var password :String
    private lateinit var mail :String
    private lateinit var collegeName :String
    private lateinit var userName :String
    private lateinit var collegeCode :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_now)

        join_register_btn.setOnClickListener{
            verifyInfo()
        }



    }

    private fun verifyInfo() {
        phone = join_phone_num.text.toString()
        password = join_password.text.toString()
        userName = join_user_name.text.toString()
        mail = join_mail.text.toString()
        collegeName = join_college_name.text.toString()
        collegeCode = join_college_code.text.toString()

        if(phone == null || mail == null ||password == null ||userName == null ||collegeCode == null ||collegeName == null )
        {
            Toast.makeText(this,"Please write all the information...!", Toast.LENGTH_LONG).show()
        }
        else{
            join_progress_bar.visibility = View.VISIBLE
            validateUser(phone,password,userName,mail,collegeCode,collegeName)
        }


    }

    private fun validateUser(phone: String, password: String, userName: String, mail: String, collegeCode: String, collegeName: String) {
        val databaseRef = FirebaseDatabase.getInstance().reference.child(collegeCode).child(phone)
        val staffId = databaseRef.push().key

        val staffDetails = staff(staffId,phone,password,userName,mail,collegeCode,collegeName)
            databaseRef.setValue(staffDetails).addOnCompleteListener {
                join_progress_bar.visibility = View.INVISIBLE
                Toast.makeText(this,"Your account is Created ...!",Toast.LENGTH_LONG).show()

                val i = Intent (this,login::class.java)
                startActivity(i)
                finish()
   }



    }
}