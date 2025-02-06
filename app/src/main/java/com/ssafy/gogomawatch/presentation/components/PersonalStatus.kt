package com.ssafy.gogomawatch.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.ssafy.gogomawatch.R

@Composable
fun PersonalStatus(title: String, current: String, goal: String, currentColor: Color, unit: String? = null) {
    Column (
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(7.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        // title
        Text (
            text = title,
            style = TextStyle(
                fontSize = 15.sp,
                fontFamily = FontFamily(Font(R.font.nanum_square_round_r)),
                color = Color(0xFFFFFFFF),
            )
        )

        // current
        Row (
            horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = current,
                style = TextStyle(
                    fontSize = 38.sp,
                    fontFamily = FontFamily(Font(R.font.nanum_square_round_b)),
                    color = currentColor,

                    )
            )
            unit?.let{
                Text(
                    text = it,
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.nanum_square_round_b)),
                        color = currentColor,
                    )
                )
            }
        }

        // goal
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon (
                painter = painterResource(id = R.drawable.icon_flag),
                contentDescription = "image description",
                tint = Color(0xFFB6B6B6),
                modifier = Modifier.size(17.dp)
            )
            Text(
                text = goal,
                style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(R.font.nanum_square_round_r)),
                    color = Color(0xFFB6B6B6),
                )
            )
            unit?.let {
                Text(
                    text = it,
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.nanum_square_round_r)),
                        color = Color(0xFFB6B6B6),
                    )
                )
            }
        }
    }
}

@Preview
@Composable
fun PersocalStatusPreview() {
    PersonalStatus("이동거리","17.29","20.00", Color.Green, "km")
}