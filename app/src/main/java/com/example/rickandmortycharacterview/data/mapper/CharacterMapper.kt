package com.example.rickandmortycharacterview.data.mapper

import com.example.rickandmortycharacterview.data.dto.CharacterDTO
import com.example.rickandmortycharacterview.domain.entity.CharacterEntity
import android.net.Uri
import com.example.rickandmortycharacterview.data.model.CharacterModel

abstract class CharacterMapper {
    companion object {
        fun mapDTOtoEntity(dto: CharacterDTO): CharacterEntity {
            return CharacterEntity(
                name = dto.name,
                image = Uri.parse(dto.image),
                status = dto.status,
                species = dto.species,
            )
        }

        fun mapModelToEntity(model: CharacterModel): CharacterEntity {
            return CharacterEntity(
                name = model.name,
                image = Uri.parse(model.image),
                status = model.status,
                species = model.species
            )
        }

        fun mapDTOtoModel(dto: CharacterDTO): CharacterModel {
            return CharacterModel(
                name = dto.name,
                image = dto.image,
                status = dto.status,
                species = dto.species,
                id = dto.id,
            )
        }
    }
}