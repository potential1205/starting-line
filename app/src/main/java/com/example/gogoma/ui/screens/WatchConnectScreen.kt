package com.example.gogoma.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.gogoma.R
import com.example.gogoma.ui.components.BottomBar
import com.example.gogoma.viewmodel.UserViewModel

@Composable
fun WatchConnectScreen (navController: NavController, userViewModel: UserViewModel) {
    val translucentColor = MaterialTheme.colorScheme.primary.copy(alpha = .2f)
    val screenHeight = LocalConfiguration.current.screenHeightDp

    Scaffold (
        bottomBar = { BottomBar(navController = navController, userViewModel) }
    ){ paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 0.dp, top = 0.dp, end = 0.dp, bottom = paddingValues.calculateBottomPadding())
        )
        {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((screenHeight * 0.45).dp)
                    .heightIn(min = 280.dp)
                    .background(color = Color(0xFFF8F8F8))
            ){
                val CanvasSize = 263.dp
                Canvas(
                    modifier = Modifier
                    .size(CanvasSize)
                    .align(Alignment.Center)
                ) {
                    drawCircle(
                        color = translucentColor,
                        radius = (CanvasSize / 2).toPx(),
                        center = center
                    )
                    drawCircle(
                        color = translucentColor,
                        radius = (CanvasSize / 2 * .8f).toPx(),
                        center = center
                    )
                    drawCircle(
                        color = translucentColor,
                        radius = (CanvasSize / 2 * .6f).toPx(),
                        center = center
                    )
                }

                val painter = painterResource(id = R.drawable.watch_device)
                Image(
                    painter = painter,
                    contentDescription = "watch device image",
                    modifier = Modifier
                        .width(139.91354.dp)
                        .height(220.dp)
                        .align(Alignment.Center)
                )
            }
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(start = 30.dp, top = 72.dp, end = 30.dp, bottom = 42.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "워치를 연동 중입니다.",
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF000000),
                        textAlign = TextAlign.Center,
                    )
                )
                Text(
                    text = "Galaxy Watch4 Classic (RN3K)",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Light,
                        color = Color(0xFF000000),
                        textAlign = TextAlign.Center,
                    ),
                    modifier = Modifier
                        .widthIn(max = 117.dp)
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun WatchConnectScreenPreview(){
    WatchConnectScreen(rememberNavController(), UserViewModel())
}