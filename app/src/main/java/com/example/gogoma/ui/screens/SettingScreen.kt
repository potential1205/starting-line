package com.example.gogoma.ui.screens

import android.widget.Toast
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SettingScreen(navController: NavController, userViewModel: UserViewModel) {
    val context = LocalContext.current
    Scaffold (
        topBar = { TopBarArrow (
            title = "설정",
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
                //카카오 SDK
//                Button(onClick = {
//                    userViewModel.unlinkKakaoAccount{success, error->
//                     if(success){
//                         println("연결 해제됨")
//                     } else {
//                         println("오류 발생"+error?.message)
//                     }
//                    }
//                }) {
//                    Text("탈퇴하기")
//                }
                Button(onClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        val result = userViewModel.unlinkAndDeleteUser(context)
                        if (result) {
                            Toast.makeText(context, "회원 탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "회원 탈퇴에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }) {
                    Text("탈퇴하기")
                }
                Button(onClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        val result = userViewModel.unlinkKakaoAccountWeb(context)
                        if (result) {
                            Toast.makeText(context, "연결이 해제되었습니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "연결 해제에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }) {
                    Text("카카오 연결 해제 - dev")
                }
            }
        }
    }
}