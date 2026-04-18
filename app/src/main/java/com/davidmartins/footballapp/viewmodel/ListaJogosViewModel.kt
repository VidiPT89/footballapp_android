package com.davidmartins.footballapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.davidmartins.footballapp.network.MatchApi
import com.davidmartins.footballapp.repository.FootballRepository

class ListaJogosViewModel(application: Application) : AndroidViewModel(application) {

    init {
        FootballRepository.init(application)
    }

    val jogos = FootballRepository.jogos
    val isLoading = FootballRepository.isLoading
    val erro = FootballRepository.erro
    val ligaSelecionada = FootballRepository.ligaSelecionada

    fun carregarJogos() = FootballRepository.carregarJogos()
    fun selecionarJogo(jogo: MatchApi) = FootballRepository.selecionarJogo(jogo)
    fun estaEmFavoritos(jogoId: Int) = FootballRepository.estaEmFavoritos(jogoId)
}
