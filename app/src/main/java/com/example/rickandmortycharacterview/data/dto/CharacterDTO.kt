package com.example.rickandmortycharacterview.data.dto

import javax.net.ssl.SSLEngineResult.Status

import kotlinx.serialization.Serializable

@Serializable
data class CharacterDTO (
    val id: Int,
    val name: String="",
    val status: String="",
    val species: String="",
    val gender: String="",
    val image: String="",
)

/* {
  "id": 2,
  "name": "Morty Smith",
  "status": "Alive",
  "species": "Human",
  "type": "",
  "gender": "Male",
  "origin": {
    "name": "Earth",
    "url": "https://rickandmortyapi.com/api/location/1"
  },
  "location": {
    "name": "Earth",
    "url": "https://rickandmortyapi.com/api/location/20"
  },
  "image": "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
  "episode": [
    "https://rickandmortyapi.com/api/episode/1",
    "https://rickandmortyapi.com/api/episode/2",
    // ...
  ],
  "url": "https://rickandmortyapi.com/api/character/2",
  "created": "2017-11-04T18:50:21.651Z"
} */