package com.example.gogoma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.room.Room
import com.example.gogoma.data.roomdb.repository.RoomRepository
import com.example.gogoma.theme.GogomaTheme
import com.example.gogoma.ui.navigation.AppNavigation
import com.example.gogoma.viewmodel.UserViewModel
import com.example.newroom.AppDatabase

class MainActivity : ComponentActivity() {
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var db: AppDatabase
    private lateinit var repository: RoomRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        userViewModel.init(applicationContext)
        setContent {
            GogomaApp(userViewModel)
        }
        db = Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "test-database"
        )
            .fallbackToDestructiveMigration()
            .build()

        repository = RoomRepository(db)
    }
}

@Composable
fun GogomaApp(userViewModel: UserViewModel){
    GogomaTheme {
        AppNavigation(userViewModel)
    }
}
