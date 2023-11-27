package com.souvenotes.souvenotes.policies

import android.text.Spanned
import android.text.method.LinkMovementMethod
import androidx.appcompat.widget.AppCompatTextView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.souvenotes.souvenotes.R
import com.souvenotes.souvenotes.SouvenotesAlertDialog
import com.souvenotes.souvenotes.SouvenotesAppBar

enum class PolicyType {
    Privacy,
    Terms
}

data class PolicyScreenState(
    val progressBarVisible: Boolean = false,
    val spannable: Spanned? = null,
    val showError: Boolean = false
)

@Composable
fun PolicyRoute(onNavigateUp: () -> Unit, viewModel: PolicyViewModel = hiltViewModel()) {
    PolicyScreen(
        policyType = viewModel.policyType,
        policyScreenState = viewModel.policyScreenState,
        onErrorDismissed = viewModel::onErrorDismissed,
        onNavigateUp = onNavigateUp
    )
}

@Composable
fun PolicyScreen(
    policyType: PolicyType,
    policyScreenState: PolicyScreenState,
    onErrorDismissed: () -> Unit,
    onNavigateUp: () -> Unit
) {
    val scrollState = rememberScrollState()
    Scaffold(topBar = {
        PolicyScreenBar(
            policyType = policyType,
            onNavigateUp = onNavigateUp
        )
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            if (policyScreenState.progressBarVisible) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            val textColor = if (MaterialTheme.colors.isLight) {
                android.R.color.black
            } else {
                android.R.color.white
            }
            val linkTextColor = if (MaterialTheme.colors.isLight) {
                R.color.colorAccent
            } else {
                R.color.colorPrimary
            }
            AndroidView(
                factory = { context -> AppCompatTextView(context) },
                update = {
                    with(it) {
                        setTextColor(ContextCompat.getColor(it.context, textColor))
                        setLinkTextColor(ContextCompat.getColor(it.context, linkTextColor))
                        text = policyScreenState.spannable
                        movementMethod = LinkMovementMethod()
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
            if (policyScreenState.showError) {
                val message = when (policyType) {
                    PolicyType.Privacy -> R.string.error_privacy
                    PolicyType.Terms -> R.string.error_terms
                }
                SouvenotesAlertDialog(
                    title = R.string.title_error,
                    message = message,
                    onDismissRequest = {
                        onErrorDismissed()
                        onNavigateUp()
                    },
                    confirmText = android.R.string.ok,
                    confirmAction = {})
            }
        }
    }
}

@Composable
fun PolicyScreenBar(policyType: PolicyType, onNavigateUp: () -> Unit) {
    val title = when (policyType) {
        PolicyType.Privacy -> R.string.title_privacy_policy
        PolicyType.Terms -> R.string.title_terms_and_conditions
    }
    SouvenotesAppBar(
        title = title,
        navigationIcon = {
            IconButton(onClick = { onNavigateUp() }) {
                Icon(Icons.Filled.ArrowBack, stringResource(R.string.content_description_back))
            }
        })
}

@Preview
@Composable
fun PolicyScreenPreview() {
    PolicyScreen(
        policyType = PolicyType.Privacy,
        policyScreenState = PolicyScreenState(),
        onErrorDismissed = {},
        onNavigateUp = {})
}