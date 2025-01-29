package com.example.gogoma.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gogoma.R
import java.text.SimpleDateFormat
import java.util.Locale

//데이터 클래스
data class Marathon(
    val title: String,
    val registrationStatus: String,
    val remainingDays: Int,
    val registrationPeriod: String,
    val location: String,
    val distance: String,
    val eventDate: String
)

@Composable
fun MarathonListItem (marathon: Marathon) {

    //날짜 변환 함수
    fun formatDate(dateString: String):String{
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
        val date = inputFormat.parse(dateString) ?: return ""

        val outputFormat = SimpleDateFormat("yyyy년 M월 d일 (E)", Locale.KOREAN)
        return outputFormat.format(date)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(start = 11.dp, end = 11.dp)
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
            .padding(top = 25.dp, bottom = 25.dp),
        horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.Start),
        verticalAlignment = Alignment.Top,
    ) {
        ImageOrPlaceholder(imageBitmap = null)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 11.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start,
        ) {
            //Top
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                StatusTag("접수중", true)
                StatusTag("D-1", true)
            }

            //Middle
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.Top),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(marathon.title)
                Text(
                    text = "접수 ${marathon.registrationPeriod}",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.nanum_square_round_l)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFFB3B3B3),
                    )
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_location_on),
                        contentDescription = "Logo",
                        tint = Color(0xFF606060),
                        modifier = Modifier.size(9.dp)
                    )
                    Text(
                        text = marathon.location,
                        style = TextStyle(
                            fontSize = 10.sp,
                            color = Color(0xFF606060)
                        )
                    )
                    Text(
                        text = "#${marathon.distance}",
                        style = TextStyle(
                            fontSize = 10.sp,
                            color = Color(0xFF606060)
                        )
                    )
                }

            }

            //Bottom
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = "대회일시 ${formatDate(marathon.eventDate)}",
                    style = TextStyle(
                        fontSize = 13.8.sp,
                        fontFamily = FontFamily(Font(R.font.nanum_square_round_b)),
                        color = MaterialTheme.colorScheme.primary,
                    )
                )
            }
        }
    }
}

@Preview
@Composable
fun MarathonListItemPreview(){
    MarathonListItem(
        marathon = Marathon(
            title = "서울 마라톤",
            registrationStatus = "접수중",
            remainingDays = 10,
            registrationPeriod = "2025-01-01 ~ 2025-02-10",
            location = "서울 한강",
            distance = "42.195km",
            eventDate = "2025-03-01"
        )
    )
}

@Composable
fun ImageOrPlaceholder(imageBitmap: ImageBitmap?) {
    Box(
        modifier = Modifier
            .size(104.dp) // 원하는 크기 설정
            .background(MaterialTheme.colorScheme.tertiary)
    ) {
        if (imageBitmap != null) {
            Image(
                painter = BitmapPainter(imageBitmap),
                contentDescription = "Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            // 이미지가 없을 때 단색 배경을 채우기
            Box(
                modifier = Modifier.fillMaxSize().background(Color.LightGray)
            )
        }
    }
}

@Composable
fun StatusTag(text: String, isAccepting: Boolean){
    val backgroundColor = if(isAccepting){
        MaterialTheme.colorScheme.secondary
    } else {
        Color(0xFFF76B6D)
    }

    val commonModifier = Modifier
        .background(color = backgroundColor)
        .padding(10.dp, 2.dp)
        .fillMaxHeight()

    val commonTextStyle = TextStyle(
        color = MaterialTheme.colorScheme.onSecondary,
        fontSize = 12.sp,
    )
    Box(
        modifier = commonModifier
    ){
        Text(
            text = text,
            style = commonTextStyle
        )
    }

}



