package com.example.fpl.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import com.example.fpl.R
import com.example.fpl.swapList

typealias GroupedPlayers = Map<PlayerPosition, List<FplPlayerWithFieldAttributes>>

@Composable
fun rememberSquadState(): SquadState {
    return remember(::SquadState)
}

// For simplicity and readability, a sub is referred to as a position swap or an actual substitution
class SquadState {
    val starters =
        players.subList(0, 11).map { toAttributedFplPlayer(it, true) }.toMutableStateList()

    val substitutes =
        players.subList(11, 15).map { toAttributedFplPlayer(it, false) }.toMutableStateList()

    var captainId by mutableStateOf(starters[0].id)
        private set

    var viceCaptainId by mutableStateOf(starters[1].id)
        private set

    var selectedPlayerForSubstitution = kotlin.run {
        val state = mutableStateOf<FplPlayerWithFieldAttributes?>(null)
        object : MutableState<FplPlayerWithFieldAttributes?> by state {
            override var value: FplPlayerWithFieldAttributes?
                get() = state.value
                set(value) {
                    state.value = value
                    if (value != null) setPotentialSubsForPlayer(value)
                    else resetPotentialSubs()
                }
        }
    }

    companion object {
        const val MIN_NUM_OF_DEF = 3
        const val MIN_NUM_OF_FWD = 1
    }

    fun selectCaptain(playerId: Int) {
        if (playerId == viceCaptainId) swapCaptains()
        else captainId = playerId
    }

    fun selectViceCaptain(playerId: Int) {
        if (playerId == captainId) swapCaptains()
        else viceCaptainId = playerId
    }

    fun makeSubstitution(replacementPlayer: FplPlayerWithFieldAttributes) {
        val selectedPlayerForSubstitution = selectedPlayerForSubstitution.value ?: return
        val arePlayersInSameLineUp =
            (selectedPlayerForSubstitution.isStarter && replacementPlayer.isStarter) ||
                    (!selectedPlayerForSubstitution.isStarter && !replacementPlayer.isStarter)

        when {
            arePlayersInSameLineUp ->
                swapPositionsInSameLineUp(selectedPlayerForSubstitution, replacementPlayer)

            else ->
                swapPositionsInDifferentLineUps(selectedPlayerForSubstitution, replacementPlayer)
        }

        this.selectedPlayerForSubstitution.value = null
    }

    private fun swapCaptains() {
        val currentCaptain = captainId
        val currentViceCaptain = viceCaptainId
        captainId = currentViceCaptain
        viceCaptainId = currentCaptain
    }

    private fun swapPositionsInDifferentLineUps(
        selectedPlayerForSubstitution: FplPlayerWithFieldAttributes,
        replacementPlayer: FplPlayerWithFieldAttributes
    ) {
        if (selectedPlayerForSubstitution.isStarter) {
            val indexOfReplacementPlayer = substitutes.indexOf(replacementPlayer)
            substitutes[indexOfReplacementPlayer] =
                selectedPlayerForSubstitution.copy(isStarter = false)

            val indexToInsert = getIndexToInsertBasedOnPosition(replacementPlayer.position)
            starters.add(indexToInsert, replacementPlayer.copy(isStarter = true))
            starters.remove(selectedPlayerForSubstitution)

            checkCaptains(selectedPlayerForSubstitution, replacementPlayer)
        } else {
            val indexOfPlayerToSub = substitutes.indexOf(selectedPlayerForSubstitution)
            substitutes[indexOfPlayerToSub] = replacementPlayer.copy(isStarter = false)

            val indexToInsert =
                getIndexToInsertBasedOnPosition(selectedPlayerForSubstitution.position)
            starters.add(indexToInsert, selectedPlayerForSubstitution.copy(isStarter = true))
            starters.remove(replacementPlayer)

            checkCaptains(replacementPlayer, selectedPlayerForSubstitution)
        }
    }

    private fun swapPositionsInSameLineUp(
        selectedPlayerForSubstitution: FplPlayerWithFieldAttributes,
        replacementPlayer: FplPlayerWithFieldAttributes
    ) {
        if (selectedPlayerForSubstitution.isStarter) {
            val indexOfPlayerToSub = starters.indexOf(selectedPlayerForSubstitution)
            val indexOfReplacement = starters.indexOf(replacementPlayer)
            starters[indexOfPlayerToSub] = replacementPlayer
            starters[indexOfReplacement] = selectedPlayerForSubstitution
        } else {
            val indexOfPlayerToSub = substitutes.indexOf(selectedPlayerForSubstitution)
            val indexOfReplacement = substitutes.indexOf(replacementPlayer)
            substitutes[indexOfPlayerToSub] = replacementPlayer
            substitutes[indexOfReplacement] = selectedPlayerForSubstitution
        }
    }

