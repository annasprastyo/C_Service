package com.example.c_service.activity

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.util.Log.e
import android.view.MenuItem
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.c_service.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.ubah_activity.*
import java.io.IOException
import java.util.*

class SettingActivity: AppCompatActivity() {

    private var actionBar: ActionBar? = null
    lateinit var fAuth: FirebaseAuth
    lateinit var helperPrefs: PrefsHelper
    lateinit var dbRef: DatabaseReference
    lateinit var filePath: Uri
    lateinit var stoRef: StorageReference
    lateinit var fstorage: FirebaseStorage

    val mAuth = FirebaseAuth.getInstance()
    val REQUEST_IMAGE = 10002
    val PERMISSION_REQUEST_CODE = 10003
    var value = 0.0

    @TargetApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ubah_activity)

        initToolbar()
        fAuth = FirebaseAuth.getInstance()
        helperPrefs = PrefsHelper(this)
        fstorage = FirebaseStorage.getInstance()
        stoRef = fstorage.reference

        Glide.with(this)
            .load(R.drawable.avatar)
            .into(img_upload)

        img_upload.setOnClickListener {
            when {
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) -> {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        != PackageManager.PERMISSION_GRANTED
                    ) {
                        requestPermissions(
                            arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ),
                            PERMISSION_REQUEST_CODE
                        )
                    } else {
                        imageChooser()
                    }
                }
                else -> {
                    imageChooser()
                }
            }

        }

        val dbRefUser = FirebaseDatabase.getInstance().getReference("User/${helperPrefs.getUID()}")
        dbRefUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                Log.e("uid", helperPrefs.getUID())
                if (p0.child("/Foto").value.toString() != "null") {
                    Glide.with(this@SettingActivity)
                        .load(p0.child("/Foto").value.toString())
                        .into(img_upload)
                }
                nama_update.setText(p0.child("/Nama").value.toString())
                val adapter = ArrayAdapter.createFromResource(this@SettingActivity, R.array.list_Department, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                department.setAdapter(adapter);
                department.setSelection(adapter.getPosition(p0.child("/Department").value.toString()))
                kontak_update.setText(p0.child("/Kontak").value.toString())
                email_update.setText(p0.child("/Email").value.toString())
            }
        })

        btn_update.setOnClickListener {
            val uidUser = fAuth.currentUser?.uid

            dbRef = FirebaseDatabase.getInstance().reference
            val update_nama = nama_update.text.toString()
            val update_department = department.selectedItem.toString()
            val update_kontak = kontak_update.text.toString()
            val update_email = email_update.text.toString()

            if (update_department.isNotEmpty() && update_email.isNotEmpty() && update_nama.isNotEmpty()
                && update_kontak.isNotEmpty()
            ) {
//                Log.e("muncul", "${uidUser}")
                dbRef.child("User/$uidUser/Nama").setValue(update_nama)
                dbRef.child("User/$uidUser/Department").setValue(update_department)
                dbRef.child("User/$uidUser/Kontak").setValue(update_kontak)
                dbRef.child("User/$uidUser/Email").setValue(update_email)
                Toast.makeText(this, "Sukses!!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this@SettingActivity, "Data Profil Harus Di Isi Semua!!", Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        actionBar = supportActionBar
        actionBar!!.setTitle("Setting Profile")
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setHomeButtonEnabled(true)
    }

    private fun imageChooser() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(Intent.createChooser(intent, "select image"), REQUEST_IMAGE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0]
                    == PackageManager.PERMISSION_DENIED
                ) {
                    Toast.makeText(this@SettingActivity, "izin ditolak!!", Toast.LENGTH_SHORT).show()
                } else {
                    imageChooser()
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            REQUEST_IMAGE -> {
                filePath = data!!.data
                uploadFile()
                try {
                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(
                        this.contentResolver, filePath
                    )
                    Glide.with(this)
                        .load(bitmap)
                        .into(img_upload)
                } catch (x: IOException) {
                    x.printStackTrace()
                }

            }
        }
    }


    fun GetFileExtension(uri: Uri): String? {
        val contentResolverz = this.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()

        return mimeTypeMap.getExtensionFromMimeType(contentResolverz.getType(uri))
    }

    private fun uploadFile() {
        llProgressBar.visibility = View.VISIBLE
        val data = FirebaseStorage.getInstance()

        val uid = helperPrefs.getUID()
        val nameX = UUID.randomUUID().toString()
        val ref: StorageReference = stoRef
            .child("images/${nameX}.${GetFileExtension(filePath)}")
//        var storage = data.reference.child("Image_Profile/$nameX").putFile(filePath)
        ref.putFile(filePath)
            .addOnProgressListener { taskSnapshot ->
                value = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
            }
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    dbRef = FirebaseDatabase.getInstance().getReference("User/$uid")
                    dbRef.child("Foto").setValue(it.toString())
                }
                Toast.makeText(this@SettingActivity, "berhasil upload", Toast.LENGTH_SHORT).show()
                llProgressBar.visibility = View.GONE

            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
            }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            android.R.id.home ->{
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
