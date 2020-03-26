package com.example.facebookmessageboard.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.facebookmessageboard.CommentItem
import com.example.facebookmessageboard.PostItem
import com.example.facebookmessageboard.R
import com.example.facebookmessageboard.ReplyItem
import java.text.SimpleDateFormat

class CommentAdapter(context: Context): RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
    lateinit var mReplyAdapter: ReplyAdapter
    val mContext = context
    private var isClear: Boolean = true
    var mOnReplyListener: OnReplyListener? = null
    val mCommentList = mutableListOf<CommentItem>()
    val inflater: LayoutInflater = LayoutInflater.from(context)

    interface OnReplyListener {
        fun onReply(comment: String, mCommentItem: CommentItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = inflater.inflate(R.layout.viewholder_comments_recycle_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mCommentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvCommentUser = itemView.findViewById<TextView>(R.id.tvCommentUser)
        val tvCommentTime = itemView.findViewById<TextView>(R.id.tvCommentTime)
        val tvCommentContents = itemView.findViewById<TextView>(R.id.tvCommentContents)
        val recycleviewReply = itemView.findViewById<RecyclerView>(R.id.recycleviewReply)
        val btnCommentToReply = itemView.findViewById<TextView>(R.id.btnCommentToReply)

        val vgReply = itemView.findViewById<ConstraintLayout>(R.id.vgReply)
        val edtReply = itemView.findViewById<EditText>(R.id.edtReply)
        val btnToReply = itemView.findViewById<ImageView>(R.id.btnToReply)

        fun bind(position: Int) {
            tvCommentUser.text = mCommentList[position].user.name
            tvCommentContents.text = mCommentList[position].content
            tvCommentTime.text = "${setTime(mCommentList[position].created_at)}"

            //輸入框和留言按鈕設置
            if (isClear == true) {
                vgReply.visibility = View.GONE
                edtReply.setText("")
                edtReply.setHint("回復${mCommentList[position].user.name}...")
                btnToReply.setImageResource(R.drawable.ic_send)
            }

            //留言數
            if (mCommentList[position].replies.size != 0) {
                showReplies(mContext, recycleviewReply, mCommentList[position].replies.toMutableList())
            }

            //監聽EditText
            editTextListener(edtReply, btnToReply)


            //出現留言框
            btnCommentToReply.setOnClickListener {
                vgReply.visibility = View.VISIBLE
                edtReply.requestFocus()
            }

            //送出留言
            btnToReply.setOnClickListener{
                if (!edtReply.text.isNullOrEmpty()) {
                    mOnReplyListener!!.onReply(edtReply.text.toString(), mCommentList[position])
                }
            }


        }
    }

    fun updateList(mList: MutableList<CommentItem>){
        mCommentList.clear()
        mCommentList.addAll(mList)
        this.notifyDataSetChanged()
    }

    //留言
    fun setOnReplykListener(mOnReplyListener: OnReplyListener) {
        this.mOnReplyListener = mOnReplyListener
    }

    //送出後清空輸入框
    fun setEditTextEmpty() {
        isClear = true
        this.notifyDataSetChanged()
    }

    private fun showReplies(context: Context, recycleview: RecyclerView, mList: MutableList<ReplyItem>){
        mReplyAdapter = ReplyAdapter(context)
        recycleview.layoutManager = LinearLayoutManager(context)
        recycleview.adapter =  mReplyAdapter
        mReplyAdapter.updateList(mList)

    }

    //監聽輸入框
    private fun editTextListener(edt: EditText, imgSend: ImageView) {
        edt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count > 0) {
                    imgSend.setImageResource(R.drawable.ic_send__blue)
                    isClear = false
                } else {
                    imgSend.setImageResource(R.drawable.ic_send)
                    isClear = true
                }
            }
            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    imgSend.setImageResource(R.drawable.ic_send__blue)
                    isClear = false
                } else {
                    imgSend.setImageResource(R.drawable.ic_send)
                    isClear = true
                }
            }
        })
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