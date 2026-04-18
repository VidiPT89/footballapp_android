package com.davidmartins.footballapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.davidmartins.footballapp.viewmodel.AdicionarEditarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdicionarEditarFavoritoScreen(
    favoritoId: String?,
    viewModel: AdicionarEditarViewModel,
    onGuardar: () -> Unit,
    onCancelar: () -> Unit
) {
    val jogoSelecionado by viewModel.jogoSelecionado.collectAsState()
    val favoritoExistente = favoritoId?.let { viewModel.getFavoritoPorId(it) }
    val eModoEdicao = favoritoExistente != null

    val equipaCasa = favoritoExistente?.equipaCasa ?: jogoSelecionado?.homeTeam?.name ?: "?"
    val equipaFora = favoritoExistente?.equipaFora ?: jogoSelecionado?.awayTeam?.name ?: "?"

    var nota by remember { mutableStateOf(favoritoExistente?.nota ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (eModoEdicao) "Editar Favorito" else "Adicionar Favorito") },
                navigationIcon = {
                    IconButton(onClick = onCancelar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Cancelar")
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "$equipaCasa  vs  $equipaFora",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            jogoSelecionado?.let {
                Text(
                    text = formatarData(it.utcDate),
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }

            Text(
                text = "Adiciona uma nota sobre este jogo:",
                color = Color.Gray
            )

            OutlinedTextField(
                value = nota,
                onValueChange = { nota = it },
                label = { Text("A tua nota") },
                placeholder = { Text("Ex: Jogo decisivo para o título!") },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        if (favoritoExistente != null) {
                            viewModel.editarFavorito(favoritoExistente.copy(nota = nota.trim()))
                        } else {
                            jogoSelecionado?.let { jogo ->
                                viewModel.adicionarFavorito(jogo, nota.trim())
                            }
                        }
                        onGuardar()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Guardar")
                }

                Button(
                    onClick = onCancelar,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Cancelar")
                }
            }
        }
    }
}
