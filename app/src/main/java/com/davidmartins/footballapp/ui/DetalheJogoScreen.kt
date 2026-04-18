package com.davidmartins.footballapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.size
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.CircleShape
import coil3.compose.AsyncImage
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import com.davidmartins.footballapp.viewmodel.DetalheJogoViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalheJogoScreen(
    viewModel: DetalheJogoViewModel,
    onAdicionarFavorito: () -> Unit,
    onEditarFavorito: (String) -> Unit,
    onVerEquipa: (Int) -> Unit,
    onVoltar: () -> Unit
) {
    val jogo by viewModel.jogoSelecionado.collectAsState()
    val favoritos by viewModel.favoritos.collectAsState()
    val liga by viewModel.ligaSelecionada.collectAsState()

    val favoritoExistente = jogo?.let { j -> favoritos.find { it.jogoId == j.id } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhe do Jogo") },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        if (jogo == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Jogo não encontrado")
                Button(onClick = onVoltar, modifier = Modifier.padding(top = 16.dp)) {
                    Text("Voltar")
                }
            }
        } else {
            val jogoAtual = jogo!!
            val equipaCasa = jogoAtual.homeTeam.name ?: "?"
            val equipaFora = jogoAtual.awayTeam.name ?: "?"
            val halfTime = jogoAtual.score.halfTime

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Info geral
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Jornada ${jogoAtual.matchday ?: "-"}  ·  ${formatarDataCompleta(jogoAtual.utcDate)}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Equipa casa
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        jogoAtual.homeTeam.id?.let { onVerEquipa(it) }
                                    }
                            ) {
                                AsyncImage(
                                    model = jogoAtual.homeTeam.crest,
                                    contentDescription = equipaCasa,
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(CircleShape)
                                )
                                Text(
                                    text = equipaCasa,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }

                            // Resultado / estado
                            if (jogoAtual.status == "FINISHED") {
                                Text(
                                    text = "${jogoAtual.score.fullTime?.home ?: "-"}  -  ${jogoAtual.score.fullTime?.away ?: "-"}",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 28.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                            } else {
                                Text(
                                    text = traduzirStatus(jogoAtual.status),
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = when (jogoAtual.status) {
                                        "IN_PLAY", "LIVE", "PAUSED" -> Color(0xFFFF9800)
                                        else -> Color.Gray
                                    },
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                            }

                            // Equipa fora
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        jogoAtual.awayTeam.id?.let { onVerEquipa(it) }
                                    }
                            ) {
                                AsyncImage(
                                    model = jogoAtual.awayTeam.crest,
                                    contentDescription = equipaFora,
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(CircleShape)
                                )
                                Text(
                                    text = equipaFora,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }

                        // Resultado ao intervalo
                        if (jogoAtual.status == "FINISHED" && halfTime != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Intervalo: ${halfTime.home ?: "-"} - ${halfTime.away ?: "-"}",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                HorizontalDivider()

                // Informação adicional
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        InfoRow(label = "Competição", valor = liga.nome)
                        InfoRow(label = "Estado", valor = traduzirStatus(jogoAtual.status))
                        InfoRow(label = "Data", valor = formatarDataCompleta(jogoAtual.utcDate))
                        jogoAtual.matchday?.let {
                            InfoRow(label = "Jornada", valor = it.toString())
                        }
                    }
                }

                // Botão favorito
                if (favoritoExistente == null) {
                    Button(
                        onClick = onAdicionarFavorito,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Adicionar aos Favoritos")
                    }
                } else {
                    Button(
                        onClick = { onEditarFavorito(favoritoExistente.id) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Editar nota do Favorito")
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, valor: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray, fontSize = 14.sp)
        Text(text = valor, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
    }
}

// Traduz os estados dos jogos para português
fun traduzirStatus(status: String): String = when (status) {
    "SCHEDULED" -> "Agendado"
    "TIMED"     -> "Confirmado"
    "LIVE",
    "IN_PLAY"   -> "A decorrer"
    "PAUSED"    -> "Intervalo"
    "FINISHED"  -> "Terminado"
    "POSTPONED" -> "Adiado"
    "CANCELLED" -> "Cancelado"
    "SUSPENDED" -> "Suspenso"
    else        -> status
}

// Formata data UTC para dd/MM/yy HH:mm
fun formatarData(utcDate: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = inputFormat.parse(utcDate)
        val outputFormat = SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault())
        outputFormat.format(date ?: return utcDate)
    } catch (e: Exception) {
        utcDate.take(10)
    }
}

// Formata data UTC para o formato extenso em português
fun formatarDataCompleta(utcDate: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = inputFormat.parse(utcDate)
        val outputFormat = SimpleDateFormat("dd 'de' MMMM 'de' yyyy  HH:mm", Locale.forLanguageTag("pt-PT"))
        outputFormat.format(date ?: return utcDate)
    } catch (e: Exception) {
        utcDate.take(10)
    }
}
