package com.example.c_service

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity: AppCompatActivity() {
    val mAuth = FirebaseAuth.getInstance()
    lateinit var helperPrefs: PrefsHelper
//    internal lateinit var set: SettingApi


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        helperPrefs = PrefsHelper(this)
        val loginBtn = findViewById<View>(R.id.btn_login) as Button
        val regisTxt = findViewById<View>(R.id.btn_register) as TextView

        loginBtn.setOnClickListener(View.OnClickListener { View ->
            login()
        })

        regisTxt.setOnClickListener { View ->
            register()
        }

    }

    private fun login() {
        val emailTxt = findViewById<View>(R.id.et_email) as EditText
        val passwordTxt = findViewById<View>(R.id.et_pass) as EditText

        var email = emailTxt.text.toString()
        var password = passwordTxt.text.toString()
//        set = SettingApi(this)

        if (!email.isEmpty() && !password.isEmpty()) {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, OnCompleteListener { task ->
                    if (task.isSuccessful) {
//                    startActivity(Intent(this, MainActivity::class.java))
                        val user = mAuth.currentUser
                        helperPrefs.saveUID(user!!.uid)
                        val dbRefUser = FirebaseDatabase.getInstance().getReference("Akun/${helperPrefs.getUID()}")
//                        dbRefUser.addValueEventListener((object : ValueEventListener {
//                            override fun onDataChange(p0: DataSnapshot) {
//                                var userid = p0.child("/ID").value.toString()
//                                var nama = p0.child("/Nama").value.toString()
//                                var photo = p0.child("/bukti").value.toString()
//
//                                set.addUpdateSettings(Const.PREF_MY_ID, userid)
//                                set.addUpdateSettings(Const.PREF_MY_NAME, nama)
//                                set.addUpdateSettings(Const.PREF_MY_DP, photo)
//                            }
//
//                            override fun onCancelled(p0: DatabaseError) {
//
//                            }
//                        }))
                            updateUI(user)
                            Toast.makeText(this, "Berhasil login", Toast.LENGTH_SHORT).show()


                    } else {
                        Toast.makeText(this, "Email Atau Password Salah!", Toast.LENGTH_SHORT).show()
                    }
                })
        } else {
            Toast.makeText(this, "Isi Form dengan lengkap!!", Toast.LENGTH_SHORT).show()
        }

    }
    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            helperPrefs.saveUID(user.uid) //berfungsi untuk save uid ke sharedpreferences
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            Log.e("TAG_ERROR", "user tidak ada")
        }
    }


    override fun onStart() {
        super.onStart()
        val user = mAuth.currentUser
        if (user != null) {
            updateUI(user)
        }
    }

    private fun register() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}