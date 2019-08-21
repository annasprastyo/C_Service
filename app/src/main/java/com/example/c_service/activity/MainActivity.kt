package com.example.c_service

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var groupAdapter = GroupAdapter<ViewHolder>()
    lateinit var mAuth: FirebaseAuth

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                val fragment = FrHomeUser()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
                finish()
            }
            R.id.navigation_order -> {
                val fragment = FrMyorder()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
                finish()
            }
            R.id.navigation_profile -> {
                val fragment = FrProfile()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
                finish()
            }
        }
        false
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.design_bottom_sheet_slide_in,
                R.anim.design_bottom_sheet_slide_out
            )
            .replace(R.id.content, fragment, fragment.javaClass.simpleName)
            .commit()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val fragment = FrHomeUser()
        addFragment(fragment)
        //setting toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation
        setSupportActionBar(toolbar)
        toolbar.title = ""
        toolbar.setLogo(R.mipmap.ic_logo)
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_actionbar,menu)
//        return super.onCreateOptionsMenu(menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
//        R.id.action_chat -> {
//            startActivity(Intent(this, ChatActivity::class.java))
//            true
//        }
//
//        else -> {
//            super.onOptionsItemSelected(item)
//        }
//    }

    override fun onResume() {
        super.onResume()
        val user = mAuth.currentUser
        if (user == null) {
            finish()
        }
    }
}

