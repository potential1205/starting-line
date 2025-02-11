package com.example.gogoma.ui.screens

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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

    val tmpkakaoUserInfo = userViewModel.tmpkakaoUserInfo
    var roadAddress by remember { mutableStateOf("") }
    var detailAddress by remember { mutableStateOf("") }
    var clothingSize by remember { mutableStateOf("") }

    // AddressApiActivity를 실행하여 주소 검색 결과를 받아오는 launcher 생성
    val addressLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // AddressApiActivity에서 반환한 "data" 값을 roadAddress에 반영
            val addressData = result.data?.getStringExtra("data") ?: ""
            roadAddress = addressData
        }
    }


    // UI 구성: 기본적으로 kakaoUserInfo의 데이터와 사용자가 입력한 추가 정보 보여주기
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "회원가입")
        Text(text = "이름: ${tmpkakaoUserInfo?.name ?: ""}")
        Text(text = "이메일: ${tmpkakaoUserInfo?.email ?: ""}")
        // ... 기타 카카오에서 받아온 정보

        OutlinedTextField(
            value = roadAddress,
            onValueChange = { /* 직접 입력하지 못하도록 비워둠 */ },
            label = { Text("도로명 주소") },
            readOnly = true
        )
        Button(onClick = {
            addressLauncher.launch(Intent(context, AddressApiActivity::class.java))
        }) {
            Text("주소 검색")
        }

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
                kakaoId = tmpkakaoUserInfo?.id ?: 0,
                name = tmpkakaoUserInfo?.name ?: "",
                profileImage = tmpkakaoUserInfo?.profileImage ?: "",
                email = tmpkakaoUserInfo?.email ?: "",
                gender = tmpkakaoUserInfo?.gender ?: "MALE",
                birthDate = tmpkakaoUserInfo?.birthDate ?: "",
                birthYear = tmpkakaoUserInfo?.birthYear ?: "",
                phoneNumber = tmpkakaoUserInfo?.phoneNumber ?: "",
                roadAddress = roadAddress,
                detailAddress = detailAddress,
                clothingSize = clothingSize
            )
            // 회원가입 API 호출
            userViewModel.signUpUser(context = context, createUserRequest = request) { success ->
                if (success) {
//                    // 회원가입 성공 시 메인 화면으로 이동
//                    navController.navigate("mypage") {
//                        // 회원가입 후 뒤로 가기 스택을 없애기 위한 설정
//                        popUpTo("signup") { inclusive = true }
//                    }
                    navController.popBackStack()
                } else {
                    // 회원가입 실패 처리
                    println("회원가입 실패")
                }
            }
        }) {
            Text("회원가입")
        }
    }
}
