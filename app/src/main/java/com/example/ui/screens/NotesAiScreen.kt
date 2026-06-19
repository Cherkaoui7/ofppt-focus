package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.foundation.text.selection.SelectionContainer
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Note
import com.example.ui.theme.*
import com.example.ui.viewmodel.StudyViewModel
import com.example.ui.viewmodel.UiState
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesAiScreen(
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    val notes by viewModel.filteredNotes.collectAsState()
    val searchQuery by viewModel.notesSearchQuery.collectAsState()
    val filterModule by viewModel.selectedFilterModule.collectAsState()
    val isDark by viewModel.isDarkMode.collectAsState()

    var activeTab by remember { mutableStateOf(0) } // 0 = Notes, 1 = AI Companion
    val tabs = listOf("Study Notepad", "Smart AI Companion")

    var isAddNoteDialogShown by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(if (isDark) CyberDarkBg else CyberLightBg)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(16.dp))

            // Main Title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Notes & AI Assistant",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isDark) Color.White else CyberDarkBg
                    )
                )

                // Quick Floating Add action for Notes in notepad tab
                if (activeTab == 0) {
                    IconButton(
                        onClick = { isAddNoteDialogShown = true },
                        modifier = Modifier
                            .size(44.dp)
                            .background(TechTeal, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Quick Add Note",
                            tint = Color.White
                        )
                    }
                }
            }

            Text(
                text = "Track your technical class logs, search records, or utilize Gemini AI to summarize and explain code concepts.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = if (isDark) Color.LightGray else Color.DarkGray
                ),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
            )

            // Dynamic Folder Tabs Row
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
                    NotepadSubPanel(
                        notes = notes,
                        searchQuery = searchQuery,
                        filterModule = filterModule,
                        onSearchChange = { viewModel.setSearchQuery(it) },
                        onFilterChange = { viewModel.setSelectedFilterModule(it) },
                        onDeleteNote = { viewModel.deleteNote(it) },
                        isDark = isDark
                    )
                } else {
                    AiAssistantSubPanel(
                        viewModel = viewModel,
                        isDark = isDark
                    )
                }
            }
        }
    }

    // --- Create Note Modal Dialog ---
    if (isAddNoteDialogShown) {
        var noteTitle by remember { mutableStateOf("") }
        var noteContent by remember { mutableStateOf("") }
        var selectedModule by remember { mutableStateOf("React") }

        val modulesList = listOf("React", "Laravel", "MongoDB", "Cloud", "UML", "Agile", "General")

        AlertDialog(
            onDismissRequest = { isAddNoteDialogShown = false },
            confirmButton = {
                Button(
                    onClick = {
                        if (noteTitle.isNotBlank() && noteContent.isNotBlank()) {
                            viewModel.addNote(
                                title = noteTitle,
                                content = noteContent,
                                moduleName = selectedModule
                            )
                            isAddNoteDialogShown = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TechTeal)
                ) {
                    Text("Save Note", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { isAddNoteDialogShown = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            shape = RoundedCornerShape(28.dp),
            containerColor = if (isDark) Color(0xFF1E293B) else Color.White,
            title = {
                Text("Create Study Log", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = noteTitle,
                        onValueChange = { noteTitle = it },
                        label = { Text("Note Title (e.g. Eloquent Seeders)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = noteContent,
                        onValueChange = { noteContent = it },
                        label = { Text("Note Details & Code snippets...") },
                        maxLines = 8,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Module connectivity chips
                    Column {
                        Text("CATEGORIZE BY MODULE", style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray, fontWeight = FontWeight.Bold))
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val rowModule = modulesList.take(4)
                            rowModule.forEach { m ->
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
                }
            }
        )
    }
}

// Sub-Panel 1: Notepad
@Composable
fun NotepadSubPanel(
    notes: List<Note>,
    searchQuery: String,
    filterModule: String,
    onSearchChange: (String) -> Unit,
    onFilterChange: (String) -> Unit,
    onDeleteNote: (Note) -> Unit,
    isDark: Boolean
) {
    val modulesList = listOf("All", "React", "Laravel", "MongoDB", "Cloud", "UML", "Agile", "General")

    Column(modifier = Modifier.fillMaxSize()) {
        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = { Text("Search title or content logs...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search notes") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = TechTeal,
                unfocusedBorderColor = if (isDark) CyberDarkBorder else CyberLightBorder
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Module Filter Row
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            items(modulesList) { m ->
                val active = filterModule == m
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (active) TechTeal.copy(alpha = 0.15f)
                            else if (isDark) Color(0xFF1E293B) else Color.White
                        )
                        .border(
                            width = 1.2.dp,
                            color = if (active) TechTeal else (if (isDark) CyberDarkBorder else CyberLightBorder),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { onFilterChange(m) }
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = m,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (active) TechTeal else Color.Gray
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Notes List
        if (notes.isEmpty()) {
            Column(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Outlined.NoteAlt, contentDescription = null, modifier = Modifier.size(56.dp), tint = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))
                Text("No Notes Found", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Text(
                    text = if (searchQuery.isNotEmpty()) "No content matched standard filters."
                    else "Keep customized lesson concepts filed. Add items using the top header button.",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 32.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(notes, key = { it.id }) { note ->
                    NoteCardRow(
                        note = note,
                        onDelete = { onDeleteNote(note) },
                        isDark = isDark
                    )
                }
            }
        }
    }
}

// Sub-Panel 2: AI Assistant
@Composable
fun AiAssistantSubPanel(
    viewModel: StudyViewModel,
    isDark: Boolean
) {
    var aiActiveMode by remember { mutableStateOf(0) } // 0 = Summarize, 1 = Explain Term
    val modes = listOf("Summarize Lesson", "Explain Concept")

    // Input fields holds
    var summarizeInputText by remember { mutableStateOf("") }
    var explainInputText by remember { mutableStateOf("") }

    val summaryState by viewModel.aiSummaryState.collectAsState()
    val explanationState by viewModel.aiExplanationState.collectAsState()

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Mode Selector segment row
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            modes.forEachIndexed { i, m ->
                val active = aiActiveMode == i
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            if (active) TechTeal
                            else if (isDark) CyberDarkCard else Color.White
                        )
                        .border(
                            1.dp,
                            if (isDark) CyberDarkBorder else CyberLightBorder,
                            RoundedCornerShape(14.dp)
                        )
                        .clickable { aiActiveMode = i }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        m,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (active) Color.White else Color.Gray
                        )
                    )
                }
            }
        }

        if (aiActiveMode == 0) {
            // --- SUMMARY PANEL ---
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    "LESSON SUMMARIZER",
                    style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                )

                OutlinedTextField(
                    value = summarizeInputText,
                    onValueChange = { summarizeInputText = it },
                    placeholder = { Text("Paste comprehensive textbook chapters, code blogs, or unorganized class notes here for a sleek outline...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp),
                    shape = RoundedCornerShape(16.dp),
                    maxLines = 10,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TechTeal,
                        unfocusedBorderColor = if (isDark) CyberDarkBorder else CyberLightBorder
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { viewModel.summarizeLesson(summarizeInputText) },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("ai_summarize_button"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TechTeal)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Psychology, contentDescription = null, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Summarize with AI", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                        }
                    }

                    if (summarizeInputText.isNotEmpty()) {
                        OutlinedButton(
                            onClick = {
                                summarizeInputText = ""
                                viewModel.resetAiSummaryState()
                            },
                            modifier = Modifier.height(50.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray)
                        ) {
                            Text("Clear")
                        }
                    }
                }

                // Response Block
                ResponseVisualBlock(
                    state = summaryState,
                    onCopyToClipboard = { txt ->
                        copyToClipboard(context, txt)
                    },
                    isDark = isDark
                )
            }
        } else {
            // --- EXPLAIN CONCEPT PANEL ---
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    "EXPLAIN REVISION TERM",
                    style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                )

                OutlinedTextField(
                    value = explainInputText,
                    onValueChange = { explainInputText = it },
                    placeholder = { Text("e.g., MVC Design Pattern, Async/Await list, NoSQL sharding index...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TechTeal,
                        unfocusedBorderColor = if (isDark) CyberDarkBorder else CyberLightBorder
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { viewModel.explainConcept(explainInputText) },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("ai_explain_button"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TechTeal)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Explain Concept", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                        }
                    }

                    if (explainInputText.isNotEmpty()) {
                        OutlinedButton(
                            onClick = {
                                explainInputText = ""
                                viewModel.resetAiExplanationState()
                            },
                            modifier = Modifier.height(50.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray)
                        ) {
                            Text("Clear")
                        }
                    }
                }

                // Response Block
                ResponseVisualBlock(
                    state = explanationState,
                    onCopyToClipboard = { txt ->
                        copyToClipboard(context, txt)
                    },
                    isDark = isDark
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun ResponseVisualBlock(
    state: UiState<String>,
    onCopyToClipboard: (String) -> Unit,
    isDark: Boolean
) {
    AnimatedVisibility(
        visible = state !is UiState.Idle,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDark) Color(0xFF1E293B) else Color.White
            ),
            border = BorderStroke(
                1.dp,
                if (isDark) CyberDarkBorder else CyberLightBorder
            )
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(TechTeal.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoMode,
                                contentDescription = null,
                                tint = TechTeal,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Gemini AI Output",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) Color.White else CyberDarkBg
                            )
                        )
                    }

                    if (state is UiState.Success) {
                        IconButton(
                            onClick = { onCopyToClipboard(state.data) },
                            modifier = Modifier.minimumInteractiveComponentSize()
                        ) {
                            Icon(Icons.Filled.ContentCopy, contentDescription = "Copy text", tint = Color.Gray)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                when (state) {
                    is UiState.Loading -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
                        ) {
                            CircularProgressIndicator(strokeWidth = 3.dp, modifier = Modifier.size(20.dp), color = TechTeal)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "AI is drafting comprehensive study notes...",
                                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                            )
                        }
                    }
                    is UiState.Error -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = AlarmRed)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = state.message,
                                style = MaterialTheme.typography.bodyMedium.copy(color = AlarmRed)
                            )
                        }
                    }
                    is UiState.Success -> {
                        // Render response text neatly (styled nicely fallback scroll)
                        SelectionContainer {
                            Text(
                                text = state.data,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = if (isDark) Color(0xFFE2E8F0) else Color(0xFF1E293B),
                                    lineHeight = 22.sp,
                                    fontFamily = FontFamily.SansSerif
                                )
                            )
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}

