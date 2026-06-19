package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Launch
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OfpptModule
import com.example.data.provider.OfpptModuleProvider
import com.example.ui.theme.*
import com.example.ui.viewmodel.StudyViewModel
import com.example.ui.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModulesScreen(
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    var selectedModule by remember { mutableStateOf<OfpptModule?>(null) }
    val isDark by viewModel.isDarkMode.collectAsState()

    AnimatedContent(
        targetState = selectedModule,
        transitionSpec = {
            if (targetState != null) {
                // Drilling down
                slideInHorizontally { width -> width } + fadeIn() togetherWith
                        slideOutHorizontally { width -> -width } + fadeOut()
            } else {
                // Popping back up
                slideInHorizontally { width -> -width } + fadeIn() togetherWith
                        slideOutHorizontally { width -> width } + fadeOut()
            }
        },
        label = "ModuleDetailsTransition"
    ) { currentModule ->
        if (currentModule == null) {
            ModulesDashboard(
                onModuleSelect = { selectedModule = it },
                isDark = isDark
            )
        } else {
            ModuleDetailView(
                module = currentModule,
                onBack = { selectedModule = null },
                viewModel = viewModel,
                isDark = isDark
            )
        }
    }
}

@Composable
fun ModulesDashboard(
    onModuleSelect: (OfpptModule) -> Unit,
    isDark: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDark) CyberDarkBg else CyberLightBg)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Academic Modules",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = if (isDark) Color.White else CyberDarkBg
            )
        )
        Text(
            text = "Explore notes, resources, practice assignments, and challenge yourself with AI study mentors.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = if (isDark) Color.LightGray else Color.DarkGray
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 32.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(OfpptModuleProvider.modules) { module ->
                ModuleGridCard(
                    module = module,
                    onClick = { onModuleSelect(module) },
                    isDark = isDark
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModuleGridCard(
    module: OfpptModule,
    onClick: () -> Unit,
    isDark: Boolean
) {
    val mColor = remember(module.colorHex) {
        Color(android.graphics.Color.parseColor(module.colorHex))
    }

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) CyberDarkCard else Color.White
        ),
        border = BorderStroke(
            1.dp,
            if (isDark) CyberDarkBorder else CyberLightBorder
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Icon capsule
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(mColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
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
                    tint = mColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column {
                Text(
                    text = module.name,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black,
                        color = if (isDark) Color.White else CyberDarkBg
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = module.description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (isDark) Color.Gray else Color.DarkGray
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModuleDetailView(
    module: OfpptModule,
    onBack: () -> Unit,
    viewModel: StudyViewModel,
    isDark: Boolean
) {
    val mColor = remember(module.colorHex) {
        Color(android.graphics.Color.parseColor(module.colorHex))
    }

    var selectedSectionIndex by remember { mutableStateOf(0) }
    val sections = listOf("Notes", "Assignments", "Resources", "AI Quiz")

    // State of selected note detail expanded popup
    var expandedNoteTopicTitle by remember { mutableStateOf<String?>(null) }

    // Revision tracker state localized
    var revisionStatus by remember { mutableStateOf("NOT_STARTED") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDark) CyberDarkBg else CyberLightBg)
    ) {
        // --- Custom App Bar ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(44.dp)
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
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back to Modules",
                    tint = mColor
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = module.name,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isDark) Color.White else CyberDarkBg
                    )
                )
                Text(
                    text = "OFPPT Companion Guide",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                )
            }
        }

        // --- Revision Status Card ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp),
            shape = RoundedCornerShape(16.dp),
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
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "REVISION STATUS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            letterSpacing = 1.sp
                        )
                    )
                    Text(
                        text = when (revisionStatus) {
                            "NOT_STARTED" -> "Not Reviewed Yet"
                            "IN_PROGRESS" -> "Actively Reviewing"
                            else -> "Completed & Revised"
                        },
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = when (revisionStatus) {
                                "NOT_STARTED" -> AlarmRed
                                "IN_PROGRESS" -> AmberWarning
                                else -> ElectricGreen
                            }
                        )
                    )
                }

                // Dropdown selectors
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    val states = listOf("NOT_STARTED", "IN_PROGRESS", "REVISED")
                    states.forEach { state ->
                        IconButton(
                            onClick = {
                                revisionStatus = state
                                viewModel.addXp(15)
                            },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(
                                    if (revisionStatus == state) mColor
                                    else if (isDark) Color(0xFF334155).copy(alpha = 0.5f)
                                    else Color(0xFFF1F5F9)
                                )
                        ) {
                            Icon(
                                imageVector = when (state) {
                                    "NOT_STARTED" -> Icons.Default.Close
                                    "IN_PROGRESS" -> Icons.Default.Book
                                    else -> Icons.Default.Check
                                },
                                contentDescription = state,
                                tint = if (revisionStatus == state) Color.White else Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        // --- Double Layer Horizontal Tab row ---
        TabRow(
            selectedTabIndex = selectedSectionIndex,
            containerColor = Color.Transparent,
            contentColor = mColor,
            divider = {},
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedSectionIndex]),
                    color = mColor
                )
            },
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            sections.forEachIndexed { idx, section ->
                Tab(
                    selected = selectedSectionIndex == idx,
                    onClick = {
                        selectedSectionIndex = idx
                        // When transitioning to AI Quiz section, boot quiz sequence
                        if (section == "AI Quiz") {
                            viewModel.fetchQuizQuestions(module.name)
                        }
                    },
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Text(
                        text = section,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = if (selectedSectionIndex == idx) FontWeight.ExtraBold else FontWeight.Medium,
                            color = if (selectedSectionIndex == idx) mColor else Color.Gray
                        ),
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }
            }
        }

        // --- Section Content Views ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 20.dp)
        ) {
            when (selectedSectionIndex) {
                0 -> { // notes
                    NotesSectionList(
                        module = module,
                        onNoteSelect = { expandedNoteTopicTitle = it },
                        isDark = isDark
                    )
                }
                1 -> { // assignments
                    AssignmentsSectionList(
                        module = module,
                        viewModel = viewModel,
                        isDark = isDark
                    )
                }
                2 -> { // resources
                    ResourcesSectionList(
                        module = module,
                        isDark = isDark
                    )
                }
                3 -> { // AI quiz sequence
                    QuizSectionView(
                        module = module,
                        viewModel = viewModel,
                        isDark = isDark
                    )
                }
            }
        }
    }

    // Expanded Note Topic dialog box
    expandedNoteTopicTitle?.let { title ->
        val matchedTopic = module.coreTopics.find { it.title == title }
        if (matchedTopic != null) {
            AlertDialog(
                onDismissRequest = { expandedNoteTopicTitle = null },
                confirmButton = {
                    TextButton(onClick = { expandedNoteTopicTitle = null }) {
                        Text("Close Note", color = mColor)
                    }
                },
                shape = RoundedCornerShape(24.dp),
                containerColor = if (isDark) Color(0xFF1E293B) else Color.White,
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Lightbulb, contentDescription = null, tint = mColor)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = matchedTopic.title,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) Color.White else CyberDarkBg
                            )
                        )
                    }
                },
                text = {
                    Text(
                        text = matchedTopic.content,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = if (isDark) Color.LightGray else Color.DarkGray,
                            lineHeight = 22.sp
                        )
                    )
                }
            )
        }
    }
}

