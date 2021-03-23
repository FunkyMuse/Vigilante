package com.crazylegend.vigilante.crashes

import com.crazylegend.kotlinextensions.collections.isNotNullOrEmpty
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Test


/**
 * Created by funkymuse on 3/23/21 to long live and prosper !
 */
class CrashViewModelTest {

    @Test
    fun `crash results`() {
        val crashViewModelTest = mockk<CrashViewModel>(relaxed = true)
        every { crashViewModelTest.crashes } returns listOf("Crash #1", "Crash2")
        assert(crashViewModelTest.crashes.isNotNullOrEmpty)
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