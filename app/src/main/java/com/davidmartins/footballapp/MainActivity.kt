package com.davidmartins.footballapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.davidmartins.footballapp.ui.AdicionarEditarFavoritoScreen
import com.davidmartins.footballapp.ui.ClassificacaoScreen
import com.davidmartins.footballapp.ui.DetalheEquipaScreen
import com.davidmartins.footballapp.ui.DetalheJogoScreen
import com.davidmartins.footballapp.ui.FavoritosScreen
import com.davidmartins.footballapp.ui.ListaJogosScreen
import com.davidmartins.footballapp.ui.SelecionarLigaScreen
import com.davidmartins.footballapp.ui.theme.ProjetoFinalTheme
import com.davidmartins.footballapp.viewmodel.AdicionarEditarViewModel
import com.davidmartins.footballapp.viewmodel.ClassificacaoViewModel
import com.davidmartins.footballapp.viewmodel.DetalheEquipaViewModel
import com.davidmartins.footballapp.viewmodel.DetalheJogoViewModel
import com.davidmartins.footballapp.viewmodel.FavoritosViewModel
import com.davidmartins.footballapp.viewmodel.ListaJogosViewModel
import com.davidmartins.footballapp.viewmodel.SelecionarLigaViewModel

class MainActivity : ComponentActivity() {

    private val selecionarLigaViewModel: SelecionarLigaViewModel by viewModels()
    private val listaViewModel: ListaJogosViewModel by viewModels()
    private val detalheViewModel: DetalheJogoViewModel by viewModels()
    private val favoritosViewModel: FavoritosViewModel by viewModels()
    private val adicionarEditarViewModel: AdicionarEditarViewModel by viewModels()
    private val classificacaoViewModel: ClassificacaoViewModel by viewModels()
    private val detalheEquipaViewModel: DetalheEquipaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjetoFinalTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "selecionar_liga"
                ) {

                    composable("selecionar_liga") {
                        SelecionarLigaScreen(
                            viewModel = selecionarLigaViewModel,
                            onLigaSelecionada = {
                                navController.navigate("lista")
                            },
                            onVerFavoritos = {
                                navController.navigate("favoritos")
                            }
                        )
                    }

                    composable("lista") {
                        ListaJogosScreen(
                            viewModel = listaViewModel,
                            onVerDetalhe = { jogo ->
                                listaViewModel.selecionarJogo(jogo)
                                navController.navigate("detalhe")
                            },
                            onVerFavoritos = {
                                navController.navigate("favoritos")
                            },
                            onVerClassificacao = {
                                navController.navigate("classificacao")
                            },
                            onTrocarLiga = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable("detalhe") {
                        DetalheJogoScreen(
                            viewModel = detalheViewModel,
                            onAdicionarFavorito = {
                                navController.navigate("adicionar_favorito")
                            },
                            onEditarFavorito = { favId ->
                                navController.navigate("editar_favorito/$favId")
                            },
                            onVerEquipa = { teamId ->
                                navController.navigate("equipa/$teamId")
                            },
                            onVoltar = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable("adicionar_favorito") {
                        AdicionarEditarFavoritoScreen(
                            favoritoId = null,
                            viewModel = adicionarEditarViewModel,
                            onGuardar = { navController.popBackStack() },
                            onCancelar = { navController.popBackStack() }
                        )
                    }

                    composable(
                        route = "editar_favorito/{favId}",
                        arguments = listOf(navArgument("favId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val favId = backStackEntry.arguments?.getString("favId") ?: ""
                        AdicionarEditarFavoritoScreen(
                            favoritoId = favId,
                            viewModel = adicionarEditarViewModel,
                            onGuardar = { navController.popBackStack() },
                            onCancelar = { navController.popBackStack() }
                        )
                    }

                    composable("favoritos") {
                        FavoritosScreen(
                            viewModel = favoritosViewModel,
                            onEditar = { favId ->
                                navController.navigate("editar_favorito/$favId")
                            },
                            onVoltar = { navController.popBackStack() }
                        )
                    }

                    composable("classificacao") {
                        ClassificacaoScreen(
                            viewModel = classificacaoViewModel,
                            onVerEquipa = { teamId ->
                                navController.navigate("equipa/$teamId")
                            },
                            onVoltar = { navController.popBackStack() }
                        )
                    }

                    composable(
                        route = "equipa/{teamId}",
                        arguments = listOf(navArgument("teamId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val teamId = backStackEntry.arguments?.getInt("teamId") ?: 0
                        DetalheEquipaScreen(
                            teamId = teamId,
                            viewModel = detalheEquipaViewModel,
                            onVoltar = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}