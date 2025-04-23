package com.example.rickandmortycharacterview.domain.repository

import com.example.rickandmortycharacterview.domain.entity.CharacterEntity

interface IRickRepository {
    suspend fun getAllCharacters(forceRefresh: Boolean = false, newPage: Boolean = false): List<CharacterEntity>
}