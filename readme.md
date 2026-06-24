# ⚽ Football App — Android

> _"Two leagues. Every match. All in your pocket."_

A Jetpack Compose Android app to follow football — browse matches, check live standings, explore full squad rosters, and manage your personal list of favourite matches and teams with custom notes.

---

## ⚡ Tech Stack

[![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white)](https://developer.android.com/compose)
[![Material Design 3](https://img.shields.io/badge/Material%20Design%203-757575?style=flat-square&logo=material-design&logoColor=white)](https://m3.material.io/)
[![Retrofit](https://img.shields.io/badge/Retrofit%202-48B983?style=flat-square&logo=square&logoColor=white)](https://square.github.io/retrofit/)
[![OkHttp](https://img.shields.io/badge/OkHttp%204-48B983?style=flat-square&logo=square&logoColor=white)](https://square.github.io/okhttp/)
[![Coil](https://img.shields.io/badge/Coil%203-FF6B35?style=flat-square&logoColor=white)](https://coil-kt.github.io/coil/)
[![API](https://img.shields.io/badge/football--data.org-API%20v4-009900?style=flat-square)](https://www.football-data.org/)
[![Min SDK](https://img.shields.io/badge/Min%20SDK-28%20(Android%209)-green?style=flat-square&logo=android&logoColor=white)](https://developer.android.com/)

---

## 🏟️ What's Inside

- 🌍 Browse matches from the **Premier League** and the **Portuguese Primeira Liga**
- 📊 Live standings table — positions, points, wins, draws, losses, and goal difference
- 🔍 Full match detail — full-time score, half-time score, and team crests
- 👕 Team detail page — club info, founding year, venue, coach, and complete squad roster
- ⭐ Save **favourite matches** with personal notes
- 🔖 Save **favourite teams** to your personal list
- ✏️ Edit and delete favourites at any time
- 💾 Persistent storage — your data survives app restarts

---

## 🏗️ Architecture

```
MVVM + Repository Pattern
│
├── 📦 Models
│   ├── Liga              →  League data (id, name, country)
│   ├── FavoritoLocal     →  Saved match with personal note
│   └── FavoritoEquipa    →  Saved team
│
├── 🧠 ViewModels (one per screen)
│   ├── SelecionarLigaViewModel
│   ├── ListaJogosViewModel
│   ├── DetalheJogoViewModel
│   ├── ClassificacaoViewModel
│   ├── DetalheEquipaViewModel
│   ├── FavoritosViewModel
│   └── AdicionarEditarViewModel
│
├── 🖼️ Views  →  Jetpack Compose screens (fully reactive via StateFlow)
│
├── 🗄️ Repository  →  FootballRepository (single source of truth)
│   ├── Remote  →  football-data.org API via Retrofit
│   └── Local   →  SharedPreferences (Gson serialisation)
│
└── 🌐 Network
    ├── FootballApiService   →  Retrofit endpoints
    └── RetrofitInstance     →  Singleton with OkHttp auth interceptor
```

---

## 📱 Screens

| Screen | What it does |
|--------|-------------|
| 🏟️ **League Selection** | Choose between Premier League and Primeira Liga |
| ⚽ **Match List** | All matches for the selected league with live scores |
| 📋 **Match Detail** | Full-time and half-time scores with team crests |
| 📊 **Standings** | Full league table with complete stats per team |
| 👕 **Team Detail** | Club info, coach, nationality and full squad roster |
| ⭐ **Favourites** | Your saved matches and teams in one place |
| ✏️ **Edit Favourite** | Add or update personal notes on a saved match |

---

## 🌐 API

Three endpoints from [football-data.org](https://www.football-data.org/) v4:

```
GET /v4/competitions/{id}/matches     →  Match list for a league
GET /v4/competitions/{id}/standings   →  League table
GET /v4/teams/{id}                    →  Team detail + squad
```

Authentication is handled by a custom OkHttp interceptor that injects the `X-Auth-Token` header on every request.

---

## 📝 Honest Notes

- 🇵🇹 All variable names and code comments are in Portuguese. The Premier League is not. It works anyway.
- 🔑 The API token is hardcoded in `RetrofitInstance.kt`. It's a known issue. It's on the list.
- 💾 Favourites are stored in SharedPreferences. Room was considered. SharedPreferences was faster to ship.
- 🎨 The standings table doesn't highlight qualification zones yet. The data is there. The colours aren't.
- 🚀 First Android project with Jetpack Compose and real API integration — more opinionated than expected, less painful than feared.

---

## 📋 Requirements

- 📱 Android 9+ (API level 28)
- 🛠️ Android Studio Ladybug or later
- 🔑 API key from [football-data.org](https://www.football-data.org/client/register)

---

## 🚀 Getting Started

```bash
git clone https://github.com/VidiPT89/footballapp_android.git
```

1. Open the project in **Android Studio**
2. Replace the token in `app/src/main/java/com/davidmartins/footballapp/network/RetrofitInstance.kt` with your own API key
3. Build and run on an emulator or a physical device running **Android 9+**

---

## 🎓 Context


_Next up: zone highlights on the standings table, proper error screens, and maybe Room. Eventually._ 🏁
