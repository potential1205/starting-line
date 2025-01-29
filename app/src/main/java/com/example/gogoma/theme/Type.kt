package com.example.gogoma.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.gogoma.R

val NanumSquareRound = FontFamily(
    Font(R.font.nanum_square_round_l, FontWeight.Light),
    Font(R.font.nanum_square_round_r, FontWeight.Normal),
    Font(R.font.nanum_square_round_b, FontWeight.Bold),
    Font(R.font.nanum_square_round_eb, FontWeight.ExtraBold)
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = NanumSquareRound,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
    ),
    titleLarge = TextStyle(
        fontFamily = NanumSquareRound,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
    ),
    labelSmall = TextStyle(
        fontFamily = NanumSquareRound,
        fontSize = 12.sp,
        fontWeight = FontWeight.Light,
    )
)