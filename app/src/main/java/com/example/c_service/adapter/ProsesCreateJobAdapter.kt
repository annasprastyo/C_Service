package com.example.c_service.adapter

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.c_service.R
import com.example.c_service.model.DetailJobModel

class ProsesCreateJobAdapter: RecyclerView.Adapter<ProsesCreateJobAdapter.ProresCreateJobViewHolder> {
    lateinit var mContext: Context
    lateinit var itemMyorder: List<DetailJobModel>
//    lateinit var listener : FirebaseDataListener

    constructor()
    constructor(mContext: Context, list: List<DetailJobModel>) {
        this.mContext = mContext
        this.itemMyorder = list
//        listener = mContext as Item1
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ProresCreateJobViewHolder {
        val view: View = LayoutInflater.from(p0.context).inflate(
            R.layout.row_prosesjob, p0, false
        )
        val uploadViewHolder = ProresCreateJobViewHolder(view)
        return uploadViewHolder
    }

    override fun getItemCount(): Int {
        return itemMyorder.size
    }

    override fun onBindViewHolder(p0: ProresCreateJobViewHolder, p1: Int) {
        val jobModel: DetailJobModel = itemMyorder.get(p1)
        val id_job = jobModel.getId_job()!!.toLong()
        p0.id_deskripsi.text = jobModel.getDeskripsi()

        if (jobModel.getIsdone()!!.toLong().equals(1)){
            p0.id_isdone.isChecked = true
        }else{
            p0.id_isdone.isChecked = false
        }

        p0.id_delete.setOnClickListener {
            //            Toast.makeText(mContext, "${p0.idtransaksi.text}/${p0.idlaundri.text}",
//                Toast.LENGTH_SHORT).show()
//            var idtransaksi = idtrans.toLong()
////            val b = Bundle()
////            b.putSerializable("kode", datax)
//            var intent = Intent(mContext, TrDetailActivity::class.java)
//            intent.putExtra("idtransaksi", idtransaksi)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            mContext.applicationContext.startActivity(intent)
        }


//            view.context.startActivity(Intent(view.context, OutletActivity::class.java))
    }

    inner class ProresCreateJobViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview){
        var ll_DetailJob : LinearLayout
        var id_deskripsi : TextView
        var id_isdone : CheckBox
        var id_delete : ImageView
        init {
            ll_DetailJob = itemview.findViewById(R.id.ll_DetailJob)
            id_deskripsi = itemview.findViewById(R.id.id_deskripsi)
            id_isdone = itemview.findViewById(R.id.id_isdone)
            id_delete = itemview.findViewById(R.id.id_delete)
        }
    }

}