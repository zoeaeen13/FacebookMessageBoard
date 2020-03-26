package com.example.facebookmessageboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.facebookmessageboard.R
import com.example.facebookmessageboard.ReplyItem
import java.text.SimpleDateFormat

class ReplyAdapter(context: Context): RecyclerView.Adapter<ReplyAdapter.ViewHolder>() {
    val mReplyList = mutableListOf<ReplyItem>()
    val inflater: LayoutInflater = LayoutInflater.from(context)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = inflater.inflate(R.layout.viewholder_reply_recycleview_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mReplyList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvReplyUser = itemView.findViewById<TextView>(R.id.tvReplyUser)
        val tvReplyTime = itemView.findViewById<TextView>(R.id.tvReplyTime)
        val tvReplyContents = itemView.findViewById<TextView>(R.id.tvReplyContents)

        fun bind(position: Int) {
            tvReplyUser.text = mReplyList[position].user.name
            tvReplyContents.text = mReplyList[position].content
            tvReplyTime.text = "${setTime(mReplyList[position].created_at)}"
        }
    }

    fun updateList(mList: MutableList<ReplyItem>){
        mReplyList.clear()
        mReplyList.addAll(mList)
        this.notifyDataSetChanged()
    }

    //時間顯示
    private fun setTime(data: String) : String {
        val postDate = data.substring(0,10)
        val postTime = data.substring(11, 19)
        val dateString = "$postDate $postTime"

        //目前系統時間
        val timeInMill = System.currentTimeMillis()
        val currentTime = timeInMill

        //將字符轉成時間戳記計算
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = dateFormat.parse(dateString)
        val time = date.time
        val diff = currentTime - time

        //回傳字串
        val minute = 1000*60
        val hour = 1000*60*60
        val day = 1000*24*60*60
        if (diff < hour) {
            return "${(diff/minute).toInt()}分鐘"
        } else if (diff > day) {
            var showMonth = data.substring(5, 7)
            if (showMonth.substring(0,1) == "0")
                showMonth = showMonth.substring(1)
            var showDay = data.substring(8, 10)
            if (showDay.substring(0,1) == "0")
                showDay = showDay.substring(1)
            val showTime = data.substring(11, 16)
            return "${showMonth}月${showDay}日${showTime}"
        } else {
            return "${(diff/hour).toInt()}小時"
        }
    }

}