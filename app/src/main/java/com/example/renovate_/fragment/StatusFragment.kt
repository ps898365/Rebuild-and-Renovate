package com.example.renovate_.fragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.renovate_.R
import com.example.renovate_.globaldata.staffglobaldata
import com.example.renovate_.model.Sample
import com.example.renovate_.viewHolder.ImageViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
//import com.firebase.ui.database.FirebaseRecyclerAdapter
//import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
//import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_status.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class StatusFragment : Fragment(R.layout.fragment_status) {
    private lateinit var recyclerView: RecyclerView
    private lateinit var queryRef:DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = staff_status_recyclerview
        recyclerView.setHasFixedSize(false)
        val layoutManager = LinearLayoutManager(activity?.applicationContext)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager

        var s = staffglobaldata.currentOnlineStaff?.collegeCode.toString() + "1234"

        queryRef  = FirebaseDatabase.getInstance().reference.child(s)
            .child("Samples")


    }

    override fun onStart() {
        super.onStart()

        var options = FirebaseRecyclerOptions.Builder<Sample>()
            .setQuery(queryRef.orderByChild("phone").equalTo(staffglobaldata.currentOnlineStaff?.phoneNumber.toString()),Sample::class.java)
            .build()

        val adapter: FirebaseRecyclerAdapter<Sample,ImageViewHolder> =
            object : FirebaseRecyclerAdapter<Sample,ImageViewHolder>(options){
                override fun onBindViewHolder(imageViewHolder:ImageViewHolder, p1: Int, sample: Sample){
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

                        var builder = AlertDialog.Builder(activity)
                        builder.setTitle("Do you wish to delete this Query ? ")
                            .setPositiveButton("yes"){dialog , which->
                                queryRef.child(sample.sampleId.toString()).removeValue()
                                    .addOnCompleteListener(OnCompleteListener {
                                        Toast.makeText(activity,"Removed Successfully", Toast.LENGTH_LONG).show()
                                    })

                            }.setNegativeButton("No"){dialog , which ->
                                dialog.dismiss()
                            }.show()
                    })


                }

                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
                    val view :View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.image_query_cardview,parent,false)

                    return ImageViewHolder(view)

                }
            }

recyclerView.adapter = adapter
        adapter.startListening()

    }

}
