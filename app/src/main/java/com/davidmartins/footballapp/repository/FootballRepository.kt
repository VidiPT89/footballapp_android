package com.davidmartins.footballapp.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.davidmartins.footballapp.model.FavoritoEquipa
import com.davidmartins.footballapp.model.FavoritoLocal
import com.davidmartins.footballapp.model.Liga
import com.davidmartins.footballapp.network.MatchApi
import com.davidmartins.footballapp.network.MatchesResponse
import com.davidmartins.footballapp.network.RetrofitInstance
import com.davidmartins.footballapp.network.StandingsResponse
import com.davidmartins.footballapp.network.TeamDetailResponse
import com.davidmartins.footballapp.network.TeamStanding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Singleton para gerir dados de futebol: API + favoritos locais
object FootballRepository {

    // SharedPreferences para favoritos locais
    private lateinit var prefs: SharedPreferences
    private val gson = Gson()

    // Liga selecionada
    private val _ligaSelecionada = MutableStateFlow(Liga(2021, "Premier League", "Inglaterra"))
    val ligaSelecionada: StateFlow<Liga> = _ligaSelecionada.asStateFlow()

    // Jogos da liga
    private val _jogos = MutableStateFlow<List<MatchApi>>(emptyList())
    val jogos: StateFlow<List<MatchApi>> = _jogos.asStateFlow()

    private val _jogoSelecionado = MutableStateFlow<MatchApi?>(null)
    val jogoSelecionado: StateFlow<MatchApi?> = _jogoSelecionado.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _erro = MutableStateFlow<String?>(null)
    val erro: StateFlow<String?> = _erro.asStateFlow()

    // Favoritos guardados localmente
    private val _favoritos = MutableStateFlow<List<FavoritoLocal>>(emptyList())
    val favoritos: StateFlow<List<FavoritoLocal>> = _favoritos.asStateFlow()

    private val _favoritosEquipas = MutableStateFlow<List<FavoritoEquipa>>(emptyList())
    val favoritosEquipas: StateFlow<List<FavoritoEquipa>> = _favoritosEquipas.asStateFlow()

    // Classificação da liga
    private val _classificacao = MutableStateFlow<List<TeamStanding>>(emptyList())
    val classificacao: StateFlow<List<TeamStanding>> = _classificacao.asStateFlow()

    private val _isLoadingClassificacao = MutableStateFlow(false)
    val isLoadingClassificacao: StateFlow<Boolean> = _isLoadingClassificacao.asStateFlow()

    private val _erroClassificacao = MutableStateFlow<String?>(null)
    val erroClassificacao: StateFlow<String?> = _erroClassificacao.asStateFlow()

    // Detalhe da equipa
    private val _equipaDetalhe = MutableStateFlow<TeamDetailResponse?>(null)
    val equipaDetalhe: StateFlow<TeamDetailResponse?> = _equipaDetalhe.asStateFlow()

    private val _isLoadingEquipa = MutableStateFlow(false)
    val isLoadingEquipa: StateFlow<Boolean> = _isLoadingEquipa.asStateFlow()

    private val _erroEquipa = MutableStateFlow<String?>(null)
    val erroEquipa: StateFlow<String?> = _erroEquipa.asStateFlow()

    // Inicialização do Repository (deve ser chamado antes de usar)
    fun init(context: Context) {
        if (!::prefs.isInitialized) {
            prefs = context.getSharedPreferences("football_prefs", Context.MODE_PRIVATE)
            carregarFavoritos()
            carregarFavoritosEquipas()
            carregarJogos()
        }
    }

    fun selecionarLiga(liga: Liga) {
        if (_ligaSelecionada.value.id == liga.id) return
        _ligaSelecionada.value = liga
        _jogos.value = emptyList()
        _jogoSelecionado.value = null
        _classificacao.value = emptyList()
        _erro.value = null
        _erroClassificacao.value = null
        carregarJogos()
    }

