package com.example.c_service.adapter

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.example.c_service.R
import com.example.c_service.activity.PrefsHelper
import com.example.c_service.createjob.ProsesCreateJobActivity
import com.example.c_service.model.DetailJobModel
import com.example.c_service.model.JobModel
import com.google.firebase.database.*

class DataJobAdapter: RecyclerView.Adapter<DataJobAdapter.DataJobViewHolder> {

    lateinit var dbref: DatabaseReference
    lateinit var delete: DatabaseReference
    lateinit var helperPrefs: PrefsHelper
    lateinit var mContext: Context
    lateinit var itemMyorder: List<JobModel>
//    lateinit var listener : FirebaseDataListener

    constructor()
    constructor(mContext: Context, list: List<JobModel>) {
        this.mContext = mContext
        this.itemMyorder = list
//        listener = mContext as Item1
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): DataJobViewHolder {
        val view: View = LayoutInflater.from(p0.context).inflate(
            R.layout.row_data_job, p0, false
        )
        val uploadViewHolder = DataJobViewHolder(view)
        return uploadViewHolder
    }

    override fun getItemCount(): Int {
        return itemMyorder.size
    }

    override fun onBindViewHolder(p0: DataJobViewHolder, p1: Int) {
        val jobModel: JobModel = itemMyorder.get(p1)
        val id_job = jobModel.getId_job()!!.toLong()
        Glide.with(mContext)
            .load(jobModel.getImage())
            .into(p0.image)

        helperPrefs = PrefsHelper(mContext)
        p0.id_judul.text = jobModel.getJudul()
        p0.id_department.text = jobModel.getDepartment()
        p0.dodate.text = jobModel.getDodate()

        if (helperPrefs.getPilih().toString().equals("create")){
            p0.ll_status.visibility = View.VISIBLE
        }else{
            p0.ll_status.visibility = View.GONE
        }

        if (jobModel.getIsdone().toString().equals("0")){
            p0.ll_status.setBackgroundColor(ContextCompat.getColor(mContext, R.color.biruDesain))
            p0.status.text = "Proses"
        }else {
            p0.ll_status.setBackgroundColor(ContextCompat.getColor(mContext, R.color.success))
            p0.dodate.text = "Selesai"
        }

        p0.ll_datajob.setOnClickListener {
            var intent = Intent(mContext, ProsesCreateJobActivity::class.java)
            intent.putExtra("Id_job", jobModel.getId_job()!!.toLong())
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            mContext.applicationContext.startActivity(intent)
        }


//            view.context.startActivity(Intent(view.context, OutletActivity::class.java))
    }

    inner class DataJobViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview){
        var ll_datajob : LinearLayout
        var ll_status : LinearLayout
        var image : ImageView
        var id_judul : TextView
        var id_department : TextView
        var dodate : TextView
        var status : TextView
        init {
            ll_datajob = itemview.findViewById(R.id.ll_datajob)
            ll_status = itemview.findViewById(R.id.ll_status)
            image = itemview.findViewById(R.id.image)
            id_judul = itemview.findViewById(R.id.id_judul)
            id_department = itemview.findViewById(R.id.id_department)
            status = itemview.findViewById(R.id.status)
            dodate = itemview.findViewById(R.id.dodate)
        }
    }
}