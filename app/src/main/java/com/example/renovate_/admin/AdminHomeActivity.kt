package com.example.renovate_.admin

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.renovate_.R
import com.example.renovate_.globaldata.adminglobaldata
import com.example.renovate_.model.Sample
import com.example.renovate_.model.staff
import com.example.renovate_.staff.MainActivity
import com.example.renovate_.viewHolder.ImageViewHolder
import com.example.renovate_.viewHolder.TextViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_admin_home.*


class AdminHomeActivity : AppCompatActivity(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var queryRef: DatabaseReference
    private lateinit var staff_recyclerview: RecyclerView
    private lateinit var staff_query: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)


        recyclerView = admin_sample_recyclerview
        recyclerView.setHasFixedSize(false)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager
        recyclerView.setLayoutManager(
            LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                true
            )
        )





//        staff_recyclerview = admin_staff_recyclerview
//        staff_recyclerview.setHasFixedSize(false)
//        val layoutManagers = LinearLayoutManager(this)
//        layoutManagers.reverseLayout = true
//        layoutManagers.stackFromEnd = true
//        staff_recyclerview.layoutManager = layoutManagers
//        staff_recyclerview.setLayoutManager(
//            LinearLayoutManager(
//                this,
//                LinearLayoutManager.HORIZONTAL , true
//            )
//        )
        var s = adminglobaldata.currentOnlineAdmin?.collegeCode.toString() +"1234"

        queryRef  = FirebaseDatabase.getInstance().reference.child(s)
            .child("Samples")
        staff_query  = FirebaseDatabase.getInstance().reference.child(adminglobaldata.currentOnlineAdmin?.collegeCode.toString())


        admin_logout.setOnClickListener{
            var i  = Intent(this, MainActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            finish()
        }


    }


    override fun onStart() {
        super.onStart()

        var options = FirebaseRecyclerOptions.Builder<Sample>()
            .setQuery(
                queryRef,
                Sample::class.java
            )
            .build()


        val adapter: FirebaseRecyclerAdapter<Sample, ImageViewHolder> =
            object : FirebaseRecyclerAdapter<Sample, ImageViewHolder>(options){
                override fun onBindViewHolder(
                    imageViewHolder: ImageViewHolder,
                    p1: Int,
                    sample: Sample
                ) {
                    imageViewHolder.name?.setText(
                        """ |Title : ${sample.title}  
                                    |
                                    |Description : ${sample.description}
                                    |
                                    |Date : ${sample.date}
                                    |
                                    |Status : ${sample.status}
                                """.trimMargin()
                    )

                    Picasso.get().load(sample.image).into(imageViewHolder.image)

                    imageViewHolder.itemView.setOnClickListener(View.OnClickListener {

                        val builder = androidx.appcompat.app.AlertDialog.Builder(this@AdminHomeActivity)
                        builder.setTitle("Active or Rejected or Ongoing")
                        val newPassword = EditText(this@AdminHomeActivity)
                        newPassword.hint = "Write Status here..."
                        builder.setView(newPassword)
                        builder.setPositiveButton("Change"){dialog , which->
                            queryRef.child(sample.sampleId.toString()).child("status").setValue(newPassword.text.toString()).addOnCompleteListener{task->
                                if(task.isSuccessful)
                                {
                                    Toast.makeText(this@AdminHomeActivity,"Password changed Successfully...!",
                                        Toast.LENGTH_LONG).show()

                                }
                            }
                        }.setNegativeButton("Cancel"){dialog ,which->
                            dialog.cancel()
                        }
                        builder.show()


                    })

                }

                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
                    val view : View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.image_query_cardview, parent, false)
                    return ImageViewHolder(view)
                }

            }

        recyclerView.adapter = adapter
        adapter.startListening()
//
//        var staffoptions = FirebaseRecyclerOptions.Builder<staff>()
//            .setQuery(
//                staff_query,
//                staff::class.java
//            )
//            .build()
//
//
//        val staff_adapter: FirebaseRecyclerAdapter<staff, TextViewHolder> =
//            object : FirebaseRecyclerAdapter<staff, TextViewHolder>(staffoptions){
//                override fun onBindViewHolder(
//                    textViewHolder: TextViewHolder,
//                    p1: Int,
//                    sample: staff
//                ) {
//                    textViewHolder.name?.setText(
//                        """ |Staff Name : ${sample.userName}
//                                    |
//                                    |Phone Number : ${sample.phoneNumber}
//                                    |
//                                    |Mail : ${sample.mail}
//                                """.trimMargin()
//                    )
//
//
//                    textViewHolder.itemView.setOnClickListener(View.OnClickListener {
//                        var builder = AlertDialog.Builder(this@AdminHomeActivity)
//                        builder.setTitle("Do you wish to delete this Query ? ")
//                            .setPositiveButton("yes") { dialog, which ->
//                                staff_query.child(sample.phoneNumber.toString()).removeValue()
//                                    .addOnCompleteListener(OnCompleteListener {
//                                        Toast.makeText(
//                                            this@AdminHomeActivity,
//                                            "Removed Successfully",
//                                            Toast.LENGTH_LONG
//                                        ).show()
//                                    })
//
//                            }.setNegativeButton("No") { dialog, which ->
//                                dialog.dismiss()
//                            }.show()
//                    })
//
//                }
//
//                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextViewHolder {
//                    val view : View = LayoutInflater.from(parent.context)
//                        .inflate(R.layout.text_query_cardview, parent, false)
//                    return TextViewHolder(view)
//                }
//
//            }
//
//        staff_recyclerview.adapter = staff_adapter
//        staff_adapter.startListening()




    }



}