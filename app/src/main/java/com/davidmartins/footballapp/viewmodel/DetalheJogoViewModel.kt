package com.davidmartins.footballapp.viewmodel

import androidx.lifecycle.ViewModel
import com.davidmartins.footballapp.repository.FootballRepository

class DetalheJogoViewModel : ViewModel() {

    val jogoSelecionado = FootballRepository.jogoSelecionado
    val favoritos = FootballRepository.favoritos
    val ligaSelecionada = FootballRepository.ligaSelecionada
}
