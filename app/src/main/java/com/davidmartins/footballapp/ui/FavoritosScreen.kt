package com.davidmartins.footballapp.ui

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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.davidmartins.footballapp.model.FavoritoEquipa
import com.davidmartins.footballapp.model.FavoritoLocal
import com.davidmartins.footballapp.viewmodel.FavoritosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritosScreen(
    viewModel: FavoritosViewModel,
    onEditar: (String) -> Unit,
    onVoltar: () -> Unit
) {
    val favoritos by viewModel.favoritos.collectAsState()
    val favoritosEquipas by viewModel.favoritosEquipas.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Os Meus Favoritos") },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        if (favoritos.isEmpty() && favoritosEquipas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Ainda não tens favoritos.\nAbre um jogo ou clube e adiciona-o!",
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                // Secção de jogos favoritos
                if (favoritos.isNotEmpty()) {
                    item {
                        Text(
                            text = "Jogos",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                    items(favoritos) { favorito ->
                        FavoritoRow(
                            favorito = favorito,
                            onEditar = { onEditar(favorito.id) },
                            onRemover = { viewModel.removerFavorito(favorito.id) }
                        )
                    }
                }

                // Secção de clubes favoritos
                if (favoritosEquipas.isNotEmpty()) {
                    item {
                        if (favoritos.isNotEmpty()) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        }
                        Text(
                            text = "Clubes",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                    items(favoritosEquipas) { favorito ->
                        FavoritoEquipaRow(
                            favorito = favorito,
                            onRemover = { viewModel.removerFavoritoEquipa(favorito.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FavoritoRow(
    favorito: FavoritoLocal,
    onEditar: () -> Unit,
    onRemover: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${favorito.equipaCasa ?: "?"}  vs  ${favorito.equipaFora ?: "?"}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = favorito.data?.let { formatarData(it) } ?: "",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 2.dp)
            )
            if (favorito.nota.isNotBlank()) {
                Text(
                    text = favorito.nota,
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = onEditar) {
                    Text("Editar")
                }
                Button(
                    onClick = onRemover,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Remover")
                }
            }
        }
    }
}

@Composable
fun FavoritoEquipaRow(
    favorito: FavoritoEquipa,
    onRemover: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = favorito.crest,
                contentDescription = favorito.nome,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Text(
                text = favorito.nome ?: "?",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            )
            Button(
                onClick = onRemover,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Remover")
            }
        }
    }
}
