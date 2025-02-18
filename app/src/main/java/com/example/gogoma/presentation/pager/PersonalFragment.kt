package com.example.gogoma.presentation.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import com.example.gogoma.presentation.screens.PersonalScreen
import com.example.gogoma.presentation.viewmodel.MarathonDataViewModel

class PersonalFragment : Fragment() {
    private val marathonDataViewModel: MarathonDataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                PersonalScreen(marathonDataViewModel)
            }
        }
    }
}