package com.example.renovate_.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.renovate_.R
import com.example.renovate_.globaldata.adminglobaldata
import com.example.renovate_.model.Admin
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_admin_login.*

class AdminLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        admin_login_btn.setOnClickListener{
            loginClient()
        }



    }

private fun loginClient() {
    val phone = admin_login_phone_num.text.toString()
    val password = admin_login_password.text.toString()
    val collegeCode = admin_login_college_code.text.toString()

    if(phone == null || password == null||collegeCode == null)
    {
        Toast.makeText(this,"Please enter the credentials!", Toast.LENGTH_LONG).show()

    }
    else
    {
        admin_progress_bar.visibility = View.VISIBLE
        authorizeStaff(phone,password,collegeCode)
    }


}

private fun authorizeStaff(phone: String, password: String, collegeCode: String) {



    val databaseRef = FirebaseDatabase.getInstance().reference
    databaseRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {

            if(snapshot.child("Admin").child(collegeCode).exists()) {
                val usersData: Admin =
                    snapshot.child("Admin").child(collegeCode).getValue(Admin::class.java)!!
                if (usersData.phoneNumber == phone)
                {
                    if (usersData.password == password)
                    {
                        if(usersData.collegeCode == collegeCode)
                        {
                            admin_progress_bar.visibility = View.INVISIBLE
                            Toast.makeText(this@AdminLoginActivity,"Welcome Admin , Logged in Successfully...!",
                                Toast.LENGTH_LONG).show()
                            adminglobaldata.currentOnlineAdmin = usersData
                            val i = Intent (this@AdminLoginActivity , AdminHomeActivity :: class.java)
                            startActivity(i)

                        }
                        else
                        {
                            admin_progress_bar.visibility = View.INVISIBLE

                            Toast.makeText(this@AdminLoginActivity,"$collegeCode is  incorrect...!", Toast.LENGTH_LONG).show()
                        }
                    }
                    else
                    {
                        admin_progress_bar.visibility = View.INVISIBLE

                        Toast.makeText(this@AdminLoginActivity,"$password is  incorrect...!", Toast.LENGTH_LONG).show()
                    }
                }
                else
                {
                    admin_progress_bar.visibility = View.INVISIBLE

                    Toast.makeText(this@AdminLoginActivity,"$phone is  not found...!", Toast.LENGTH_LONG).show()
                }
            }
            else
            {

                admin_progress_bar.visibility = View.INVISIBLE

                Toast.makeText(this@AdminLoginActivity,"User is not Found...!", Toast.LENGTH_LONG).show()
            }

        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }

    })

}


}