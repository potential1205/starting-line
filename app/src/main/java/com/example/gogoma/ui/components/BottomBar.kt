package com.example.gogoma.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.gogoma.R
import com.example.gogoma.theme.GogomaTheme
import com.example.gogoma.viewmodel.UserViewModel

@Composable
fun BottomBar(navController : NavController, userViewModel: UserViewModel){
    val isLoggedIn = userViewModel.isLoggedIn
    val currRoute = navController.currentDestination?.route

    val bottomInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp + bottomInset)
            .background(color = MaterialTheme.colorScheme.background)
            .padding(top = 5.dp, start = 28.dp, end = 28.dp, bottom = 5.dp + bottomInset),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        listOf(
            "main" to Pair(R.drawable.icon_home, "홈"),
            "registList" to Pair(R.drawable.icon_list, "신청내역"),
            "friendList" to Pair(R.drawable.icon_friend, "친구"),
            "paceSetting" to Pair(R.drawable.icon_running, "달리기"),
        ).forEach {(route, iconAndTitle) ->
            val (icon, title) = iconAndTitle
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconButton (
                    modifier = Modifier.size(28.dp),
                    onClick = {
                        if (!isLoggedIn && route != "main" && currRoute != "signpage") {
                            navController.navigate("signpage") {
                                popUpTo("signpage") { inclusive = true } //signpage는 스택에 쌓지 않음
                            }
                        } else {
                            if(currRoute == route) {
                                navController.popBackStack(route, inclusive = true)
                            } //같은 페이지라면 스택을 쌓지 않음
                            navController.navigate(route)
                        }
                    }
                ){
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = "icon of ${route}",
                        tint = if (currRoute == route) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    color = if (currRoute == route) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onBackground
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomBarPreview() {
    GogomaTheme {
        BottomBar(navController = rememberNavController(), userViewModel = viewModel())
    }
}