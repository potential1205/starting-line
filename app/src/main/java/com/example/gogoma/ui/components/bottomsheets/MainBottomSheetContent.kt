package com.example.gogoma.ui.components.bottomsheets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.gogoma.R
import com.example.gogoma.ui.components.FilterListItemContent
import com.example.gogoma.ui.components.FilterListItemSelect
import com.example.gogoma.ui.components.FilterListItemTitle
import com.example.gogoma.viewmodel.BottomSheetViewModel
import com.example.gogoma.viewmodel.MarathonListViewModel

@Composable
fun MainBottomSheetContent(bottomSheetViewModel: BottomSheetViewModel, marathonListViewModel: MarathonListViewModel) {
    // BottomSheetContentWithTitle 사용
    BottomSheetContentWithTitle (
        title = "정렬",
        headerLeftContent = {
            if(bottomSheetViewModel.isSubPageVisible){
                IconButton(
                    onClick = { bottomSheetViewModel.goBackToPreviousPage() },
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_back),
                        contentDescription = "Back Arrow",
                        tint = Color.Black,
                    )
                }
            }else{
                Text(
                    text = "닫기",
                    modifier = Modifier.clickable {
                        bottomSheetViewModel.hideBottomSheet()
                    }
                )
            }
        },
        bottomButton = {
            if(bottomSheetViewModel.pageName=="기본"){
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    onClick = {
                        marathonListViewModel.applyFilters()
                        bottomSheetViewModel.hideBottomSheet()
                    }
                ) {
                    Text("대회 보기")
                }
            }
        }
    ) {

        val filterTitles = marathonListViewModel.filterTitles
        val filterContents = marathonListViewModel.filterContents

        //하단 필터 내용
        LazyColumn {
            if(bottomSheetViewModel.pageName == "기본"){
                items(filterTitles) { title ->
                    val filterContent = when (title) {
                        "지역" -> marathonListViewModel.pendingFilters.city ?: "모든 지역"
                        "접수 상태" -> marathonListViewModel.pendingFilters.marathonStatus ?: "모든 접수 상태"
                        "종목" -> marathonListViewModel.pendingFilters.courseTypeList?.joinToString(", ") ?: "모든 종목"
                        "년도" -> marathonListViewModel.pendingFilters.year ?: "모든 년도"
                        "월" -> marathonListViewModel.pendingFilters.month ?: "모든 월"
                        else -> "모든 ${title}"
                    }

                    FilterListItemSelect(title, filterContent) {
                        bottomSheetViewModel.showSubPage(title, "필터")
                    }
                }
            } else {
                val contentList = filterContents[bottomSheetViewModel.pageName]
                if (contentList != null) {
                    item {
                        FilterListItemTitle(bottomSheetViewModel.pageName)
                    }
                    item {
                        FilterListItemContent("전체", onClick = {
                            if(bottomSheetViewModel.isSubPageVisible){//기본 페이지에서 들어간 경우
                                when (bottomSheetViewModel.pageName) {
                                    "지역" -> marathonListViewModel.updatePendingFilter(city = null)
                                    "접수 상태" -> marathonListViewModel.updatePendingFilter(marathonStatus = null)
                                    "종목" -> marathonListViewModel.updatePendingFilter(courseTypeList = null)
                                    "년도" -> marathonListViewModel.updatePendingFilter(year = null)
                                    "월" -> marathonListViewModel.updatePendingFilter(month = null)
                                }

                                // 이전 모달창으로 돌아가기
                                bottomSheetViewModel.goBackToPreviousPage()
                            }else{//하위 페이지에 바로 들어간 경우
                                // 필터 값 업데이트
                                when (bottomSheetViewModel.pageName) {
                                    "지역" -> marathonListViewModel.updateFilters(city = null)
                                    "접수 상태" -> marathonListViewModel.updateFilters(marathonStatus = null)
                                    "종목" -> marathonListViewModel.updateFilters(courseTypeList = null)
                                    "년도" -> marathonListViewModel.updateFilters(year = null)
                                    "월" -> marathonListViewModel.updateFilters(month = null)
                                }

                                // 모달창 닫기
                                bottomSheetViewModel.hideBottomSheet()
                            }
                        })
                    }
                    items(contentList) { content ->
                        FilterListItemContent(content, onClick = {

                            if(bottomSheetViewModel.isSubPageVisible){//기본 페이지에서 들어간 경우
                                when (bottomSheetViewModel.pageName) {
                                    "지역" -> marathonListViewModel.updatePendingFilter(city = content)
                                    "접수 상태" -> marathonListViewModel.updatePendingFilter(marathonStatus = content)
                                    "종목" -> marathonListViewModel.updatePendingFilter(courseTypeList = listOf(content))
                                    "년도" -> marathonListViewModel.updatePendingFilter(year = content)
                                    "월" -> marathonListViewModel.updatePendingFilter(month = content)
                                }

                                // 이전 모달창으로 돌아가기
                                bottomSheetViewModel.goBackToPreviousPage()
                            }else{//하위 페이지에 바로 들어간 경우
                                // 필터 값 업데이트
                                when (bottomSheetViewModel.pageName) {
                                    "지역" -> marathonListViewModel.updateFilters(city = content)
                                    "접수 상태" -> marathonListViewModel.updateFilters(marathonStatus = content)
                                    "종목" -> marathonListViewModel.updateFilters(courseTypeList = listOf(content))
                                    "년도" -> marathonListViewModel.updateFilters(year = content)
                                    "월" -> marathonListViewModel.updateFilters(month = content)
                                }

                                // 모달창 닫기
                                bottomSheetViewModel.hideBottomSheet()
                            }

                        })
                    }
                } else {
                    item {
                        Text("잘못된 필터 값입니다.")
                    }
                }
            }
        }
    }
}