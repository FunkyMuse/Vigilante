package com.crazylegend.vigilante.headset.ui

import androidx.paging.PagingData
import com.crazylegend.vigilante.headset.database.HeadsetModel
import com.crazylegend.vigilante.utils.MainCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by funkymuse on 3/23/21 to long live and prosper !
 */
class HeadsetViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    //what a pain in the ass this paging library to test
    @Test
    fun `get headset record`() {
        val headsetViewModel = mockk<HeadsetViewModel>(relaxed = true)
        coEvery { headsetViewModel.headsetData } returns flow { PagingData.from(listOf(HeadsetModel(headsetUsageType = 1, chargingType = 1, batteryPercentage = 30, id = 1))) }
        runBlockingTest {
            headsetViewModel.headsetData.collectIndexed { index, _ ->
                assert(index == 1)
            }
        }
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Before
    fun setup() {
    }


    @After
    fun tearDown() {
        unmockkAll()
    }
}