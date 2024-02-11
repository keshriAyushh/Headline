package com.ayush.headline.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

//Another table to differentiate between liked articles and the cached articles

@Parcelize
@Entity(tableName = "liked")
data class LikedArticle(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String? = "",
    val description: String? = "",
    val imageUrl: String? = "",
    var isLiked: Boolean? = false,
    val url: String? = "",
    val source: Source? = Source(),
    val publishedAt: String? = "",
    val content: String? = ""
) : Parcelable
