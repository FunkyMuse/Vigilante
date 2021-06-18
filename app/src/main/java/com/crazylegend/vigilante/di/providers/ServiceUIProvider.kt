package com.crazylegend.vigilante.di.providers

import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.crazylegend.common.exhaustive
import com.crazylegend.contextgetters.windowManager
import com.crazylegend.view.removeViewsByTag
import com.crazylegend.vigilante.databinding.ServiceLayoutBinding
import com.crazylegend.vigilante.di.qualifiers.ServiceContext
import com.crazylegend.vigilante.utils.DotPosition
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

/**
 * Created by funkymuse on 6/13/21 to long live and prosper !
 */
@ServiceScoped
class ServiceUIProvider @Inject constructor(@ServiceContext private val context: Context) {

    private val windowManager get() = context.windowManager
    private val serviceLayout get() = ServiceLayoutBinding.inflate(LayoutInflater.from(context))

    private val topLeftLayout = serviceLayout
    private val topLeftLayoutParams: WindowManager.LayoutParams = createParams(Gravity.TOP or Gravity.START)

    private val topRightLayout = serviceLayout
    private val topRightLayoutParams: WindowManager.LayoutParams = createParams(Gravity.TOP or Gravity.END)

    private val bottomLeftLayout = serviceLayout
    private val bottomLeftLayoutParams: WindowManager.LayoutParams = createParams(Gravity.BOTTOM or Gravity.START)

    private val bottomRightLayout = serviceLayout
    private val bottomRightLayoutParams: WindowManager.LayoutParams = createParams(Gravity.BOTTOM or Gravity.END)

    private val centerLeftVerticalLayout = serviceLayout
    private val centerLeftVerticalLayoutParams: WindowManager.LayoutParams = createParams(Gravity.CENTER_VERTICAL or Gravity.START)

    private val centerRightVerticalLayout = serviceLayout
    private val centerRightVerticalLayoutParams: WindowManager.LayoutParams = createParams(Gravity.CENTER_VERTICAL or Gravity.END)

    private val centerTopHorizontalLayout = serviceLayout
    private val centerTopHorizontalLayoutParams: WindowManager.LayoutParams = createParams(Gravity.CENTER_HORIZONTAL or Gravity.TOP)

    private val centerBottomHorizontalLayout = serviceLayout
    private val centerBottomHorizontalLayoutParams: WindowManager.LayoutParams = createParams(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM)

    fun initViews() {
        windowManager?.addView(topLeftLayout.root, topLeftLayoutParams)
        windowManager?.addView(topRightLayout.root, topRightLayoutParams)

        windowManager?.addView(bottomLeftLayout.root, bottomLeftLayoutParams)
        windowManager?.addView(bottomRightLayout.root, bottomRightLayoutParams)

        windowManager?.addView(centerLeftVerticalLayout.root, centerLeftVerticalLayoutParams)
        windowManager?.addView(centerRightVerticalLayout.root, centerRightVerticalLayoutParams)

        windowManager?.addView(centerTopHorizontalLayout.root, centerTopHorizontalLayoutParams)
        windowManager?.addView(centerBottomHorizontalLayout.root, centerBottomHorizontalLayoutParams)
    }

    fun addDot(layout: FrameLayout, gravityPosition: Int, tag: String) {
        val linearLayout = getLayoutByPosition(gravityPosition)
        addViewToLayout(linearLayout.root, tag, layout)
    }

    fun removeDot(gravityPosition: Int, tag: String) {
        val linearLayout = getLayoutByPosition(gravityPosition)

        linearLayout.root.apply {
            removeViewsByTag(tag)
        }
    }

    fun updateDot(gravityPosition: Int) {
        val linearLayout = getLayoutByPosition(gravityPosition)
        val linearLayoutParams = getLayoutParamsByPosition(gravityPosition)
        windowManager?.updateViewLayout(linearLayout.root, linearLayoutParams)
    }

    private fun getLayoutParamsByPosition(gravityPosition: Int): WindowManager.LayoutParams {
        return when (gravityPosition) {
            DotPosition.TOP_RIGHT.position -> topRightLayoutParams
            DotPosition.TOP_LEFT.position -> topLeftLayoutParams

            DotPosition.BOTTOM_RIGHT.position -> bottomRightLayoutParams
            DotPosition.BOTTOM_LEFT.position -> bottomLeftLayoutParams

            DotPosition.CENTER_LEFT.position -> centerLeftVerticalLayoutParams
            DotPosition.CENTER_RIGHT.position -> centerRightVerticalLayoutParams

            DotPosition.TOP_CENTER.position -> centerTopHorizontalLayoutParams
            DotPosition.BOTTOM_CENTER.position -> centerBottomHorizontalLayoutParams
            else -> topRightLayoutParams
        }.exhaustive
    }

    private fun getLayoutByPosition(gravityPosition: Int): ServiceLayoutBinding {
        return when (gravityPosition) {
            DotPosition.TOP_RIGHT.position -> topRightLayout
            DotPosition.TOP_LEFT.position -> topLeftLayout

            DotPosition.BOTTOM_RIGHT.position -> bottomRightLayout
            DotPosition.BOTTOM_LEFT.position -> bottomLeftLayout

            DotPosition.CENTER_LEFT.position -> centerLeftVerticalLayout
            DotPosition.CENTER_RIGHT.position -> centerRightVerticalLayout

            DotPosition.TOP_CENTER.position -> centerTopHorizontalLayout
            DotPosition.BOTTOM_CENTER.position -> centerBottomHorizontalLayout
            else -> topRightLayout
        }.exhaustive
    }

    private fun addViewToLayout(linearLayout: LinearLayout, tag: String, layout: FrameLayout) {
        linearLayout.apply {
            removeViewsByTag(tag)
            layout.tag = tag
            addView(layout)
        }
    }

    private fun createParams(layoutPositionGravity: Int): WindowManager.LayoutParams = WindowManager.LayoutParams().apply {
        type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
        format = PixelFormat.TRANSLUCENT
        flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        width = WindowManager.LayoutParams.WRAP_CONTENT
        height = WindowManager.LayoutParams.WRAP_CONTENT
        gravity = layoutPositionGravity
    }

    fun cleanUp() {
        windowManager?.removeView(topLeftLayout.root)
        windowManager?.removeView(topRightLayout.root)

        windowManager?.removeView(bottomLeftLayout.root)
        windowManager?.removeView(bottomRightLayout.root)

        windowManager?.removeView(centerLeftVerticalLayout.root)
        windowManager?.removeView(centerRightVerticalLayout.root)

        windowManager?.removeView(centerTopHorizontalLayout.root)
        windowManager?.removeView(centerBottomHorizontalLayout.root)
    }
}