@Composable
fun NoteCardRow(
    note: Note,
    onDelete: () -> Unit,
    isDark: Boolean
) {
    val dateText = remember(note.timestamp) {
        val sdf = SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
        sdf.format(java.util.Date(note.timestamp))
    }

    var isNoteDialogExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isNoteDialogExpanded = true },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) CyberDarkCard else Color.White
        ),
        border = BorderStroke(
            1.dp,
            if (isDark) CyberDarkBorder else CyberLightBorder
        )
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(TechTeal.copy(alpha = 0.12f))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = note.moduleName.uppercase(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TechTeal,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = dateText,
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete personal study note",
                        tint = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isDark) Color.White else CyberDarkBg
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = if (isDark) Color.LightGray else Color.DarkGray
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }

    // Detail expanded modal for notes
    if (isNoteDialogExpanded) {
        AlertDialog(
            onDismissRequest = { isNoteDialogExpanded = false },
            confirmButton = {
                TextButton(onClick = { isNoteDialogExpanded = false }) {
                    Text("Close note", color = TechTeal)
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = if (isDark) Color(0xFF1E293B) else Color.White,
            title = {
                Column {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(TechTeal.copy(alpha = 0.15f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = note.moduleName.uppercase(),
                            style = MaterialTheme.typography.bodySmall.copy(color = TechTeal, fontWeight = FontWeight.Bold)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = note.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) Color.White else CyberDarkBg
                        )
                    )
                }
            },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text(
                        text = note.content,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = if (isDark) Color.LightGray else Color.DarkGray,
                            lineHeight = 22.sp
                        )
                    )
                }
            }
        )
    }
}

private fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Gemini Study Assistant Response", text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, "Lesson summary copied to clipboard!", Toast.LENGTH_SHORT).show()
}
