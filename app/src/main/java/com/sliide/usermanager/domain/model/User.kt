package com.sliide.usermanager.domain.model

data class User(
    val id: Long,
    val name: String,
    val email: String,
    val creationTime: String
)