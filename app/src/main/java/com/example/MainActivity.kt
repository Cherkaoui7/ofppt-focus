package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.StudyViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: StudyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkState by viewModel.isDarkMode.collectAsState()
            
            MyApplicationTheme(darkTheme = isDarkState) {
                val isLoggedIn by viewModel.isUserLoggedIn.collectAsState()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (!isLoggedIn) {
                        WelcomeScreen(
                            viewModel = viewModel,
                            modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)
                        )
                    } else {
                        MainAppContainer(viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MainAppContainer(
    viewModel: StudyViewModel
) {
    var selectedTab by remember { mutableStateOf(0) }
    val isDark by viewModel.isDarkMode.collectAsState()

    val navigationItems = listOf(
        NavigationTabItem("Dashboard", Icons.Default.Dashboard, Icons.Outlined.Dashboard),
        NavigationTabItem("Modules", Icons.Default.School, Icons.Outlined.School),
        NavigationTabItem("Planner", Icons.Default.CalendarMonth, Icons.Outlined.CalendarMonth),
        NavigationTabItem("Notes & AI", Icons.Default.NoteAlt, Icons.Outlined.NoteAlt)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = NavigationBarDefaults.Elevation,
                windowInsets = WindowInsets.navigationBars,
                modifier = Modifier.testTag("app_navigation_bar")
            ) {
                navigationItems.forEachIndexed { idx, tab ->
                    val active = selectedTab == idx
                    NavigationBarItem(
                        selected = active,
                        onClick = { selectedTab = idx },
                        icon = {
                            Icon(
                                imageVector = if (active) tab.activeIcon else tab.inactiveIcon,
                                contentDescription = tab.label
                            )
                        },
                        label = { Text(tab.label) },
                        alwaysShowLabel = true,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                            unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        // Adaptive top/safe status padding handling notch overlaps safely
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    if (targetState > initialState) {
                        // Slide active tabs to left
                        slideInHorizontally { width -> width / 2 } + fadeIn() togetherWith
                                slideOutHorizontally { width -> -width / 2 } + fadeOut()
                    } else {
                        // Slide active tabs to right
                        slideInHorizontally { width -> -width / 2 } + fadeIn() togetherWith
                                slideOutHorizontally { width -> width / 2 } + fadeOut()
                    }
                },
                label = "MainTabsNavigationContainer"
            ) { currentIdx ->
                when (currentIdx) {
                    0 -> DashboardScreen(
                        viewModel = viewModel,
                        onNavigateToTasks = { selectedTab = 2 },
                        onNavigateToNotes = { selectedTab = 3 },
                        onNavigateToModules = { selectedTab = 1 },
                        onQuickActionAddTask = { selectedTab = 2 } // Triggers tasks pane
                    )
                    1 -> ModulesScreen(
                        viewModel = viewModel
                    )
                    2 -> TasksExamsScreen(
                        viewModel = viewModel
                    )
                    3 -> NotesAiScreen(
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

data class NavigationTabItem(
    val label: String,
    val activeIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val inactiveIcon: androidx.compose.ui.graphics.vector.ImageVector
)
