package com.example.gogoma.presentation.pager

import MyPagerAdapter
import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.gogoma.presentation.viewmodel.MarathonDataViewModel

@Composable
fun ViewPagerScreen(activity: FragmentActivity, marathonDataViewModel: MarathonDataViewModel) {
    var viewPager: ViewPager2? by remember { mutableStateOf(null) }

    // ViewModel에서 상태를 가져오기
    val nearbyCount = marathonDataViewModel.nearbyCount.collectAsState().value
    val context = LocalContext.current

    // gapDistance가 10000 이하인 인원 변동 체크하여 진동 및 페이지 이동
    LaunchedEffect (nearbyCount) {
        // 시작 100m와 끝 100m 제외
        if(marathonDataViewModel.marathonState.value.currentDistance <= 10000 ||
            marathonDataViewModel.marathonState.value.currentDistance >= marathonDataViewModel.marathonState.value.totalDistance - 10000){
            // 아무 동작 없음
        } else {
            if (nearbyCount > 0) {
                vibrate(context) // 진동 울리기
                viewPager?.currentItem = 2 // ViewPager의 세 번째 페이지(인덱스 2)로 이동
            }
        }
    }

    // 뒤로 가기 버튼이 눌려도 아무 동작도 하지 않음
    BackHandler {
        // 아무 동작도 수행하지 않음 (즉, 현재 화면 유지)
    }

    AndroidView(
        factory = { context ->
            ViewPager2(context).apply {
                adapter = MyPagerAdapter(activity)
                orientation = ViewPager2.ORIENTATION_HORIZONTAL
                viewPager = this // ViewPager2 인스턴스를 저장
            }
        }
    )
}

// 진동 함수 추가
fun vibrate(context: Context) {
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (vibrator.hasVibrator()) {
        val effect = VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
        vibrator.vibrate(effect)
    }
}
