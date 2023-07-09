@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.fpl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.fpl.component.ClickableIcon
import com.example.fpl.component.FieldLayout
import com.example.fpl.component.GradientBackground
import com.example.fpl.component.PlayerCard
import com.example.fpl.component.PlayerProfile
import com.example.fpl.component.SlitLine
import com.example.fpl.component.draw18YardArc
import com.example.fpl.component.drawCornerArc
import com.example.fpl.component.drawField
import com.example.fpl.component.drawFieldLine
import com.example.fpl.component.drawKickOffLine
import com.example.fpl.component.horizontalGradientBrush
import com.example.fpl.model.ChipStatus
import com.example.fpl.model.Field
import com.example.fpl.model.FplPlayerWithFieldAttributes
import com.example.fpl.model.FplPlayerWithStats
import com.example.fpl.model.SquadState
import com.example.fpl.ui.theme.DarkFieldGreen
import com.example.fpl.ui.theme.DarkPurple
import com.example.fpl.ui.theme.SemiTransparentGreen
import kotlin.math.roundToInt


@Composable
fun TeamSelectionScreen(
    squadState: SquadState,
    selectedPlayerProfile: FplPlayerWithStats?,
    setPlayerProfile: (FplPlayerWithStats) -> Unit,
    closeModal: () -> Unit,
    selectCaptain: (Int) -> Unit,
    selectViceCaptain: (Int) -> Unit,
    selectPlayerToSub: (Int) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TeamSelectionTopBar() }
    ) {
        TeamSelectionBody(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            squadState = squadState,
            selectedPlayerProfile = selectedPlayerProfile,
            setPlayerProfile = setPlayerProfile,
            closeModal = closeModal,
            selectCaptain = selectCaptain,
            selectViceCaptain = selectViceCaptain,
            selectPlayerToSub = selectPlayerToSub
        )
    }
}

