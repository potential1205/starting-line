package com.example.gogoma.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.gogoma.R
import com.example.gogoma.theme.GogomaTheme
import com.example.gogoma.ui.screens.MainScreen

@Composable
fun TopBar(){
    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp + topInset)
            .background(MaterialTheme.colorScheme.background)
            .padding(start = 5.dp, end = 5.dp, top = topInset),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        //로고
        Row(
            modifier = Modifier
                .padding(start = 23.dp, top = 5.dp, end = 23.dp, bottom = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.logo_image),
                contentDescription = "Logo",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(30.dp)
            )
            Icon(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
        }

        //알림
        IconButton (onClick = { /* 클릭 시 동작 */ }){
            Icon(
                painter = painterResource(id = R.drawable.icon_notifications),
                contentDescription = "icon of notifications",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(26.dp).padding(2.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    GogomaTheme {
        TopBar()
    }
}