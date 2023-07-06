@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.fpl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.fpl.component.ClickableIcon
import com.example.fpl.component.FieldLayout
import com.example.fpl.component.PlayerCard
import com.example.fpl.component.PlayerProfile
import com.example.fpl.component.draw18YardArc
import com.example.fpl.component.drawCornerArc
import com.example.fpl.component.drawField
import com.example.fpl.component.drawFieldLine
import com.example.fpl.component.drawKickOffLine
import com.example.fpl.model.Field
import com.example.fpl.model.FplPlayerWithFieldAttributes
import com.example.fpl.model.FplPlayerWithStats
import com.example.fpl.ui.theme.DarkPurple
import com.example.fpl.ui.theme.LightGray
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
            modifier = Modifier.padding(it),
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
        Divider(
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth(0.8F)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))
        GameweekHeader(modifier = Modifier.padding(horizontal = 8.dp))

        Spacer(modifier = Modifier.height(16.dp))
        FieldView(
            modifier = Modifier.fillMaxSize(),
            starters = squadState.starters,
            substitutes = squadState.substitutes,
            playerToSub = squadState.playerToSub.value,
            captainId = squadState.captainId,
            viceCaptainId = squadState.viceCaptainId,
            setPlayerProfile = setPlayerProfile,
            cancelSubstitution = { squadState.playerToSub.value = null },
            makeSubstitution = { squadState.makeSubstitution(it) }
        )

        if (selectedPlayerProfile != null) {
            ModalBottomSheet(
                onDismissRequest = closeModal,
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                dragHandle = null,
                shape = Shapes().extraSmall
            ) {
                PlayerProfile(
                    selectedPlayerProfile = selectedPlayerProfile,
                    isCaptain = selectedPlayerProfile.player.id == squadState.captainId,
                    isViceCaptain = selectedPlayerProfile.player.id == squadState.viceCaptainId,
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


        FieldLayout(
            modifier = Modifier
                .drawBehind {
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
                .drawWithContent {
                    if (shouldRender) drawContent()
                },
            verticalSpacing = 16.dp,
            horizontalSpacing = horizontalSpace
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

            Substitutes(
                substitutes = substitutes,
                playerToSub = playerToSub,
                contentWidth = playerWidth,
                modifier = Modifier.padding(8.dp)
            )
        }

    }
}

@Composable
fun TeamSelectionTopBar() {
    TopAppBar(
        navigationIcon = { ClickableIcon(imageVector = Icons.Default.ArrowBack) { } },
        title = { TeamSelectionTitle(text = "Pinky and the bane") },
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
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
        color = DarkPurple,
        modifier = modifier,
        textAlign = textAlign
    )
}

@Composable
fun GameweekHeader(modifier: Modifier) {
    Box(
        modifier = modifier
            .height(IntrinsicSize.Max)
            .fillMaxWidth()
    ) {
        ClickableIcon(
            modifier = Modifier.align(Alignment.CenterStart),
            backgroundColor = LightGray,
            iconRes = R.drawable.chevron_left,
        ) { }

        TeamSelectionTitle(
            text = "Gameweek 38",
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
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
                when (playerToSub) {
                    null -> setPlayerProfile(player.toFplPlayerWithStats())
                    player -> cancelSubstitution()
                    else -> makeSubstitution(player)
                }
            }
        )
    }
}

@Composable
fun Substitutes(
    substitutes: List<FplPlayerWithFieldAttributes>,
    playerToSub: FplPlayerWithFieldAttributes?,
    contentWidth: Dp,
    modifier: Modifier
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
        substitutes.forEach {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = it.position.name, style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.height(24.dp))
                PlayerCard(
                    modifier = Modifier.width(contentWidth),
                    player = it,
                    playerToSub = playerToSub,
                    onClick = {}
                )
            }
        }
    }
}