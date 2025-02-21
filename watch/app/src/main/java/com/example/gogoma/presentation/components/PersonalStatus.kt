package com.example.gogoma.presentation.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.gogoma.R

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
                color = MaterialTheme.colors.onBackground,
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
//                    fontFamily = MaterialTheme.typography.caption1.fontFamily,
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Bold,
                    color = currentColor,
                    )
            )
            unit?.let{
                Text(
                    text = it,
                    style = TextStyle(
//                        fontFamily = MaterialTheme.typography.caption1.fontFamily,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color =currentColor,
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
                tint = MaterialTheme.colors.onBackground,
                modifier = Modifier.size(17.dp)
            )
            Text(
                text = goal,
                style = TextStyle(
                    fontSize = 15.sp,
                    color = MaterialTheme.colors.onBackground,
                )
            )
            unit?.let {
                Text(
                    text = it,
                    style = TextStyle(
                        fontSize = 15.sp,
                        color = MaterialTheme.colors.onBackground,
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