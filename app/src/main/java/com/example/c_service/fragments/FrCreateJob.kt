package com.example.c_service.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.c_service.R
import com.example.c_service.createjob.CreateJobActivity
import kotlinx.android.synthetic.main.fr_createjob_activity.*

class FrCreateJob: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fr_createjob_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        id_createdjob.setOnClickListener {
            startActivity(Intent(activity!!, CreateJobActivity::class.java))
        }

    }
}