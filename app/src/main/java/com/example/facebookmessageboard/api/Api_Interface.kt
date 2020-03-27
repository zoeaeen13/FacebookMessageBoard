package com.example.facebookmessageboard.api

import com.example.facebookmessageboard.*
import retrofit2.Call
import retrofit2.http.*

interface Api_Interface {

    //註冊
    @POST("/api/register")
    fun register(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    //登入
    @POST("/api/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    //訪客查看留言版
    @GET("/api/index")
    fun getView(): Call<BoardResponseData>

    //登入後留言版
    @GET("/api/board")
    fun getBoard(): Call<BoardResponseData>

    //發文
    @POST("/api/post")
    fun toPost(@Body postRequest: PostRequest): Call<PostResponse>

    //按讚
    @GET("api/like/{post_id}")
    fun setLike(
        @Path("post_id") post_id: String,
        @Query("isLike") isLike: Int
    ): Call<LikeResponse>

}