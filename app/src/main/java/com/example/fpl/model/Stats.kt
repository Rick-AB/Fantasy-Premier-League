package com.example.fpl.model

data class Stats(
    val form: Float,
    val price: Float,
    val selectedPercentage: Float,
    val gameweekPoints: Int,
    val totalPoints: Int,
    val ictIndex: Int
)

val stats = Stats(
    form = 7.7F,
    price = 11.0F,
    selectedPercentage = 23.0F,
    gameweekPoints = 7,
    totalPoints = 69,
    ictIndex = 7823,
)
