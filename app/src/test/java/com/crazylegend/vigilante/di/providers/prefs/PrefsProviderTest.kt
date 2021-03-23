package com.crazylegend.vigilante.di.providers.prefs

import com.crazylegend.vigilante.utils.verifyBooleanInstanceAndCall
import com.crazylegend.vigilante.utils.verifyFloatInstanceAndCall
import com.crazylegend.vigilante.utils.verifyIntInstanceAndCall
import io.mockk.*
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test

/**
 * Created by funkymuse on 3/23/21 to long live and prosper !
 */
class PrefsProviderTest {

    val defaultPreferences = spyk<DefaultPreferences>()

    @Test
    fun `update Date Format and get the updated date`() {

        val dateFormat = "dd.MM.yyyy HH:mm:ss"
        defaultPreferences.updateDateFormat(dateFormat)
        every { defaultPreferences.getDateFormat } returns dateFormat
        assertThat(defaultPreferences.getDateFormat, instanceOf(String::class.java))
        assertEquals(defaultPreferences.getDateFormat, dateFormat)
    }

    @Test
    fun `notifications test`() {
        verifyBooleanInstanceAndCall { defaultPreferences.areNotificationsEnabled }
    }

    @Test
    fun `dark theme test`() {
        verifyBooleanInstanceAndCall { defaultPreferences.isDarkThemeEnabled }
    }

    @Test
    fun `dot status test`() {
        verifyBooleanInstanceAndCall { defaultPreferences.isDotEnabled }
    }

    @Test
    fun `is app excluded from notifications test`() {
        verifyBooleanInstanceAndCall { defaultPreferences.isVigilanteExcludedFromNotifications }
    }

    @Test
    fun `camera color test`() {
        verifyIntInstanceAndCall { defaultPreferences.getCameraColorPref }
    }

    @Test
    fun `camera size`() {
        verifyFloatInstanceAndCall { defaultPreferences.getCameraSizePref }
    }

    @Test
    fun `intro shown`() {
        every { defaultPreferences.isIntroShown } returns false
        defaultPreferences.setIntroShown()
        every { defaultPreferences.isIntroShown } returns true
        assertEquals(defaultPreferences.isIntroShown, true)
        verify { defaultPreferences.setIntroShown() }
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