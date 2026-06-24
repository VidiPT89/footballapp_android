# ⚽ Football App Android — Premier League & Primeira Liga Companion

> A Jetpack Compose Android app for browsing matches, standings, teams, and managing favourites across two football leagues.

Follow two of Europe's top leagues — the English Premier League and the Portuguese Primeira Liga — all from one app. Browse live match results with full-time and half-time scores, explore detailed standings with positions, points, wins, draws, losses, and goal difference. Tap into any team to see their full profile including founding year, venue, head coach, and complete squad roster. Save your favourite matches and teams with personal notes, and manage your collection with full edit and delete support. All favourites persist locally using SharedPreferences and Gson serialisation.

## 📦 What's Inside

- 🏆 Two leagues in one app: Premier League + Portuguese Primeira Liga
- ⚽ Browse all matches with scores and fixture details
- 📊 Live standings table with positions, points, W/D/L, and goal difference
- 🔍 Match detail screen showing full-time and half-time scores with club crests
- 🏟️ Team detail with founded year, venue, head coach, and full squad roster
- ⭐ Save favourite matches with personal notes
- 💙 Save favourite teams to a dedicated collection
- ✏️ Edit favourites — update notes or details anytime
- 🗑️ Delete favourites you no longer follow
- 💾 Persistent storage with SharedPreferences + Gson serialisation
- 🎨 Material Design 3 UI with Jetpack Compose
- 🖼️ Async image loading with Coil 3 for crests and badges
- 🔐 OkHttp auth interceptor for automatic API key injection
- 🇵🇹 Portuguese variable and ViewModel names throughout the codebase

## 🛠️ Tech Stack

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=flat&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-4285F4?style=flat&logo=jetpackcompose&logoColor=white)
![Material Design 3](https://img.shields.io/badge/Material_Design_3-757575?style=flat&logo=materialdesign&logoColor=white)
![Retrofit](https://img.shields.io/badge/Retrofit_2-48B983?style=flat&logo=square&logoColor=white)
![OkHttp](https://img.shields.io/badge/OkHttp_4-48B983?style=flat&logo=square&logoColor=white)
![Coil](https://img.shields.io/badge/Coil_3-FF6B00?style=flat)
![Android](https://img.shields.io/badge/Min_SDK_28-3DDC84?style=flat&logo=android&logoColor=white)
![football-data.org](https://img.shields.io/badge/API-football--data.org_v4-green?style=flat)

## 🏗️ Architecture

The app follows **MVVM + Repository Pattern**, cleanly separating UI, business logic, and data access:

```
footballapp_android/
├── models/
│   ├── Liga.swift                    # League model (competition info, teams, matches)
│   ├── FavoritoLocal.swift           # Favourite match model (match data + notes)
│   └── FavoritoEquipa.swift          # Favourite team model (team data + notes)
├── viewmodels/
│   ├── SelecionarLigaViewModel.kt    # League selection logic (PL vs Primeira Liga)
│   ├── ListaJogosViewModel.kt        # Match list fetching and filtering
│   ├── DetalheJogoViewModel.kt       # Single match detail with scores
│   ├── ClassificacaoViewModel.kt     # Standings table data and sorting
│   ├── DetalheEquipaViewModel.kt     # Team profile, coach, and squad roster
│   ├── FavoritosViewModel.kt         # Favourites collection management
│   └── AdicionarEditarViewModel.kt   # Add/edit favourite form logic
├── repository/
│   └── FootballRepository.kt         # Single source of truth: remote + local
│       ├── Remote: Retrofit → football-data.org v4
│       └── Local: SharedPreferences + Gson
├── network/
│   ├── FootballApiService.kt         # Retrofit API interface definitions
│   └── RetrofitInstance.kt           # Retrofit + OkHttp client with auth interceptor
├── ui/
│   ├── screens/
│   │   ├── LeagueSelectionScreen.kt  # Choose Premier League or Primeira Liga
│   │   ├── MatchListScreen.kt        # Scrollable list of matches with scores
│   │   ├── MatchDetailScreen.kt      # Full-time/half-time scores with crests
│   │   ├── StandingsScreen.kt        # League table with all stats columns
│   │   ├── TeamDetailScreen.kt       # Team info, venue, coach, and full squad
│   │   ├── FavouritesScreen.kt       # Saved matches and teams collection
│   │   └── EditFavouriteScreen.kt    # Edit notes on a saved favourite
│   └── theme/
│       └── Theme.kt                  # Material Design 3 colour scheme and typography
└── app/
    └── MainActivity.kt               # App entry point and Compose navigation host
```

## 📱 Screens

| Screen | Description |
|--------|-------------|
| 🏆 **League Selection** | Choose between Premier League and Portuguese Primeira Liga |
| ⚽ **Match List** | Browse all matches in the selected league with scores and dates |
| 📋 **Match Detail** | Full-time and half-time scores, home/away crests, and match info |
| 📊 **Standings** | Complete league table — position, team, points, W/D/L, goal difference |
| 🏟️ **Team Detail** | Club profile with founded year, venue, head coach, and full squad roster |
| ⭐ **Favourites** | Personal collection of saved matches and teams with notes |
| ✏️ **Edit Favourite** | Update personal notes or remove a saved favourite |

## 🔄 How It Works

1. **League Selection** — User picks Premier League or Primeira Liga from the home screen
2. **Repository Fetch** — `FootballRepository` routes the request to `FootballApiService` via Retrofit
3. **Auth Interceptor** — OkHttp interceptor automatically injects the football-data.org API key into every request header
4. **API Response** — JSON is deserialised into Kotlin data classes (matches, standings, teams)
5. **ViewModel Updates** — Each ViewModel holds state via Compose `State`; UI recomposes on changes
6. **Image Loading** — Coil 3 asynchronously loads club crests and badges from URLs
7. **Save Favourite** — User adds a match or team to favourites with optional notes → serialised to JSON via Gson → stored in SharedPreferences
8. **Edit/Delete** — Favourites can be updated or removed; changes persist immediately

## 🚀 How to Run

```bash
# 1. Clone the repository
git clone https://github.com/VidiPT89/footballapp_android.git

# 2. Open in Android Studio
#    File → Open → select the project root folder

# 3. Add your football-data.org API key
#    Find the auth interceptor in RetrofitInstance.kt and set your key

# 4. Select an emulator (API 28+) or connected device

# 5. Build and run (Shift+F10)
```

## 📝 Notes

- All ViewModel and model names are in **Portuguese** (e.g., `SelecionarLiga`, `ListaJogos`, `DetalheJogo`, `FavoritoLocal`), reflecting the developer's native language
- The **Repository Pattern** provides a single source of truth — the UI never calls the API or SharedPreferences directly
- **SharedPreferences + Gson** was chosen over Room for lightweight favourite storage without the overhead of a full database
- The **OkHttp auth interceptor** keeps API key management in one place — no need to pass tokens through every call
- **Coil 3** handles image caching automatically, preventing redundant network requests for club crests
- The app targets **Min SDK 28** (Android 9) to balance modern API access with broad device coverage

---

Developed by **David Arsénio Martins** — *"Vidi"*
