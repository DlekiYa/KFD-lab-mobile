package com.example.rickandmortycharacterview.common.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.rickandmortycharacterview.data.model.CharacterModel
import com.example.rickandmortycharacterview.data.service.CharactersDAO


@Database(
    entities = [CharacterModel::class],
    version = 1,
    exportSchema = false,
)
abstract class Database : RoomDatabase() {
    abstract fun charactersDAO(): CharactersDAO

    companion object{
        const val DATABASE_NAME = "characters.db"
    }
}