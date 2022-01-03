package com.souvenotes.souvenotes

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.souvenotes.souvenotes.ui.theme.SouvenotesBrown
import com.souvenotes.souvenotes.ui.theme.SouvenotesYellow

@Composable
fun SouvenotesAppBar(
    @StringRes title: Int,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    val isLightTheme = MaterialTheme.colors.isLight
    val backgroundColor = if (isLightTheme) {
        SouvenotesYellow
    } else {
        MaterialTheme.colors.surface
    }
    val contentColor = if (isLightTheme) {
        SouvenotesBrown
    } else {
        SouvenotesYellow
    }

    TopAppBar(
        title = { Text(stringResource(title)) },
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        navigationIcon = navigationIcon,
        actions = actions
    )
}

@Composable
fun SouvenotesAlertDialog(
    @StringRes title: Int? = null,
    @StringRes message: Int,
    onDismissRequest: () -> Unit,
    @StringRes confirmText: Int,
    confirmAction: () -> Unit,
    @StringRes cancelText: Int? = null,
    cancelAction: (() -> Unit)? = null
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(elevation = 8.dp) {
            Column(modifier = Modifier.padding(top = 16.dp)) {
                title?.let { titleRes ->
                    Text(
                        text = stringResource(titleRes),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                    )
                }
                Text(
                    text = stringResource(message),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(16.dp)
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    cancelText?.let { cancelRes ->
                        TextButton(
                            onClick = {
                                cancelAction?.invoke()
                                onDismissRequest()
                            },
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(text = stringResource(cancelRes))
                        }
                    }
                    TextButton(
                        onClick = {
                            confirmAction()
                            onDismissRequest()
                        },
                        modifier = Modifier.padding(16.dp),
                    ) {
                        Text(stringResource(confirmText))
                    }
                }
            }
        }
    }
}

fun getAdView(context: Context, adId: String): AdView {
    return AdView(context).apply {
        adUnitId = adId
        adSize = getAdSize(context)
        loadAd(AdRequest.Builder().build())
    }
}

fun getAdSize(context: Context): AdSize {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
        if (windowManager != null) {
            val widthPixels = windowManager.currentWindowMetrics.bounds.width()
            val dpi = context.resources.configuration.densityDpi.toDouble()
            val density = dpi / DisplayMetrics.DENSITY_DEFAULT

            val adWidth = (widthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
        }
        return AdSize.BANNER
    } else if (context is Activity) {
        val display = context.windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)

        val widthPixels = outMetrics.widthPixels
        val density = outMetrics.density

        val adWidth = (widthPixels / density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
    }
    return AdSize.BANNER
}