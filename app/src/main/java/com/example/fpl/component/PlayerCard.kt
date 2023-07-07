@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.fpl.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fpl.model.FplPlayerWithFieldAttributes
import com.example.fpl.players
import com.example.fpl.ui.theme.DarkPurple
import com.example.fpl.ui.theme.Green
import com.example.fpl.ui.theme.LightBlue
import com.example.fpl.ui.theme.SemiDarkGreen

@Composable
fun PlayerCard(
    modifier: Modifier,
    player: FplPlayerWithFieldAttributes,
    playerToSub: FplPlayerWithFieldAttributes?,
    isCaptain: Boolean = false,
    isViceCaptain: Boolean = false,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val roundedShape = Shapes().extraSmall
    val (color, columnModifier) = remember(playerToSub) {
        val isSubActive = playerToSub != null
        when {
            isSubActive && player.isStarter && (player.isPotentialSub || playerToSub?.id == player.id) -> Pair(
                Color.Red,
                Modifier
                    .border(
                        width = 2.dp,
                        color = Color.Red,
                        shape = roundedShape
                    )
                    .background(Color.Black.copy(alpha = 0.3f), roundedShape)
            )

            playerToSub?.id == player.id -> Pair(
                Color.Green, Modifier
                    .border(
                        width = 2.dp,
                        brush = Brush.verticalGradient(listOf(LightBlue, Green)),
                        shape = roundedShape
                    )
                    .background(Color.White.copy(alpha = 0.2f), roundedShape)
            )

            !player.isPlayerToSub && player.isPotentialSub && isSubActive -> Pair(
                DarkPurple,
                Modifier
                    .border(
                        width = 2.dp,
                        color = DarkPurple,
                        shape = roundedShape
                    )
                    .background(Color.Black.copy(alpha = 0.3f), roundedShape)
            )

            else -> Pair(SemiDarkGreen, Modifier)
        }
    }

    Column(
        modifier = modifier
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .then(columnModifier)

    ) {
        Spacer(modifier = Modifier.height(12.dp))
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

        val reusableTextStyle = MaterialTheme.typography.bodySmall.copy(
            textAlign = TextAlign.Center,
            color = Color.White,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(4.dp))
        AutoResizeText(
            text = player.name,
            style = reusableTextStyle,
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .then(
                    if (playerToSub != null && (player.isPotentialSub || playerToSub.id == player.id))
                        Modifier.background(Color.Transparent)
                    else
                        Modifier.background(DarkPurple)
                )
                .wrapContentHeight(align = Alignment.CenterVertically)
                .padding(horizontal = 4.dp)
        )

        val bottomCornerSize = CornerSize(6.dp)
        AutoResizeText(
            text = "ARS",
            style = reusableTextStyle,
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .background(
                    color = color,
                    shape = roundedShape.copy(
                        topStart = CornerSize(0.dp),
                        topEnd = CornerSize(0.dp),
                        bottomStart = bottomCornerSize,
                        bottomEnd = bottomCornerSize
                    )
                )
                .wrapContentHeight(align = Alignment.CenterVertically)
                .padding(horizontal = 4.dp)
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PlayerCardPrev() {
    Column {
        val player = FplPlayerWithFieldAttributes(
            player = players[5],
            isStarter = true,
            isPotentialSub = true,
            isPlayerToSub = false
        )
        PlayerCard(
            modifier = Modifier.width(80.dp),
            player = player,
            playerToSub = player,
            onClick = {})
    }
}
