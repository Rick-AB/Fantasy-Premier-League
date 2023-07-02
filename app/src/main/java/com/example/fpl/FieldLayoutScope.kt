package com.example.fpl

import androidx.compose.ui.Modifier
import com.example.fpl.component.PlayerData
import com.example.fpl.model.PlayerPosition

interface FieldLayoutScope {

    fun Modifier.assignPosition(position: PlayerPosition) = this.then(PlayerData(position))

    companion object : FieldLayoutScope
}