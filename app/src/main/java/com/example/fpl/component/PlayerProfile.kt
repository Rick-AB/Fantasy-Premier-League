package com.example.fpl.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fpl.R
import com.example.fpl.model.FplPlayerWithStats
import com.example.fpl.model.PlayerPosition
import com.example.fpl.model.Stat
import com.example.fpl.model.players
import com.example.fpl.model.stats
import com.example.fpl.textBrush
import com.example.fpl.ui.theme.DarkPurple
import com.example.fpl.ui.theme.DarkPurpleVariant
import com.example.fpl.ui.theme.Gray
import com.example.fpl.ui.theme.Green
import com.example.fpl.ui.theme.LightBlue

enum class StatTextStyleType {
    STAT_NAME, STAT_VALUE, RANKING
}

@Composable
fun PlayerProfile(
    selectedPlayerProfile: FplPlayerWithStats,
    isCaptain: Boolean,
    isViceCaptain: Boolean,
    captainOptionsEnabled: Boolean,
    closeModal: () -> Unit,
    selectCaptain: () -> Unit,
    selectViceCaptain: () -> Unit,
    onSubstituteClick: () -> Unit,
) {
    val verticalBrush = Brush.verticalGradient(
        0.0F to Color.Transparent,
        0.98F to Color.White
    )

    Column {
        GradientBackground(customVerticalBrush = verticalBrush) {
            Column {
                PlayerDetails(
                    modifier = Modifier,
                    playerName = selectedPlayerProfile.name,
                    playerClub = selectedPlayerProfile.clubName,
                    playerPosition = selectedPlayerProfile.position,
                    playerShirt = selectedPlayerProfile.shirtRes,
                    closeModal = closeModal
                )
                Stats(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth(),
                    position = selectedPlayerProfile.position,
                    stats = selectedPlayerProfile.stats
                )
            }
        }

        Buttons(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 24.dp),
            onSubstituteClick = onSubstituteClick
        )

        CaptainActions(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            isCaptain = isCaptain,
            isViceCaptain = isViceCaptain,
            captainOptionsEnabled = captainOptionsEnabled,
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
    Column(modifier = modifier) {
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
                    .fillMaxWidth(0.25F)
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
fun Buttons(
    modifier: Modifier,
    onSubstituteClick: () -> Unit,
) {
    Row(modifier = modifier) {
        val buttonShape = Shapes().extraSmall
        val buttonHeight = 60.dp
        Button(
            onClick = onSubstituteClick,
            colors = ButtonDefaults.buttonColors(
                contentColor = DarkPurple,
                containerColor = Color.Transparent
            ),
            shape = buttonShape,
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
            shape = buttonShape,
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

@Composable
private fun Stats(
    modifier: Modifier,
    position: PlayerPosition,
    stats: List<Stat>
) {
    val semiTransparent = Color.White.copy(alpha = 0.4F)
    val statNameTextStyle = MaterialTheme.typography.bodyMedium
    val statValueTextStyle = MaterialTheme.typography.titleMedium
    val rankingTextStyle = MaterialTheme.typography.bodySmall
    var useableStatNameTextStyle by remember { mutableStateOf(statNameTextStyle) }
    var useableStatValueTextStyle by remember { mutableStateOf(statValueTextStyle) }
    var useableRankingTextStyle by remember { mutableStateOf(rankingTextStyle) }

    val updateTextStyle: (StatTextStyleType, TextStyle) -> Unit = { statTextStyleType, textStyle ->
        when (statTextStyleType) {
            StatTextStyleType.STAT_NAME -> {
                if (textStyle.fontSize.value < useableStatNameTextStyle.fontSize.value)
                    useableStatNameTextStyle = textStyle
            }

            StatTextStyleType.STAT_VALUE -> {
                if (textStyle.fontSize.value < useableStatValueTextStyle.fontSize.value)
                    useableStatValueTextStyle = textStyle
            }

            StatTextStyleType.RANKING -> {
                if (textStyle.fontSize.value < useableRankingTextStyle.fontSize.value)
                    useableRankingTextStyle = textStyle
            }
        }
    }

    Card(
        modifier = modifier,
        shape = Shapes().extraSmall,
        colors = CardDefaults.cardColors(semiTransparent),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            stats.forEachIndexed { index, stat ->
                StatItem(
                    modifier = Modifier.weight(1F),
                    name = stat.statName,
                    value = if (stat.statName.contains("price", true)) "$${stat.statValue}m"
                    else "${stat.statValue}",
                    ranking = stat.statRanking,
                    statNameTextStyle = useableStatNameTextStyle,
                    statValueTextStyle = useableStatValueTextStyle,
                    statRankingTextStyle = useableRankingTextStyle,
                    updateTextStyle = updateTextStyle
                )

                if (index != stats.lastIndex) {
                    Divider(
                        modifier = Modifier
                            .width(1.dp)
                            .fillMaxHeight(0.5F)
                            .align(Alignment.CenterVertically)
                    )
                }
            }
        }

        Text(
            text = stringResource(
                id = R.string.ranking_for,
                PlayerPosition.fullNameFor(position)
            ),
            color = DarkPurple,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 12.dp)
        )
    }
}

@Composable
fun CaptainActions(
    modifier: Modifier,
    isCaptain: Boolean,
    isViceCaptain: Boolean,
    captainOptionsEnabled: Boolean,
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
            enabled = captainOptionsEnabled,
            onClick = selectCaptain
        )

        CaptainItem(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.vice_captain),
            checked = isViceCaptain,
            enabled = captainOptionsEnabled,
            onClick = selectViceCaptain
        )
    }
}

@Composable
fun CaptainItem(
    modifier: Modifier,
    text: String,
    checked: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            enabled = !checked && enabled,
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
            color = Color.White
        )
    }
}

@Composable
fun StatItem(
    modifier: Modifier,
    name: String,
    value: String,
    ranking: Pair<Int, Int>,
    statNameTextStyle: TextStyle,
    statValueTextStyle: TextStyle,
    statRankingTextStyle: TextStyle,
    updateTextStyle: (StatTextStyleType, TextStyle) -> Unit
) {
    val textColor = DarkPurple
    val textAlign = TextAlign.Center

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        AutoResizeText(
            modifier = Modifier,
            text = name,
            color = textColor,
            style = statNameTextStyle.copy(textAlign = textAlign),
            reportUseableStyle = { updateTextStyle(StatTextStyleType.STAT_NAME, it) }
        )

        Spacer(modifier = Modifier.height(4.dp))
        AutoResizeText(
            modifier = Modifier,
            text = value,
            style = statValueTextStyle.copy(
                textAlign = textAlign,
                fontWeight = FontWeight.Bold
            ),
            color = textColor,
            reportUseableStyle = { updateTextStyle(StatTextStyleType.STAT_VALUE, it) }
        )

        AutoResizeText(
            modifier = Modifier,
            text = "${ranking.first} of ${ranking.second}",
            color = textColor,
            style = statRankingTextStyle.copy(textAlign = textAlign),
            reportUseableStyle = { updateTextStyle(StatTextStyleType.RANKING, it) }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PlayerProfilePrev() {
    val player = FplPlayerWithStats(
        players[10],
        stats = stats
    )
    PlayerProfile(player,
        isCaptain = true,
        isViceCaptain = false,
        captainOptionsEnabled = false,
        closeModal = {},
        selectCaptain = {},
        selectViceCaptain = {}
    ) {}
}