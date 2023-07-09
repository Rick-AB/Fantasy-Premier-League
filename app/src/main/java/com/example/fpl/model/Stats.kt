package com.example.fpl.model

data class Stat(
    val statName: String,
    val statValue: Float,
    val statRanking: Pair<Int, Int>
)

val stats = listOf(
    Stat(statName = "Price", statValue = 8.5F, statRanking = Pair(2, 246)),
    Stat(statName = "Form", statValue = 0.0F, statRanking = Pair(241, 246)),
    Stat(statName = "Pts / Match", statValue = 5.7F, statRanking = Pair(3, 246)),
    Stat(statName = "ICT Index", statValue = 309.2F, statRanking = Pair(4, 246)),
)
