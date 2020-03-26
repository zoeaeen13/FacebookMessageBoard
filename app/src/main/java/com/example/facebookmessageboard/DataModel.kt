package com.example.facebookmessageboard


data class BoardResponseData(
    val posts: List<PostItem>
)

data class PostItem(
    val comments: List<CommentItem>,
    val content: String,
    val created_at: String,
    val id: Int,
    val is_like: List<Any>,
    val like_list: List<Likes>,
    val user: User
)

data class CommentItem(
    val content: String,
    val created_at: String,
    val id: Int,
    val replies: List<ReplyItem>,
    val user: User
)

data class ReplyItem(
    val content: String,
    val created_at: String,
    val id: Int,
    val user: User
)

data class Likes(
    val user: User
)

data class User(
    val name: String
)

data class PostRequest(
    val parent_id: Int?,
    val content: String,
    val layer: Int
)

data class PostResponse(
    val msg: String
)

data class RegisterRequest(
    val name: String,
    val password: String
)

data class RegisterResponse(
    val msg: String
)

data class LoginRequest(
    val name: String,
    val password: String
)

data class LoginResponse(
    val msg: String
)

data class LikeResponse(
    val msg: String
)
