package com.example.c_service.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.c_service.R
import com.example.c_service.activity.LoginActivity
import com.example.c_service.activity.PrefsHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fr_profile_activity.*
import kotlinx.android.synthetic.main.fr_profile_activity.view.*

class FrProfile: Fragment() {

    lateinit var fAuth: FirebaseAuth
    lateinit var helperPrefs: PrefsHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fr_profile_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fAuth = FirebaseAuth.getInstance()
        helperPrefs = PrefsHelper(activity!!)

        Glide.with(view.context)
            .load(R.drawable.avatar)
            .into(view.avatar)
//        nama.setText(fUser)

        val dbRefUser = FirebaseDatabase.getInstance().getReference("User/${helperPrefs.getUID()}")
        dbRefUser.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                Log.e("uid", helperPrefs.getUID())
                if (p0.child("/Foto").value.toString() != "null") {
                    Glide.with(view.context)
                        .load(p0.child("/Foto").value.toString())
                        .into(view.avatar)
                }

                view.nama.text = p0.child("/Nama").value.toString()
                view.department.text = p0.child("/Department").value.toString()
                view.kontak.text = p0.child("/Kontak").value.toString()
                view.email.text = p0.child("/Email").value.toString()
            }

        })

        btn_logout.setOnClickListener {
            signOut()
            startActivity(Intent(activity!!, LoginActivity::class.java))
            activity!!.finish()
        }
    }
    fun signOut() {
        fAuth.signOut()

    }
}