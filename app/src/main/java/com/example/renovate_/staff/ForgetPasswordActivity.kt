package com.example.renovate_.staff

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.renovate_.R
import com.example.renovate_.globaldata.staffglobaldata
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_forget_password.*

class ForgetPasswordActivity : AppCompatActivity() {

    private lateinit var check :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        check = intent.getStringExtra("check").toString()


    }


    override fun onStart() {
        super.onStart()

        forget_password_phone_number.visibility = View.GONE
        forget_password_college_code.visibility = View.GONE

        if(check == "settings")
        {
            displayPreviousAnswers()
            question_btn.setOnClickListener {
                setAnswer()
            }
        }
        else if(check == "login")
        {
            forget_password_phone_number.visibility = View.VISIBLE
            forget_password_college_code.visibility = View.VISIBLE
            page_title.setText("Recover Password")
            question_btn.setText("Verify Answer")
            question_btn.setOnClickListener { verifyAnswer() }
        }


    }

    private fun verifyAnswer() {
        val phone  = forget_password_phone_number.text.toString()
        val collegeCode  = forget_password_college_code.text.toString()
        val answer1  = question_1.text.toString().toLowerCase()
        val answer2  = question_2.text.toString().toLowerCase()

        if(phone!= "" && collegeCode!= "" &&answer1!= "" &&answer2!= "" )
        {
            val ref = FirebaseDatabase.getInstance().reference
                .child(collegeCode)
                .child(phone)

            ref.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists())
                    {
                        if(snapshot.child("Security Questions").exists())
                        {
                            val ans1 = snapshot.child("Security Questions").child("answer1").value.toString()
                            val ans2 = snapshot.child("Security Questions").child("answer2").value.toString()
                            if(ans1==answer1&&ans2==answer2)
                            {
                                val builder = AlertDialog.Builder(this@ForgetPasswordActivity)
                                builder.setTitle("New Password")
                                val newPassword = EditText(this@ForgetPasswordActivity)
                                newPassword.hint = "Write Password here..."
                                builder.setView(newPassword)
                                builder.setPositiveButton("Change"){dialog , which->
                                    ref.child("password").setValue(newPassword.text.toString()).addOnCompleteListener{task->
                                        if(task.isSuccessful)
                                        {
                                            Toast.makeText(this@ForgetPasswordActivity,"Password changed Successfully...!",
                                                Toast.LENGTH_LONG).show()
                                            val intent = Intent(this@ForgetPasswordActivity ,login::class.java)
                                            startActivity(intent)
                                        }
                                    }
                                }.setNegativeButton("Cancel"){dialog ,which->
                                    dialog.cancel()
                                }
                                builder.show()

                            }
                            else
                            {
                                Toast.makeText(this@ForgetPasswordActivity,"Answers are wrong...!",
                                    Toast.LENGTH_LONG).show()
                            }
                        }
                        else
                        {
                            Toast.makeText(this@ForgetPasswordActivity,"You have not answered Security Questions...!",
                                Toast.LENGTH_LONG).show()
                        }
                    }
                    else
                    {
                        Toast.makeText(this@ForgetPasswordActivity,"User not found..!", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        }
        else
        {
            Toast.makeText(this,"Please Enter your details..!", Toast.LENGTH_LONG).show()
        }

    }

    private fun displayPreviousAnswers() {
        val ref = FirebaseDatabase.getInstance().reference
            .child(staffglobaldata.currentOnlineStaff?.collegeCode.toString())
            .child(staffglobaldata.currentOnlineStaff?.phoneNumber.toString())
            .child("Security Questions")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    val ans1 = snapshot.child("answer1").value.toString()
                    val ans2 = snapshot.child("answer2").value.toString()
                    question_1.setText(ans1)
                    question_2.setText(ans2)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun setAnswer() {
        val answer1 = question_1.text.toString().toLowerCase()
        val answer2 = question_2.text.toString().toLowerCase()
        if (answer1 == "" || answer2 == "")
        {
            Toast.makeText(this,"Please answer the both Questions..!", Toast.LENGTH_LONG).show()
        }
        else
        {
            val ref = FirebaseDatabase.getInstance().reference
                .child(staffglobaldata.currentOnlineStaff?.collegeCode.toString())
                .child(staffglobaldata.currentOnlineStaff?.phoneNumber.toString())
                .child("Security Questions")

            val answerMap = HashMap<String,Any>()
            answerMap["answer1"]= answer1
            answerMap["answer2"]= answer2
            ref.updateChildren(answerMap).addOnCompleteListener{task->
                if(task.isSuccessful)
                {
                    Toast.makeText(this,"You have answered Security Questions..!", Toast.LENGTH_LONG).show()
                    onBackPressed()
                }
            }


        }

    }


}