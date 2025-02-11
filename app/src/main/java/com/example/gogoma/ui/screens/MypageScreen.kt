package com.example.gogoma.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.gogoma.ui.components.BottomBar
import com.example.gogoma.ui.components.TopBarArrow
import com.example.gogoma.viewmodel.UserViewModel

@Composable
fun MypageScreen(navController: NavController, userViewModel: UserViewModel) {
    val context = LocalContext.current
    Scaffold (
        topBar = { TopBarArrow (
            title = "마이 페이지",
            onBackClick = { navController.popBackStack() }
        )
        },
        bottomBar = { BottomBar(navController = navController, userViewModel) }
    ){ paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)){
            Column {
                Text("마이페이지입니다")
                Text("${userViewModel.kakaoUserInfo?.name}님 안녕하세요")
                Button(onClick = {
                    userViewModel.logout(context)
                }) {
                    Text("로그아웃하기")
                }
                Button(onClick = {
                    userViewModel.unlinkKakaoAccount{success, error->
                     if(success){
                         println("연결 해제됨")
                     } else {
                         println("오류 발생"+error?.message)
                     }
                    }
                }) {
                    Text("탈퇴하기")
                }
            }
        }
    }
}