package com.souvenotes.souvenotes.policies

import android.content.res.AssetManager
import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.Spanned
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

class PolicyViewModel(private val policyType: PolicyType, private val assetManager: AssetManager) :
    ViewModel() {

    companion object {
        private const val PRIVACY_NAME = "souvenotes_privacy_policy.txt"
        private const val TERMS_NAME = "souvenotes_terms_of_service.txt"
    }

    var policyScreenState by mutableStateOf(PolicyScreenState())
        private set
    
    init {
        loadText()
    }

    fun onErrorDismissed() {
        policyScreenState = policyScreenState.copy(showError = false)
    }

    private fun loadText() {
        viewModelScope.launch {
            policyScreenState = policyScreenState.copy(progressBarVisible = true)

            val filename = when (policyType) {
                PolicyType.Privacy -> PRIVACY_NAME
                PolicyType.Terms -> TERMS_NAME
            }
            policyScreenState = try {
                val fileString = getText(filename)
                policyScreenState.copy(spannable = getHtml(fileString), progressBarVisible = false)
            } catch (e: Exception) {
                policyScreenState.copy(showError = true, progressBarVisible = false)
            }
        }
    }

    private fun getText(filename: String): String {
        val stringBuilder = StringBuilder()
        val inputStream = assetManager.open(filename)
        val inputStreamReader = InputStreamReader(inputStream)
        val bufferedReader = BufferedReader(inputStreamReader)
        var nextLine = bufferedReader.readLine()
        while (nextLine != null) {
            stringBuilder.append(nextLine)
            nextLine = bufferedReader.readLine()
        }
        bufferedReader.close()
        inputStreamReader.close()
        inputStream.close()
        return stringBuilder.toString()
    }

    private fun getHtml(html: String): Spanned {
        return HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}