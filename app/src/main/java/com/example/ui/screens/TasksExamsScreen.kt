package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.ui.text.style.TextAlign
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Exam
import com.example.data.model.Task
import com.example.ui.theme.*
import com.example.ui.viewmodel.StudyViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksExamsScreen(
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    val tasks by viewModel.allTasks.collectAsState()
    val exams by viewModel.allExams.collectAsState()
    val isDark by viewModel.isDarkMode.collectAsState()

    var activeTab by remember { mutableStateOf(0) } // 0 = Tasks, 1 = Exams
    val tabs = listOf("Task Board", "Exam Planner")

    // Modals Control
    var isAddTaskDialogShown by remember { mutableStateOf(false) }
    var isAddExamDialogShown by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(if (isDark) CyberDarkBg else CyberLightBg)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(16.dp))

            // Heading Title Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Calendar & Planning",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isDark) Color.White else CyberDarkBg
                    )
                )

                // Quick Floating Add action in the header
                IconButton(
                    onClick = {
                        if (activeTab == 0) isAddTaskDialogShown = true
                        else isAddExamDialogShown = true
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .background(TechTeal, CircleShape)
                        .testTag("floating_quick_add")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Quick Add Item",
                        tint = Color.White
                    )
                }
            }

            Text(
                text = "Structure your day, coordinate active homework priorities, and plan exam deadlines.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = if (isDark) Color.LightGray else Color.DarkGray
                ),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
            )

            // Dynamic folder Tabs
            TabRow(
                selectedTabIndex = activeTab,
                containerColor = Color.Transparent,
                contentColor = TechTeal,
                divider = {},
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[activeTab]),
                        color = TechTeal
                    )
                },
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                tabs.forEachIndexed { idx, title ->
                    Tab(
                        selected = activeTab == idx,
                        onClick = { activeTab = idx },
                        modifier = Modifier.minimumInteractiveComponentSize()
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = if (activeTab == idx) FontWeight.ExtraBold else FontWeight.Medium,
                                color = if (activeTab == idx) TechTeal else Color.Gray
                            ),
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    }
                }
            }

            // Tabs Layout panels
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp)
            ) {
                if (activeTab == 0) {
                    TasksSubBoard(
                        tasks = tasks,
                        viewModel = viewModel,
                        isDark = isDark
                    )
                } else {
                    ExamsSubBoard(
                        exams = exams,
                        viewModel = viewModel,
                        isDark = isDark
                    )
                }
            }
        }
    }

    // --- Add Task Dialog Modals ---
    if (isAddTaskDialogShown) {
        var taskTitle by remember { mutableStateOf("") }
        var taskDesc by remember { mutableStateOf("") }
        var selectedPriority by remember { mutableStateOf("MEDIUM") }
        var selectedModule by remember { mutableStateOf("React") }
        var daysOffset by remember { mutableStateOf(1) } // Default 1 day offset

        val modulesList = listOf("React", "Laravel", "MongoDB", "Cloud", "UML", "Agile", "General")

        AlertDialog(
            onDismissRequest = { isAddTaskDialogShown = false },
            confirmButton = {
                Button(
                    onClick = {
                        if (taskTitle.isNotBlank()) {
                            val dueDateMillis = System.currentTimeMillis() + (daysOffset * 24 * 60 * 60 * 1000L)
                            viewModel.addTask(
                                title = taskTitle,
                                description = taskDesc,
                                priority = selectedPriority,
                                dueDate = dueDateMillis,
                                moduleName = selectedModule
                            )
                            isAddTaskDialogShown = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TechTeal)
                ) {
                    Text("Add Task", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { isAddTaskDialogShown = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            shape = RoundedCornerShape(28.dp),
            containerColor = if (isDark) Color(0xFF1E293B) else Color.White,
            title = {
                Text("Create Study Task", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = taskTitle,
                        onValueChange = { taskTitle = it },
                        label = { Text("Task Title (e.g. Scaffolding Controllers)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = taskDesc,
                        onValueChange = { taskDesc = it },
                        label = { Text("Brief Instructions (optional)") },
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Module Select Option
                    Column {
                        Text("COMPANION MODULE", style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray, fontWeight = FontWeight.Bold))
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            val chunkedList = modulesList.take(4)
                            chunkedList.forEach { mod ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (selectedModule == mod) TechTeal
                                            else if (isDark) Color(0xFF334155)
                                            else Color(0xFFF1F5F9)
                                        )
                                        .clickable { selectedModule = mod }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        mod,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (selectedModule == mod) Color.White else Color.Gray
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // Priority Select Row
                    Column {
                        Text("PRIORITY LEVEL", style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray, fontWeight = FontWeight.Bold))
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val priorities = listOf("LOW", "MEDIUM", "HIGH")
                            priorities.forEach { p ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            if (selectedPriority == p) {
                                                when (p) {
                                                    "HIGH" -> AlarmRed
                                                    "MEDIUM" -> AmberWarning
                                                    else -> TechTeal
                                                }
                                            } else if (isDark) Color(0xFF334155) else Color(0xFFF1F5F9)
                                        )
                                        .clickable { selectedPriority = p }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = p,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (selectedPriority == p) Color.White else Color.Gray
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // Simple Due Date Day offset slider / incrementer
                    Column {
                        Text("DUE DATE IN", style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray, fontWeight = FontWeight.Bold))
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("$daysOffset Days from now", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = if (isDark) Color.White else CyberDarkBg))
                            Row {
                                IconButton(
                                    onClick = { daysOffset = maxOf(1, daysOffset - 1) },
                                    modifier = Modifier.minimumInteractiveComponentSize()
                                ) {
                                    Icon(Icons.Default.RemoveCircleOutline, contentDescription = "Decrement", tint = TechTeal)
                                }
                                IconButton(
                                    onClick = { daysOffset += 1 },
                                    modifier = Modifier.minimumInteractiveComponentSize()
                                ) {
                                    Icon(Icons.Default.AddCircleOutline, contentDescription = "Increment", tint = TechTeal)
                                }
                            }
                        }
                    }
                }
            }
        )
    }

    // --- Add Exam Dialog Modals ---
    if (isAddExamDialogShown) {
        var examTitle by remember { mutableStateOf("") }
        var selectedModule by remember { mutableStateOf("React") }
        var daysOffset by remember { mutableStateOf(7) } // Exams default in a week offset

        val modulesList = listOf("React", "Laravel", "MongoDB", "Cloud", "UML", "Agile")

        AlertDialog(
            onDismissRequest = { isAddExamDialogShown = false },
            confirmButton = {
                Button(
                    onClick = {
                        if (examTitle.isNotBlank()) {
                            val examDateMillis = System.currentTimeMillis() + (daysOffset * 24 * 60 * 60 * 1000L)
                            viewModel.addExam(
                                moduleName = selectedModule,
                                title = examTitle,
                                date = examDateMillis,
                                revisionStatus = "NOT_STARTED"
                            )
                            isAddExamDialogShown = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TechTeal)
                ) {
                    Text("Add Exam Planner", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { isAddExamDialogShown = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            shape = RoundedCornerShape(28.dp),
            containerColor = if (isDark) Color(0xFF1E293B) else Color.White,
            title = {
                Text("Add Exam Target", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    OutlinedTextField(
                        value = examTitle,
                        onValueChange = { examTitle = it },
                        label = { Text("Exam Title (e.g. Midterm Quiz, Final Board)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Module selectivity
                    Column {
                        Text("CHOOSE MODULE TARGET", style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray, fontWeight = FontWeight.Bold))
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val firstBatch = modulesList.take(3)
                            firstBatch.forEach { m ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (selectedModule == m) TechTeal else if (isDark) Color(0xFF334155) else Color(0xFFF1F5F9))
                                        .clickable { selectedModule = m }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(m, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = if (selectedModule == m) Color.White else Color.Gray))
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val secondBatch = modulesList.drop(3)
                            secondBatch.forEach { m ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (selectedModule == m) TechTeal else if (isDark) Color(0xFF334155) else Color(0xFFF1F5F9))
                                        .clickable { selectedModule = m }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(m, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = if (selectedModule == m) Color.White else Color.Gray))
                                }
                            }
                        }
                    }

                    // Scheduled countdown slider
                    Column {
                        Text("SCHEDULE DATE", style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray, fontWeight = FontWeight.Bold))
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("$daysOffset Days Countdown", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = if (isDark) Color.White else CyberDarkBg))
                            Row {
                                IconButton(
                                    onClick = { daysOffset = maxOf(2, daysOffset - 1) },
                                    modifier = Modifier.minimumInteractiveComponentSize()
                                ) {
                                    Icon(Icons.Default.RemoveCircleOutline, contentDescription = "Decrement", tint = TechTeal)
                                }
                                IconButton(
                                    onClick = { daysOffset += 1 },
                                    modifier = Modifier.minimumInteractiveComponentSize()
                                ) {
                                    Icon(Icons.Default.AddCircleOutline, contentDescription = "Increment", tint = TechTeal)
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}

// Sub-Board: Tasks Control
@Composable
fun TasksSubBoard(
    tasks: List<Task>,
    viewModel: StudyViewModel,
    isDark: Boolean
) {
    if (tasks.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Outlined.Assignment, contentDescription = null, modifier = Modifier.size(56.dp), tint = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Your Task Board is Empty", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Text("Hit the quick-add button in the top header to schedule study sessions.", style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray), textAlign = TextAlign.Center)
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 32.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(tasks, key = { it.id }) { task ->
                TaskCardRowInteractive(
                    task = task,
                    onToggle = { viewModel.toggleTaskCompletion(task) },
                    onDelete = { viewModel.deleteTask(task) },
                    isDark = isDark
                )
            }
        }
    }
}

// Sub-Board: Exams Planner
@Composable
fun ExamsSubBoard(
    exams: List<Exam>,
    viewModel: StudyViewModel,
    isDark: Boolean
) {
    if (exams.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Outlined.HourglassEmpty, contentDescription = null, modifier = Modifier.size(56.dp), tint = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
            Text("No Scheduled Exams Yet", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            Text("Stay ahead of school timelines. Program calendar milestone exams here.", style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray), textAlign = TextAlign.Center)
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 32.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(exams, key = { it.id }) { exam ->
                ExamCardRowInteractive(
                    exam = exam,
                    onUpdateStatus = { st -> viewModel.updateExamStatus(exam, st) },
                    onDelete = { viewModel.deleteExam(exam) },
                    isDark = isDark
                )
            }
        }
    }
}

@Composable
fun TaskCardRowInteractive(
    task: Task,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    isDark: Boolean
) {
    val dateText = remember(task.dueDate) {
        val sdf = SimpleDateFormat("EEEE, MMM dd", java.util.Locale.getDefault())
        sdf.format(java.util.Date(task.dueDate))
    }

    val pColor = when (task.priority.uppercase()) {
        "HIGH" -> AlarmRed
        "MEDIUM" -> AmberWarning
        else -> TechTeal
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) CyberDarkCard else Color.White
        ),
        border = BorderStroke(
            1.dp,
            if (isDark) CyberDarkBorder else CyberLightBorder
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onToggle,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = if (task.isCompleted) Icons.Filled.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                    contentDescription = "Toggle completion",
                    tint = if (task.isCompleted) ElectricGreen else Color.Gray,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(pColor.copy(alpha = 0.15f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = task.priority,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = pColor,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(CyberPurple.copy(alpha = 0.1f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = task.moduleName,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = CyberPurple,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

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

                if (task.description.isNotBlank()) {
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Due $dateText", style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray))
                }
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.minimumInteractiveComponentSize()
            ) {
                Icon(Icons.Filled.DeleteOutline, contentDescription = "Delete task", tint = Color.Gray)
            }
        }
    }
}

@Composable
fun ExamCardRowInteractive(
    exam: Exam,
    onUpdateStatus: (String) -> Unit,
    onDelete: () -> Unit,
    isDark: Boolean
) {
    val dateText = remember(exam.examDate) {
        val sdf = SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
        sdf.format(java.util.Date(exam.examDate))
    }

    val daysCountdown = remember(exam.examDate) {
        val diff = exam.examDate - System.currentTimeMillis()
        (diff / (1000 * 60 * 60 * 24)).coerceAtLeast(0)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) CyberDarkCard else Color.White
        ),
        border = BorderStroke(
            1.dp,
            if (isDark) CyberDarkBorder else CyberLightBorder
        )
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header: Module name and Delete Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(TechTeal.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = exam.moduleName.uppercase(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TechTeal,
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$daysCountdown Days Left",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = if (daysCountdown <= 3) AlarmRed else Color.Gray,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete Exam Milestone",
                        tint = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Body: Exam Title and Schedule Time
            Text(
                text = exam.examTitle,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) Color.White else CyberDarkBg
                )
            )

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                Icon(Icons.Default.Alarm, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(12.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Scheduled $dateText", style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray))
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Footer: Revision Status segment control
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "REVISION STATUS",
                    style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray, fontWeight = FontWeight.Bold)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    val statusMap = mapOf(
                        "NOT_STARTED" to "Pending",
                        "IN_PROGRESS" to "Reviewing",
                        "REVISED" to "Completed"
                    )

                    statusMap.forEach { (key, title) ->
                        val active = exam.revisionStatus == key
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (active) {
                                        when (key) {
                                            "NOT_STARTED" -> AlarmRed.copy(alpha = 0.12f)
                                            "IN_PROGRESS" -> AmberWarning.copy(alpha = 0.12f)
                                            else -> ElectricGreen.copy(alpha = 0.12f)
                                        }
                                    } else if (isDark) Color(0xFF334155).copy(alpha = 0.4f) else Color(0xFFF1F5F9)
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (active) {
                                        when (key) {
                                            "NOT_STARTED" -> AlarmRed
                                            "IN_PROGRESS" -> AmberWarning
                                            else -> ElectricGreen
                                        }
                                    } else Color.Transparent,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { onUpdateStatus(key) }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (active) {
                                        when (key) {
                                            "NOT_STARTED" -> AlarmRed
                                            "IN_PROGRESS" -> AmberWarning
                                            else -> ElectricGreen
                                        }
                                    } else Color.Gray
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
