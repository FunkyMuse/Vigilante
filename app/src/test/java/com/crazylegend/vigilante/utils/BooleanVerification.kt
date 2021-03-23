package com.crazylegend.vigilante.utils

import io.mockk.every
import io.mockk.verify
import junit.framework.Assert.assertNotNull
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import kotlin.random.Random

/**
 * Created by funkymuse on 3/23/21 to long live and prosper !
 */

inline fun verifyBooleanInstanceAndCall(crossinline call: () -> Boolean) {
    every { call() } returns Random.nextBoolean()
    verifyCall(call)
}

inline fun verifyIntInstanceAndCall(crossinline call: () -> Int) {
    every { call() } returns Random.nextInt()
    verifyCall(call)
}

inline fun verifyFloatInstanceAndCall(crossinline call: () -> Float) {
    every { call() } returns Random.nextFloat()
    verifyCall(call)
}

inline fun verifyDoubleInstanceAndCall(crossinline call: () -> Float) {
    every { call() } returns Random.nextFloat()
    verifyCall(call)
}
inline fun <reified T : Any> verifyCall(crossinline call: () -> T) {
    assertThat(call(), instanceOf(T::class.java))
    assertNotNull(call())
    verify { call() }
}