package com.crazylegend.vigilante.di.providers.prefs

import android.graphics.Color
import com.crazylegend.vigilante.di.providers.prefs.DefaultPreferencessProvider.Companion.DEFAULT_DOT_COLOR
import com.crazylegend.vigilante.di.providers.prefs.DefaultPreferencessProvider.Companion.DEFAULT_DOT_SIZE
import com.crazylegend.vigilante.utils.toggleValue
import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat

/**
 * Created by funkymuse on 3/23/21 to long live and prosper !
 */
class PrefsProviderTest {

    val defaultPreferences = FakePrefsProvider()

    @Test
    fun `update Date Format and get the updated date`() {
        val dateFormat = "yyyy.MM.dd HH:mm:ss"
        assertThat(defaultPreferences.getDateFormat, instanceOf(String::class.java))
        defaultPreferences.updateDateFormat(dateFormat)
        assertThat(defaultPreferences.getDateFormat, instanceOf(String::class.java))
        assertThat(SimpleDateFormat(dateFormat), instanceOf(SimpleDateFormat::class.java))
    }

    @Test
    fun `notifications test`() {
        toggleValue(initialExpectation = false, { defaultPreferences.areNotificationsEnabled }) { defaultPreferences.updateNotificationsValue(true) }
    }

    @Test
    fun `dark theme test`() {
        toggleValue(initialExpectation = false, { defaultPreferences.isDarkThemeEnabled }) { defaultPreferences.changeTheme() }
    }

    @Test
    fun `dot status test`() {
        toggleValue(initialExpectation = false, { defaultPreferences.isDotEnabled }) { defaultPreferences.setDotStatus(true) }
    }

    @Test
    fun `is app excluded from notifications test`() {
        toggleValue(initialExpectation = false, { defaultPreferences.isVigilanteExcludedFromNotifications }) { defaultPreferences.setExcludeVigilanteFromNotificationsStatus(true) }
    }

    @Test
    fun `camera color test`() {
        val setColor = Color.CYAN
        assertEquals(defaultPreferences.getCameraColorPref, DEFAULT_DOT_COLOR)
        defaultPreferences.setCameraColor(setColor)
        assertEquals(defaultPreferences.getCameraColorPref, setColor)
    }

    @Test
    fun `camera size`() {
        val customSize = 50f
        assertEquals(defaultPreferences.getCameraSizePref, DEFAULT_DOT_SIZE)
        defaultPreferences.setCameraSize(customSize)
        assertEquals(defaultPreferences.getCameraSizePref, customSize)
    }

    @Test
    fun `intro shown`() {
        toggleValue(initialExpectation = false, { defaultPreferences.isIntroShown }) { defaultPreferences.setIntroShown() }
    }


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}