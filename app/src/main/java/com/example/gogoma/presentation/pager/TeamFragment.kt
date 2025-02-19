package com.example.gogoma.presentation.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.gogoma.presentation.screens.TeamScreen
import com.example.gogoma.presentation.viewmodel.MarathonDataViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

class TeamFragment : Fragment() {
    private val marathonDataViewModel: MarathonDataViewModel by activityViewModels()
    private var viewPager: ViewPager2? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = ComposeView(requireContext()).apply {
            setContent {
                TeamScreen(marathonDataViewModel)
            }
        }

        // ViewPager2 가져오기
        viewPager = activity?.findViewById(ViewPager2_ID)

        // ✅ 상태 감지 후 TeamRoadScreen(팀 화면 2)로 이동
        lifecycleScope.launch {
            marathonDataViewModel.marathonState.collect { state ->
                val shouldNavigateToTeamRoad = state.friendInfoList.any { it.gapDistance.absoluteValue <= 10000 }
                if (shouldNavigateToTeamRoad) {
                    viewPager?.setCurrentItem(2, true) // 팀 화면 2로 이동
                }
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewPager = null
    }
}
