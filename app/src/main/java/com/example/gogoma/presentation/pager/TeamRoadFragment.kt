package com.example.gogoma.presentation.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.gogoma.presentation.screens.TeamRoadScreen
import com.example.gogoma.presentation.viewmodel.MarathonDataViewModel

class TeamRoadFragment : Fragment() {
    private val marathonDataViewModel: MarathonDataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                TeamRoadScreen(marathonDataViewModel) // ✅ ViewPager2 전달
            }
        }
    }
}