    fun carregarJogos() {
        _isLoading.value = true
        _erro.value = null
        val ligaId = _ligaSelecionada.value.id

        RetrofitInstance.api.getMatches(ligaId).enqueue(object : Callback<MatchesResponse> {
            override fun onResponse(call: Call<MatchesResponse>, response: Response<MatchesResponse>) {
                if (response.isSuccessful) {
                    _jogos.value = response.body()?.matches ?: emptyList()
                } else {
                    _erro.value = "Erro ${response.code()}: ${response.message()}"
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<MatchesResponse>, t: Throwable) {
                _erro.value = "Sem ligação à internet"
                _isLoading.value = false
            }
        })
    }

    fun selecionarJogo(jogo: MatchApi) {
        _jogoSelecionado.value = jogo
    }

    fun carregarClassificacao() {
        _isLoadingClassificacao.value = true
        _erroClassificacao.value = null
        val ligaId = _ligaSelecionada.value.id

        RetrofitInstance.api.getStandings(ligaId).enqueue(object : Callback<StandingsResponse> {
            override fun onResponse(call: Call<StandingsResponse>, response: Response<StandingsResponse>) {
                if (response.isSuccessful) {
                    val total = response.body()?.standings?.find { it.type == "TOTAL" }
                    _classificacao.value = total?.table ?: emptyList()
                } else {
                    _erroClassificacao.value = "Erro ${response.code()}: ${response.message()}"
                }
                _isLoadingClassificacao.value = false
            }

            override fun onFailure(call: Call<StandingsResponse>, t: Throwable) {
                _erroClassificacao.value = "Sem ligação à internet"
                _isLoadingClassificacao.value = false
            }
        })
    }

    fun carregarDetalheEquipa(teamId: Int) {
        _isLoadingEquipa.value = true
        _erroEquipa.value = null
        _equipaDetalhe.value = null

        RetrofitInstance.api.getTeamDetail(teamId).enqueue(object : Callback<TeamDetailResponse> {
            override fun onResponse(call: Call<TeamDetailResponse>, response: Response<TeamDetailResponse>) {
                if (response.isSuccessful) {
                    _equipaDetalhe.value = response.body()
                } else {
                    _erroEquipa.value = "Erro ${response.code()}: ${response.message()}"
                }
                _isLoadingEquipa.value = false
            }

            override fun onFailure(call: Call<TeamDetailResponse>, t: Throwable) {
                _erroEquipa.value = "Sem ligação à internet"
                _isLoadingEquipa.value = false
            }
        })
    }

    private fun carregarFavoritos() {
        val json = prefs.getString("favoritos", null)
        if (json != null) {
            val tipo = object : TypeToken<List<FavoritoLocal>>() {}.type
            _favoritos.value = gson.fromJson(json, tipo)
        }
    }

    private fun guardarFavoritos() {
        prefs.edit().putString("favoritos", gson.toJson(_favoritos.value)).apply()
    }

    fun adicionarFavorito(jogo: MatchApi, nota: String) {
        val novo = FavoritoLocal(
            jogoId = jogo.id,
            equipaCasa = jogo.homeTeam.name ?: "?",
            equipaFora = jogo.awayTeam.name ?: "?",
            data = jogo.utcDate,
            nota = nota
        )
        _favoritos.value = _favoritos.value + novo
        guardarFavoritos()
    }

    fun editarFavorito(favorito: FavoritoLocal) {
        _favoritos.value = _favoritos.value.map { if (it.id == favorito.id) favorito else it }
        guardarFavoritos()
    }

    fun removerFavorito(id: String) {
        _favoritos.value = _favoritos.value.filter { it.id != id }
        guardarFavoritos()
    }

    fun getFavoritoPorId(id: String): FavoritoLocal? = _favoritos.value.find { it.id == id }

    fun estaEmFavoritos(jogoId: Int): Boolean = _favoritos.value.any { it.jogoId == jogoId }

    private fun carregarFavoritosEquipas() {
        val json = prefs.getString("favoritos_equipas", null)
        if (json != null) {
            val tipo = object : TypeToken<List<FavoritoEquipa>>() {}.type
            _favoritosEquipas.value = gson.fromJson(json, tipo)
        }
    }

    private fun guardarFavoritosEquipas() {
        prefs.edit().putString("favoritos_equipas", gson.toJson(_favoritosEquipas.value)).apply()
    }

    fun adicionarFavoritoEquipa(equipa: TeamDetailResponse) {
        val novo = FavoritoEquipa(
            teamId = equipa.id,
            nome = equipa.name,
            crest = equipa.crest
        )
        _favoritosEquipas.value = _favoritosEquipas.value + novo
        guardarFavoritosEquipas()
    }

    fun removerFavoritoEquipa(id: String) {
        _favoritosEquipas.value = _favoritosEquipas.value.filter { it.id != id }
        guardarFavoritosEquipas()
    }
}
