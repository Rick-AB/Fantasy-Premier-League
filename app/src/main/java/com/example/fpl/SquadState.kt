package com.example.fpl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import com.example.fpl.model.FplPlayer
import com.example.fpl.model.PlayerPosition

@Composable
fun rememberSquadState(): SquadState {
    return remember(::SquadState)
}

class SquadState {
    val starters = players.subList(0, 11).toMutableStateList()
    val substitutes = players.subList(11, 15).toMutableStateList()
    var captainId by mutableStateOf(starters[0].id)
        private set
    var viceCaptainId by mutableStateOf(starters[1].id)
        private set

    fun selectCaptain(playerId: Int) {
        if (playerId == viceCaptainId) swapCaptains(viceCaptainId, playerId)
        else captainId = playerId
    }

    fun selectViceCaptain(playerId: Int) {
        if (playerId == captainId) swapCaptains(playerId, captainId)
        else viceCaptainId = playerId
    }

    private fun swapCaptains(newCaptainId: Int, newViceCaptainId: Int) {
        captainId = newViceCaptainId
        viceCaptainId = newCaptainId
    }
}

val players = listOf(
    FplPlayer(1, PlayerPosition.GKP, "Ramsdale", "Arsenal", R.drawable.arsenal_gk),
    FplPlayer(2, PlayerPosition.DEF, "Estupinan", "Brighton", R.drawable.brighton),
    FplPlayer(3, PlayerPosition.DEF, "Trippier", "Newcastle", R.drawable.newcastle),
    FplPlayer(4, PlayerPosition.DEF, "Schar", "Newcastle", R.drawable.newcastle),
    FplPlayer(5, PlayerPosition.DEF, "Mee", "Brentford", R.drawable.brentford),
    FplPlayer(6, PlayerPosition.MID, "Rashford", "Man Utd", R.drawable.united),
    FplPlayer(7, PlayerPosition.MID, "Fernandes", "Man Utd", R.drawable.united),
    FplPlayer(8, PlayerPosition.MID, "Mitoma", "Brighton", R.drawable.brighton),
    FplPlayer(9, PlayerPosition.MID, "Mac Allister", "Brighton", R.drawable.brighton),
    FplPlayer(10, PlayerPosition.FWD, "Kane", "Tottenham", R.drawable.tottenham),
    FplPlayer(11, PlayerPosition.FWD, "Wilson", "Newcastle", R.drawable.newcastle),
    FplPlayer(12, PlayerPosition.GKP, "Heaton", "Man Utd", R.drawable.united_gk),
    FplPlayer(13, PlayerPosition.MID, "Bernado", "Man City", R.drawable.city),
    FplPlayer(14, PlayerPosition.FWD, "Haaland", "Man City", R.drawable.city),
    FplPlayer(15, PlayerPosition.DEF, "Williams", "Nott. Forest", R.drawable.forest),
)