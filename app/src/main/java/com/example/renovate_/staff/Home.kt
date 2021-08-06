package com.example.renovate_.staff

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.renovate_.R
import com.example.renovate_.fragment.HomeFragment
import com.example.renovate_.fragment.StatusFragment
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_home.*

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val homeFragment = HomeFragment()
        val statusFragment = StatusFragment()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_fragment,homeFragment)
            commit()
        }

        home_img.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fl_fragment,homeFragment)
                addToBackStack(null)
                commit()
            }
        }
        home_status_img.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fl_fragment,statusFragment)
                addToBackStack(null)
                commit()
            }
        }
        home_setting_img.setOnClickListener{
            val i  = Intent(this, ForgetPasswordActivity::class.java)
            i.putExtra("check","settings")
            startActivity(i)
        }


        home_logout_img.setOnClickListener {
            var i  = Intent(this@Home , MainActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            Paper.book().destroy()
            finish()
        }




    }
}