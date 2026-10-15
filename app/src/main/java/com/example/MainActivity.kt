package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.sp
import com.example.notification.HagagNotificationHelper
import com.example.ui.HagagViewModel
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.StatsScreen
import com.example.ui.screens.TasksScreen
import com.example.ui.screens.TimerScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PrimaryAmber
import com.example.ui.theme.SurfaceDark

class MainActivity : ComponentActivity() {

    private val viewModel: HagagViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Create notification channels
        HagagNotificationHelper.createNotificationChannels(this)

        setContent {
            MyApplicationTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    HagagStudyApp(viewModel = viewModel)
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        // If the user left or closed the app during an active pomodoro study session
        viewModel.onAppBackgrounded()
    }
}

data class NavTab(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

@Composable
fun HagagStudyApp(
    viewModel: HagagViewModel
) {
    val currentTab by viewModel.currentTab.collectAsState()

    // Handle back press to return to home tab
    if (currentTab != 0) {
        BackHandler {
            viewModel.selectTab(0)
        }
    }

    val tabs = listOf(
        NavTab(
            title = "الرئيسية",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            testTag = "nav_home"
        ),
        NavTab(
            title = "المؤقت",
            selectedIcon = Icons.Filled.Timer,
            unselectedIcon = Icons.Outlined.Timer,
            testTag = "nav_timer"
        ),
        NavTab(
            title = "الكويز",
            selectedIcon = Icons.Filled.Psychology,
            unselectedIcon = Icons.Outlined.Psychology,
            testTag = "nav_quiz"
        ),
        NavTab(
            title = "المهام",
            selectedIcon = Icons.Filled.ListAlt,
            unselectedIcon = Icons.Outlined.ListAlt,
            testTag = "nav_tasks"
        ),
        NavTab(
            title = "الإحصائيات",
            selectedIcon = Icons.Filled.BarChart,
            unselectedIcon = Icons.Outlined.BarChart,
            testTag = "nav_stats"
        )
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("bottom_nav_bar"),
                containerColor = SurfaceDark,
                contentColor = Color.White
            ) {
                tabs.forEachIndexed { index, tab ->
                    val isSelected = currentTab == index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.selectTab(index) },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                contentDescription = tab.title
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 11.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = PrimaryAmber,
                            indicatorColor = PrimaryAmber,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.testTag(tab.testTag)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            when (currentTab) {
                0 -> HomeScreen(
                    viewModel = viewModel,
                    onNavigateToTimer = { viewModel.selectTab(1) },
                    onNavigateToTasks = { viewModel.selectTab(3) },
                    onNavigateToQuiz = { viewModel.selectTab(2) }
                )
                1 -> TimerScreen(viewModel = viewModel)
                2 -> QuizScreen(viewModel = viewModel)
                3 -> TasksScreen(viewModel = viewModel)
                4 -> StatsScreen(viewModel = viewModel)
            }
        }
    }
}
