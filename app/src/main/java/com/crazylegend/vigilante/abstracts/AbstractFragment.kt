package com.crazylegend.vigilante.abstracts

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * Created by crazy on 10/14/20 to long live and prosper !
 */
abstract class AbstractFragment<BINDING : ViewBinding>(contentLayoutId: Int) : Fragment(contentLayoutId) {

    abstract val binding: BINDING
}