    private fun setPotentialSubsForPlayer(selectedPlayerForSubstitution: FplPlayerWithFieldAttributes) {
        val startersGroupedByPos = starters.groupBy { it.position }
        when {
            selectedPlayerForSubstitution.position == PlayerPosition.GKP -> setPotentialSubsForGkp(
                selectedPlayerForSubstitution
            )

            selectedPlayerForSubstitution.isStarter -> setPotentialSubsForOutgoingPlayer(
                selectedPlayerForSubstitution,
                startersGroupedByPos
            )

            else -> setPotentialSubsForIncomingPlayer(startersGroupedByPos)
        }
    }

    private fun setPotentialSubsForOutgoingPlayer(
        selectedPlayerForSubstitution: FplPlayerWithFieldAttributes,
        startersGroupedByPos: GroupedPlayers
    ) {
        val playerPosition = selectedPlayerForSubstitution.position
        val canSubOut =
            canRemovePlayerFromPosition(playerPosition, startersGroupedByPos[playerPosition]!!.size)

        val substitutesCopy = substitutes.map { player ->
            val isPotentialSub =
                if (canSubOut) player.position != PlayerPosition.GKP
                else player.position == selectedPlayerForSubstitution.position

            if (isPotentialSub) player.copy(isPotentialSub = true)
            else player
        }
        substitutes.swapList(substitutesCopy)
    }

    private fun setPotentialSubsForIncomingPlayer(startersGroupedByPos: GroupedPlayers) {
        val startersCopy = starters.map { player ->
            val playerPosition = player.position
            val isValidPosition = canRemovePlayerFromPosition(
                position = playerPosition,
                currentNumOfPlayersAtPos = startersGroupedByPos[playerPosition]!!.size
            )
            if (isValidPosition) player.copy(isPotentialSub = true)
            else player
        }
        starters.swapList(startersCopy)

        val substitutesCopy = substitutes.map { player ->
            val isPotentialSub =
                player.position != PlayerPosition.GKP && player.id != this.selectedPlayerForSubstitution.value?.id

            if (isPotentialSub) player.copy(isPotentialSub = true)
            else player
        }
        substitutes.swapList(substitutesCopy)

    }

    private fun setPotentialSubsForGkp(selectedPlayerForSubstitution: FplPlayerWithFieldAttributes) {
        fun isValidPlayer(player: FplPlayerWithFieldAttributes): Boolean {
            return player.position == selectedPlayerForSubstitution.position && player.id != selectedPlayerForSubstitution.id
        }

        val startersCopy = starters.map { player ->
            if (isValidPlayer(player)) player.copy(isPotentialSub = true)
            else player
        }
        starters.swapList(startersCopy)

        val substitutesCopy = substitutes.map { player ->
            if (isValidPlayer(player)) player.copy(isPotentialSub = true)
            else player
        }
        substitutes.swapList(substitutesCopy)
    }

    private fun resetPotentialSubs() {
        val startersCopy = starters.map { player ->
            player.copy(isPotentialSub = false)
        }
        starters.swapList(startersCopy)

        val substitutesCopy = substitutes.map { player ->
            player.copy(isPotentialSub = false)
        }
        substitutes.swapList(substitutesCopy)
    }

    private fun checkCaptains(
        outgoingPlayer: FplPlayerWithFieldAttributes,
        incomingPlayer: FplPlayerWithFieldAttributes
    ) {
        if (captainId == outgoingPlayer.id) captainId = incomingPlayer.id
        if (viceCaptainId == outgoingPlayer.id) viceCaptainId = incomingPlayer.id
    }

    private fun canRemovePlayerFromPosition(
        position: PlayerPosition,
        currentNumOfPlayersAtPos: Int
    ): Boolean {
        return when (position) {
            PlayerPosition.GKP -> false
            PlayerPosition.DEF -> currentNumOfPlayersAtPos > MIN_NUM_OF_DEF
            PlayerPosition.FWD -> currentNumOfPlayersAtPos > MIN_NUM_OF_FWD
            else -> true
        }
    }

    private fun getIndexToInsertBasedOnPosition(position: PlayerPosition): Int {
        return when (position) {
            PlayerPosition.DEF -> starters.indexOfLast { it.position == position } + 1
            PlayerPosition.MID -> starters.indexOfFirst { it.position == position }
            PlayerPosition.FWD -> starters.indexOfFirst { it.position == position }
            else -> starters.indexOfFirst { it.position == position }
        }
    }

    private fun toAttributedFplPlayer(
        fplPlayer: FplPlayer,
        isStarter: Boolean
    ): FplPlayerWithFieldAttributes {
        return FplPlayerWithFieldAttributes(
            player = fplPlayer,
            isStarter = isStarter,
            isPotentialSub = false,
            isPlayerToSub = false
        )
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