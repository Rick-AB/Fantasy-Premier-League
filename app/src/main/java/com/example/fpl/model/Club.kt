package com.example.fpl.model

data class Club(
    val id: Int,
    val clubName: String,
    val nextFixture: String
)

val arsenal = Club(0, "Arsenal", "NFO (H)")
val brighton = Club(1, "Brighton", "LUT (H)")
val newcastle = Club(2, "Newcastle", "AVL (H)")
val brentford = Club(3, "Brentford", "TOT (H)")
val manUtd = Club(4, "Man Utd", "WOL (H)")
val tottenham = Club(5, "Tottenham", "BRE (A)")
val manCity = Club(6, "Man City", "BUR (A)")
val nottHam = Club(7, "Nott. F", "ARS (A)")
val liverpool = Club(8, "Liverpool", "CHE (A)")
val chelsea = Club(9, "Chelsea", "LIV (H)")
val villa = Club(10, "A. Villa", "NEW (A)")
