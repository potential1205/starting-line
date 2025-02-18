package com.example.gogoma.theme

import androidx.compose.material3.Typography
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

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Pretendard,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
    ),
    bodySmall = TextStyle(
        fontFamily = Pretendard,
        fontSize = 12.sp,
        fontWeight = FontWeight.Light,
    ),
    titleLarge = TextStyle(
        fontFamily = Pretendard,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
    ),
    labelSmall = TextStyle(
        fontFamily = Pretendard,
        fontSize = 12.sp,
        fontWeight = FontWeight.Light,
    ),
    displayLarge = TextStyle(
        fontFamily = PartialSansKR,
    ),
    displayMedium = TextStyle(
        fontFamily = PartialSansKR,
    ),
    displaySmall = TextStyle(
        fontFamily = PartialSansKR,
    )
)