package com.davidmartins.footballapp.model

import java.util.UUID

data class FavoritoEquipa(
    val id: String = UUID.randomUUID().toString(),
    val teamId: Int,
    val nome: String? = null,
    val crest: String? = null
)
