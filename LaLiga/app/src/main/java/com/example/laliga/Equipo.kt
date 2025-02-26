package com.example.laliga

import java.io.Serializable

data class Equipo(
    val idTeam: String? = null,
    val strTeam: String? = null,
    val strTeamShort: String? = null,
    val strAlternate: String? = null,
    val intFormedYear: String? = null,
    val strStadium: String? = null,
    val strStadiumThumb: String? = null,
    val strTeamBadge: String? = null,
    val strTeamJersey: String? = null,
    val strTeamLogo: String? = null,
    val strDescriptionEN: String? = null,
    val strLeague: String? = null
): Serializable

