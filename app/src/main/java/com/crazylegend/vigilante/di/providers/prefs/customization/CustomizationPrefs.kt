package com.crazylegend.vigilante.di.providers.prefs.customization

import android.content.SharedPreferences
import android.view.Gravity
import com.crazylegend.sharedpreferences.putFloat
import com.crazylegend.sharedpreferences.putInt
import com.crazylegend.vigilante.customization.CustomizationFragment
import com.crazylegend.vigilante.di.qualifiers.EncryptedPrefs
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by funkymuse on 6/12/21 to long live and prosper !
 */
@Singleton
class CustomizationPrefs @Inject constructor(
        @EncryptedPrefs private val defaultPrefs: SharedPreferences
) : CustomizationContract {

    companion object {
        const val DEFAULT_NOTIFICATION_COLOR = 31727
        const val DEFAULT_VIBRATION_POSITION = 0
        const val DEFAULT_LAYOUT_POSITION = 0
        const val DEFAULT_SPACING = 5
        const val DEFAULT_DOT_COLOR = 31727
        const val DEFAULT_DOT_SIZE = 20f

    }

    //region save
    override fun saveColorPref(prefBaseName: String, pickedColor: Int) =
            defaultPrefs.putInt(prefBaseName, pickedColor)

    override fun saveSizePref(prefBaseName: String, sizeSlider: Float) =
            defaultPrefs.putFloat(prefBaseName, sizeSlider)

    override fun savePositionPref(prefBaseName: String, position: Int) =
            defaultPrefs.putInt(prefBaseName, position)

    override fun saveNotificationColorPref(prefBaseName: String, pickedColor: Int) =
            defaultPrefs.putInt(prefBaseName, pickedColor)

    //endregion


    //region get
    override fun getColorPref(basePref: String) =
            defaultPrefs.getInt(basePref + CustomizationFragment.COLOR_DOT_PREF_ADDITION,
                    DEFAULT_DOT_COLOR
            )

    override fun getSizePref(basePref: String) =
            defaultPrefs.getFloat(basePref + CustomizationFragment.SIZE_PREF_ADDITION,
                    DEFAULT_DOT_SIZE
            )

    /**
    0 is top right corner
    1 is top left corner

    2 is center right side
    3 is center left side

    4 is bottom right corner
    5 is bottom left corner

    6 is top center horizontal
    7 is bottom center horizontal
     * @return Int
     */
    override fun getPositionPref(basePref: String) =
            defaultPrefs.getInt(basePref + CustomizationFragment.POSITION_PREF_ADDITION,
                    DEFAULT_LAYOUT_POSITION
            )

    override fun getPositionSpacing(basePref: String) =
            defaultPrefs.getInt(basePref + CustomizationFragment.POSITION_SPACING_ADDITION,
                    DEFAULT_SPACING
            )

    override fun saveSpacing(basePref: String, spacing: Int) =
            defaultPrefs.putInt(basePref + CustomizationFragment.POSITION_SPACING_ADDITION, spacing)

    override fun getLayoutPosition(pref: Int) = when (pref) {
        0 -> Gravity.TOP or Gravity.END
        1 -> Gravity.TOP or Gravity.START
        2 -> Gravity.CENTER_VERTICAL or Gravity.END
        3 -> Gravity.CENTER_VERTICAL or Gravity.START
        4 -> Gravity.BOTTOM or Gravity.END
        5 -> Gravity.BOTTOM or Gravity.START
        6 -> Gravity.TOP or Gravity.CENTER_HORIZONTAL
        7 -> Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        else -> Gravity.TOP or Gravity.END
    }

    override fun getNotificationColorPref(basePref: String) =
            defaultPrefs.getInt(basePref + CustomizationFragment.COLOR_NOTIFICATION_PREF_ADDITION,
                    DEFAULT_NOTIFICATION_COLOR
            )


    override fun getVibrationPref(basePref: String) =
            defaultPrefs.getInt(basePref + CustomizationFragment.VIBRATION_PREF_ADDITION,
                    DEFAULT_VIBRATION_POSITION
            )

    override fun getVibrationEffect(pref: Int) = when (pref) {
        0 -> null
        1 -> longArrayOf(60, 80, 100)
        2 -> longArrayOf(150, 180, 200)
        3 -> longArrayOf(250, 300, 350)
        else -> null
    }
    //endregion
}