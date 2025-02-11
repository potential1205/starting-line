package com.example.gogoma.ui.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.gogoma.R
import com.example.gogoma.ui.components.BottomBar
import com.example.gogoma.ui.components.ButtonBasic
import com.example.gogoma.ui.components.ButtonKakao
import com.example.gogoma.ui.components.TopBarArrow
import com.example.gogoma.viewmodel.UserViewModel
import com.kakao.sdk.user.UserApiClient

@Composable
fun SignScreen(navController: NavController, userViewModel: UserViewModel) {
    val context = LocalContext.current

    Scaffold (
        topBar = { TopBarArrow (
            title = "",
            onBackClick = { navController.popBackStack() }
        )
        },
        bottomBar = { BottomBar(navController = navController, userViewModel) }
    ){ paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)){
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 20.dp, end = 20.dp, bottom = 70.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // 로고
                Column(
                    modifier = Modifier
                        .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(9.dp, Alignment.Top),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.logo_image),
                        contentDescription = "Logo",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(107.dp)
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.width(65.dp)
                    )
                }
                ButtonKakao(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        userViewModel.loginWithKakao(context) { success ->
                            if (success) {
                                Toast.makeText(context, "로그인 성공", Toast.LENGTH_SHORT).show()
                                Log.i("SignScreen", "로그인 성공")
//                                navController.navigate("mypage")
                                navController.popBackStack() // 이전 화면으로 돌아감
                            } else {
                                Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT).show()
                                Log.e("SignScreen", "로그인 실패")
                            }
                        }
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