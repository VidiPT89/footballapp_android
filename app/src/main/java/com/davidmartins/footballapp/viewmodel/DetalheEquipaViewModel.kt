package com.davidmartins.footballapp.viewmodel

import androidx.lifecycle.ViewModel
import com.davidmartins.footballapp.network.TeamDetailResponse
import com.davidmartins.footballapp.repository.FootballRepository

class DetalheEquipaViewModel : ViewModel() {

    val equipaDetalhe = FootballRepository.equipaDetalhe
    val isLoading = FootballRepository.isLoadingEquipa
    val erro = FootballRepository.erroEquipa
    val favoritosEquipas = FootballRepository.favoritosEquipas

    fun carregarEquipa(teamId: Int) = FootballRepository.carregarDetalheEquipa(teamId)
    fun adicionarFavoritoEquipa(equipa: TeamDetailResponse) = FootballRepository.adicionarFavoritoEquipa(equipa)
    fun removerFavoritoEquipa(id: String) = FootballRepository.removerFavoritoEquipa(id)
}
