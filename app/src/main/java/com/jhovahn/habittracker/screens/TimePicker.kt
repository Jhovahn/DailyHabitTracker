package com.jhovahn.habittracker.screens

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NumberScroll(
    onValueChange: (String) -> Unit,
    limit: Int = 59,
    beforeLabel: String = "",
    afterLabel: String = "",
    default: Int = 0,
) {
    val state = rememberLazyListState(initialFirstVisibleItemIndex = default)
    val snapBehavior = rememberSnapFlingBehavior(lazyListState = state)
    val showNumber by remember { derivedStateOf { state.firstVisibleItemIndex } }
    val centerIndex by remember {
        derivedStateOf {
            val layoutInfo = state.layoutInfo
            val visibleItems = layoutInfo.visibleItemsInfo
            if (visibleItems.isEmpty()) -1 else {
                val viewPortCenter =
                    (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2

                visibleItems.minByOrNull { item ->
                    val itemCenter = item.offset + (item.size / 2)
                    kotlin.math.abs(itemCenter - viewPortCenter)
                }?.index ?: -1
            }
        }
    }

    LaunchedEffect(state.isScrollInProgress) {
        if (!state.isScrollInProgress) {
            onValueChange(state.firstVisibleItemIndex.toString())
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(end = 16.dp),
        horizontalArrangement = Arrangement.End
    ) {
        if (beforeLabel.isNotBlank()) Text(
            text = beforeLabel, modifier = Modifier.padding(0.dp), color = Color.Gray
        )

        LazyColumn(
            state = state,
            flingBehavior = snapBehavior,
            modifier = Modifier
                .height(80.dp)
                .padding(16.dp)
        ) {

            items(limit) { index ->
                val isSelected = index == centerIndex
                Text(
                    text = "$showNumber",
                    modifier = Modifier
                        .scale(if (isSelected) 1.5f else 1.0f)
                        .padding(8.dp)
                )
            }
        }
        if (afterLabel.isNotBlank()) Text(
            text = afterLabel,
            modifier = Modifier.padding(0.dp),
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}