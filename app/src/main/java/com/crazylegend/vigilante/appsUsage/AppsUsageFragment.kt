package com.crazylegend.vigilante.appsUsage

import android.os.Bundle
import android.view.View
import com.crazylegend.viewbinding.viewBinding
import com.crazylegend.vigilante.R
import com.crazylegend.vigilante.abstracts.AbstractFragment
import com.crazylegend.vigilante.databinding.LayoutRecyclerBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by crazy on 11/3/20 to long live and prosper !
 */
@AndroidEntryPoint
class AppsUsageFragment : AbstractFragment<LayoutRecyclerBinding>(R.layout.layout_recycler) {

    override val binding: LayoutRecyclerBinding by viewBinding(LayoutRecyclerBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
/*val cal: Calendar = Calendar.getInstance()
           cal.add(Calendar.YEAR, -1)
           val queryUsageStats = requireContext().usageStatsManager?.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
                   cal.timeInMillis, System.currentTimeMillis())
           queryUsageStats?.asSequence()?.forEach {

           }*/
    }
}