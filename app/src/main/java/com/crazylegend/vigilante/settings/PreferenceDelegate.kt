package com.crazylegend.vigilante.settings

import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Created by funkymuse on 5/28/21 to long live and prosper !
 */
class PreferenceDelegate<T : Preference>(private val key: String) :
    ReadOnlyProperty<PreferenceFragmentCompat, T> {

    override fun getValue(thisRef: PreferenceFragmentCompat, property: KProperty<*>): T {
        return thisRef.findPreference(key)!!
    }
}

fun <T : Preference> getPreference(key: String) = PreferenceDelegate<T>(key)