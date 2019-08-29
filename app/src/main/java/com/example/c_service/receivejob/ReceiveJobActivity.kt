package com.example.c_service.receivejob

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.example.c_service.R
import com.example.c_service.activity.MainActivity
import com.example.c_service.adapter.DataJobAdapter
import com.example.c_service.data.SettingApi
import com.example.c_service.model.JobModel
import com.google.firebase.database.DatabaseReference

class ReceiveJobActivity : AppCompatActivity() {

    private var actionBar: ActionBar? = null
    lateinit var dbref: DatabaseReference
    internal lateinit var set: SettingApi
    private var DataJobAdapter: DataJobAdapter? = null
    private var rvDataJob: RecyclerView? = null
    private var list: MutableList<JobModel> = ArrayList<JobModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.)
    }

    fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        actionBar = supportActionBar
        actionBar!!.setTitle("Data Receive Job")
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setHomeButtonEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}