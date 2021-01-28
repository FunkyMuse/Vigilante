package com.crazylegend.vigilante.filter

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

/**
 * Created by crazy on 11/4/20 to long live and prosper !
 */

@Parcelize
@Keep
data class FilterModel(@StringRes val title: Int, var isChecked: Boolean = false) : Parcelable