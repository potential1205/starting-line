package com.example.gogoma.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.gogoma.theme.GogomaTheme
import com.example.gogoma.ui.components.Filter
import com.example.gogoma.ui.components.Marathon
import com.example.gogoma.ui.components.MarathonListItem
import com.example.gogoma.ui.components.SearchBar

@Composable
fun MainScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    onFilterClick: (String) -> Unit,
) {
    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val bottomInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    // 더미 데이터 생성
    val marathonList : List<Marathon> = listOf(
        Marathon(
            title = "서울 마라톤",
            registrationStatus = "접수중",
            remainingDays = 10,
            registrationPeriod = "2025-01-01 ~ 2025-02-10",
            location = "서울 한강",
            distance = "42.195km",
            eventDate = "2025-03-01"
        ),
        Marathon(
            title = "부산 마라톤",
            registrationStatus = "마감",
            remainingDays = 0,
            registrationPeriod = "2025-02-01 ~ 2025-03-10",
            location = "부산 해운대",
            distance = "21.097km",
            eventDate = "2025-04-01"
        ),
        Marathon(
            title = "대전 마라톤",
            registrationStatus = "접수중",
            remainingDays = 20,
            registrationPeriod = "2025-01-10 ~ 2025-02-20",
            location = "대전 한밭수목원",
            distance = "10km",
            eventDate = "2025-05-01"
        ),
        Marathon(
            title = "서울 마라톤",
            registrationStatus = "접수중",
            remainingDays = 10,
            registrationPeriod = "2025-01-01 ~ 2025-02-10",
            location = "서울 한강",
            distance = "42.195km",
            eventDate = "2025-03-01"
        ),
        Marathon(
            title = "부산 마라톤",
            registrationStatus = "마감",
            remainingDays = 0,
            registrationPeriod = "2025-02-01 ~ 2025-03-10",
            location = "부산 해운대",
            distance = "21.097km",
            eventDate = "2025-04-01"
        ),
        Marathon(
            title = "대전 마라톤",
            registrationStatus = "접수중",
            remainingDays = 20,
            registrationPeriod = "2025-01-10 ~ 2025-02-20",
            location = "대전 한밭수목원",
            distance = "10km",
            eventDate = "2025-05-01"
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 65.dp+topInset, bottom = 65.dp+bottomInset),
    ){
        //스크롤 영역
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                SearchBar()
            }
            item {
                Filter(
                    onFilterClick = onFilterClick
                )
            }
            //마라톤 리스트
            items(marathonList){marathon ->
                MarathonListItem(marathon = marathon)
            }
        }
    }
}