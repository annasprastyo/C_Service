package com.example.c_service.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.c_service.R
import com.example.c_service.activity.PrefsHelper
import com.example.c_service.createjob.DataCreateJobActivity
import com.example.c_service.receivejob.ProsesReceiveJobActivity
import com.example.c_service.receivejob.ReceiveJobActivity
import kotlinx.android.synthetic.main.fr_receivejob_activity.*

class FrReceiveJob: Fragment() {

    lateinit var helperPrefs : PrefsHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fr_receivejob_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        helperPrefs = PrefsHelper(activity!!)
        ll_ReceiveJob.setOnClickListener {
            startActivity(Intent(activity!!, ReceiveJobActivity::class.java))
            helperPrefs.savePilih("receive")
        }
        ll_Proses_Receive.setOnClickListener {
            startActivity(Intent(activity!!, ProsesReceiveJobActivity::class.java))
            helperPrefs.savePilih("receive")
        }
    }
}