package com.example.laliga

data class Liga (
    val leagues: ArrayList<League>? = null
)

data class League (
    val idLeague: String? = null,
    val strLeague: String? = null,
    val strSport: String? = null,
    val strLeagueAlternate: String? = null
)

enum class StrSport {
    AmericanFootball,
    Basketball,
    IceHockey,
    Motorsport,
    Soccer
}