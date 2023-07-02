package com.example.fpl.model

import androidx.annotation.DrawableRes

enum class PlayerPosition {
    GKP, DEF, MID, FWD;

    companion object {
        fun fullNameFor(position: PlayerPosition): String {
            return when (position) {
                GKP -> "Keeper"
                DEF -> "Defender"
                MID -> "Midfielder"
                FWD -> "Forward"
            }
        }
    }
}

data class FplPlayer(
    val id: Int,
    val position: PlayerPosition,
    val name: String,
    val clubName: String,

    @DrawableRes
    val shirtRes: Int,
)

data class FplPlayerWithStats(
    val player: FplPlayer,
    val form: Float,
    val price: Float,
    val selectedPercentage: Float,
    val gameweekPoints: Int,
    val totalPoints: Int,
    val ictIndex: Int
)

data class FplPlayerWithFieldAttributes(
    val player: FplPlayer,

    )
