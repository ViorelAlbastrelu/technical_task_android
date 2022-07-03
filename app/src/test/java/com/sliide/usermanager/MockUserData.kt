package com.sliide.usermanager

import com.sliide.usermanager.domain.model.User
import com.sliide.usermanager.domain.name0
import com.sliide.usermanager.domain.name1
import com.sliide.usermanager.domain.name2
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object MockUserData {

    private val localDate: String
        get() {
            val date: LocalDate = LocalDate.now()
            return date.format(DateTimeFormatter.ISO_DATE) ?: ""
        }

    val mockApiUsersListResponse = listOf(
        com.sliide.usermanager.api.model.User(0, name0, "$name0@email.com"),
        com.sliide.usermanager.api.model.User(1, name1, "$name1@email.com"),
        com.sliide.usermanager.api.model.User(2, name2, "$name2@email.com"),
    )

    val mockUserList = listOf(
        User(0, name0, "$name0@email.com", localDate),
        User(1, name1, "$name1@email.com", localDate),
        User(2, name2, "$name2@email.com", localDate)
    )
}