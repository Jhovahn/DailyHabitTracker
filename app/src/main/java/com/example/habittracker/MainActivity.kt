package com.example.habittracker

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.habittracker.data.HabitDatabase
import com.example.habittracker.navigation.NavGraph
import com.example.habittracker.ui.theme.HabitTrackerTheme
import com.example.habittracker.viewmodel.HabitViewModel
import com.example.habittracker.viewmodel.HabitViewModelFactory

class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}
    private var incomingHabitId by mutableLongStateOf(-1L)
    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("ScheduleExactAlarm")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        incomingHabitId = intent.getLongExtra("habitId", -1)
        val db = HabitDatabase.getDatabase(applicationContext)
        val dao = db.habitDao()
        val viewModelFactory = HabitViewModelFactory(dao)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkAndRequestPermission()
        }
        setContent {
            HabitTrackerTheme {
                val viewModel: HabitViewModel = viewModel(factory = viewModelFactory)
                NavGraph(viewModel, incomingHabitId)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkAndRequestPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED -> {
                //NO NEED TO ASK FOR PERMISSIONS
            }
            shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                //POTENTIAL DIALOGUE TO EXPLAIN THAT THE APPS NEEDS PERMISSION
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            else -> {
                //REQUEST PERMISSION
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        incomingHabitId = intent.getLongExtra("habitId", -1)
    }
}