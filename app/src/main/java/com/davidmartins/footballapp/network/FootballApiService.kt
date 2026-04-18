package com.davidmartins.footballapp.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

// Define os endpoints da API de futebol usando Retrofit
interface FootballApiService {

    // Endpoint para obter os jogos de uma competição
    @GET("competitions/{id}/matches")
    fun getMatches(@Path("id") competitionId: Int): Call<MatchesResponse>

    // Endpoint para obter a classificação de uma competição específica
    @GET("competitions/{id}/standings")
    fun getStandings(@Path("id") competitionId: Int): Call<StandingsResponse>

    // Endpoint para obter os detalhes de uma equipa específica
    @GET("teams/{id}")
    fun getTeamDetail(@Path("id") teamId: Int): Call<TeamDetailResponse>
}
