package com.example.contactdetails

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactAdapter(context: Context, listcont: List<ContactDataItem>?) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {
    var list:List<ContactDataItem>?=listcont

    //    private var activity:Activity?=null


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image1:ImageView?=itemView.findViewById(R.id.image)
        var name:TextView?=itemView.findViewById(R.id.name)
        val phone:TextView?=itemView.findViewById(R.id.phone)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view : View  =LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
        return ViewHolder(view) as ViewHolder
    }

    override fun onBindViewHolder(holder: ContactAdapter.ViewHolder, position: Int) {
        var data: ContactDataItem? =list?.get(position)
        holder.image1?.setImageBitmap(data!!.contactimage)
        holder.name?.setText(data!!.contactname)
        holder.phone?.setText(data!!.contactnumber)
    }

    override fun getItemCount(): Int {

       if(list==null){
           return 0
       }else{
           return list!!.size
       }
    }

}
