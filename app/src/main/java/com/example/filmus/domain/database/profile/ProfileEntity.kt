package com.example.filmus.domain.database.profile

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
data class ProfileEntity(
    @PrimaryKey val id: String,
    val nickname: String,
    val email: String,
    val avatarLink: String,
    val name: String,
    val gender: Int,
    val birthDate: String
)