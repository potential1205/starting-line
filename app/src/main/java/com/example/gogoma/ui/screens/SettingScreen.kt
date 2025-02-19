package com.example.gogoma.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.gogoma.R
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
            Column (
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                // 프로필
                Row (
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = 25.dp, top = 10.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    val painter = if (userViewModel.kakaoUserInfo?.profileImage.isNullOrEmpty()) {
                        painterResource(id = R.drawable.logo_image)
                    } else {
                        val secureProfileImage = userViewModel.kakaoUserInfo?.profileImage?.replaceFirst("http://", "https://")
                        rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data(data = secureProfileImage)
                                .apply {
                                    crossfade(true) // 이미지 로딩 시 부드러운 전환 효과
                                    placeholder(R.drawable.icon_running) // 로딩 중에 보여줄 이미지
                                    error(R.drawable.icon_close) // 에러 발생 시 보여줄 이미지
                                }
                                .build()
                        )
                    }
                    Image(
                        painter = painter,
                        contentDescription = "image description",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(67.dp)
                            .clip(CircleShape)
                    )

                    Column (
                        modifier = Modifier.padding(start = 25.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ){
                        Text(
                            text = userViewModel.kakaoUserInfo?.name.toString(),
                            style = TextStyle(
                                fontSize = 18.sp,
                                textAlign = TextAlign.Center,
                            )
                        )
                        Text(
                            text = userViewModel.kakaoUserInfo?.email.toString(),
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Light,
                                color = Color(0xFFAAAAAA),
                                textAlign = TextAlign.Center,
                            )
                        )
                    }
                }

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    thickness = 1.dp,
                    color = Color(0xFFF4F4F4)
                )

                // 로그아웃
                var logoutDialog by remember { mutableStateOf(false) }
                Row ( modifier = Modifier
                    .fillMaxWidth()
                    .height(86.dp)
                    .padding(start = 20.dp)
                    .clickable { logoutDialog = true },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "로그아웃하기",
                        style = TextStyle(
                            fontSize = 18.sp,
                            color = Color(0xFF6F6F6F)
                        )
                    )
                }

                // AlertDialog
                if (logoutDialog) {
                    AlertDialog(
                        onDismissRequest = { logoutDialog = false }, // 다이얼로그 바깥을 클릭하면 닫힘
                        title = { Text("로그아웃") },
                        text = { Text("정말 로그아웃 하시겠습니까?") },
                        confirmButton = {
                            TextButton (
                                onClick = {
                                    userViewModel.logout(context) // 로그아웃 실행
                                    logoutDialog = false // 다이얼로그 닫기
                                }
                            ) {
                                Text("확인")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = { logoutDialog = false } // 취소 시 다이얼로그 닫기
                            ) {
                                Text("취소")
                            }
                        }
                    )
                }

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    thickness = 1.dp,
                    color = Color(0xFFF9F9F9)
                )

                // 회원 탈퇴
                var withdrawDialog by remember { mutableStateOf(false) }
                Row( modifier = Modifier
                    .fillMaxWidth()
                    .height(86.dp)
                    .padding(start = 20.dp)
                    .clickable { withdrawDialog = true },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "탈퇴하기",
                        style = TextStyle(
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    )
                }

                // 회원 탈퇴 다이얼로그
                if (withdrawDialog) {
                    AlertDialog(
                        onDismissRequest = { withdrawDialog = false },
                        title = { Text("회원 탈퇴") },
                        text = { Text("정말 탈퇴하시겠습니까? 탈퇴 후에는 복구할 수 없습니다.") },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    CoroutineScope(Dispatchers.Main).launch {
                                        val result = userViewModel.unlinkAndDeleteUser(context)
                                        if (result) {
                                            Toast.makeText(context, "회원 탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(context, "회원 탈퇴에 실패했습니다.", Toast.LENGTH_SHORT).show()
                                        }
                                        withdrawDialog = false
                                    }
                                }
                            ) {
                                Text("탈퇴")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { withdrawDialog = false }) {
                                Text("취소")
                            }
                        }
                    )
                }

//                // 데브모드 용
//                Button(onClick = {
//                    CoroutineScope(Dispatchers.Main).launch {
//                        val result = userViewModel.unlinkKakaoAccountWeb(context)
//                        if (result) {
//                            Toast.makeText(context, "연결이 해제되었습니다.", Toast.LENGTH_SHORT).show()
//                        } else {
//                            Toast.makeText(context, "연결 해제에 실패했습니다.", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                }) {
//                    Text("카카오 연결 해제 - dev")
//                }
            }
        }
    }
}