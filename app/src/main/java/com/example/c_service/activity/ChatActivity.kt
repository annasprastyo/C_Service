package com.example.c_service.activity

import android.annotation.SuppressLint
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.example.c_service.R
import com.example.c_service.activity.PrefsHelper
import com.example.c_service.fragments.FrChat
import com.example.c_service.service.NotificationService
import kotlinx.android.synthetic.main.chat_activity.*

class ChatActivity : AppCompatActivity() {

    private var actionBar: ActionBar? = null
    lateinit var fab: FloatingActionButton
    internal lateinit var mJobScheduler: JobScheduler
    lateinit var helperPref: PrefsHelper

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_activity)

        fab = findViewById<FloatingActionButton>(R.id.add)
        helperPref = PrefsHelper(this)
        //setting toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation
        setSupportActionBar(toolbar)


        toolbar.title = "Chat"
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.grey_text))

        initComponent()
        initToolbar()

        fab.setOnClickListener {
            val i = Intent(this@ChatActivity, SelectFriendActivity::class.java)
            startActivity(i)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Tools.systemBarLolipop(this)
            mJobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            val builder = JobInfo.Builder(1, ComponentName(packageName, NotificationService::class.java.name))
            builder.setPeriodic(900000)
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            mJobScheduler.schedule(builder.build())
        }

    }


    private fun initComponent() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val ctf = FrChat()

        fragmentTransaction.add(R.id.main_container, ctf, "Chat History")
        fragmentTransaction.commit()

    }

    fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        actionBar = supportActionBar
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