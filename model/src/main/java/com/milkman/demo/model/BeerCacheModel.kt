package com.milkman.demo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "beers")
data class BeerCacheModel(
    @PrimaryKey
    val id: Int?,
    val name: String?,
    val tagline: String?,
    val description: String?,
    val imageUrl : String?
)