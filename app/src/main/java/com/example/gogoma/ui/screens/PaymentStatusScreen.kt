import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.gogoma.theme.BrandColor1
import com.example.gogoma.ui.components.BottomBar
import com.example.gogoma.ui.components.BottomBarButton
import com.example.gogoma.ui.components.TopBar
import com.example.gogoma.ui.components.TopBarArrow

@Composable
fun PaymentStatusScreen(isSuccess: Boolean, onConfirm: () -> Unit, onNavigateToMain: (() -> Unit)? = null) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if (isSuccess) {
            TopBarArrow("신청 완료", {})
        } else {
            TopBar()
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier.weight(1f).fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (isSuccess) {
                SuccessContent(onConfirm)
            } else {
                FailureContent(onConfirm, onNavigateToMain)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (isSuccess) {
            BottomBarButton(
                text = "완료",
                backgroundColor = BrandColor1,
                textColor = Color.White,
                onClick = {}
            )
        } else {
            BottomBar(navController = rememberNavController())
        }
    }
}

@Composable
fun SuccessContent(onConfirm: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "✅ 신청이 완료되었습니다.", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "2022 서울마라톤\n2022.03.20.  #10km", fontSize = 16.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onConfirm,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "완료", color = Color.White, fontSize = 16.sp)
        }
    }
}

@Composable
fun FailureContent(onConfirm: () -> Unit, onNavigateToMain: (() -> Unit)?) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "❌ 신청에 실패했습니다.", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onConfirm,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "결과 확인하러 가기", color = Color.White, fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (onNavigateToMain != null) {
            Button(
                onClick = onNavigateToMain,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8BC34A)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "메인 페이지로 돌아가기", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPaymentStatusScreenSuccess() {
    PaymentStatusScreen(isSuccess = true, onConfirm = {}, onNavigateToMain = null)
}

@Preview(showBackground = true)
@Composable
fun PreviewPaymentStatusScreenFailure() {
    PaymentStatusScreen(isSuccess = false, onConfirm = {}, onNavigateToMain = {})
}
