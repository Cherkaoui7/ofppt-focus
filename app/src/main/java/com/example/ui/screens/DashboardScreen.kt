package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Exam
import com.example.data.model.OfpptModule
import com.example.data.model.Task
import com.example.data.provider.OfpptModuleProvider
import com.example.ui.theme.*
import com.example.ui.viewmodel.StudyViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: StudyViewModel,
    onNavigateToTasks: () -> Unit,
    onNavigateToNotes: () -> Unit,
    onNavigateToModules: () -> Unit,
    onQuickActionAddTask: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tasks by viewModel.allTasks.collectAsState()
    val exams by viewModel.allExams.collectAsState()
    val xpPoints by viewModel.xpPoints.collectAsState()
    val streak by viewModel.studyStreak.collectAsState()
    val userName by viewModel.userName.collectAsState()
    val isDark by viewModel.isDarkMode.collectAsState()

    // Calculated Progress
    val totalTasks = tasks.size
    val completedTasks = tasks.count { it.isCompleted }
    val progressRatio = if (totalTasks > 0) completedTasks.toFloat() / totalTasks else 0f

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(if (isDark) CyberDarkBg else CyberLightBg)
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // --- 1. Top Greeting Header & Wave ---
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "STUDENT PORTAL",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (isDark) Color.LightGray else SlateGrayText,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "OFPPT",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                color = if (isDark) Color.White else DeepNavyText,
                                fontWeight = FontWeight.Black
                            )
                        )
                        Text(
                            text = "Focus",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                color = TechTeal,
                                fontWeight = FontWeight.Black
                            )
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Accent Theme Toggle Icon
                    IconButton(
                        onClick = { viewModel.toggleTheme() },
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                if (isDark) Color(0xFF1E293B) else Color.White,
                                CircleShape
                            )
                            .border(
                                1.dp,
                                if (isDark) CyberDarkBorder else CyberLightBorder,
                                CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = if (isDark) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                            contentDescription = "Toggle Theme Mode",
                            tint = if (isDark) TechTeal else SlateGrayText,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Avatar badge as styled in the Clean Minimalism design HTML
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(BrandBlueLight)
                            .border(2.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (userName.isNotEmpty()) userName.take(1).uppercase() else "A",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = DeepNavyText,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }

        // --- 2. Gamified Hub Card: XP, Streaks & Badges ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDark) CyberDarkCard else Color.White
                ),
                border = BorderStroke(
                    1.dp,
                    if (isDark) CyberDarkBorder else CyberLightBorder
                )
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.OfflineBolt,
                                contentDescription = "Streak Icon",
                                tint = AmberWarning,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "$streak Days Streak",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) Color.White else CyberDarkBg
                                )
                            )
                        }

                        // Badge / Points capsule
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(TechTeal.copy(alpha = 0.15f))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "$xpPoints XP",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TechTeal
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Simulated badge rack
                    Text(
                        text = "EARNED BADGES",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (isDark) Color.Gray else Color.DarkGray,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        BadgeItem(
                            icon = Icons.Outlined.School,
                            title = "Novice",
                            unlocked = xpPoints >= 50,
                            isDark = isDark
                        )
                        BadgeItem(
                            icon = Icons.Outlined.Task,
                            title = "Crusher",
                            unlocked = completedTasks >= 3,
                            isDark = isDark
                        )
                        BadgeItem(
                            icon = Icons.Outlined.Psychology,
                            title = "AI Wizard",
                            unlocked = xpPoints >= 150,
                            isDark = isDark
                        )
                    }
                }
            }
        }

        // --- 3. Circular Progress Ring & Statistics Overview ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDark) Color(0xFF132A42) else TechTeal
                ),
                border = BorderStroke(
                    1.dp,
                    if (isDark) Color(0xFF1E3A5F) else Color.Transparent
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Study Progress",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (totalTasks > 0) {
                                "$completedTasks of $totalTasks coursework tasks completed today."
                            } else {
                                "No active coursework tasks added. Start your day!"
                            },
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.85f),
                                lineHeight = 16.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        
                        // Custom glassmorphic capsule badge from Design HTML
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .background(Color.White.copy(alpha = 0.2f))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(if (progressRatio >= 1f) ElectricGreen else Color(0xFF4ADE80)) // Vibrant green indicator
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${(progressRatio * 100).toInt()}% Progress",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        if (totalTasks == 0) {
                            Spacer(modifier = Modifier.height(10.dp))
                            TextButton(
                                onClick = onQuickActionAddTask,
                                modifier = Modifier.minimumInteractiveComponentSize()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Add First Task", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = Color.White))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Beautiful high-fidelity Circular Progress Ring reflecting the SVG diagram
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(80.dp)
                    ) {
                        CircularProgressIndicator(
                            progress = { progressRatio },
                            modifier = Modifier.fillMaxSize(),
                            color = Color.White,
                            strokeWidth = 7.dp,
                            trackColor = Color.White.copy(alpha = 0.2f)
                        )
                        Text(
                            text = "${(progressRatio * 100).toInt()}%",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        )
                    }
                }
            }
        }

        // --- 4. Today's Core Tasks Mini-List ---
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Today's Study Checklist",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) Color.White else CyberDarkBg
                    )
                )

                TextButton(
                    onClick = onNavigateToTasks,
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Text("View All", style = MaterialTheme.typography.bodyMedium.copy(color = TechTeal, fontWeight = FontWeight.Bold))
                }
            }
        }

        if (tasks.isEmpty()) {
            item {
                EmptyStateCard(
                    title = "Your Queue is Clear!",
                    desc = "No academic tasks due. Add tasks using Quick Actions down below to customize your schedule.",
                    icon = Icons.Outlined.CheckCircle,
                    isDark = isDark
                )
            }
        } else {
            val incompleteToday = tasks.filter { !it.isCompleted }.take(3)
            if (incompleteToday.isEmpty()) {
                item {
                    EmptyStateCard(
                        title = "Unmatched Execution!",
                        desc = "All tasks completed! Revise course resources or study modules to maximize grades.",
                        icon = Icons.Outlined.EmojiEvents,
                        isDark = isDark
                    )
                }
            } else {
                items(incompleteToday, key = { it.id }) { task ->
                    TaskBriefRow(
                        task = task,
                        onToggle = { viewModel.toggleTaskCompletion(task) },
                        isDark = isDark
                    )
                }
            }
        }

        // --- 5. Upcoming Exam Countdown Alert ---
        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Exam Countdown",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) Color.White else CyberDarkBg
                    )
                )
            }
        }

        val primaryExam = exams.firstOrNull { it.revisionStatus != "REVISED" && it.examDate > System.currentTimeMillis() }
        if (primaryExam == null) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToTasks() },
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDark) CyberDarkCard else Color.White
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (isDark) CyberDarkBorder else CyberLightBorder
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(ElectricGreen.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Outlined.SentimentSatisfied, contentDescription = null, tint = ElectricGreen)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                "No stressful exams ahead",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) Color.White else DeepNavyText
                                )
                            )
                            Text(
                                "Add course exam dates inside the Exams section.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = SlateGrayText
                                )
                            )
                        }
                    }
                }
            }
        } else {
            item {
                val timeDiff = primaryExam.examDate - System.currentTimeMillis()
                val daysDiff = (timeDiff / (1000 * 60 * 60 * 24)).coerceAtLeast(0)
                val examMonth = remember(primaryExam.examDate) {
                    val sdf = SimpleDateFormat("MMM", java.util.Locale.getDefault())
                    sdf.format(java.util.Date(primaryExam.examDate)).uppercase()
                }
                val examDay = remember(primaryExam.examDate) {
                    val sdf = SimpleDateFormat("dd", java.util.Locale.getDefault())
                    sdf.format(java.util.Date(primaryExam.examDate))
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDark) CyberDarkCard else Color.White
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (isDark) CyberDarkBorder else CyberLightBorder
                    )
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Examen à venir",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) Color.White else DeepNavyText
                                )
                            )
                            
                            // High-contrast Urgent Capsule Badge from Design HTML
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(100.dp))
                                    .background(UrgentRedBg)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "URGENT",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = UrgentRedText,
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 0.5.sp
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Minimalist Calendar Badge from Design HTML
                            Column(
                                modifier = Modifier
                                    .width(56.dp)
                                    .height(56.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(BrandBlueLight),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = examMonth,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = DeepNavyText,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = examDay,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = DeepNavyText,
                                        fontWeight = FontWeight.Black
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = primaryExam.examTitle,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDark) Color.White else DeepNavyText
                                    )
                                )
                                Text(
                                    text = "${primaryExam.moduleName} • Salle 02",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = SlateGrayText
                                    )
                                )
                            }
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "$daysDiff",
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        color = TechTeal
                                    )
                                )
                                Text(
                                    text = "DAYS LEFT",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = SlateGrayText,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- 6. Study Module Shortcuts Deck ---
        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Modules Quick Access",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) Color.White else CyberDarkBg
                    )
                )
                TextButton(
                    onClick = onNavigateToModules,
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Text("Details", style = MaterialTheme.typography.bodyMedium.copy(color = TechTeal, fontWeight = FontWeight.Bold))
                }
            }
        }

        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                OfpptModuleProvider.modules.take(3).forEach { module ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(android.graphics.Color.parseColor(module.colorHex)).copy(alpha = 0.12f))
                            .border(1.dp, Color(android.graphics.Color.parseColor(module.colorHex)).copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                            .clickable { onNavigateToModules() }
                            .padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = when (module.name) {
                                    "React" -> Icons.Default.Code
                                    "Laravel" -> Icons.Default.Terminal
                                    "MongoDB" -> Icons.Default.Storage
                                    "Cloud" -> Icons.Default.CloudQueue
                                    "UML" -> Icons.Default.AccountTree
                                    else -> Icons.Default.Groups
                                },
                                contentDescription = module.name,
                                tint = Color(android.graphics.Color.parseColor(module.colorHex)),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = module.name,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isDark) Color.White else CyberDarkBg
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

