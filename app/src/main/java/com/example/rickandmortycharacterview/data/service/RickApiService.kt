package com.example.rickandmortycharacterview.data.service

import com.example.rickandmortycharacterview.common.api.NetworkModule
import com.example.rickandmortycharacterview.data.dto.CharacterListDTO
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import android.util.Log

object RickApiService {
    private const val BASE_URL = "https://rickandmortyapi.com/api/"

    suspend fun getAllCharacters(): CharacterListDTO {
        return NetworkModule.publicClient.get("$BASE_URL/character").body()
    }

    suspend fun getCharacterById(id: Int): CharacterListDTO {
        return NetworkModule.publicClient.get("$BASE_URL/character/$id").body()
    }
}