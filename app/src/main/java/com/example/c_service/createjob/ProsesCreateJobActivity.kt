package com.example.c_service.createjob

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.c_service.R
import com.example.c_service.activity.ChatDetailsActivity
import com.example.c_service.adapter.ProsesCreateJobAdapter
import com.example.c_service.data.SettingApi
import com.example.c_service.model.DetailJobModel
import com.example.c_service.model.Friend
import com.example.c_service.utilities.Const
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.job_info_proses.*

class ProsesCreateJobActivity : AppCompatActivity(){

    var a: Long? = null
    var Id_job : Long? = null
    internal lateinit var set: SettingApi
    private var rvDetailJob: RecyclerView? = null
    private var ProsesCreateJobAdapter: ProsesCreateJobAdapter? = null
    private var actionBar: ActionBar? = null
    private var list: MutableList<DetailJobModel> = ArrayList<DetailJobModel>()
    lateinit var dbref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.job_info_proses)

        initToolbar()
        set = SettingApi(this)
        Id_job = intent.getLongExtra("Id_job",0)
        rvDetailJob = findViewById(R.id.rvDetailJob)
        rvDetailJob!!.layoutManager = LinearLayoutManager(this)
        rvDetailJob!!.setHasFixedSize(true)
        getDataJob(Id_job!!.toLong())
        getDataDetailJob(Id_job!!.toLong())
        getDataReceive()

        add.setOnClickListener {
            content_add.visibility = View.VISIBLE
        }

        content_add.setOnClickListener {
            content_add.visibility = View.GONE
        }
        grub_add.setOnClickListener {

        }
        btn_simpan_detail.setOnClickListener {
            addDetailJob(Id_job!!.toLong())
        }



    }

    private fun getDataReceive() {
        val dbRefUser = FirebaseDatabase.getInstance().getReference("User/ACUbmfcG88TMl0pnsSmMjqp9PMg1")
        dbRefUser.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                nama_receive.text = p0.child("/Nama").value.toString()
                receive_department.text = p0.child("/Department").value.toString()
                receive_email.text = p0.child("/Email").value.toString()
                id_chat.setOnClickListener {
                }
            }

        })
    }

    private fun getDataJob(Id_job : Long) {

        val dbRefUser = FirebaseDatabase.getInstance().getReference("DataJob/$Id_job")
        dbRefUser.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                Glide.with(this@ProsesCreateJobActivity)
                    .load(p0.child("image").value.toString())
                    .into(id_image)
                id_judul.text = p0.child("/judul").value.toString()
                id_nama.text = p0.child("/nama").value.toString()
                id_department.text = p0.child("/department").value.toString()
                id_tanggal.text = p0.child("/dodate").value.toString()
                id_deskripsi.text = p0.child("/deskripsi").value.toString()
            }

        })
    }

    fun getDataDetailJob(Id_job : Long){
        dbref = FirebaseDatabase.getInstance().getReference("DataDetailJob")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                list = ArrayList<DetailJobModel>()
                for (dataSnapshot in p0.children) {
                    val addDataAll = dataSnapshot.getValue(DetailJobModel::class.java)

//                    if(dataSnapshot == null){
//                        rvDetailJob!!.adapter = null
//                    }else{

                    if (addDataAll!!.getId_job() == Id_job!!.toLong()) {
                        addDataAll!!.setKey(dataSnapshot.key!!)
                        list.add(addDataAll)
                        ProsesCreateJobAdapter = ProsesCreateJobAdapter(this@ProsesCreateJobActivity, list)
                        rvDetailJob!!.adapter = ProsesCreateJobAdapter
                    }
//                    Log.e("TAG_ERROR", "${list}")

//                    }
//                    Log.e("view", "${dataSnapshot}")
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.e("TAG_ERROR", p0.message)
            }

        })

    }

    fun addDetailJob(Id_job : Long){

        dbref = FirebaseDatabase.getInstance().getReference("DataDetailJob/")
        dbref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(this@ProsesCreateJobActivity, "Gk iso", Toast.LENGTH_SHORT).show()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    a = 1
                    if (p0.exists()) {
                        p0.children.indexOfLast {
                            a = it.key!!.toLong() + 1
                            true
                        }
                    }
                    dbref.child("/$a/id_job").setValue(Id_job!!.toLong())
                    dbref.child("/$a/id_detail_job").setValue(a)
                    dbref.child("/$a/id_user").setValue(set.readSetting(Const.PREF_MY_NAME))
                    dbref.child("/$a/deskripsi").setValue(et_deskripsi.text.toString())
                    dbref.child("/$a/isdone").setValue(0)
                    Toast.makeText(this@ProsesCreateJobActivity, "Sukses!!", Toast.LENGTH_SHORT).show()
                    et_deskripsi.setText("")
                    content_add.visibility = View.GONE
                }

            })
    }

    fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        actionBar = supportActionBar
        actionBar!!.setTitle("Proses Job")
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setHomeButtonEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this, DataCreateJobActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}