package com.crazylegend.vigilante.utils

import org.junit.Assert

/**
 * Created by funkymuse on 3/23/21 to long live and prosper !
 */

inline fun toggleValue(initialExpectation: Boolean, initialValue: ()-> Boolean,  toggler: () -> Unit) {
    Assert.assertEquals(initialValue(), initialExpectation)
    toggler()
    Assert.assertEquals(initialValue(), !initialExpectation)
}