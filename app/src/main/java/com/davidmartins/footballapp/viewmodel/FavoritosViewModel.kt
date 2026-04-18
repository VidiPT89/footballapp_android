package com.davidmartins.footballapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.davidmartins.footballapp.repository.FootballRepository

class FavoritosViewModel(application: Application) : AndroidViewModel(application) {

    init {
        FootballRepository.init(application)
    }

    val favoritos = FootballRepository.favoritos
    val favoritosEquipas = FootballRepository.favoritosEquipas

    fun removerFavorito(id: String) = FootballRepository.removerFavorito(id)
    fun removerFavoritoEquipa(id: String) = FootballRepository.removerFavoritoEquipa(id)
}
