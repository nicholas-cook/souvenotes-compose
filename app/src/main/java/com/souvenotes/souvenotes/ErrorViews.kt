package com.souvenotes.souvenotes

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextFieldError(@StringRes errorRes: Int, modifier: Modifier = Modifier) {
    CompositionLocalProvider(
        LocalTextStyle provides LocalTextStyle.current.copy(
            fontSize = 12.sp,
            color = MaterialTheme.colors.error
        )
    ) {
        Text(
            text = stringResource(errorRes),
            modifier = modifier
                .requiredHeight(16.dp)
                .padding(start = 16.dp, end = 12.dp),
        )
    }
}