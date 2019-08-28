package com.example.c_service.createjob

import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.example.c_service.R
import com.example.c_service.adapter.ProsesCreateJobAdapter
import com.example.c_service.model.DetailJobModel
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.job_info_proses.*

class ProsesCreateJobActivity : AppCompatActivity(){

    var Id_job : Long? = null
    private var rvDetailJob: RecyclerView? = null
    private var ProsesCreateJobAdapter: ProsesCreateJobAdapter? = null
    private var actionBar: ActionBar? = null
    private var list: MutableList<DetailJobModel> = ArrayList<DetailJobModel>()
    lateinit var dbref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.job_info_proses)

        initToolbar()
        Id_job = intent.getLongExtra("Id_job",0)
        rvDetailJob = findViewById(R.id.rvDetailJob)
        rvDetailJob!!.layoutManager = LinearLayoutManager(this)
        rvDetailJob!!.setHasFixedSize(true)
        getDataJob(Id_job!!.toLong())
        getDataDetailJob(Id_job!!.toLong())

    }

    private fun getDataJob(Id_job : Long) {

        val dbRefUser = FirebaseDatabase.getInstance().getReference("DataJob/$Id_job")
        dbRefUser.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                Glide.with(this@ProsesCreateJobActivity)
                    .load(p0.child("/Image").value.toString())
                    .into(id_image)
                id_judul.text = p0.child("/Judul").value.toString()
                id_nama.text = p0.child("/Nama").value.toString()
                id_department.text = p0.child("/Department").value.toString()
                id_tanggal.text = p0.child("/Dodate").value.toString()
                id_deskripsi.text = p0.child("/Deskripsi").value.toString()
            }

        })
    }

    fun getDataDetailJob(Id_job : Long){
        dbref = FirebaseDatabase.getInstance().getReference("DetailDataJob/$Id_job")
        dbref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                list = ArrayList<DetailJobModel>()
                for (dataSnapshot in p0.children) {
                    val addDataAll = dataSnapshot.getValue(DetailJobModel::class.java)
//                    if (addDataAll!!.getId_user() == uidUser) {
                        addDataAll!!.setKey(dataSnapshot.key!!)
                        list.add(addDataAll)
                        ProsesCreateJobAdapter = ProsesCreateJobAdapter(this@ProsesCreateJobActivity, list)
                        rvDetailJob!!.adapter = ProsesCreateJobAdapter
//                    }
//                    Log.e("TAG_ERROR", "${list}")
//                    Log.e("view", "${dataSnapshot}")
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.e("TAG_ERROR", p0.message)
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
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}