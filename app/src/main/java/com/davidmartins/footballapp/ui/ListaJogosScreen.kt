package com.davidmartins.footballapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.size
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.CircleShape
import coil3.compose.AsyncImage
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.davidmartins.footballapp.network.MatchApi
import com.davidmartins.footballapp.viewmodel.ListaJogosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaJogosScreen(
    viewModel: ListaJogosViewModel,
    onVerDetalhe: (MatchApi) -> Unit,
    onVerFavoritos: () -> Unit,
    onVerClassificacao: () -> Unit,
    onTrocarLiga: () -> Unit
) {
    val jogos by viewModel.jogos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val erro by viewModel.erro.collectAsState()
    val liga by viewModel.ligaSelecionada.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(liga.nome, fontWeight = FontWeight.Bold)
                        Text(liga.pais, fontSize = 12.sp, color = Color.Gray)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onTrocarLiga) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Trocar Liga"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onVerClassificacao) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.List,
                            contentDescription = "Classificação"
                        )
                    }
                    IconButton(onClick = onVerFavoritos) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Os meus favoritos",
                            tint = Color.Red
                        )
                    }
                }
            )
        }
    ) { padding ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            erro != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = erro!!, color = Color.Red, textAlign = TextAlign.Center)
                    Button(
                        onClick = { viewModel.carregarJogos() },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text("Tentar novamente")
                    }
                }
            }

            jogos.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Sem jogos disponíveis")
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(jogos) { jogo ->
                        JogoListaRow(
                            jogo = jogo,
                            eFavorito = viewModel.estaEmFavoritos(jogo.id),
                            onClick = { onVerDetalhe(jogo) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun JogoListaRow(
    jogo: MatchApi,
    eFavorito: Boolean,
    onClick: () -> Unit
) {
    val equipaCasa = jogo.homeTeam.shortName ?: jogo.homeTeam.name ?: "?"
    val equipaFora = jogo.awayTeam.shortName ?: jogo.awayTeam.name ?: "?"

    val corStatus = when (jogo.status) {
        "FINISHED" -> Color(0xFF4CAF50)
        "IN_PLAY", "LIVE", "PAUSED" -> Color(0xFFFF9800)
        else -> Color.Gray
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (eFavorito)
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Jornada + data + estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "J${jogo.matchday ?: "-"}",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
                Text(
                    text = traduzirStatus(jogo.status),
                    fontSize = 11.sp,
                    color = corStatus,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = formatarData(jogo.utcDate),
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }

            // Equipas + resultado
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Equipa casa
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    AsyncImage(
                        model = jogo.homeTeam.crest,
                        contentDescription = equipaCasa,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                    )
                    Text(
                        text = equipaCasa,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 6.dp)
                    )
                }

                if (jogo.status == "FINISHED") {
                    Text(
                        text = "${jogo.score.fullTime?.home ?: "-"} - ${jogo.score.fullTime?.away ?: "-"}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                } else {
                    Text(
                        text = "vs",
                        fontWeight = FontWeight.Light,
                        color = Color.Gray,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }

                // Equipa fora
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = equipaFora,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        textAlign = TextAlign.End,
                        modifier = Modifier.padding(end = 6.dp)
                    )
                    AsyncImage(
                        model = jogo.awayTeam.crest,
                        contentDescription = equipaFora,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                    )
                }
            }

            if (eFavorito) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favorito",
                        tint = Color.Red,
                        modifier = Modifier.padding(end = 2.dp)
                    )
                }
            }
        }
    }
}
