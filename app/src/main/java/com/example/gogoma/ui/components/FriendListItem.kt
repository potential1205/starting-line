package com.example.gogoma.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.gogoma.R
import com.example.gogoma.data.model.FriendResponse
import com.example.gogoma.theme.GogomaTheme

@Composable
fun FriendListItem(friendResponse: FriendResponse, isMe: Boolean = false) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 18.dp, top = 10.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row (
            modifier = Modifier
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // 등수 (글자 크기 고정)
            CompositionLocalProvider (LocalDensity provides Density(LocalDensity.current.density, fontScale = 1f)) {
                Text(
                    text = "${friendResponse.rank}",
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = if(isMe) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                        fontWeight = if(isMe) FontWeight.ExtraBold else FontWeight.Bold,
                        textAlign = TextAlign.Right
                    ),
                    modifier = Modifier.width(20.dp)
                )
            }

            // 이미지
            val painter = if (friendResponse.profileImage.isNullOrEmpty()) {
                painterResource(id = R.drawable.logo_image)
            } else {
                val secureProfileImage = friendResponse.profileImage?.replaceFirst("http://", "https://")
                rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = secureProfileImage)
                        .apply {
                            crossfade(true) // 이미지 로딩 시 부드러운 전환 효과
                            placeholder(R.drawable.icon_running) // 로딩 중에 보여줄 이미지
                            error(R.drawable.icon_close) // 에러 발생 시 보여줄 이미지
                        }
                        .build()
                )
            }
            Image(
                painter = painter,
                contentDescription = "image description",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
            // 이름
            Text(
                text = friendResponse.name,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = if(isMe) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                    fontWeight = if(isMe) FontWeight.Medium else FontWeight.Normal,
                    textAlign = TextAlign.Center,
                ),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )
        }

        // 누적 거리
        Text(
            text = "${formattedDistance(friendResponse.totalDistance)}",
            style = TextStyle(
                fontSize = 13.sp,
                color = if(isMe) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                fontWeight = if(isMe) FontWeight.Medium else FontWeight.Normal,
            )
        )
    }
}

fun formattedDistance(courseType: Int): String {
    val kmValue = courseType / 100000.0
    return if (kmValue % 1 == 0.0) {
        // 소수점이 0일 때는 정수로 표시
        "${kmValue.toInt()}km"
    } else {
        // 소수점이 있을 때는 소수점 2자리까지 표시
        "%.3fkm".format(kmValue)
    }
}

@Preview(showBackground = true)
@Composable
fun FriendListItemPreview() {
    GogomaTheme {
        FriendListItem(sampleFriendResponse)
    }
}

val sampleFriendResponse = FriendResponse(
    rank = 1,
    friendId = 101,
    name = "김지훈",
    profileImage = null,  // profile 값이 null
    totalDistance = 320
)
