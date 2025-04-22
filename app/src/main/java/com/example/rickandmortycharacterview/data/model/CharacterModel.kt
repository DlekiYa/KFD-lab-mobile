package com.example.rickandmortycharacterview.data.model

import androidx.room.PrimaryKey
import androidx.room.Entity

@Entity(tableName= "characters")
data class CharacterModel(
    @PrimaryKey val id: Int,
    val name: String,
    val image: String,
    val status: String,
    val species: String,
)