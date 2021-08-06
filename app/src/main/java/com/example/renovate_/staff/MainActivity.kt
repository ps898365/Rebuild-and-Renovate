package com.example.renovate_.staff

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.renovate_.R
import com.example.renovate_.admin.AdminLoginActivity
import com.example.renovate_.globaldata.staffglobaldata
import com.example.renovate_.model.staff
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_join_now_btn.setOnClickListener {
            val i = Intent(this, join_now::class.java)
            startActivity(i)
        }
        main_login_btn.setOnClickListener {
            val i = Intent(this, login::class.java)
            startActivity(i)
        }

        main_admin_login.setOnLongClickListener(object: View.OnLongClickListener{
            override fun onLongClick(v: View?): Boolean {
                val i = Intent(this@MainActivity, AdminLoginActivity::class.java)
                startActivity(i)
                return true
            }

        })

        admin_main_login_btn.setOnClickListener {
            val i = Intent(this, AdminLoginActivity::class.java)
            startActivity(i)
        }



        Paper.init(this)
        val phone = Paper.book().read<String>(staffglobaldata.userPhoneKey)
        val password = Paper.book().read<String>(staffglobaldata.userPasswordKey)
        val collegeCode = Paper.book().read<String>(staffglobaldata.userCollegeCode)

        if(phone!=null && password!=null&&collegeCode!=null)
        {
            if(!TextUtils.isEmpty(phone)&&!TextUtils.isEmpty(password)&&!TextUtils.isEmpty(collegeCode))
            {
                authorizeStaff(phone,password,collegeCode)
            }
        }



    }

    private fun authorizeStaff(phone: String, password: String, collegeCode: String) {


        val databaseRef = FirebaseDatabase.getInstance().reference
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.child(collegeCode).child(phone).exists()) {
                    val usersData: staff =
                        snapshot.child(collegeCode).child(phone).getValue(staff::class.java)!!
                    if (usersData.phoneNumber == phone)
                    {
                        if (usersData.password == password)
                        {
                            if(usersData.collegeCode == collegeCode)
                            {
                                Toast.makeText(this@MainActivity,"Welcome User , Logged in Successfully...!", Toast.LENGTH_LONG).show()
                                staffglobaldata.currentOnlineStaff = usersData
                                val i = Intent (this@MainActivity , Home :: class.java)
                                startActivity(i)

                            }
                            else
                            {

                                Toast.makeText(this@MainActivity,"$collegeCode is  incorrect...!", Toast.LENGTH_LONG).show()
                            }
                        }
                        else
                        {

                            Toast.makeText(this@MainActivity,"$password is  incorrect...!", Toast.LENGTH_LONG).show()
                        }
                    }
                    else
                    {

                        Toast.makeText(this@MainActivity,"$phone is  not found...!", Toast.LENGTH_LONG).show()
                    }
                }
                else
                {


                    Toast.makeText(this@MainActivity,"User is not Found...!", Toast.LENGTH_LONG).show()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }


}