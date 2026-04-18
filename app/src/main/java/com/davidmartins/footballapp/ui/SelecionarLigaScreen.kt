package com.davidmartins.footballapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.davidmartins.footballapp.model.Liga
import com.davidmartins.footballapp.viewmodel.SelecionarLigaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelecionarLigaScreen(
    viewModel: SelecionarLigaViewModel,
    onLigaSelecionada: () -> Unit,
    onVerFavoritos: () -> Unit
) {
    val ligas = listOf(
        Liga(2021, "Premier League", "Inglaterra 🏴󠁧󠁢󠁥󠁮󠁧󠁿"),
        Liga(2017, "Primeira Liga", "Portugal 🇵🇹")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Football App", fontWeight = FontWeight.Bold)
                        Text("Escolhe uma liga", fontSize = 12.sp, color = Color.Gray)
                    }
                },
                actions = {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ligas.forEach { liga ->
                LigaCard(
                    liga = liga,
                    onClick = {
                        viewModel.selecionarLiga(liga)
                        onLigaSelecionada()
                    }
                )
            }
        }
    }
}

@Composable
fun LigaCard(liga: Liga, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = liga.pais,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
                Text(
                    text = liga.nome,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Entrar",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
