package com.example.fpl.model

import androidx.annotation.DrawableRes
import com.example.fpl.R

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
    val club: Club,

    @DrawableRes
    val shirtRes: Int,
) {
    constructor(fplPlayer: FplPlayer) : this(
        id = fplPlayer.id,
        position = fplPlayer.position,
        name = fplPlayer.name,
        club = fplPlayer.club,
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


val players = listOf(
    FplPlayer(1, PlayerPosition.GKP, "Ramsdale", arsenal, R.drawable.arsenal_gk),
    FplPlayer(2, PlayerPosition.DEF, "Estupinan", brighton, R.drawable.brighton),
    FplPlayer(3, PlayerPosition.DEF, "Trippier", newcastle, R.drawable.newcastle),
    FplPlayer(4, PlayerPosition.DEF, "T. Arnold", liverpool, R.drawable.liverpool),
    FplPlayer(5, PlayerPosition.DEF, "Mee", brentford, R.drawable.brentford),
    FplPlayer(6, PlayerPosition.MID, "Rashford", manUtd, R.drawable.united),
    FplPlayer(7, PlayerPosition.MID, "Odegaard", arsenal, R.drawable.arsenal),
    FplPlayer(8, PlayerPosition.MID, "Mitoma", brighton, R.drawable.brighton),
    FplPlayer(9, PlayerPosition.MID, "Maddision", tottenham, R.drawable.tottenham),
    FplPlayer(10, PlayerPosition.FWD, "Kane", tottenham, R.drawable.tottenham),
    FplPlayer(11, PlayerPosition.FWD, "Wilson", newcastle, R.drawable.newcastle),
    FplPlayer(12, PlayerPosition.GKP, "Heaton", manUtd, R.drawable.united_gk),
    FplPlayer(13, PlayerPosition.MID, "Ramsey", villa, R.drawable.villa),
    FplPlayer(14, PlayerPosition.FWD, "Haaland", manCity, R.drawable.city),
    FplPlayer(15, PlayerPosition.DEF, "Williams", nottHam, R.drawable.forest),
)