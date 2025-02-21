package com.example.gogoma.presentation.theme

import androidx.wear.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.gogoma.R

val Pretendard = FontFamily(
    Font(R.font.pretendard_extralight, FontWeight.ExtraLight),
    Font(R.font.pretendard_light, FontWeight.Light),
    Font(R.font.pretendard_thin, FontWeight.Thin),
    Font(R.font.pretendard_regular, FontWeight.Normal),
    Font(R.font.pretendard_medium, FontWeight.W500),
    Font(R.font.pretendard_bold, FontWeight.Bold),
    Font(R.font.pretendard_black, FontWeight.Black),
    Font(R.font.pretendard_semibold, FontWeight.SemiBold),
    Font(R.font.pretendard_extrabold, FontWeight.ExtraBold)
)

val PartialSansKR = FontFamily(
    Font(R.font.partialsanskr_regular, FontWeight.Normal)
)

val WearTypography = Typography(
    title1 = TextStyle(
        fontFamily = Pretendard,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    ),
    title2 = TextStyle(
        fontFamily = Pretendard,
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium
    ),
    body1 = TextStyle(
        fontFamily = Pretendard,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal
    ),
    body2 = TextStyle(
        fontFamily = Pretendard,
        fontSize = 14.sp,
        fontWeight = FontWeight.Light
    ),
    caption1 = TextStyle(
        fontFamily = PartialSansKR,
        fontSize = 12.sp
    ),
    caption2 = TextStyle(
        fontFamily = PartialSansKR,
        fontSize = 10.sp
    )
)