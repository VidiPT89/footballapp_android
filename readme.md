# ⚽ Football App — Android

> Browse matches, standings and squads from the Premier League and Portuguese Primeira Liga.

A Jetpack Compose Android app that connects to the football-data.org API to deliver match schedules, live standings, full squad rosters and a personal favourites system with notes — all wrapped in Material Design 3.

## 📦 What's Inside

- 🏆 Two leagues — Premier League and Portuguese Primeira Liga
- 📅 Match browsing with detail view
- 📊 Live league standings
- 👥 Full squad rosters per team
- ⭐ Save favourite matches and teams with personal notes
- 💾 Persistent local storage

## 🛠️ Tech Stack

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=flat&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-4285F4?style=flat&logo=jetpackcompose&logoColor=white)
![Material Design 3](https://img.shields.io/badge/Material_Design_3-757575?style=flat&logo=materialdesign&logoColor=white)
![Retrofit](https://img.shields.io/badge/Retrofit_2-48B983?style=flat)
![OkHttp](https://img.shields.io/badge/OkHttp_4-48B983?style=flat)
![Coil](https://img.shields.io/badge/Coil_3-2B2B2B?style=flat)

## 🏗️ Architecture

MVVM + Repository Pattern

```
├── data/
│   ├── api/            # Retrofit service (football-data.org v4)
│   └── repository/     # Data layer
├── model/              # Data classes
├── ui/
│   ├── screens/        # Compose screens
│   └── viewmodel/      # ViewModels
└── navigation/         # App navigation
```

## 📱 Screens

| Screen | Description |
|--------|-------------|
| League Selection | Choose between Premier League or Primeira Liga |
| Match List | Upcoming and recent matches |
| Match Detail | Score, teams, date and venue |
| Standings | Full league table |
| Team Detail | Info, crest and squad roster |
| Favourites | Saved matches and teams with notes |
| Edit Favourite | Add or update personal notes |

## 🚀 How to Run

```bash
# 1. Clone the repository
git clone https://github.com/VidiPT89/footballapp_android.git

# 2. Open in Android Studio

# 3. Add your API key from football-data.org

# 4. Build & run (Min SDK 28)
```

## 📝 Notes

- Requires a free API key from [football-data.org](https://www.football-data.org/) (v4)
- Minimum SDK: 28 (Android 9.0)

---

Developed by **David Martins**
