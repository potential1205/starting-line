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
fun FriendListItem(friendResponse: FriendResponse) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp, bottom = 15.dp, start = 15.dp, end = 30.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row (
            modifier = Modifier
                .wrapContentWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // 등수
            Column (
                modifier = Modifier
                    .width(30.dp)
                    .padding(end = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CompositionLocalProvider (LocalDensity provides Density(LocalDensity.current.density, fontScale = 1f)) {
                    Text(
                        text = "${friendResponse.rank}",
                        style = TextStyle(
                            fontSize = 19.sp, // ✅ 시스템 설정에 영향을 받지 않음
                            fontFamily = MaterialTheme.typography.displayLarge.fontFamily,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                    )
                }
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
                    .size(56.dp)
                    .clip(CircleShape)
            )

            // 이름
            Text(
                text = friendResponse.name,
                style = TextStyle(
                    fontSize = 17.5.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                ),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 12.dp)
            )
        }

        Row(
            modifier = Modifier
                .wrapContentWidth()
                .background(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(size = 5.dp))
                .padding(start = 10.dp, top = 5.dp, end = 10.dp, bottom = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "${friendResponse.totalDistance}km",
                style = TextStyle(
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            )
        }
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
