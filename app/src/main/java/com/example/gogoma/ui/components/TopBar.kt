package com.example.gogoma.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.gogoma.R
import com.example.gogoma.theme.GogomaTheme
import com.example.gogoma.theme.NeutralDark

@Composable
fun TopBar(navController: NavController){
    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .background(MaterialTheme.colorScheme.background)
            .padding(start = 5.dp, end = 5.dp, top = topInset),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ){
        //설정
        IconButton (onClick = { navController.navigate("setting") }){
            Icon(
                painter = painterResource(id = R.drawable.icon_settings),
                contentDescription = "icon of notifications",
                tint = NeutralDark,
                modifier = Modifier.size(26.dp).padding(2.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    GogomaTheme {
        TopBar(rememberNavController())
    }
}