package com.example.facebookmessageboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.facebookmessageboard.R
import com.example.facebookmessageboard.ReplyItem
import java.text.SimpleDateFormat

class DialogAdapter(context: Context): RecyclerView.Adapter<DialogAdapter.ViewHolder>() {
    val mPeopleList = mutableListOf<String>()
    val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = inflater.inflate(R.layout.viewholder_dialog_recycleview_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mPeopleList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvPeopleName = itemView.findViewById<TextView>(R.id.tvPeopleName)
        val imgPeople = itemView.findViewById<ImageView>(R.id.imgPeople)
        fun bind(position: Int) {
            tvPeopleName.text = mPeopleList[position]

            val num = (Math.random()*8).toInt()+1
            imgPeople.setImageResource(pickImage(num))
        }
    }

    fun updateList(mList: MutableList<String>){
        println("============$mList")
        mPeopleList.clear()
        mPeopleList.addAll(mList)
        this.notifyDataSetChanged()
    }

    private fun pickImage(num : Int): Int {
        when (num) {
            1 -> {
                return R.drawable.demo1
            }
            2 -> {
                return R.drawable.demo2
            }
            3 -> {
                return R.drawable.demo3
            }
            4 -> {
                return R.drawable.demo4
            }
            5 -> {
                return R.drawable.demo5
            }
            6 -> {
                return R.drawable.demo6
            }
            7 -> {
                return R.drawable.demo7
            }
            8 -> {
                return R.drawable.demo8
            }
        }
        return R.drawable.demo1
    }

}