// Sub-components: 1. Notes list
@Composable
fun NotesSectionList(
    module: OfpptModule,
    onNoteSelect: (String) -> Unit,
    isDark: Boolean
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        items(module.coreTopics) { topic ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNoteSelect(topic.title) },
                shape = RoundedCornerShape(16.dp),
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
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = topic.title,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) Color.White else CyberDarkBg
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = topic.content,
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Launch,
                        contentDescription = "Expand note icon",
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

// Sub-component: 2. Assignments
@Composable
fun AssignmentsSectionList(
    module: OfpptModule,
    viewModel: StudyViewModel,
    isDark: Boolean
) {
    // Standard localized checkbox tracking
    var completedListState = remember { mutableStateMapOf<String, Boolean>() }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        items(module.defaultAssignments) { assignment ->
            val isDone = completedListState[assignment.title] ?: false

            Card(
                modifier = Modifier.fillMaxWidth(),
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
                    IconButton(
                        onClick = {
                            val nextState = !isDone
                            completedListState[assignment.title] = nextState
                            if (nextState) {
                                viewModel.addXp(assignment.points)
                            } else {
                                viewModel.addXp(-assignment.points)
                            }
                        },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = if (isDone) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
                            contentDescription = "Mark done",
                            tint = if (isDone) ElectricGreen else Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = assignment.title,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) Color.White else CyberDarkBg
                                )
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(TechTeal.copy(alpha = 0.15f))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "+${assignment.points} XP",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TechTeal,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = assignment.description,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = if (isDark) Color.LightGray else Color.DarkGray
                            )
                        )
                    }
                }
            }
        }
    }
}

