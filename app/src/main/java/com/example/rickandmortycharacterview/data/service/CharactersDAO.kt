package com.example.rickandmortycharacterview.data.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.rickandmortycharacterview.data.model.CharacterModel

@Dao
interface CharactersDAO {
    @Query("SELECT * FROM characters")
    suspend fun getAllCharacters() : List<CharacterModel>

    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun getCharactersById(id: String): CharacterModel

    @Insert
    suspend fun insertAll(characters: List<CharacterModel>)
}