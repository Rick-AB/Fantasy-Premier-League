package com.example.fpl

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.example.fpl.model.FplPlayerWithStats
import com.example.fpl.model.rememberSquadState
import com.example.fpl.ui.theme.FplTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            FplTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    val squadState = rememberSquadState()
                    var selectedPlayerProfile: FplPlayerWithStats? by remember { mutableStateOf(null) }

                    TeamSelectionScreen(
                        squadState = squadState,
                        selectedPlayerProfile = selectedPlayerProfile,
                        setPlayerProfile = { selectedPlayerProfile = it },
                        closeModal = { selectedPlayerProfile = null },
                        selectCaptain = { playerId -> squadState.selectCaptain(playerId) },
                        selectViceCaptain = { playerId -> squadState.selectViceCaptain(playerId) },
                        selectPlayerToSub = { playerId ->
                            val playerWithFieldAttributes =
                                (squadState.starters + squadState.substitutes).find { it.id == playerId }
                            squadState.selectedPlayerForSubstitution.value =
                                playerWithFieldAttributes
                        }
                    )
                }
            }
        }
    }
}