// Sub-component: 3. Practice Resources with dynamic browser triggers
@Composable
fun ResourcesSectionList(
    module: OfpptModule,
    isDark: Boolean
) {
    val uriHandler = LocalUriHandler.current

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        items(module.studyResources) { resource ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        try {
                            uriHandler.openUri(resource.url)
                        } catch (e: Exception) {
                            // Suppressed
                        }
                    },
                shape = RoundedCornerShape(16.dp),
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
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    when (resource.type) {
                                        "DOC" -> TechTeal.copy(alpha = 0.15f)
                                        "VIDEO" -> AlarmRed.copy(alpha = 0.15f)
                                        else -> CyberPurple.copy(alpha = 0.15f)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (resource.type) {
                                    "DOC" -> Icons.Default.MenuBook
                                    "VIDEO" -> Icons.Default.PlayCircle
                                    else -> Icons.Default.Layers
                                },
                                contentDescription = null,
                                tint = when (resource.type) {
                                    "DOC" -> TechTeal
                                    "VIDEO" -> AlarmRed
                                    else -> CyberPurple
                                },
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = resource.title,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) Color.White else CyberDarkBg
                                )
                            )
                            Text(
                                text = when (resource.type) {
                                    "DOC" -> "Official Documentation Guide"
                                    "VIDEO" -> "Step-by-Step Video Walkthrough"
                                    else -> "Interactive Skill Tutorial"
                                },
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.Launch,
                        contentDescription = "Open resource Link",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

// Sub-component: 4. Beautiful AI smart Quiz section
@Composable
fun QuizSectionView(
    module: OfpptModule,
    viewModel: StudyViewModel,
    isDark: Boolean
) {
    val quizState by viewModel.aiQuizState.collectAsState()
    val currentIndex by viewModel.currentQuizIndex.collectAsState()
    val score by viewModel.quizScore.collectAsState()
    val selectedAnswer by viewModel.quizSelectedAnswer.collectAsState()
    val isAnswered by viewModel.quizIsAnswered.collectAsState()

    val mColor = remember(module.colorHex) {
        Color(android.graphics.Color.parseColor(module.colorHex))
    }

    when (val state = quizState) {
        is UiState.Idle -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Button(
                    onClick = { viewModel.fetchQuizQuestions(module.name) },
                    colors = ButtonDefaults.buttonColors(containerColor = mColor)
                ) {
                    Text("Initialize AI Practice Quiz")
                }
            }
        }
        is UiState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = mColor)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Generating Smart AI Quiz with Gemini...",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray, fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center
                )
            }
        }
        is UiState.Success -> {
            val questions = state.data
            if (questions.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Could not prepare question catalog. Try again.", color = Color.Gray)
                }
            } else {
                val currentQuestion = questions[currentIndex]

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    // Quiz progress pill
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Question ${currentIndex + 1} of ${questions.size}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isDark) Color.White else CyberDarkBg
                                )
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ElectricGreen.copy(alpha = 0.15f))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Score: $score",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = ElectricGreen,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                )
                            }
                        }
                    }

                    // Question Card
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isDark) Color(0xFF1E293B) else Color.White
                            ),
                            border = BorderStroke(
                                1.dp,
                                mColor.copy(alpha = 0.3f)
                            )
                        ) {
                            Text(
                                text = currentQuestion.question,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) Color.White else CyberDarkBg,
                                    lineHeight = 22.sp
                                ),
                                modifier = Modifier.padding(20.dp)
                            )
                        }
                    }

                    // Multiple-choice buttons
                    items(currentQuestion.options.size) { optIdx ->
                        val optionText = currentQuestion.options[optIdx]

                        val containerColor = when {
                            selectedAnswer == optIdx && optIdx == currentQuestion.correctAnswerIndex -> ElectricGreen.copy(alpha = 0.15f)
                            selectedAnswer == optIdx && optIdx != currentQuestion.correctAnswerIndex -> AlarmRed.copy(alpha = 0.15f)
                            isAnswered && optIdx == currentQuestion.correctAnswerIndex -> ElectricGreen.copy(alpha = 0.15f)
                            else -> if (isDark) CyberDarkCard else Color.White
                        }

                        val borderColor = when {
                            selectedAnswer == optIdx && optIdx == currentQuestion.correctAnswerIndex -> ElectricGreen
                            selectedAnswer == optIdx && optIdx != currentQuestion.correctAnswerIndex -> AlarmRed
                            isAnswered && optIdx == currentQuestion.correctAnswerIndex -> ElectricGreen
                            else -> if (isDark) CyberDarkBorder else CyberLightBorder
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(containerColor)
                                .border(1.2.dp, borderColor, RoundedCornerShape(16.dp))
                                .clickable {
                                    viewModel.selectQuizAnswer(optIdx, currentQuestion.correctAnswerIndex)
                                }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isAnswered && optIdx == currentQuestion.correctAnswerIndex -> ElectricGreen
                                            selectedAnswer == optIdx && optIdx != currentQuestion.correctAnswerIndex -> AlarmRed
                                            else -> if (isDark) Color(0xFF334155) else Color(0xFFF1F5F9)
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = when (optIdx) {
                                        0 -> "A"
                                        1 -> "B"
                                        2 -> "C"
                                        else -> "D"
                                    },
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isAnswered && (optIdx == currentQuestion.correctAnswerIndex || selectedAnswer == optIdx)) Color.White else Color.Gray
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Text(
                                text = optionText,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = if (isDark) Color.White else CyberDarkBg
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Explanation reveal
                    if (isAnswered) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isDark) Color(0xFF0F172A) else Color(0xFFF8FAFC)
                                ),
                                border = BorderStroke(
                                    1.dp,
                                    if (selectedAnswer == currentQuestion.correctAnswerIndex) ElectricGreen.copy(alpha = 0.4f) else AlarmRed.copy(alpha = 0.4f)
                                )
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = if (selectedAnswer == currentQuestion.correctAnswerIndex) "🎉 CORRECT!" else "❌ INCORRECT",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = if (selectedAnswer == currentQuestion.correctAnswerIndex) ElectricGreen else AlarmRed
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = currentQuestion.explanation,
                                        style = MaterialTheme.typography.bodySmall.copy(color = if (isDark) Color.LightGray else Color.DarkGray)
                                    )
                                }
                            }
                        }

                        // Next question or Final statistics
                        item {
                            Button(
                                onClick = {
                                    if (currentIndex < questions.size - 1) {
                                        viewModel.nextQuizQuestion(questions.size)
                                    } else {
                                        viewModel.resetQuizState()
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = mColor),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = if (currentIndex < questions.size - 1) "Proceed to Next Question" else "Finish Practice Quiz (+${score * 10} XP)",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }
            }
        }
        is UiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = state.message, color = Color.Gray, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.fetchQuizQuestions(module.name) }, colors = ButtonDefaults.buttonColors(containerColor = mColor)) {
                        Text("Retry AI Quiz Generation")
                    }
                }
            }
        }
    }
}
