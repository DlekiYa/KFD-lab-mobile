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
    private var _nextPage : Int = 1

    override suspend fun getAllCharacters(forceRefresh: Boolean, newPage: Boolean): List<CharacterEntity> {
        val localData = dao.getAllCharacters()

        _nextPage = localData.size / 20 + 1

        Log.v("WHAT", "Recieved a query with force refresh parameter = $forceRefresh and localData size = ${localData.size} and newPage = $newPage and current page is $_nextPage")
        if (localData.isEmpty() || forceRefresh) {
            val remoteData = apiService.getAllCharacters(_nextPage)
            if (localData.isEmpty()) {
                val data : List<CharacterModel> = remoteData.results.map({CharacterMapper.mapDTOtoModel(it)})
                dao.insertAll(data)
            }
            if (!localData.isEmpty()) {
                return localData.map { CharacterMapper.mapModelToEntity(it) }
            }
            return remoteData.results.map({CharacterMapper.mapDTOtoEntity(it)})
        }

        if (newPage) {
            val remoteData = apiService.getAllCharacters(_nextPage)
            _nextPage += 1
            Log.v("MOCO", remoteData.toString() + " " + localData.size)
            if (_nextPage * 20 != localData.size) {
                val data : List<CharacterModel> = remoteData.results.map({CharacterMapper.mapDTOtoModel(it)})
                dao.insertAll(data)
                Log.v("HOEH", data.toString())
            }
            return dao.getAllCharacters().map { CharacterMapper.mapModelToEntity(it) }
        }

        return localData.map { CharacterMapper.mapModelToEntity(it) }
    }
}