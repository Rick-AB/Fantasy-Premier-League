package com.example.fpl.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.fpl.R
import com.example.fpl.model.FplPlayerWithStats
import com.example.fpl.model.PlayerPosition
import com.example.fpl.model.stats
import com.example.fpl.players
import com.example.fpl.textBrush
import com.example.fpl.ui.theme.DarkPurple
import com.example.fpl.ui.theme.DarkPurpleVariant
import com.example.fpl.ui.theme.Gray
import com.example.fpl.ui.theme.Green
import com.example.fpl.ui.theme.LightBlue
import com.example.fpl.ui.theme.SeaBlue

@Composable
fun PlayerProfile(
    selectedPlayerProfile: FplPlayerWithStats,
    isCaptain: Boolean,
    isViceCaptain: Boolean,
    closeModal: () -> Unit,
    selectCaptain: () -> Unit,
    selectViceCaptain: () -> Unit
) {
    val (player, form, price, selectedPercentage, gameweekPoints, totalPoints, ictIndex) = selectedPlayerProfile

    Column(modifier = Modifier) {
        PlayerDetails(
            modifier = Modifier,
            playerName = player.name,
            playerClub = player.clubName,
            playerPosition = player.position,
            playerShirt = player.shirtRes,
            closeModal = closeModal
        )

        Stats(
            modifier = Modifier.padding(horizontal = 8.dp),
            form = form,
            price = price,
            selectedPercentage = selectedPercentage,
            gameweekPoints = gameweekPoints,
            totalPoints = totalPoints,
            ictIndex = ictIndex
        )

        Buttons(modifier = Modifier.padding(horizontal = 8.dp, vertical = 24.dp))
        CaptainActions(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            isCaptain = isCaptain,
            isViceCaptain = isViceCaptain,
            selectCaptain = selectCaptain,
            selectViceCaptain = selectViceCaptain
        )
    }
}

@Composable
fun PlayerDetails(
    modifier: Modifier,
    playerName: String,
    playerClub: String,
    playerPosition: PlayerPosition,
    @DrawableRes playerShirt: Int,
    closeModal: () -> Unit
) {
    Column(
        modifier = modifier.background(brush = Brush.horizontalGradient(listOf(LightBlue, SeaBlue)))
    ) {
        ClickableIcon(imageVector = Icons.Default.Close, onClick = closeModal)

        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            Image(
                alignment = BiasAlignment(0F, -1F),
                painter = painterResource(id = playerShirt),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.3F)
                    .aspectRatio(.5F)
            )

            Spacer(modifier = Modifier.width(24.dp))
            Column {
                val zeroCornerSize = CornerSize(0.dp)
                Text(
                    text = PlayerPosition.fullNameFor(playerPosition),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .background(
                            color = DarkPurple,
                            shape = ShapeDefaults.Small.copy(
                                topStart = zeroCornerSize,
                                topEnd = zeroCornerSize
                            )
                        )
                        .padding(horizontal = 14.dp, vertical = 4.dp)
                        .textBrush(Brush.horizontalGradient(listOf(Green, LightBlue)))
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = playerName,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = playerClub,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}

@Composable
fun Buttons(modifier: Modifier) {
    Row(modifier = modifier) {
        val buttonShape = Shapes().extraSmall
        val buttonHeight = 60.dp
        Button(
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(
                contentColor = DarkPurple,
                containerColor = Color.Transparent
            ),
            modifier = Modifier
                .weight(0.6F)
                .height(buttonHeight)
                .background(
                    brush = Brush.horizontalGradient(listOf(Green, LightBlue)),
                    shape = buttonShape
                )
        ) {
            Text(
                text = stringResource(id = R.string.substitute),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Spacer(modifier = Modifier.width(12.dp))
        Button(
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.Black,
                containerColor = Color.Transparent
            ),
            modifier = Modifier
                .weight(0.4F)
                .height(buttonHeight)
                .background(color = Gray, shape = buttonShape)

        ) {
            Text(
                text = stringResource(id = R.string.full_profile),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

val statsModifier = Modifier

@Composable
private fun Stats(
    modifier: Modifier,
    form: Float,
    price: Float,
    selectedPercentage: Float,
    gameweekPoints: Int,
    totalPoints: Int,
    ictIndex: Int
) {
    val semiTransparent = Color.Gray.copy(alpha = 0.4F)
    var offsetY by remember { mutableStateOf(0) }

    Surface(
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = Shapes().medium,
                clip = false,
                ambientColor = semiTransparent,
                spotColor = semiTransparent
            )
            .onGloballyPositioned { layoutCoordinates ->
                val heightInPx = layoutCoordinates.size.height
                offsetY = -heightInPx / 2
            }
            .offset { IntOffset(0, offsetY) },
        color = semiTransparent,
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(
                start = 24.dp,
                end = 12.dp,
                top = 12.dp,
                bottom = 12.dp
            ),
        ) {
            item {
                StatItem(
                    modifier = statsModifier,
                    statTitle = stringResource(id = R.string.form),
                    statValue = "$form"
                )
            }

            item {
                StatItem(
                    modifier = statsModifier,
                    statTitle = stringResource(id = R.string.value),
                    statValue = "$${price}m"
                )
            }

            item {
                StatItem(
                    modifier = statsModifier,
                    statTitle = stringResource(id = R.string.selected),
                    statValue = "${selectedPercentage}%"
                )
            }

            item {
                StatItem(
                    modifier = statsModifier,
                    statTitle = "GW 10",
                    statValue = "$gameweekPoints pts"
                )
            }

            item {
                StatItem(
                    modifier = statsModifier,
                    statTitle = stringResource(id = R.string.total),
                    statValue = "$totalPoints pts"
                )
            }

            item {
                StatItem(
                    modifier = statsModifier,
                    statTitle = stringResource(id = R.string.ict_index),
                    statValue = "$ictIndex"
                )
            }
        }
    }

}

@Composable
fun CaptainActions(
    modifier: Modifier,
    isCaptain: Boolean,
    isViceCaptain: Boolean,
    selectCaptain: () -> Unit,
    selectViceCaptain: () -> Unit
) {
    Row(
        modifier = modifier.background(DarkPurpleVariant),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CaptainItem(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.captain),
            checked = isCaptain,
            onClick = selectCaptain
        )

        CaptainItem(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.vice_captain),
            checked = isViceCaptain,
            onClick = selectViceCaptain
        )
    }
}

@Composable
fun CaptainItem(
    modifier: Modifier,
    text: String,
    checked: Boolean,
    onClick: () -> Unit
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            enabled = !checked,
            checked = checked,
            onCheckedChange = { onClick() },
            colors = CheckboxDefaults.colors(
                checkedColor = Color.White,
                checkmarkColor = DarkPurple,
                uncheckedColor = Color.Gray,
                disabledCheckedColor = Color.White,
                disabledUncheckedColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Composable
fun StatItem(modifier: Modifier, statTitle: String, statValue: String) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = statTitle,
            style = MaterialTheme.typography.titleSmall,
            color = DarkPurple,
            fontWeight = FontWeight.Normal
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = statValue,
            style = MaterialTheme.typography.titleLarge,
            color = DarkPurple,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PlayerProfilePrev() {
    val (form, price, selectedPercentage, gameweekPoints, totalPoints, ictIndex) = stats
    val player = FplPlayerWithStats(
        players[10],
        form,
        price,
        selectedPercentage,
        gameweekPoints,
        totalPoints,
        ictIndex
    )
    PlayerProfile(player,
        isCaptain = true,
        isViceCaptain = false,
        closeModal = {},
        selectCaptain = {}
    ) {}
}