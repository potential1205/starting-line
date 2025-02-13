package com.example.gogoma.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gogoma.data.model.MarathonDetailResponse
import com.example.gogoma.theme.GogomaTheme
import com.example.gogoma.viewmodel.MarathonDetailViewModel

@Composable
fun  MarathonDetailItem(marathonDetail: MarathonDetailResponse) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(start = 20.dp, top = 10.dp, end = 20.dp, bottom = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start,
    ) {
        Row (
            modifier = Modifier
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = marathonDetail.marathon.title,
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }

        Row(
            modifier = Modifier
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(6.0371174812316895.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            marathonDetail.marathonTypeList.forEach { type ->
                HashTag(type.courseType)
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            thickness = 1.dp,
            color = Color(0xFFDDDDDD)
        )

        //대회 정보
        val formattedRaceStartTime = FormattedDate(marathonDetail.marathon.raceStartTime)
        InfoTableRow(label = "대회일시", value = formattedRaceStartTime)
        InfoTableRow(label = "대회장소", value = marathonDetail.marathon.location)
        InfoTableRow(label = "대회종목", value = marathonDetail.marathonTypeList.joinToString("/") { "${it.courseType}" })
        InfoTableRow(
            label = "접수기간",
            value = if (marathonDetail.marathon.registrationStartDateTime == null && marathonDetail.marathon.registrationEndDateTime == null) {
                "접수 선착순"
            } else {
                "접수 ${marathonDetail.marathon.registrationStartDateTime ?: ""}~${marathonDetail.marathon.registrationEndDateTime ?: ""}"
            },
        )
        // marathonTypeList 그룹화 및 매니아 처리
        val groupedCourseTypes = marathonDetail.marathonTypeList
            .groupBy { it.courseType } // courseType 기준으로 그룹화
            .mapValues { (_, types) ->
                // 각 courseType에 대해 "매니아"가 있는지 확인
                val regularPrice = types.firstOrNull { it.etc.isEmpty() }?.price ?: ""
                val maniaPrices = types.filter { it.etc == "매니아" }.joinToString(" ") { it.price }

                // "매니아" 가격과 일반 가격을 합쳐서 출력
                if (maniaPrices.isNotEmpty()) {
                    "${regularPrice}원 (매니아 : ${maniaPrices}원)"
                } else {
                    "${regularPrice}원"
                }
            }
        InfoTableRow(label = "대회가격", value = groupedCourseTypes.entries.joinToString("\n") {
            "${it.key}: ${it.value}"
        })
        InfoTableRow(label = "대회주최", value = marathonDetail.marathon.hostList.joinToString(", ") { "${it}" })
        InfoTableRow(label = "대회주관", value = marathonDetail.marathon.organizerList.joinToString(", ") { "${it}" })
        InfoTableRow(label = "대회후원", value = marathonDetail.marathon.sponsorList.joinToString(", ") { "${it}" })
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 12.dp),
            thickness = 1.dp,
            color = Color(0xFFDDDDDD)
        )
    }
}

@Preview
@Composable
fun MarathonDetailScreenPreview(){
    GogomaTheme {
        val marathonDetailViewModel: MarathonDetailViewModel = viewModel()

        LaunchedEffect(25) {
            marathonDetailViewModel.loadMarathonDetail(25)
        }

        val marathonDetail = marathonDetailViewModel.marathonDetail

        marathonDetail?.let { MarathonDetailItem(it) }
    }
}