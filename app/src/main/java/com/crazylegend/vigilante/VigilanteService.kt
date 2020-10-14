package com.crazylegend.vigilante

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

/**
 * Created by crazy on 10/14/20 to long live and prosper !
 */
class VigilanteService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
    }

    override fun onInterrupt() {

    }
}