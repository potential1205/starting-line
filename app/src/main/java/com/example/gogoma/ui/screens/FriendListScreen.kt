package com.example.gogoma.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gogoma.data.model.FriendResponse
import com.example.gogoma.theme.GogomaTheme
import com.example.gogoma.ui.components.BottomBar
import com.example.gogoma.ui.components.FriendListItem
import com.example.gogoma.ui.components.TopBarArrow
import com.example.gogoma.utils.TokenManager
import com.example.gogoma.viewmodel.FriendsViewModel
import com.example.gogoma.viewmodel.UserViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun FriendListScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    friendsViewModel: FriendsViewModel
) {
    val friends = friendsViewModel.friends.collectAsState()
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        friendsViewModel.fetchFriends(TokenManager.getAccessToken(context))

        friendsViewModel.errorMessage.collectLatest { message ->
            errorMessage = message
        }
    }

    Scaffold (
        topBar = { TopBarArrow (
            title = "친구",
            onBackClick = { navController.popBackStack() },
            refreshAction = { friendsViewModel.updateFriend(TokenManager.getAccessToken(context)) }
        )
        },
        bottomBar = { BottomBar(navController = navController, userViewModel) }
    ){ paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)){
            if (errorMessage != null) {
                Text(text = errorMessage ?:"", color = Color.Red)
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 5.dp, vertical = 5.dp),
                ) {
                    friends.value?.let {
                        itemsIndexed(it.friendResponseList) { index, friend ->
                            FriendListItem(
                                friend.copy(rank = index + 1),
                                isMe = (friends.value!!.userId == friend.friendId)
                            )

                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FriendListScreenPreview() {
    GogomaTheme {
//        FriendListScreen(friendsViewModel)
    }
}

val friendsLists = listOf(
    FriendResponse(rank = 1, friendId = 101, name = "김용현", profileImage = null, totalDistance = 320),
    FriendResponse(rank = 2, friendId = 102, name = "이주호", profileImage = null, totalDistance = 280),
    FriendResponse(rank = 3, friendId = 103, name = "박민경", profileImage = null, totalDistance = 210),
    FriendResponse(rank = 4, friendId = 104, name = "이재훈", profileImage = null, totalDistance = 180),
    FriendResponse(rank = 5, friendId = 105, name = "김지수", profileImage = null, totalDistance = 150),
    FriendResponse(rank = 6, friendId = 106, name = "백지민", profileImage = null, totalDistance = 140),
    FriendResponse(rank = 7, friendId = 107, name = "윤도현", profileImage = null, totalDistance = 130),
    FriendResponse(rank = 8, friendId = 108, name = "최경민", profileImage = null, totalDistance = 120),
    FriendResponse(rank = 9, friendId = 109, name = "정수연", profileImage = null, totalDistance = 110),
    FriendResponse(rank = 10, friendId = 110, name = "한지훈", profileImage = null, totalDistance = 100),
    FriendResponse(rank = 11, friendId = 111, name = "오세진", profileImage = null, totalDistance = 95),
    FriendResponse(rank = 12, friendId = 112, name = "강민호", profileImage = null, totalDistance = 85),
    FriendResponse(rank = 13, friendId = 113, name = "임다은", profileImage = null, totalDistance = 78),
    FriendResponse(rank = 14, friendId = 114, name = "서지훈", profileImage = null, totalDistance = 70),
    FriendResponse(rank = 15, friendId = 115, name = "문지영", profileImage = null, totalDistance = 65)
)
