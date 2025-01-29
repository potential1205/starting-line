package com.example.gogoma.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gogoma.R
import com.example.gogoma.theme.GogomaTheme

@Composable
fun SearchBar(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(top = 3.dp, bottom = 6.dp, start = 10.dp, end = 10.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start,
    ) {
        Row(
            modifier = Modifier
                .border(width = 1.dp, color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(size = 30.dp))
                .fillMaxWidth()
                .height(45.dp)
                .background(color = MaterialTheme.colorScheme.background)
                .padding(start = 22.dp, end = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            ) {
            Text(
                text = "텍스트",
                style = TextStyle(
                    fontSize = 13.sp,
                    fontWeight = FontWeight(300),
                    color = Color(0xFFC6C6C6),
                )
            )
            IconButton (
                modifier = Modifier.padding(0.dp),
                onClick = { /* 클릭 시 동작 */ }
            ){
                Icon(
                    painter = painterResource(id = R.drawable.icon_search),
                    contentDescription = "search button",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(26.dp).padding(3.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    SearchBar()
}