package com.souvenotes.souvenotes.settings

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.souvenotes.souvenotes.BuildConfig
import com.souvenotes.souvenotes.R
import com.souvenotes.souvenotes.SouvenotesAlertDialog
import com.souvenotes.souvenotes.SouvenotesAppBar
import com.souvenotes.souvenotes.repository.prefs.AppThemePref
import org.koin.androidx.compose.getViewModel

@Composable
fun SettingsRoute(
    onNavigateUp: () -> Unit,
    onChangeEmailClicked: () -> Unit,
    onChangePasswordClicked: () -> Unit,
    onDeleteAccountClicked: () -> Unit,
    onTermsClicked: () -> Unit,
    onPrivacyClicked: () -> Unit, viewModel: SettingsViewModel = getViewModel()
) {
    SettingsScreen(
        onNavigateUp = onNavigateUp,
        currentTheme = viewModel.currentAppTheme,
        onThemeChanged = viewModel::onAppThemeSelected,
        onChangeEmailClicked = onChangeEmailClicked,
        onChangePasswordClicked = onChangePasswordClicked,
        onDeleteAccountClicked = onDeleteAccountClicked,
        onTermsClicked = onTermsClicked,
        onPrivacyClicked = onPrivacyClicked
    )
}

@Composable
fun SettingsScreen(
    onNavigateUp: () -> Unit,
    currentTheme: AppThemePref,
    onThemeChanged: (newTheme: AppThemePref) -> Unit,
    onChangeEmailClicked: () -> Unit,
    onChangePasswordClicked: () -> Unit,
    onDeleteAccountClicked: () -> Unit,
    onTermsClicked: () -> Unit,
    onPrivacyClicked: () -> Unit
) {
    val scrollState = rememberScrollState()
    var showThemeDialog: Boolean by remember { mutableStateOf(false) }
    var showEmailDialog: Boolean by remember { mutableStateOf(false) }
    Scaffold(topBar = { SettingsScreenBar(onNavigateUp = onNavigateUp) }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            SettingsOption(
                title = R.string.change_theme,
                onClick = { showThemeDialog = true }
            )
            Divider()
            SettingsOption(
                title = R.string.change_email,
                onClick = onChangeEmailClicked
            )
            Divider()
            SettingsOption(
                title = R.string.change_password,
                onClick = onChangePasswordClicked
            )
            Divider()
            SettingsOption(
                title = R.string.delete_account,
                onClick = onDeleteAccountClicked
            )
            Divider()
            SettingsOption(
                title = R.string.terms_and_conditions,
                onClick = onTermsClicked
            )
            Divider()
            SettingsOption(
                title = R.string.privacy_policy,
                onClick = onPrivacyClicked
            )
            Divider()
            SettingsOption(
                title = R.string.contact_us,
                onClick = { showEmailDialog = true }
            )
            Divider()
        }
    }
    if (showThemeDialog) {
        var selectedAppTheme: AppThemePref by remember { mutableStateOf(currentTheme) }
        Dialog(onDismissRequest = { showThemeDialog = false }) {
            Card(elevation = 8.dp) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp)
                            .selectable(
                                selected = selectedAppTheme == AppThemePref.Light,
                                onClick = { selectedAppTheme = AppThemePref.Light }
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedAppTheme == AppThemePref.Light,
                            onClick = { selectedAppTheme = AppThemePref.Light },
                            colors = RadioButtonDefaults.colors()
                        )
                        Text(text = stringResource(R.string.option_light), fontSize = 16.sp)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp)
                            .selectable(
                                selected = selectedAppTheme == AppThemePref.Dark,
                                onClick = { selectedAppTheme = AppThemePref.Dark }
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedAppTheme == AppThemePref.Dark,
                            onClick = { selectedAppTheme = AppThemePref.Dark })
                        Text(text = stringResource(R.string.option_dark), fontSize = 16.sp)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp)
                            .selectable(
                                selected = selectedAppTheme == AppThemePref.System,
                                onClick = { selectedAppTheme = AppThemePref.System }
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedAppTheme == AppThemePref.System,
                            onClick = { selectedAppTheme = AppThemePref.System })
                        Text(text = stringResource(R.string.option_system), fontSize = 16.sp)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = { showThemeDialog = false },
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(text = stringResource(android.R.string.cancel))
                        }
                        TextButton(
                            onClick = {
                                onThemeChanged(selectedAppTheme)
                                showThemeDialog = false
                            },
                            modifier = Modifier.padding(16.dp),
                        ) {
                            Text(stringResource(R.string.action_confirm))
                        }
                    }
                }
            }
        }
    }
    if (showEmailDialog) {
        val context = LocalContext.current
        val emailIntent = getEmailIntent(context)
        val canSendEmail = canSendEmail(emailIntent, context)
        val confirmText = if (canSendEmail) {
            R.string.open_email
        } else {
            R.string.copy_email_address
        }
        SouvenotesAlertDialog(
            message = R.string.contact_us_message,
            onDismissRequest = { showEmailDialog = false },
            confirmText = confirmText,
            confirmAction = {
                if (canSendEmail) {
                    context.startActivity(emailIntent)
                } else {
                    copyEmailToClipboard(context)
                }
            })
    }
}

@Composable
fun SettingsScreenBar(onNavigateUp: () -> Unit) {
    SouvenotesAppBar(
        title = R.string.title_settings,
        navigationIcon = {
            IconButton(onClick = { onNavigateUp() }) {
                Icon(Icons.Filled.ArrowBack, stringResource(R.string.content_description_back))
            }
        })
}

@Composable
fun SettingsOption(@StringRes title: Int, onClick: () -> Unit) {
    Text(
        text = stringResource(title),
        fontSize = 20.sp,
        modifier = Modifier
            .clickable { onClick() }
            .padding(16.dp)
            .fillMaxWidth()
    )
}

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(
        onNavigateUp = {},
        currentTheme = AppThemePref.System,
        onThemeChanged = {},
        onChangeEmailClicked = {},
        onChangePasswordClicked = {},
        onDeleteAccountClicked = {},
        onTermsClicked = {},
        onPrivacyClicked = {})
}

private fun getEmailIntent(context: Context): Intent {
    val supportEmail = context.getString(R.string.support_email)
    val supportSubject = context.getString(R.string.support_subject, BuildConfig.VERSION_NAME)
    return Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(supportEmail))
        putExtra(Intent.EXTRA_SUBJECT, supportSubject)
    }
}

private fun canSendEmail(emailIntent: Intent, context: Context): Boolean {
    return emailIntent.resolveActivity(context.packageManager) != null
}

private fun copyEmailToClipboard(context: Context) {
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
    val clip = ClipData.newPlainText(
        context.getString(R.string.support_email_label),
        context.getString(R.string.support_email)
    )
    clipboardManager?.let {
        it.setPrimaryClip(clip)
        Toast.makeText(context, R.string.copied_to_clipboard, Toast.LENGTH_LONG).show()
    }
}