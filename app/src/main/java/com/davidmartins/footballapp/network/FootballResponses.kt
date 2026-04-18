package com.davidmartins.footballapp.network

import com.google.gson.annotations.SerializedName

data class MatchesResponse(
    @SerializedName("matches") val matches: List<MatchApi>
)

data class MatchApi(
    @SerializedName("id") val id: Int,
    @SerializedName("utcDate") val utcDate: String,
    @SerializedName("status") val status: String,
    @SerializedName("matchday") val matchday: Int?,
    @SerializedName("homeTeam") val homeTeam: TeamApi,
    @SerializedName("awayTeam") val awayTeam: TeamApi,
    @SerializedName("score") val score: ScoreApi
)

// Referência de equipa (usada em jogos e classificação)
data class TeamApi(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("shortName") val shortName: String?,
    @SerializedName("crest") val crest: String?
)

data class ScoreApi(
    @SerializedName("fullTime") val fullTime: ScoreTimeApi?,
    @SerializedName("halfTime") val halfTime: ScoreTimeApi?
)

// Golos por tempo
data class ScoreTimeApi(
    @SerializedName("home") val home: Int?,
    @SerializedName("away") val away: Int?
)

data class StandingsResponse(
    @SerializedName("standings") val standings: List<StandingGroup>
)

// Detalhes da Classificação
data class StandingGroup(
    @SerializedName("type") val type: String,
    @SerializedName("table") val table: List<TeamStanding>
)

// Detalhes da Classificação por Equipa
data class TeamStanding(
    @SerializedName("position") val position: Int,
    @SerializedName("team") val team: TeamApi,
    @SerializedName("playedGames") val playedGames: Int,
    @SerializedName("won") val won: Int,
    @SerializedName("draw") val draw: Int,
    @SerializedName("lost") val lost: Int,
    @SerializedName("points") val points: Int,
    @SerializedName("goalsFor") val goalsFor: Int,
    @SerializedName("goalsAgainst") val goalsAgainst: Int,
    @SerializedName("goalDifference") val goalDifference: Int
)

data class TeamDetailResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("shortName") val shortName: String?,
    @SerializedName("crest") val crest: String?,
    @SerializedName("founded") val founded: Int?,
    @SerializedName("venue") val venue: String?,
    @SerializedName("coach") val coach: CoachApi?,
    @SerializedName("squad") val squad: List<PlayerApi>
)

data class CoachApi(
    @SerializedName("name") val name: String?,
    @SerializedName("nationality") val nationality: String?
)

data class PlayerApi(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("position") val position: String?,
    @SerializedName("nationality") val nationality: String?,
    @SerializedName("dateOfBirth") val dateOfBirth: String?
)
