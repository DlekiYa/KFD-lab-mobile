package com.example.rickandmortycharacterview.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CharacterListInfoDTO (
    val count: Int=0,
    val pages: Int=0,
    val next: String?=null,
    val prev: String?=null,
)