package com.example.rickandmortycharacterview.data.dto

import kotlinx.serialization.Serializable


@Serializable
data class CharacterListDTO (
    val info: CharacterListInfoDTO,
    val results: List<CharacterDTO>,
)