@Composable
fun TeamSelectionBody(
    modifier: Modifier,
    squadState: SquadState,
    selectedPlayerProfile: FplPlayerWithStats?,
    setPlayerProfile: (FplPlayerWithStats) -> Unit,
    closeModal: () -> Unit,
    selectCaptain: (Int) -> Unit,
    selectViceCaptain: (Int) -> Unit,
    selectPlayerToSub: (Int) -> Unit,
) {
    Column(modifier = modifier) {
        GradientBackground {
            Column {
                SlitLine(
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth(0.9F)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(12.dp))
                GameweekHeader(modifier = Modifier.padding(horizontal = 8.dp))

                Spacer(modifier = Modifier.height(12.dp))
                SlitLine(
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth(0.9F)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Chips(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        FieldView(
            modifier = Modifier.fillMaxSize(),
            starters = squadState.starters,
            substitutes = squadState.substitutes,
            playerToSub = squadState.selectedPlayerForSubstitution.value,
            captainId = squadState.captainId,
            viceCaptainId = squadState.viceCaptainId,
            setPlayerProfile = setPlayerProfile,
            cancelSubstitution = { squadState.selectedPlayerForSubstitution.value = null },
            makeSubstitution = { replacementPlayer -> squadState.makeSubstitution(replacementPlayer) }
        )

        if (selectedPlayerProfile != null) {
            ModalBottomSheet(
                onDismissRequest = closeModal,
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                dragHandle = null,
                shape = Shapes().extraSmall,
                modifier = Modifier.fillMaxWidth(0.97F)
            ) {
                val captainOptionsEnabled: () -> Boolean =
                    { squadState.starters.find { it.id == selectedPlayerProfile.id } != null }

                PlayerProfile(
                    selectedPlayerProfile = selectedPlayerProfile,
                    isCaptain = selectedPlayerProfile.player.id == squadState.captainId,
                    isViceCaptain = selectedPlayerProfile.player.id == squadState.viceCaptainId,
                    captainOptionsEnabled = captainOptionsEnabled(),
                    closeModal = closeModal,
                    selectCaptain = { selectCaptain(selectedPlayerProfile.player.id); closeModal() },
                    selectViceCaptain = { selectViceCaptain(selectedPlayerProfile.player.id); closeModal() },
                    onSubstituteClick = { selectPlayerToSub(selectedPlayerProfile.id); closeModal() }
                )
            }
        }
    }
}

@Composable
fun FieldView(
    modifier: Modifier,
    starters: List<FplPlayerWithFieldAttributes>,
    substitutes: List<FplPlayerWithFieldAttributes>,
    playerToSub: FplPlayerWithFieldAttributes?,
    captainId: Int,
    viceCaptainId: Int,
    setPlayerProfile: (FplPlayerWithStats) -> Unit,
    cancelSubstitution: () -> Unit,
    makeSubstitution: (FplPlayerWithFieldAttributes) -> Unit
) {
    BoxWithConstraints(modifier = modifier) {
        val density = LocalDensity.current
        val maxWidthInPx = with(density) { maxWidth.toPx() }
        val maxHeightInPx = with(density) { maxHeight.toPx() }
        val field = remember { Field(maxWidthInPx, maxHeightInPx) }
        var shouldRender by remember { mutableStateOf(false) }
        val (playerWidth, horizontalSpace) = remember {
            val maxPlayerPerRow = 5
            val minWidth = 60
            var width = 80F
            var space = 8F
            fun getShouldResize(): Boolean {
                val totalSpace = maxPlayerPerRow.minus(1).times(space)
                val totalPlayerWidth = maxPlayerPerRow.times(width)
                return maxWidth.value.minus(totalSpace.plus(totalPlayerWidth)) < 4
            }

            while (getShouldResize()) {
                if (width < minWidth) {
                    width = minWidth.toFloat()
                    space = space.times(0.95F)
                } else {
                    width = width.times(0.95F)
                }
            }

            shouldRender = true
            Pair(width.roundToInt().dp, space.roundToInt().dp)
        }

        Column(modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .drawWithCache {
                onDrawBehind {
                    drawField(field.fieldBounds)
                    drawFieldLine(field.touchlineBounds)
                    drawFieldLine(field.eighteenYardBoxBounds)
                    drawFieldLine(field.sixYardBoxBounds)
                    draw18YardArc(field.eighteenYardBoxBounds)
                    drawKickOffLine(field.kickOffLineBounds)
                    drawCornerArc(
                        field.leftFlagStartCoordinate,
                        field.leftFlagEndCoordinate
                    )
                    drawCornerArc(
                        field.rightFlagStartCoordinate,
                        field.rightFlagEndCoordinate
                    )
                }
            }
            .drawWithContent { if (shouldRender) drawContent() }
        ) {
            FieldLayout(
                modifier = Modifier,
                crossAxisSpacing = 16.dp,
                spacing = horizontalSpace
            ) {
                Starters(
                    starters = starters,
                    playerToSub = playerToSub,
                    contentWidth = playerWidth,
                    captainId = captainId,
                    viceCaptainId = viceCaptainId,
                    setPlayerProfile = setPlayerProfile,
                    cancelSubstitution = cancelSubstitution,
                    makeSubstitution = makeSubstitution
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
            Substitutes(
                modifier = Modifier
                    .background(DarkFieldGreen)
                    .padding(8.dp),
                substitutes = substitutes,
                playerToSub = playerToSub,
                contentWidth = playerWidth,
                setPlayerProfile = setPlayerProfile,
                cancelSubstitution = cancelSubstitution,
                makeSubstitution = makeSubstitution
            )
        }
    }
}

@Composable
fun TeamSelectionTopBar() {
    TopAppBar(
        navigationIcon = { ClickableIcon(imageVector = Icons.Default.ArrowBack) { } },
        title = { TeamSelectionTitle(text = stringResource(id = R.string.pick_team)) },
        modifier = Modifier.background(horizontalGradientBrush),
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
    )
}

@Composable
fun TeamSelectionTitle(
    modifier: Modifier = Modifier,
    text: String,
    textAlign: TextAlign? = null
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = DarkPurple,
        modifier = modifier,
        textAlign = textAlign
    )
}

@Composable
fun GameweekHeader(modifier: Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.gameweek_deadline, "1"),
            textAlign = TextAlign.Center,
        )

        Text(
            text = "Friday 11 Aug, 18:30",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun FieldLayoutScope.Starters(
    starters: List<FplPlayerWithFieldAttributes>,
    playerToSub: FplPlayerWithFieldAttributes?,
    contentWidth: Dp,
    captainId: Int,
    viceCaptainId: Int,
    setPlayerProfile: (FplPlayerWithStats) -> Unit,
    cancelSubstitution: () -> Unit,
    makeSubstitution: (FplPlayerWithFieldAttributes) -> Unit,
) {
    starters.forEach { player ->
        PlayerCard(
            modifier = Modifier
                .width(contentWidth)
                .assignPosition(player.position),
            player = player,
            playerToSub = playerToSub,
            isCaptain = player.id == captainId,
            isViceCaptain = player.id == viceCaptainId,
            onClick = {
                when {
                    playerToSub == null -> setPlayerProfile(player.toFplPlayerWithStats())
                    playerToSub == player -> cancelSubstitution()
                    player.isPotentialSub -> makeSubstitution(player)
                    else -> {}
                }
            }
        )
    }
}

@Composable
fun Substitutes(
    modifier: Modifier,
    substitutes: List<FplPlayerWithFieldAttributes>,
    playerToSub: FplPlayerWithFieldAttributes?,
    contentWidth: Dp,
    setPlayerProfile: (FplPlayerWithStats) -> Unit,
    cancelSubstitution: () -> Unit,
    makeSubstitution: (FplPlayerWithFieldAttributes) -> Unit,

    ) {
    val zeroCornerSize = CornerSize(0.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = SemiTransparentGreen,
                shape = Shapes().medium.copy(
                    bottomStart = zeroCornerSize,
                    bottomEnd = zeroCornerSize
                )
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        substitutes.forEachIndexed { index, player ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val position = if (index == 0) player.position.name
                else "${index}. ${player.position.name}"

                Text(text = position, style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.height(4.dp))
                PlayerCard(
                    modifier = Modifier.width(contentWidth),
                    player = player,
                    playerToSub = playerToSub,
                    onClick = {
                        when {
                            playerToSub == null -> setPlayerProfile(player.toFplPlayerWithStats())
                            playerToSub == player -> cancelSubstitution()
                            player.isPotentialSub -> makeSubstitution(player)
                            else -> {}
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun Chips(modifier: Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Chip(
            modifier = Modifier.weight(1f),
            chipName = "Bench Boost",
            chipStatus = ChipStatus.NOT_PLAYED
        )

        Spacer(modifier = Modifier.width(8.dp))
        Chip(
            modifier = Modifier.weight(1f),
            chipName = "Free Hit",
            chipStatus = ChipStatus.UNAVAILABLE
        )

        Spacer(modifier = Modifier.width(8.dp))
        Chip(
            modifier = Modifier.weight(1f),
            chipName = "Triple Captain",
            chipStatus = ChipStatus.NOT_PLAYED
        )
    }
}

@Composable
fun Chip(modifier: Modifier, chipName: String, chipStatus: ChipStatus) {
    Card(
        modifier = modifier,
        shape = Shapes().extraSmall,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        val (chipBackgroundColor, chopTextColor) = if (chipStatus != ChipStatus.UNAVAILABLE)
            Pair(DarkPurple, Color.White)
        else
            Pair(Color.White, DarkPurple)

        Text(
            text = chipName,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            color = chopTextColor,
            modifier = Modifier
                .fillMaxWidth()
                .background(chipBackgroundColor)
                .padding(vertical = 2.dp),
        )

        val otherColor = if (chipStatus != ChipStatus.UNAVAILABLE) DarkPurple else Color.Gray
        if (chipStatus == ChipStatus.UNAVAILABLE) {
            Divider(
                thickness = 1.dp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }

        val chipStatusText = when (chipStatus) {
            ChipStatus.PLAYED -> "PLAYED"
            ChipStatus.NOT_PLAYED -> "PLAY"
            ChipStatus.UNAVAILABLE -> "UNAVAILABLE"
        }
        Text(
            text = chipStatusText,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            color = otherColor,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 2.dp)
        )
    }
}