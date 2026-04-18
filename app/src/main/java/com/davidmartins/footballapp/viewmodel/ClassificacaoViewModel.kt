package com.davidmartins.footballapp.viewmodel

import androidx.lifecycle.ViewModel
import com.davidmartins.footballapp.repository.FootballRepository

class ClassificacaoViewModel : ViewModel() {

    val classificacao = FootballRepository.classificacao
    val isLoading = FootballRepository.isLoadingClassificacao
    val erro = FootballRepository.erroClassificacao
    val ligaSelecionada = FootballRepository.ligaSelecionada

    fun carregarClassificacao() = FootballRepository.carregarClassificacao()
}
