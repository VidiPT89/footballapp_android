package com.davidmartins.footballapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.davidmartins.footballapp.model.Liga
import com.davidmartins.footballapp.repository.FootballRepository

class SelecionarLigaViewModel(application: Application) : AndroidViewModel(application) {

    init {
        FootballRepository.init(application)
    }

    fun selecionarLiga(liga: Liga) = FootballRepository.selecionarLiga(liga)
}
