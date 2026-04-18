package com.davidmartins.footballapp.model

import java.util.UUID

data class FavoritoLocal(
    val id: String = UUID.randomUUID().toString(),
    val jogoId: Int,
    val equipaCasa: String? = null,
    val equipaFora: String? = null,
    val data: String? = null,
    val nota: String = ""
)
