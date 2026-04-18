package com.davidmartins.footballapp.viewmodel

import androidx.lifecycle.ViewModel
import com.davidmartins.footballapp.model.FavoritoLocal
import com.davidmartins.footballapp.network.MatchApi
import com.davidmartins.footballapp.repository.FootballRepository

class AdicionarEditarViewModel : ViewModel() {

    val jogoSelecionado = FootballRepository.jogoSelecionado

    fun getFavoritoPorId(id: String): FavoritoLocal? = FootballRepository.getFavoritoPorId(id)
    fun adicionarFavorito(jogo: MatchApi, nota: String) = FootballRepository.adicionarFavorito(jogo, nota)
    fun editarFavorito(favorito: FavoritoLocal) = FootballRepository.editarFavorito(favorito)
}
