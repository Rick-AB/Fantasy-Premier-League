@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.fpl.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.fpl.model.FplPlayerWithFieldAttributes
import com.example.fpl.model.FplPlayerWithStats
import com.example.fpl.ui.theme.DarkPurple

@Composable
fun PlayerCard(
    modifier: Modifier,
    player: FplPlayerWithFieldAttributes,
    isCaptain: Boolean = false,
    isViceCaptain: Boolean = false,
    onClick: (FplPlayerWithStats) -> Unit
) {
    Column(modifier = modifier.clickable { onClick(player.toFplPlayerWithStats()) }) {
        Box {
            Image(
                painter = painterResource(id = player.shirtRes),
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth(),
                contentDescription = ""
            )

            Column(modifier = Modifier.align(Alignment.TopEnd)) {
                AnimatedVisibility(visible = isCaptain) { PlayerArmBand(char = 'C') }

                AnimatedVisibility(visible = isViceCaptain) { PlayerArmBand(char = 'V') }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))
        AutoResizeText(
            text = player.name,
            style = MaterialTheme.typography.bodySmall.copy(
                textAlign = TextAlign.Center,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .background(DarkPurple)
                .wrapContentHeight(align = Alignment.CenterVertically)
                .padding(horizontal = 8.dp)
        )

        val bottomCornerSize = CornerSize(6.dp)
        AutoResizeText(
            text = "ARS",
            style = MaterialTheme.typography.bodySmall.copy(textAlign = TextAlign.Center),
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .background(
                    color = Color.White.copy(alpha = 0.7F),
                    shape = Shapes().small.copy(
                        topStart = CornerSize(0.dp),
                        topEnd = CornerSize(0.dp),
                        bottomStart = bottomCornerSize,
                        bottomEnd = bottomCornerSize
                    )
                )
                .wrapContentHeight(align = Alignment.CenterVertically)
                .padding(horizontal = 8.dp)
        )
    }
}

@Composable
fun PlayerArmBand(char: Char) {
    Badge(
        containerColor = DarkPurple,
        contentColor = Color.White,
        modifier = Modifier.size(16.dp)
    ) {
        Text(text = char.toString())
    }
}
