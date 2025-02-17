package com.example.gogoma.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.gogoma.R
import com.example.gogoma.data.model.MarathonPreviewDto
import com.example.gogoma.data.model.MarathonType
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Locale

@Composable
fun MarathonListItem (marathonPreviewDto: MarathonPreviewDto, onClick: () -> Unit) {

    var isColumn by remember { mutableStateOf(false) }
    var totalLocationWidth by remember { mutableStateOf(0) }
    var totalHashWidth by remember { mutableStateOf(0) }

    val screenWidth = LocalConfiguration.current.screenWidthDp
    val contentWidth = screenWidth - 21  // 패딩값

    val locationModifier = Modifier.onGloballyPositioned { coordinates ->
        totalLocationWidth = coordinates.size.width
    }
    val hashModifier = Modifier.onGloballyPositioned { coordinates ->
        totalHashWidth = coordinates.size.width
    }

    LaunchedEffect (totalLocationWidth + totalHashWidth) {
        isColumn = (totalLocationWidth + totalHashWidth) > contentWidth
    }

    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
            .then(
                Modifier.drawBehind {
                    val strokeWidth = 1.dp.toPx()
                    val y = size.height - strokeWidth / 2
                    drawLine(
                        color = Color(0xFFE7E7E7), // 하단 선의 색상
                        start = androidx.compose.ui.geometry.Offset(0f, y),
                        end = androidx.compose.ui.geometry.Offset(size.width, y),
                        strokeWidth = strokeWidth
                    )
                }
            )
            .padding(start = 20.dp, top = 20.dp, end = 16.dp, bottom = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        //Left
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = marathonPreviewDto.title,
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.W500,
                    color = Color(0xFF000000),
                )
            )
            val formattedRaceStartTime = FormattedDate(marathonPreviewDto.raceStartTime)
            Text(
                text = "대회일시 ${(formattedRaceStartTime)}",
                style = TextStyle(
                    fontSize = 11.8.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF999999),
                )
            )
        }

        //Right
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val marathonStatus = marathonPreviewDto.marathonStatus
            val marathonStatusText = when (marathonStatus) {
                "OPEN" -> "접수중"
                "CLOSED" -> "접수 종료"
                "FINISHED" -> "접수 마감"
                else -> marathonStatus
            }

            val marathonStatusBackgroundColor = when (marathonStatus) {
                "OPEN" -> MaterialTheme.colorScheme.secondary
                "CLOSED" -> MaterialTheme.colorScheme.error
                "FINISHED" -> Color.Gray
                else -> MaterialTheme.colorScheme.secondary
            }

            Text(
                text = marathonPreviewDto.dday,
                style = TextStyle(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Light,
                    color = Color(0xFF2680FF),
                )
            )
            Box(
                modifier = Modifier
                    .background(color = Color(0xFF2680FF), shape = RoundedCornerShape(size = 4.dp))
                    .padding(start = 10.dp, top = 4.dp, end = 10.dp, bottom = 4.dp)
            ){
                Text(
                    text = marathonStatusText,
                    style = TextStyle(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFFFFFFFF),
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MarathonListItemPreview(){
    MarathonListItem(
        MarathonPreviewDto(
            id = 1,
            title = "서울 마라톤",
            registrationStartDateTime = "2025-01-01T00:00:00",
            registrationEndDateTime = "2025-02-10T23:59:59",
            raceStartTime = "2025-03-01T09:00:00",
            location = "서울 한강",
            city = "서울",
            region = "서울",
            district = "한강공원",
            marathonStatus = "OPEN",  // 모집중
            thumbnailImage = "https://example.com/seoul_thumbnail.jpg",
            courseTypeList = listOf("풀코스", "하프코스"),
            marathonTypeList = listOf(
                MarathonType(id = 1, marathonId = 1, courseType = "풀코스", price = "50000", etc = "마라톤 풀코스"),
                MarathonType(id = 2, marathonId = 1, courseType = "하프코스", price = "30000", etc = "하프마라톤")
            ),
            dday = "30"
        ),
        onClick = {
            println("Marathon item clicked!")
        }
    )
}

@Composable
fun ImageOrPlaceholder(imageUrl: String?) {
    val round = 10.dp
    Box(
        modifier = Modifier
            .width(125.dp)
            .height(105.dp)
            .border(width = 0.4.dp, color = Color(0xFFE7E7E7), shape = RoundedCornerShape(size = round))
            .background(color = MaterialTheme.colorScheme.tertiary, shape = RoundedCornerShape(size = round))
    ) {
        val painter =
            rememberAsyncImagePainter(ImageRequest.Builder
                (LocalContext.current).data(data = imageUrl).apply(block = fun ImageRequest.Builder.() {
                crossfade(true)  // 이미지 로딩 시 부드러운 전환 효과
                placeholder(R.drawable.icon_running) // 로딩 중에 보여줄 이미지
                error(R.drawable.icon_close) // 에러 발생 시 보여줄 이미지
            }).build()
            )
        Image(
            painter = painter,
            contentDescription = "Image",
            modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(round)),
            contentScale = ContentScale.Crop
        )
    }
}



