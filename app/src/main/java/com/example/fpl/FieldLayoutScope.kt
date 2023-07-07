package com.example.fpl

import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import com.example.fpl.component.PlayerData
import com.example.fpl.model.PlayerPosition

interface FieldLayoutScope {
    @Stable
    fun Modifier.assignPosition(position: PlayerPosition) = this.then(PlayerData(position))

    companion object : FieldLayoutScope
}