// Helper badge card view
@Composable
fun BadgeItem(
    icon: ImageVector,
    title: String,
    unlocked: Boolean,
    isDark: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(72.dp)
            .padding(vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(
                    if (unlocked) TechTeal.copy(alpha = 0.15f)
                    else if (isDark) Color(0xFF334155).copy(alpha = 0.5f)
                    else Color(0xFFE2E8F0)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (unlocked) TechTeal else Color.Gray,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Bold,
                color = if (unlocked) {
                    if (isDark) Color.White else CyberDarkBg
                } else Color.Gray
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun TaskBriefRow(
    task: Task,
    onToggle: () -> Unit,
    isDark: Boolean
) {
    val dateText = remember(task.dueDate) {
        val sdf = SimpleDateFormat("MMM dd", java.util.Locale.getDefault())
        sdf.format(java.util.Date(task.dueDate))
    }

    val pColor = when (task.priority.uppercase()) {
        "HIGH" -> AlarmRed
        "MEDIUM" -> AmberWarning
        else -> TechTeal
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (isDark) CyberDarkCard else Color.White)
            .border(1.dp, if (isDark) CyberDarkBorder else CyberLightBorder, RoundedCornerShape(16.dp))
            .clickable { onToggle() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // High quality ripple circle check target
        IconButton(
            onClick = onToggle,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = if (task.isCompleted) Icons.Filled.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                contentDescription = "Toggle Complete",
                tint = if (task.isCompleted) ElectricGreen else Color.Gray,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (task.isCompleted) Color.Gray else (if (isDark) Color.White else CyberDarkBg),
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(pColor)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${task.moduleName} • Due $dateText",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                )
            }
        }
    }
}

@Composable
fun EmptyStateCard(
    title: String,
    desc: String,
    icon: ImageVector,
    isDark: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) CyberDarkCard.copy(alpha = 0.5f) else Color.White
        ),
        border = BorderStroke(
            1.dp,
            if (isDark) CyberDarkBorder.copy(alpha = 0.5f) else CyberLightBorder
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background((if (isDark) Color(0xFF334155) else Color(0xFFF1F5F9))),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = TechTeal)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isDark) Color.White else CyberDarkBg
                    )
                )
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.Gray
                    )
                )
            }
        }
    }
}
