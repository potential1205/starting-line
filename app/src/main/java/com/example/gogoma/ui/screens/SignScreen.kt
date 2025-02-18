package com.example.gogoma.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.gogoma.BuildConfig
import com.example.gogoma.R
import com.example.gogoma.ui.components.BottomBar
import com.example.gogoma.ui.components.ButtonBasic
import com.example.gogoma.ui.components.ButtonKakao
import com.example.gogoma.ui.components.TopBarArrow
import com.example.gogoma.ui.components.TopBarClose
import com.example.gogoma.viewmodel.UserViewModel
import com.kakao.sdk.user.UserApiClient
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

@Composable
fun SignScreen(navController: NavController, userViewModel: UserViewModel) {
    val context = LocalContext.current
    val authUrl = "https://kauth.kakao.com/oauth/authorize" +
            "?response_type=code" +
            "&client_id=${BuildConfig.CLIENT_ID}" +
            "&redirect_uri=${BuildConfig.REDIRECT_URI}" +
            "&scope=friends"

    Scaffold (
        topBar = {
            TopBarClose(onCloseClick ={
                navController.popBackStack()
            })
        },
    ){ paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(paddingValues)
        ){
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "logo image",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "마라톤의 상쾌한 출발선에 서 보세요.",
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFFFFFFF),
                    ),
                    modifier = Modifier
                        .padding(18.dp)
                )
                ButtonKakao(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 22.dp, end= 22.dp, bottom = 79.dp),
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(authUrl))
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignScreenPreview() {
    SignScreen(rememberNavController(), UserViewModel())
}