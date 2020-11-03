package com.crazylegend.vigilante.home.section

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * Created by crazy on 11/2/20 to long live and prosper !
 */
data class SectionItem(@StringRes val title: Int, @DrawableRes val icon: Int, val action: SectionItemAction) {

    enum class SectionItemAction {
        CAMERA, MIC, PERMISSIONS, CLIPBOARD, HEADSET, NOTIFICATIONS,
        LOCK_SCREEN, APPS_USAGE
    }
}