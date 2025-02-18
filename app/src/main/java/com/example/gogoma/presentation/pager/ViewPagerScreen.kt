package com.example.gogoma.presentation.pager

import MyPagerAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.viewpager2.widget.ViewPager2
import com.example.gogoma.presentation.viewmodel.MarathonDataViewModel

@Composable
fun ViewPagerScreen(activity: FragmentActivity, navController: NavController, marathonDataViewModel: MarathonDataViewModel) {
    AndroidView(
        factory = { context ->
            ViewPager2(context).apply {
                adapter = MyPagerAdapter(activity, navController, marathonDataViewModel)
                orientation = ViewPager2.ORIENTATION_HORIZONTAL
            }
        }
    )
}