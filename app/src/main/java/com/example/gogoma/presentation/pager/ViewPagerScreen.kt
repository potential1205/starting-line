package com.example.gogoma.presentation.pager

import MyPagerAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2

@Composable
fun ViewPagerScreen(activity: FragmentActivity) {
    AndroidView(
        factory = { context ->
            ViewPager2(context).apply {
                adapter = MyPagerAdapter(activity)
                orientation = ViewPager2.ORIENTATION_HORIZONTAL
            }
        }
    )
}