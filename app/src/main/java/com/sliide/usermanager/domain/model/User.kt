package com.sliide.usermanager.domain.model

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val creationTime: String
)