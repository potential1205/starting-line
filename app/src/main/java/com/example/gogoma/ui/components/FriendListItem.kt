package com.example.gogoma.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.gogoma.R
import com.example.gogoma.data.model.Friend

@Composable
fun FriendListItem(friend: Friend) {
    Row (
        modifier = Modifier
            .width(382.dp)
            .height(60.dp)
            .padding(top = 2.dp, bottom = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "${friend.rank}",
            style = TextStyle(
                fontSize = 26.sp,
                fontFamily = FontFamily(Font(R.font.nanum_square_round_eb)),
                color = Color(0xFF000000),
                textAlign = TextAlign.Center,
            )
        )
        val painter = if (friend.profile.isNullOrEmpty()) {
            painterResource(id = R.drawable.logo_image)
        } else {
            rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(data = friend.profile)
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
            contentScale = ContentScale.FillBounds
        )
        Row(
            modifier = Modifier
                .width(186.dp)
                .height(42.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = friend.name,
                style = TextStyle(
                    fontSize = 17.5.sp,
                    fontFamily = FontFamily(Font(R.font.nanum_square_round_r)),
                    color = Color(0xFF000000),
                    textAlign = TextAlign.Center,
                )
            )
        }
        Row(
            modifier = Modifier
                .width(60.dp)
                .height(27.dp)
                .background(color = Color(0xFFBDF46F), shape = RoundedCornerShape(size = 5.dp))
                .padding(start = 10.dp, top = 5.dp, end = 10.dp, bottom = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "${friend.totalDistance}km",
                style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(R.font.nanum_square_round_r)),
                    color = Color(0xFF000000),
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FriendListItemPreview() {
    FriendListItem(sampleFriend)
}

val sampleFriend = Friend(
    rank = 1,
    id = 101,
    name = "김지훈",
    profile = null,  // profile 값이 null
    totalDistance = 320.7
)

val friendsList = listOf(
    Friend(
        rank = 1,
        id = 101,
        name = "Alice Smith",
        profile = null,  // profile 값이 null
        totalDistance = 320.7
    ),
    Friend(
        rank = 2,
        id = 102,
        name = "Bob Johnson",
        profile = null,  // profile 값이 null
        totalDistance = 280.5
    ),
    Friend(
        rank = 3,
        id = 103,
        name = "Charlie Brown",
        profile = null,  // profile 값이 null
        totalDistance = 210.0
    ),
    Friend(
        rank = 4,
        id = 104,
        name = "Diana Lee",
        profile = null,  // profile 값이 null
        totalDistance = 180.3
    ),
    Friend(
        rank = 5,
        id = 105,
        name = "Eve Clark",
        profile = null,  // profile 값이 null
        totalDistance = 150.9
    )
)
