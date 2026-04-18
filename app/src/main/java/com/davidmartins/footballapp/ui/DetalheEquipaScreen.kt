package com.davidmartins.footballapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.davidmartins.footballapp.network.PlayerApi
import com.davidmartins.footballapp.viewmodel.DetalheEquipaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalheEquipaScreen(
    teamId: Int,
    viewModel: DetalheEquipaViewModel,
    onVoltar: () -> Unit
) {
    val equipa by viewModel.equipaDetalhe.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val erro by viewModel.erro.collectAsState()
    val favoritosEquipas by viewModel.favoritosEquipas.collectAsState()
    val favoritoExistente = favoritosEquipas.find { it.teamId == teamId }

    LaunchedEffect(teamId) {
        viewModel.carregarEquipa(teamId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(equipa?.name ?: "Equipa") },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        when {
            isLoading || (equipa == null && erro == null) -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }

            erro != null -> {
                Column(
                    modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(erro!!, color = Color.Red, textAlign = TextAlign.Center)
                    Button(
                        onClick = { viewModel.carregarEquipa(teamId) },
                        modifier = Modifier.padding(top = 16.dp)
                    ) { Text("Tentar novamente") }
                }
            }

            equipa != null -> {
                val equipaAtual = equipa!!

                // Agrupar jogadores por posição
                val jogadoresPorPosicao = equipaAtual.squad
                    .groupBy { it.position ?: "Sem posição" }
                    .toSortedMap(compareBy {
                        when (it) {
                            "Goalkeeper" -> 0
                            "Defence" -> 1
                            "Midfield" -> 2
                            "Offence" -> 3
                            else -> 4
                        }
                    })

                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Cabeçalho do clube
                    item {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                AsyncImage(
                                    model = equipaAtual.crest,
                                    contentDescription = equipaAtual.name,
                                    modifier = Modifier.size(80.dp).clip(CircleShape)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = equipaAtual.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                // Botão favorito
                                if (favoritoExistente == null) {
                                    Button(
                                        onClick = { viewModel.adicionarFavoritoEquipa(equipaAtual) },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.FavoriteBorder,
                                            contentDescription = null,
                                            modifier = Modifier.padding(end = 6.dp)
                                        )
                                        Text("Adicionar aos Favoritos")
                                    }
                                } else {
                                    Button(
                                        onClick = { viewModel.removerFavoritoEquipa(favoritoExistente.id) },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Favorite,
                                            contentDescription = null,
                                            modifier = Modifier.padding(end = 6.dp)
                                        )
                                        Text("Remover dos Favoritos")
                                    }
                                }

                                // Informações adicionais do clube
                                Spacer(modifier = Modifier.height(8.dp))
                                HorizontalDivider()
                                Spacer(modifier = Modifier.height(8.dp))
                                equipaAtual.founded?.let {
                                    InfoRow(label = "Fundado em", valor = "$it")
                                }
                                equipaAtual.venue?.let {
                                    InfoRow(label = "Estádio", valor = it)
                                }
                                equipaAtual.coach?.name?.let {
                                    InfoRow(label = "Treinador", valor = it)
                                }
                                equipaAtual.coach?.nationality?.let {
                                    InfoRow(label = "Nacionalidade", valor = it)
                                }
                            }
                        }
                    }

                    // Plantel por posição
                    jogadoresPorPosicao.forEach { (posicao, jogadores) ->
                        item {
                            Text(
                                text = traduzirPosicao(posicao),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                            )
                        }
                        items(jogadores) { jogador ->
                            JogadorRow(jogador = jogador)
                        }
                    }
                }
            }
        }
    }
}

// Linha de cada jogador no plantel
@Composable
fun JogadorRow(jogador: PlayerApi) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = jogador.name,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )
            jogador.nationality?.let {
                Text(
                    text = it,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

// Traduz as posições dos jogadores para português
fun traduzirPosicao(posicao: String): String = when (posicao) {
    "Goalkeeper" -> "Guarda-Redes"
    "Defence"    -> "Defesas"
    "Midfield"   -> "Médios"
    "Offence"    -> "Avançados"
    else         -> posicao
}
