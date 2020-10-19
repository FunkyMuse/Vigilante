package com.crazylegend.vigilante.clipboard

import android.annotation.SuppressLint
import android.content.Context
import android.view.accessibility.AccessibilityEvent
import com.crazylegend.kotlinextensions.locale.LocaleHelper
import com.crazylegend.kotlinextensions.log.debug
import com.crazylegend.kotlinextensions.tryOrNull
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.di.qualifiers.ServiceContext
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

/**
 * Created by crazy on 10/19/20 to long live and prosper !
 */
@SuppressLint("DefaultLocale")
@ServiceScoped
class ClipboardProvider @Inject constructor(
        @ServiceContext private val context: Context) {

    private var selectedText: String? = null
    private var copiedText: String? = null

    fun processEvent(event: AccessibilityEvent) {
        val hasClickedCopy = hasUserClickedCopy(event)

        val hasClickedPaste = hasUserClickedPaste(event)
        val mainText = event.text?.toString()

        if (event.eventType == AccessibilityEvent.TYPE_VIEW_CLICKED) {
            if (hasClickedCopy) {
                copiedText = selectedText
                debug { "IS CLICKED COPY $hasClickedCopy for $selectedText" }
            }
            if (hasClickedPaste) {
                debug { "IS CLICKED PASTE $hasClickedPaste for $selectedText" }
            }
        }

        if (event.eventType == AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED) {
            selectedText = tryOrNull { event.text?.firstOrNull()?.substring(event.fromIndex, event.toIndex) }
            debug { "SELECTED THE TEXT $selectedText" }
        }
    }

    private fun hasUserClickedPaste(event: AccessibilityEvent): Boolean =
            (event.contentDescription != null &&
                    event.contentDescription.toString().toLowerCase()
                            .equals(LocaleHelper.getLocalizedString(context, R.string.paste), true))

    private fun hasUserClickedCopy(event: AccessibilityEvent): Boolean =
            (event.contentDescription != null &&
                    (event.contentDescription.toString().toLowerCase()
                            .equals(LocaleHelper.getLocalizedString(context, R.string.copy), true) ||
                            event.contentDescription.toString().toLowerCase()
                                    .equals(LocaleHelper.getLocalizedString(context, R.string.cut), true)
                            ))


}