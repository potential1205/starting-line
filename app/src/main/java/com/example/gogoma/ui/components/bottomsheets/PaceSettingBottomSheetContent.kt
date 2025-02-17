package com.example.gogoma.ui.components.bottomsheets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.gogoma.ui.components.FilterListItemContent
import com.example.gogoma.ui.components.FilterListItemTitle
import com.example.gogoma.utils.TokenManager
import com.example.gogoma.viewmodel.BottomSheetViewModel
import com.example.gogoma.viewmodel.PaceViewModel

@Composable
fun PaceSettingBottomSheetContent(bottomSheetViewModel: BottomSheetViewModel, paceViewModel: PaceViewModel){
    val context = LocalContext.current

    BottomSheetContentWithTitle (
        title = "목표 페이스",
        headerLeftContent = {
            Text(
                text = "닫기",
                modifier = Modifier.clickable {
                    bottomSheetViewModel.hideBottomSheet()
                }
            )
        },
    ) {
        val paceMap = mapOf(
            300 to "3:00 세계적 수준 - 극히 빠른 페이스",
            330 to "3:30 올림픽급 – 최상위 경쟁자 수준",
            400 to "4:00 엘리트 – 프로급의 뛰어난 능력",
            430 to "4:30 프로 – 전문 선수에 가까운 속도",
            500 to "5:00 챔피언 – 경쟁력 있는 주자",
            530 to "5:30 도전자 – 우승을 노릴 만한 페이스",
            600 to "6:00 페이스 세터 – 기준을 제시하는 주자",
            630 to "6:30 민첩한 – 발이 빠른 주자",
            700 to "7:00 꾸준한 – 일정한 보폭을 유지하는 주자",
            730 to "7:30 탄탄한 – 신뢰할 수 있는 달리기",
            800 to "8:00 시간 관리 – 자신의 페이스를 잘 지키는",
            830 to "8:30 일관된 – 변함없이 꾸준한 페이스",
            900 to "9:00 믿음직한 – 안정적인 러닝 스타일",
            930 to "9:30 일상 주자 – 누구나 할 수 있는 속도",
            1000 to "10:00 균형 잡힌 – 무리하지 않고 달리는",
            1030 to "10:30 인내심 있는 – 오랜 시간 달릴 수 있는",
            1100 to "11:00 회복력이 좋은 – 역경을 이겨내는 주자",
            1130 to "11:30 주말 전사 – 가볍게 즐기는 러너",
            1200 to "12:00 부담 없는 – 여유로운 조깅 페이스",
            1230 to "12:30 편안하게 달리는 – 느긋한 주행 스타일",
            1300 to "13:00 한가롭게 – 여유로운 크루징 페이스",
            1330 to "13:30 느긋한 – 산책하듯 편안한 페이스"
        )

        LazyColumn {
            item {
                FilterListItemTitle("/km")
            }
            items(paceMap.entries.toList()) { (value, text) ->
                FilterListItemContent(text, onClick = {
                    paceViewModel.marathonStartInitDataResponse.value?.let {
                        paceViewModel.patchMarathonPace(TokenManager.getAccessToken(context).toString(), it.marathonId, value)
                    }
                    // 모달창 닫기
                    bottomSheetViewModel.hideBottomSheet()

                })
            }
        }
    }
}
