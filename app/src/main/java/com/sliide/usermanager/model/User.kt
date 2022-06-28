package com.sliide.usermanager.model

data class User(
    val id: Long,
    val name: String,
    val email: String,
    val gender: String,
    val status: String
)