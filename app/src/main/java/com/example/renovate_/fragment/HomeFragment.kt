package com.example.renovate_.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.renovate_.R
import com.example.renovate_.globaldata.staffglobaldata
import com.example.renovate_.model.Sample
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var title :String
    private lateinit var description:String
    private lateinit var downloadImageUrl:String
    private val GalleryPicks = 1
    private val status = "Inactive"
    private var ImageUri : Uri? = null
    private var sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
    private val currentDate = sdf.format(Date()).toString()
    private val queryref = FirebaseStorage.getInstance().reference.child(staffglobaldata.currentOnlineStaff?.collegeCode.toString())


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        home_fragment_select_image.setOnClickListener{
            openGallery()
        }

        home_query_btn.setOnClickListener {
            verifyInfo()
        }

    }

    private fun verifyInfo() {
        title = home_title.text.toString()
        description  = home_description.text.toString()
        if(ImageUri ==null || title == null || description == null )
        {
            Toast.makeText(activity?.applicationContext , "Please provide the required Information  " , Toast.LENGTH_LONG).show()

        }
        else
        {
            storeQueryInfo()
        }
    }

    private fun storeQueryInfo(){

        home_progress_bar.visibility = View.VISIBLE

        val bmp = MediaStore.Images.Media.getBitmap(activity?.applicationContext?.contentResolver, ImageUri)

        var baos = ByteArrayOutputStream()
        Objects.requireNonNull(bmp).compress(Bitmap.CompressFormat.JPEG, 25, baos)
        val data = baos.toByteArray()


        val filePath = queryref.child(ImageUri!!.lastPathSegment + ".jpg")
        val uploadTask = filePath.putBytes(data)


        uploadTask.addOnFailureListener {
            OnFailureListener { e ->
                val exception = e.toString()

                Toast.makeText(activity, "Error:$exception", Toast.LENGTH_SHORT)
                    .show()
            }

        }.addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot?> {
            Toast.makeText(activity, "Sample Image Uploaded Successfully", Toast.LENGTH_LONG).show()
            val urlTask: Task<Uri> = uploadTask.continueWithTask<Uri>(
                Continuation<UploadTask.TaskSnapshot?, Task<Uri?>?> { task ->
                    if (!task.isSuccessful) {
                        throw task.exception!!
                    }
                    downloadImageUrl = filePath.downloadUrl.toString()
                    filePath.downloadUrl

                }
            ).addOnCompleteListener(OnCompleteListener<Uri> { task ->
                if (task.isSuccessful) {
                    downloadImageUrl = task.result.toString()
                    Toast.makeText(activity, "Got the Sample Url Successfully", Toast.LENGTH_LONG)
                        .show()
                    saveProductToDatabase()
                }
            })

        })

    }

    private fun saveProductToDatabase(){


        var S  = staffglobaldata.currentOnlineStaff?.collegeCode.toString() + "1234"

        var databaseRef = FirebaseDatabase.getInstance().reference.child(S)
            .child("Samples")

        var sampleId = databaseRef.push().key
        databaseRef = databaseRef.child(sampleId.toString())
        val sample = Sample (staffglobaldata.currentOnlineStaff?.phoneNumber.toString(),sampleId,downloadImageUrl,currentDate,title,description,status)
        databaseRef.setValue(sample).addOnCompleteListener(OnCompleteListener { task ->
            if(task.isSuccessful)
            {
                home_progress_bar.visibility = View.INVISIBLE
                Toast.makeText(activity?.applicationContext," Query is added Successfully..!", Toast.LENGTH_LONG).show()
                val fragment = StatusFragment()
                fragmentManager?.beginTransaction()?.replace(R.id.fl_fragment,fragment)?.commit()

            }
        })

    }



    private fun openGallery(){
        val i  = Intent()
        i.action = Intent.ACTION_GET_CONTENT
        i.type = "image/*"
        startActivityForResult(i,GalleryPicks)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == GalleryPicks && resultCode == Activity.RESULT_OK && data !=null)
        {
            ImageUri = data.data
            home_fragment_select_image.setImageURI(ImageUri)
        }


    }




}