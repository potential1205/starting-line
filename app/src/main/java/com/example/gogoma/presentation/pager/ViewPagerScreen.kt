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
                id = ViewPager2_ID // ✅ ID 추가 (TeamFragment에서 찾을 수 있도록)
                adapter = MyPagerAdapter(activity)
                orientation = ViewPager2.ORIENTATION_HORIZONTAL
            }
        }
    )
}

// ✅ ID 상수 선언 (TeamFragment에서 사용하기 위해)
const val ViewPager2_ID = 12345
