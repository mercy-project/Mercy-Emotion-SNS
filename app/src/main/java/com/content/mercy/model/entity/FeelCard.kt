package com.content.mercy.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feelcard")
data class FeelCard(
    @PrimaryKey val id: Long,
    val date: String,
    val feeling: String,
    val description: String,
    val image: String
)