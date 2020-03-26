package com.example.facebookmessageboard

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.facebookmessageboard.adapter.CommentAdapter
import com.example.facebookmessageboard.adapter.DialogAdapter
import com.example.facebookmessageboard.adapter.MessageAdapter
import com.example.facebookmessageboard.api.API
import kotlinx.android.synthetic.main.activity_message_board.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessageBoardActivity : AppCompatActivity() {
    lateinit var mMessageAdapter: MessageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_board)

        mMessageAdapter = MessageAdapter(this)
        recycleviewBoard.layoutManager = LinearLayoutManager(this)
        recycleviewBoard.adapter = mMessageAdapter

        val num = (Math.random()*8).toInt()+1
        imgPostUser.setImageResource(pickImage(num))


        getBoard()
        setPost()

        //登出
        tvLogout.setOnClickListener{
            API.token = null
            finish()
        }

        //顯示按讚人數
        mMessageAdapter.showPeopleLike(object : MessageAdapter.OnShowLikeListener{
            override fun onShowLike(number: Int, mPeopleList: MutableList<String>) {
                val inflater = LayoutInflater.from(this@MessageBoardActivity)
                val view = inflater.inflate(R.layout.dialog_view, null)
                val dialog_station = AlertDialog.Builder(this@MessageBoardActivity)
                    .setView(view)
                    .show()

                val tvLikeNumber = view.findViewById<TextView>(R.id.tvLikeNumber)
                val rvLikePeople = view.findViewById<RecyclerView>(R.id.rvLikePeople)
                tvLikeNumber.text = "$number"

                val dialogAdapter= DialogAdapter(this@MessageBoardActivity)
                rvLikePeople.layoutManager = LinearLayoutManager(this@MessageBoardActivity)
                rvLikePeople.adapter = dialogAdapter
                dialogAdapter.updateList(mPeopleList)

            }
        })

        //留言評論
        mMessageAdapter.setOnItemCheckListener(object: MessageAdapter.OnItemCheckListener {
            override fun onClick(comment: String, mPostItem: PostItem) {
                clearEditText(1)
                toPost(mPostItem.id, comment, 1)
            }
        })

        //按讚
        mMessageAdapter.setOnLikeListener(object: MessageAdapter.OnLikeListener{
            override fun onLike(mPostItem: PostItem) {
                if (mPostItem.is_like.size > 0) {
                    toLike(mPostItem.id, 0)
                } else {
                    toLike(mPostItem.id,1)
                }
            }
        })

        //回復
        mMessageAdapter.setOnCommentReplyListener(object : MessageAdapter.OnCommentReplyListener{
            override fun onCommentReply(reply: String, mCommentItem: CommentItem) {
                toPost(mCommentItem.id, reply, 2)
            }
        })




    }

    //更新留言板
    private fun getBoard(){
        API.apiInterface.getBoard().enqueue(object : Callback<BoardResponseData>{
            override fun onFailure(call: Call<BoardResponseData>, t: Throwable) {
            }

            override fun onResponse(call: Call<BoardResponseData>, response: Response<BoardResponseData>) {
                if (response.isSuccessful){
                    val responseBody = response.body()
                    val dataList = responseBody!!.posts
                    val mList = dataList.toMutableList()
                    mMessageAdapter.updateList(mList)
                }
            }
        })
    }

    //留言/評論/回覆
    private fun toPost(parent_id: Int?, contents: String, layer: Int){
        API.apiInterface.toPost(PostRequest(parent_id, contents, layer))
            .enqueue(object: Callback<PostResponse> {
                override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                }
                override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                    if (response.isSuccessful){
                        getBoard()
                    }
                }
            })
    }

    private fun toLike(post_id: Int, isLike: Int) {
        API.apiInterface.setLike("$post_id", isLike).enqueue(object: Callback<LikeResponse>{
            override fun onFailure(call: Call<LikeResponse>, t: Throwable) {

            }
            override fun onResponse(call: Call<LikeResponse>, response: Response<LikeResponse>) {
                if (response.isSuccessful) {
                    getBoard()
                }
            }
        })
    }

    //清空輸入框
    private fun clearEditText(mode: Int?){
        when(mode){
            null -> {
                edtToPost.setText("")
            }
            1 -> {
                mMessageAdapter.setEditTextEmpty()
            }
        }
    }

    //發文
    private fun setPost(){
        btnToPost.setOnClickListener{
            if (!edtToPost.text.isNullOrEmpty()){
                toPost(null, edtToPost.text.toString(), 0)
                clearEditText(null)
            }
        }

        edtToPost.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count > 0) {
                    btnToPost.setTextColor(Color.parseColor("#3b5995"))
                    btnToPost.isClickable = true
                }
            }
            override fun afterTextChanged(s: Editable?) {

            }
        })
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
