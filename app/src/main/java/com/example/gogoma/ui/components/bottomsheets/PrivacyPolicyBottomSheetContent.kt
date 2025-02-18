package com.example.gogoma.ui.components.bottomsheets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gogoma.ui.components.FilterListItemTitle

@Composable
fun PrivacyPolicyBottomSheetContent(
    onDismiss: () -> Unit
) {
    BottomSheetContentWithTitle(
        title = "개인정보 처리방침",
        headerLeftContent = {
            Text(
                text = "닫기",
                modifier = Modifier.clickable { onDismiss() }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            item { FilterListItemTitle("📜 개인정보 처리방침") }
            item { Text("1️⃣ **수집하는 개인정보 항목 및 수집 방법**") }
            item {
                Text("- 이름, 생년월일, 연락처, 이메일 주소 등\n" +
                        "- 결제 정보(카드번호, 계좌번호 등)\n" +
                        "- 서비스 이용 기록, 접속 로그 등")
            }
            item { Text("2️⃣ **개인정보 수집 및 이용 목적**") }
            item {
                Text("- 서비스 제공 및 운영\n" +
                        "- 결제 처리 및 환불\n" +
                        "- 고객 지원 및 문의 대응\n" +
                        "- 법적 의무 준수")
            }
            item { Text("3️⃣ **개인정보 보유 및 이용 기간**") }
            item {
                Text("개인정보는 이용 목적 달성 후 파기되며, 법령에 따라 일정 기간 보관될 수 있습니다.")
            }
            item { Text("4️⃣ **이용자의 권리 및 행사 방법**") }
            item {
                Text("이용자는 개인정보 열람, 수정, 삭제를 요청할 수 있습니다.")
            }
            item { Text("5️⃣ **보안 및 보호조치**") }
            item {
                Text("- 개인정보 암호화 저장\n" +
                        "- 접근 권한 최소화\n" +
                        "- 정기적 보안 점검 실시")
            }
            item { Text("6️⃣ **문의 및 담당자**") }
            item {
                Text("- 성명: 홍길동\n" +
                        "- 연락처: 010-6685-9610\n" +
                        "- 이메일: gernica10@gmail.com")
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}
