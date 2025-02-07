package com.example.gogoma.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.gogoma.viewmodel.UserViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.gogoma.data.model.CreateUserRequest

@Composable
fun SignUpScreen(navController: NavController, userViewModel: UserViewModel) {
    val context = LocalContext.current

    val kakaoUserInfo = userViewModel.kakaoUserInfo
    var roadAddress by remember { mutableStateOf("") }
    var detailAddress by remember { mutableStateOf("") }
    var clothingSize by remember { mutableStateOf("") }

    // UI 구성: 기본적으로 kakaoUserInfo의 데이터와 사용자가 입력한 추가 정보 보여주기
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "회원가입")
        Text(text = "이름: ${kakaoUserInfo?.name ?: ""}")
        Text(text = "이메일: ${kakaoUserInfo?.email ?: ""}")
        // ... 기타 카카오에서 받아온 정보

        OutlinedTextField(
            value = roadAddress,
            onValueChange = { roadAddress = it },
            label = { Text("도로명 주소") }
        )
        OutlinedTextField(
            value = detailAddress,
            onValueChange = { detailAddress = it },
            label = { Text("상세 주소") }
        )
        OutlinedTextField(
            value = clothingSize,
            onValueChange = { clothingSize = it },
            label = { Text("옷 사이즈") }
        )
        Button (onClick = {
            // CreateUserRequest 구성
            val request = CreateUserRequest(
                kakaoId = kakaoUserInfo?.id ?: 0,
                name = kakaoUserInfo?.name ?: "",
                profileImage = kakaoUserInfo?.profileImage ?: "",
                email = kakaoUserInfo?.email ?: "",
                gender = kakaoUserInfo?.gender ?: "MALE",
                birthDate = kakaoUserInfo?.birthDate ?: "",
                birthYear = kakaoUserInfo?.birthYear ?: "",
                phoneNumber = kakaoUserInfo?.phoneNumber ?: "",
                roadAddress = roadAddress,
                detailAddress = detailAddress,
                clothingSize = clothingSize
            )
            // 회원가입 API 호출
            userViewModel.signUpUser(context = context, createUserRequest = request) { success ->
                if (success) {
                    // 회원가입 성공 시 메인 화면으로 이동
                    navController.navigate("main_screen") {
                        // 회원가입 후 뒤로 가기 스택을 없애기 위한 설정
                        popUpTo("sign_up_screen") { inclusive = true }
                    }
                } else {
                    // 회원가입 실패 처리
                    println("되지 아니하다")
                }
            }
        }) {
            Text("회원가입")
        }
    }
}
