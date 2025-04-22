package com.example.rickandmortycharacterview.data.repository

import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.example.rickandmortycharacterview.data.mapper.CharacterMapper
import com.example.rickandmortycharacterview.data.model.CharacterModel
import com.example.rickandmortycharacterview.data.service.CharactersDAO
import com.example.rickandmortycharacterview.data.service.RickApiService
import com.example.rickandmortycharacterview.domain.entity.CharacterEntity
import com.example.rickandmortycharacterview.domain.repository.IRickRepository

class RickRepository(
    private val apiService: RickApiService,
    private val dao: CharactersDAO,
) : IRickRepository{
    override suspend fun getAllCharacters(forceRefresh: Boolean): List<CharacterEntity> {
        val localData = dao.getAllCharacters()

        Log.v("WHAT", "Recieved a query with force refresh parameter = $forceRefresh and localData size = ${localData.size}")
        if (localData.isEmpty() || forceRefresh) {
            val remoteData = apiService.getAllCharacters()
            if (localData.isEmpty()) {
                val data : List<CharacterModel> = remoteData.results.map({CharacterMapper.mapDTOtoModel(it)})
                dao.insertAll(data)
            }
            return remoteData.results.map({CharacterMapper.mapDTOtoEntity(it)})
        }
        return localData.map { CharacterMapper.mapModelToEntity(it) }
    }
}