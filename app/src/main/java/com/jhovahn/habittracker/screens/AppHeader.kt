package com.jhovahn.habittracker.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.jhovahn.habittracker.ui.theme.headerFontFamily

@Composable
fun AppHeader(
    text: String,
    modifier: Modifier,
) {
    Text(
        text = text,
        modifier = modifier,
        fontFamily = headerFontFamily,
        fontSize = 30.sp
    )
}