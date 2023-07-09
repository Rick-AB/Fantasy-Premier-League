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

open class FplPlayer(
    val id: Int,
    val position: PlayerPosition,
    val name: String,
    val clubName: String,

    @DrawableRes
    val shirtRes: Int,
) {
    constructor(fplPlayer: FplPlayer) : this(
        id = fplPlayer.id,
        position = fplPlayer.position,
        name = fplPlayer.name,
        clubName = fplPlayer.clubName,
        shirtRes = fplPlayer.shirtRes
    )

    fun toFplPlayerWithStats(): FplPlayerWithStats {
        return FplPlayerWithStats(
            player = this,
            stats = stats
        )
    }
}

data class FplPlayerWithStats(
    val player: FplPlayer,
    val stats: List<Stat>
) : FplPlayer(player)

data class FplPlayerWithFieldAttributes(
    val player: FplPlayer,
    val isStarter: Boolean,
    val isPotentialSub: Boolean,
    val isPlayerToSub: Boolean
) : FplPlayer(player)
