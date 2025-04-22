package com.example.rickandmortycharacterview.presentation.viewmodel

import android.util.Log
import com.example.rickandmortycharacterview.data.repository.RickRepository
import com.example.rickandmortycharacterview.domain.entity.CharacterEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CharacterViewModel (
    private val repository: RickRepository,
) {
    private val _characters = MutableStateFlow(emptyList<CharacterEntity>())

    val characters : StateFlow<List<CharacterEntity>> = _characters.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError.asStateFlow()

    suspend fun loadCharacters(forceRefresh: Boolean = false) {
        _isLoading.value = true
        _isError.value = false
        try {
            val charactersList = repository.getAllCharacters(forceRefresh)
            _characters.value = charactersList
        } catch (e: Exception) {
            _isError.value = true
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun getFirstCharacter() : String {
        delay(5000)
        if (characters.value.isEmpty()) {
            return ""
        }
        return characters.value[0].name
    }
}