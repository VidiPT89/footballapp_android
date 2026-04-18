package com.davidmartins.footballapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
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
import com.davidmartins.footballapp.network.TeamStanding
import com.davidmartins.footballapp.viewmodel.ClassificacaoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassificacaoScreen(
    viewModel: ClassificacaoViewModel,
    onVerEquipa: (Int) -> Unit,
    onVoltar: () -> Unit
) {
    val classificacao by viewModel.classificacao.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val erro by viewModel.erro.collectAsState()
    val liga by viewModel.ligaSelecionada.collectAsState()

    LaunchedEffect(liga.id) {
        viewModel.carregarClassificacao()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Classificação", fontWeight = FontWeight.Bold)
                        Text(liga.nome, fontSize = 12.sp, color = Color.Gray)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        when {
            isLoading || (classificacao.isEmpty() && erro == null) -> {
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
                        onClick = { viewModel.carregarClassificacao() },
                        modifier = Modifier.padding(top = 16.dp)
                    ) { Text("Tentar novamente") }
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("#", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.weight(0.5f))
                            Text("Clube", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.weight(3f))
                            Text("J", fontSize = 12.sp, color = Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.weight(0.7f))
                            Text("V", fontSize = 12.sp, color = Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.weight(0.7f))
                            Text("E", fontSize = 12.sp, color = Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.weight(0.7f))
                            Text("D", fontSize = 12.sp, color = Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.weight(0.7f))
                            Text("DG", fontSize = 12.sp, color = Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.weight(0.8f))
                            Text("Pts", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.weight(0.8f))
                        }
                        HorizontalDivider()
                    }

                    items(classificacao) { standing ->
                        ClassificacaoRow(
                            standing = standing,
                            onClick = {
                                standing.team.id?.let { onVerEquipa(it) }
                            }
                        )
                    }
                }
            }
        }
    }
}

// Cor de fundo conforme a posição: Champions, Europa, descida
@Composable
fun ClassificacaoRow(standing: TeamStanding, onClick: () -> Unit) {
    val corFundo = when (standing.position) {
        in 1..4  -> Color(0xFF4CAF50).copy(alpha = 0.1f)
        5        -> Color(0xFFFF9800).copy(alpha = 0.1f)
        in 18..20 -> Color(0xFFF44336).copy(alpha = 0.1f)
        else     -> Color.Transparent
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = corFundo)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${standing.position}",
                fontSize = 13.sp,
                fontWeight = if (standing.position <= 4) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier.weight(0.5f)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(3f)
            ) {
                AsyncImage(
                    model = standing.team.crest,
                    contentDescription = standing.team.name,
                    modifier = Modifier.size(22.dp).clip(CircleShape)
                )
                Text(
                    text = standing.team.shortName ?: standing.team.name ?: "",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 6.dp)
                )
            }

            Text("${standing.playedGames}", fontSize = 13.sp, textAlign = TextAlign.Center, modifier = Modifier.weight(0.7f))
            Text("${standing.won}", fontSize = 13.sp, textAlign = TextAlign.Center, modifier = Modifier.weight(0.7f))
            Text("${standing.draw}", fontSize = 13.sp, textAlign = TextAlign.Center, modifier = Modifier.weight(0.7f))
            Text("${standing.lost}", fontSize = 13.sp, textAlign = TextAlign.Center, modifier = Modifier.weight(0.7f))
            Text(
                text = if (standing.goalDifference >= 0) "+${standing.goalDifference}" else "${standing.goalDifference}",
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                color = when {
                    standing.goalDifference > 0 -> Color(0xFF4CAF50)
                    standing.goalDifference < 0 -> Color(0xFFF44336)
                    else -> Color.Gray
                },
                modifier = Modifier.weight(0.8f)
            )
            Text(
                text = "${standing.points}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(0.8f)
            )
        }
    }
}
