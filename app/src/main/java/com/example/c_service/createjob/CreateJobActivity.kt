package com.example.c_service.createjob

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.example.c_service.R
import kotlinx.android.synthetic.main.activity_created_job.*
import java.util.*
import android.view.View
import java.text.SimpleDateFormat
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.webkit.MimeTypeMap
import android.widget.DatePicker
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.c_service.activity.PrefsHelper
import com.example.c_service.utilities.Const
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_created_job.img_upload
import kotlinx.android.synthetic.main.activity_created_job.*
import java.io.IOException

class CreateJobActivity : AppCompatActivity() {

    private var actionBar: ActionBar? = null
    var textview_date: TextView? = null
    var cal = Calendar.getInstance()


    lateinit var fAuth: FirebaseAuth
    lateinit var helperPrefs: PrefsHelper
    lateinit var dbRef: DatabaseReference
    lateinit var filePath: Uri
    lateinit var stoRef: StorageReference
    lateinit var fstorage: FirebaseStorage

    val REQUEST_IMAGE = 10002
    val PERMISSION_REQUEST_CODE = 10003
    var value = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_created_job)

        initToolbar()
        textview_date = this.et_dodate

        textview_date!!.text = "--/--/----"

        // create an OnDateSetListener
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }

        // when you click on the button, show DatePickerDialog that is set with OnDateSetListener
        et_dodate!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(this@CreateJobActivity,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }

        })

        fAuth = FirebaseAuth.getInstance()
        helperPrefs = PrefsHelper(this)
        fstorage = FirebaseStorage.getInstance()
        stoRef = fstorage.reference



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

        btn_buat.setOnClickListener {
            createjob()
        }


    }

    fun createjob(){
        val uidUser = fAuth.currentUser?.uid

        val judul = et_judul.text.toString()
        val department = to_department.selectedItem.toString()
        val deskripsi = et_deskripsi.text.toString()
        val dodate = et_dodate.text.toString()


//                Log.e("muncul", "${uidUser}")
        if (judul.isNotEmpty() && department.isNotEmpty() && deskripsi.isNotEmpty()
            && dodate.isNotEmpty()
        ) {

            dbRef = FirebaseDatabase.getInstance().reference
            dbRef.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    var i = 1
                    if (p0.exists()){
                        p0.children.indexOfLast {
                            i = it.key!!.toInt() + 1
                            true
                        }
                        dbRef.child("DataJob/$i/Judul").setValue(judul)
                        dbRef.child("DataJob/$i/Nama").setValue(Const.PREF_MY_NAME)
                        dbRef.child("DataJob/$i/Department").setValue(department)
                        dbRef.child("DataJob/$i/Deskripsi").setValue(deskripsi)
                        dbRef.child("DataJob/$i/Dodate").setValue(dodate)
                        Toast.makeText(this@CreateJobActivity, "Sukses!!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

            })
        } else {
            Toast.makeText(this@CreateJobActivity, "Data Profil Harus Di Isi Semua!!", Toast.LENGTH_SHORT).show()
        }
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
                    Toast.makeText(this@CreateJobActivity, "izin ditolak!!", Toast.LENGTH_SHORT).show()
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
        ll_loading.visibility = View.VISIBLE
        val data = FirebaseStorage.getInstance()

        val uid = helperPrefs.getUID()
        val nameX = UUID.randomUUID().toString()
        val ref: StorageReference = stoRef
            .child("createjob/${nameX}.${GetFileExtension(filePath)}")
//        var storage = data.reference.child("Image_Profile/$nameX").putFile(filePath)
        ref.putFile(filePath)
            .addOnProgressListener { taskSnapshot ->
                value = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
            }
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
//                    dbRef = FirebaseDatabase.getInstance().getReference("createjob/$uid")
//                    dbRef.child("image").setValue(it.toString())
                }
                Toast.makeText(this@CreateJobActivity, "berhasil upload", Toast.LENGTH_SHORT).show()
                ll_loading.visibility = View.GONE

            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
            }

    }

    private fun updateDateInView() {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        textview_date!!.text = sdf.format(cal.getTime())
    }

    fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        actionBar = supportActionBar
        actionBar!!.setTitle("Create Job")
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setHomeButtonEnabled(true)
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