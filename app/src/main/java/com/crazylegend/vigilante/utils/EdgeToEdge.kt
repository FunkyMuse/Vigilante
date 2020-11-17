/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.crazylegend.vigilante.utils

import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.view.updatePadding
import com.crazylegend.vigilante.R
import com.google.android.material.appbar.AppBarLayout

/**
 * A utility for edge-to-edge display. It provides several features needed to make the app
 * displayed edge-to-edge on Android Q with gestural navigation.
 */
object EdgeToEdge : EdgeToEdgeImpl by EdgeToEdgeApi21()

private interface EdgeToEdgeImpl {

    /**
     * Configures a root view of an Activity in edge-to-edge display.
     * @param root A root view of an Activity.
     */
    fun setUpRoot(root: ViewGroup) {}

    /**
     * Configures an app bar and a toolbar for edge-to-edge display.
     * @param appBar An [AppBarLayout].
     * @param toolbar A [Toolbar] in the [appBar].
     */
    fun setUpAppBar(appBar: AppBarLayout, toolbar: Toolbar) {}

    /**
     * Configures a scrolling content for edge-to-edge display.
     * @param scrollingContent A scrolling ViewGroup. This is typically a RecyclerView or a
     * ScrollView. It should be as wide as the screen, and should touch the bottom edge of
     * the screen.
     */
    fun setUpScrollingContent(scrollingContent: ViewGroup) {}
}

@RequiresApi(21)
private class EdgeToEdgeApi21 : EdgeToEdgeImpl {

    override fun setUpRoot(root: ViewGroup) {
        root.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }

    override fun setUpAppBar(appBar: AppBarLayout, toolbar: Toolbar) {
        val toolbarPadding = toolbar.resources.getDimensionPixelSize(R.dimen.spacing_medium)
        appBar.setOnApplyWindowInsetsListener { _, windowInsets ->
            appBar.updatePadding(top = windowInsets.systemWindowInsetTop)
            toolbar.updatePadding(
                    left = toolbarPadding + windowInsets.systemWindowInsetLeft,
                    right = windowInsets.systemWindowInsetRight
            )
            windowInsets
        }
    }

    override fun setUpScrollingContent(scrollingContent: ViewGroup) {
        val originalPaddingLeft = scrollingContent.paddingLeft
        val originalPaddingRight = scrollingContent.paddingRight
        val originalPaddingBottom = scrollingContent.paddingBottom
        val originalPaddingTop = scrollingContent.paddingTop
        scrollingContent.setOnApplyWindowInsetsListener { _, windowInsets ->
            scrollingContent.updatePadding(
                    left = originalPaddingLeft + windowInsets.systemWindowInsetLeft,
                    right = originalPaddingRight + windowInsets.systemWindowInsetRight,
                    bottom = originalPaddingBottom + windowInsets.systemWindowInsetBottom,
                    top = originalPaddingTop + windowInsets.systemWindowInsetTop
            )
            windowInsets
        }
    }
}
