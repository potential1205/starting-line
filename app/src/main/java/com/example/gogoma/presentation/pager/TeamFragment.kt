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

        // ✅ ViewPager2 가져오기
        viewPager = activity?.findViewById(ViewPager2_ID)

        // ✅ StateFlow 감지 후 TeamRoadScreen으로 이동
        lifecycleScope.launch {
            marathonDataViewModel.marathonState.collect { state ->
                if(state.currentDistance <= 10000 || state.currentDistance >= state.totalDistance - 10000){

                }else {
                    val nearbyCount =
                        state.friendInfoList.count { !it.isMe && it.gapDistance.absoluteValue <= 10000 } // ✅ 현재 "거리 내 인원 수"
                    val previousCount =
                        marathonDataViewModel.previousNearbyCount.value // ✅ ViewModel에서 이전 값 가져오기

                    if (nearbyCount != previousCount) { // ✅ 인원 수 변화 감지
                        marathonDataViewModel.updateNearbyCount(nearbyCount) // ✅ 값 업데이트
                        if (nearbyCount > 0) {
                            viewPager?.setCurrentItem(2, true) // 🔥 팀 화면 2로 이동
                        }
                    }
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

