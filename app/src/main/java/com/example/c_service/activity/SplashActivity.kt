package com.example.c_service

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SplashActivity : AppCompatActivity() {

    lateinit var tv_splash : TextView
    lateinit var logo : ImageView
    lateinit var animbot : Animation
    lateinit var animtop : Animation
    var saveuser: String? = null
    lateinit var helperPrefs: PrefsHelper

    val mAuth = FirebaseAuth.getInstance()
    val user = mAuth.currentUser

    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 4000 //4 seconds
    internal val mRunnable: Runnable = Runnable {


        if (!isFinishing) {
            val intent = Intent(applicationContext, LoginActivity::class.java)

            startActivity(intent)

            finish()
            Toast.makeText(this, "Selamat Datang di CUCIIN.APP", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        helperPrefs = PrefsHelper(this)
        logo = findViewById(R.id.logosplash)
        tv_splash = findViewById(R.id.tv_splash)
        animbot = AnimationUtils.loadAnimation(this, R.anim.frombottom)
        animtop = AnimationUtils.loadAnimation(this, R.anim.fromtop)

        logo.startAnimation(animtop)
        tv_splash.startAnimation(animbot)

        //Initialize the Handler

        mDelayHandler = Handler()

        //Navigate with delay
        if (user == null) {
            mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)
        } else {
            updateUI(user)
//            Toast.makeText(this, "${user}", Toast.LENGTH_SHORT).show()
        }


    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null)
            startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    public override fun onDestroy() {

        if (mDelayHandler != null) {

            mDelayHandler!!.removeCallbacks(mRunnable)

        }

        super.onDestroy()

    }
}