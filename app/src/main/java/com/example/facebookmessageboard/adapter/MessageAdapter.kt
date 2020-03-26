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
import com.example.facebookmessageboard.api.API
import kotlinx.android.synthetic.main.activity_message_board.*
import java.text.SimpleDateFormat

class MessageAdapter(context: Context): RecyclerView.Adapter<MessageAdapter.ViewHolder>() {
    lateinit var mCommentAdapter: CommentAdapter
    private var isClear: Boolean = true
    val mContext = context
    val mMessageList = mutableListOf<PostItem>()
    var mOnItemCheckListener: OnItemCheckListener? = null
    var mOnShowLikeListener: OnShowLikeListener? = null
    var mOnLikeListener: OnLikeListener? = null
    var mOnCommentReplyListener: OnCommentReplyListener? = null

    val inflater: LayoutInflater = LayoutInflater.from(context)

    interface OnItemCheckListener {
        fun onClick(comment: String, mPostItem: PostItem)
    }

    interface OnShowLikeListener {
        fun onShowLike(number: Int, mPeopleList: MutableList<String>)
    }
    interface OnLikeListener {
        fun onLike(mPostItem: PostItem)
    }

    interface OnCommentReplyListener {
        fun onCommentReply(reply: String, mCommentItem: CommentItem)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = inflater.inflate(R.layout.viewholder_message_board_recycleview_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mMessageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvPostUser = itemView.findViewById<TextView>(R.id.tvPostUser)
        val tvPostTime = itemView.findViewById<TextView>(R.id.tvPostTime)
        val tvPostContents = itemView.findViewById<TextView>(R.id.tvPostContents)
        val imgLikeCircle = itemView.findViewById<ImageView>(R.id.imgLikeCircle)
        val tvLikeNumber = itemView.findViewById<TextView>(R.id.tvLikeNumber)
        val tvMessages = itemView.findViewById<TextView>(R.id.tvMessages)
        val tvMessageNumber = itemView.findViewById<TextView>(R.id.tvMessageNumber)
        val divider = itemView.findViewById<View>(R.id.divider)
        val imgLikeIcon = itemView.findViewById<ImageView>(R.id.imgLikeIcon)
        val imgMessageIcon = itemView.findViewById<ImageView>(R.id.imgMessageIcon)
        val recycleviewComments = itemView.findViewById<RecyclerView>(R.id.recycleviewComments)
        val btnToComment = itemView.findViewById<ImageView>(R.id.btnToComment)
        val edtComment = itemView.findViewById<EditText>(R.id.edtComment)
        val vgMessage = itemView.findViewById<ConstraintLayout>(R.id.vgMessage)

        fun bind(position: Int) {
            tvPostUser.text = mMessageList[position].user.name
            tvPostContents.text = mMessageList[position].content

            if(API.token == null) {
                vgMessage.visibility = View.GONE
            }


            //輸入框和留言按鈕設置
            if (isClear == true) {
                edtComment.setText("")
                btnToComment.setImageResource(R.drawable.ic_send)
            }

            //按讚數目
            if (!mMessageList[position].like_list.isNullOrEmpty()) {
                tvLikeNumber.text = "${mMessageList[position].like_list.size}"
                imgLikeCircle.visibility = View.VISIBLE
                tvLikeNumber.visibility = View.VISIBLE
            } else {
                imgLikeCircle.visibility = View.GONE
                tvLikeNumber.visibility = View.GONE
            }

            imgLikeCircle.setOnClickListener {
                tvLikeNumber.performClick()
            }

            tvLikeNumber.setOnClickListener {
                val list = mutableListOf<String>()
                val number = mMessageList[position].like_list.size-1
                for (i in 0..number) {
                    list.add(mMessageList[position].like_list[i].user.name)
                }
                mOnShowLikeListener!!.onShowLike(number+1, list)
            }


            //留言數目
            if (mMessageList[position].comments.size != 0) {
                val comments = mMessageList[position].comments.toMutableList()
                showComment(mContext, recycleviewComments, comments)
                showCommentNumber(mMessageList[position], tvMessageNumber)
            } else {
                tvMessages.visibility = View.GONE
                tvMessageNumber.visibility = View.GONE
            }

            //分隔線
            if (mMessageList[position].like_list.isNullOrEmpty() && mMessageList[position].comments.size == 0) {
                    divider.visibility = View.GONE

            }

            //取得使用者按讚狀態
            if (mMessageList[position].is_like.size > 0) {
                imgLikeIcon.setImageResource(R.drawable.facebook_like_blue)
            } else {
                imgLikeIcon.setImageResource(R.drawable.facebook_like)

            }

            //顯示時間
            tvPostTime.text = setTime(mMessageList[position].created_at)


            //按讚
            imgLikeIcon.setOnClickListener{
                mOnLikeListener!!.onLike(mMessageList[position])
            }

            //按留言
            imgMessageIcon.setOnClickListener{
                edtComment.requestFocus()
            }

            //監聽EditText
            editTextListener(edtComment, btnToComment)

            //送出留言
            btnToComment.setOnClickListener{
                if (!edtComment.text.isNullOrEmpty())
                mOnItemCheckListener!!.onClick(edtComment.text.toString(), mMessageList[position])
            }

        }
    }

    //刷新
    fun updateList(mList: MutableList<PostItem>){
        mMessageList.clear()
        mMessageList.addAll(mList)
        this.notifyDataSetChanged()
    }

    //留言
    fun setOnItemCheckListener(mOnItemCheckListener: OnItemCheckListener) {
        this.mOnItemCheckListener = mOnItemCheckListener
    }

    //留言之回復
    fun setOnCommentReplyListener(mOnCommentReplyListener: OnCommentReplyListener) {
        this.mOnCommentReplyListener = mOnCommentReplyListener
    }

    //按讚
    fun setOnLikeListener(mOnLikeListener: OnLikeListener) {
        this.mOnLikeListener = mOnLikeListener
    }

    //讚數
    fun showPeopleLike(mOnShowLikeListener: OnShowLikeListener) {
        this.mOnShowLikeListener = mOnShowLikeListener
    }

    //送出後清空輸入框
    fun setEditTextEmpty() {
        isClear = true
        this.notifyDataSetChanged()
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


    //綁定評論
    private fun showComment(context: Context, recycleview: RecyclerView, mList: MutableList<CommentItem>){
        mCommentAdapter = CommentAdapter(context)
        recycleview.layoutManager = LinearLayoutManager(context)
        recycleview.adapter =  mCommentAdapter

        mCommentAdapter.updateList(mList)
        mCommentAdapter.setOnReplykListener(object : CommentAdapter.OnReplyListener{
            override fun onReply(comment: String, mCommentItem: CommentItem) {
                mOnCommentReplyListener!!.onCommentReply(comment, mCommentItem)
            }
        })
    }

    //評論數量
    private fun showCommentNumber(item: PostItem, textView: TextView) {
        //有幾則留言
        val commentsNumber = item.comments.size
        val times =commentsNumber -1
        var sum = 0
        for (i in 0..times) {
            sum += item.comments[i].replies.size
        }
        textView.text = "${sum + commentsNumber}"
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