package com.crazylegend.vigilante.headset.database

import com.crazylegend.vigilante.utils.MainCoroutineRule
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by funkymuse on 3/23/21 to long live and prosper !
 */
class HeadsetRepositoryTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @MockK
    lateinit var headsetRepository: HeadsetRepository

    @Test
    fun addHeadsetRecord() {
        coJustRun { headsetRepository.addHeadsetRecord(any()) }
    }

    @Test
    fun getAllHeadsetRecords() {
        coEvery { headsetRepository.getAllHeadsetRecords().invalid } returns false
        headsetRepository.getAllHeadsetRecords()
        verify { headsetRepository.getAllHeadsetRecords() }
    }

    @Test
    fun removeHeadsetRecord() {
        coJustRun { headsetRepository.removeHeadsetRecord(any()) }
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