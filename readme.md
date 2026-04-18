# FootballApp

Aplicação Android para acompanhar futebol em tempo real — jogos, classificações, detalhes de equipas e favoritos pessoais.

## Funcionalidades

- **Selecção de Liga** — Premier League (Inglaterra) e Primeira Liga (Portugal)
- **Lista de Jogos** — marcadores, estado do jogo e equipas participantes
- **Detalhe do Jogo** — resultado final, intervalo e acesso rápido às equipas
- **Classificação** — tabela completa com pontos, golos e diferença de golos
- **Detalhe da Equipa** — informação do clube, treinador e plantel completo
- **Favoritos** — guarda jogos e equipas favoritas com notas pessoais

## Stack Tecnológico

| Camada | Tecnologia |
|---|---|
| Linguagem | Kotlin |
| UI | Jetpack Compose + Material Design 3 |
| Navegação | Navigation Compose 2.8.4 |
| Rede | Retrofit 2 + OkHttp 4 |
| Serialização | Gson 2.11 |
| Imagens | Coil 3 |
| Estado | ViewModel + StateFlow |
| Persistência local | SharedPreferences |
| Build | Gradle Kotlin DSL |

## Arquitectura

O projecto segue o padrão **MVVM com Repository**:

```
UI (Composables)
    ↕  StateFlow
ViewModel
    ↕
Repository  ──→  Retrofit (football-data.org API)
            ──→  SharedPreferences (favoritos locais)
```

## API

Integração com [football-data.org](https://www.football-data.org/) v4:
- `GET /competitions/{id}/matches` — jogos da liga
- `GET /competitions/{id}/standings` — classificação
- `GET /teams/{id}` — detalhe e plantel da equipa

Para correr o projecto precisas de uma API key gratuita do football-data.org. Coloca-a em `RetrofitInstance.kt`.

## Requisitos

- Android Studio Ladybug ou superior
- Android SDK 28+
- Java 11
- API key de [football-data.org](https://www.football-data.org/client/register)

## Como Correr

1. Clona o repositório:
   ```bash
   git clone https://github.com/teu-username/FootballApp.git
   ```
2. Abre o projecto no Android Studio
3. Substitui `SEU_TOKEN` pela tua API key em `app/src/main/java/com/davidmartins/footballapp/network/RetrofitInstance.kt`
4. Corre num emulador ou dispositivo físico (Android 9+)

## Estrutura do Projecto

```
app/src/main/java/com/davidmartins/footballapp/
├── model/          # Data classes (Liga, FavoritoLocal, FavoritoEquipa)
├── network/        # Retrofit, endpoints e DTOs da API
├── repository/     # Fonte única de dados (API + local)
├── ui/             # Ecrãs em Jetpack Compose
│   └── theme/      # Cores, tipografia e tema
└── viewmodel/      # ViewModels por ecrã
```

## Ecrãs

| Ecrã | Descrição |
|---|---|
| `SelecionarLigaScreen` | Escolha da liga e acesso aos favoritos |
| `ListaJogosScreen` | Lista de jogos da liga seleccionada |
| `DetalheJogoScreen` | Resultado e detalhes de um jogo |
| `ClassificacaoScreen` | Tabela classificativa da liga |
| `DetalheEquipaScreen` | Plantel e informação da equipa |
| `FavoritosScreen` | Jogos e equipas guardados |
| `AdicionarEditarFavoritoScreen` | Criar/editar nota num jogo favorito |

## Licença

MIT
## Estrutura do Projecto

```
app/src/main/java/com/davidmartins/footballapp/
├── model/          # Data classes (Liga, FavoritoLocal, FavoritoEquipa)
├── network/        # Retrofit, endpoints e DTOs da API
├── repository/     # Fonte única de dados (API + local)
├── ui/             # Ecrãs em Jetpack Compose
│   └── theme/      # Cores, tipografia e tema
└── viewmodel/      # ViewModels por ecrã
```

## Ecrãs

| Ecrã | Descrição |
|---|---|
| `SelecionarLigaScreen` | Escolha da liga e acesso aos favoritos |
| `ListaJogosScreen` | Lista de jogos da liga seleccionada |
| `DetalheJogoScreen` | Resultado e detalhes de um jogo |
| `ClassificacaoScreen` | Tabela classificativa da liga |
| `DetalheEquipaScreen` | Plantel e informação da equipa |
| `FavoritosScreen` | Jogos e equipas guardados |
| `AdicionarEditarFavoritoScreen` | Criar/editar nota num jogo favorito |

## Licença

MIT
