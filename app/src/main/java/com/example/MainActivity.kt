package com.example

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SettingsBackupRestore
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.AppDatabase
import com.example.data.CgpaRecord
import com.example.data.GpaRecord
import com.example.data.GradeCalculation
import com.example.data.RecordRepository
import com.example.ui.CalculatorViewModel
import com.example.ui.NotesTabScreen
import com.example.utils.Exporter
import kotlinx.coroutines.launch
import java.util.Locale

// Custom design colors
val ResultifyPrimary = Color(0xFF0C50B5)
val ResultifySecondary = Color(0xFF4C8DFF)
val ResultifyBackgroundLight = Color(0xFFF5F7FA)
val ResultifyBackgroundDark = Color(0xFF121212)
val ResultifySurfaceLight = Color(0xFFFFFFFF)
val ResultifySurfaceDark = Color(0xFF1E1E1E)

private val LightColorSchemeCustom = lightColorScheme(
    primary = ResultifyPrimary,
    secondary = ResultifySecondary,
    background = ResultifyBackgroundLight,
    surface = ResultifySurfaceLight,
    onPrimary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F)
)

private val DarkColorSchemeCustom = darkColorScheme(
    primary = Color(0xFFFF8F00),
    secondary = Color(0xFFFF8F00),
    background = ResultifyBackgroundDark,
    surface = ResultifySurfaceDark,
    onPrimary = Color.Black,
    onBackground = Color(0xFFE3E2E6),
    onSurface = Color(0xFFE3E2E6)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            val context = LocalContext.current
            val db = remember { AppDatabase.getDatabase(context) }
            val repository = remember { RecordRepository(db.recordDao()) }
            val app = context.applicationContext as Application
            
            // Instantiate ViewModel manually to bypass generic factory setup
            val viewModel: CalculatorViewModel = viewModel {
                CalculatorViewModel(app, repository)
            }
            
            val isDark by viewModel.enableDarkMode.collectAsState()

            MaterialTheme(
                colorScheme = if (isDark) DarkColorSchemeCustom else LightColorSchemeCustom
            ) {
                var currentScreen by remember { mutableStateOf("main") } // "main", "info", "settings", "gpa_result", "cgpa_result"

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    when (currentScreen) {
                        "info" -> InfoScreen(
                            onBack = { currentScreen = "main" }
                        )
                        "settings" -> SettingsScreen(
                            viewModel = viewModel,
                            onBack = { currentScreen = "main" },
                            onGoToInfo = { currentScreen = "info" }
                        )
                        "gpa_result" -> GpaResultScreen(
                            viewModel = viewModel,
                            onBack = { currentScreen = "main" }
                        )
                        "cgpa_result" -> CgpaResultScreen(
                            viewModel = viewModel,
                            onBack = { currentScreen = "main" }
                        )
                        else -> MainDashboard(
                            viewModel = viewModel,
                            onNavigateToInfo = { currentScreen = "info" },
                            onNavigateToSettings = { currentScreen = "settings" },
                            onNavigateToGpaResult = { currentScreen = "gpa_result" },
                            onNavigateToCgpaResult = { currentScreen = "cgpa_result" }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MainDashboard(
    viewModel: CalculatorViewModel,
    onNavigateToInfo: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToGpaResult: () -> Unit,
    onNavigateToCgpaResult: () -> Unit
) {
    var activeTab by remember { mutableIntStateOf(0) } // 0 = Calculator, 1 = Records
    var subTab by remember { mutableIntStateOf(0) } // 0 = GPA, 1 = CGPA
    val isDark by viewModel.enableDarkMode.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Resultify",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = FontFamily.SansSerif
                    )
                    Row {
                        IconButton(onClick = { viewModel.setDarkMode(!isDark) }) {
                            Icon(
                                imageVector = if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Toggle Theme",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(onClick = onNavigateToSettings) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(onClick = onNavigateToInfo) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "About App",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier.navigationBarsPadding(),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = activeTab == 0,
                    onClick = { activeTab = 0 },
                    icon = { Icon(Icons.Default.Calculate, contentDescription = "Calculator") },
                    label = { Text("Calculator") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.testTag("calculator_tab")
                )
                NavigationBarItem(
                    selected = activeTab == 1,
                    onClick = { activeTab = 1 },
                    icon = { Icon(Icons.Default.List, contentDescription = "Records") },
                    label = { Text("Records") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.testTag("records_tab")
                )
                NavigationBarItem(
                    selected = activeTab == 2,
                    onClick = { activeTab = 2 },
                    icon = { Icon(Icons.Default.EditNote, contentDescription = "Keep in Mind") },
                    label = { Text("Keep in Mind", maxLines = 1, overflow = TextOverflow.Ellipsis) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.testTag("notes_tab")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (activeTab) {
                0 -> CalculatorTabScreen(
                    viewModel = viewModel,
                    subTab = subTab,
                    onSubTabChanged = { subTab = it },
                    onNavigateToGpaResult = onNavigateToGpaResult,
                    onNavigateToCgpaResult = onNavigateToCgpaResult
                )
                1 -> RecordsTabScreen(
                    viewModel = viewModel,
                    onEditGpa = { gpaRecord ->
                        viewModel.prefillGpaRecord(gpaRecord)
                        activeTab = 0
                        subTab = 0
                    },
                    onEditCgpa = { cgpaRecord ->
                        viewModel.prefillCgpaRecord(cgpaRecord)
                        activeTab = 0
                        subTab = 1
                    }
                )
                else -> NotesTabScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun CalculatorTabScreen(
    viewModel: CalculatorViewModel,
    subTab: Int,
    onSubTabChanged: (Int) -> Unit,
    onNavigateToGpaResult: () -> Unit,
    onNavigateToCgpaResult: () -> Unit
) {
    val pagerState = rememberPagerState(initialPage = subTab, pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(subTab) {
        if (pagerState.currentPage != subTab) {
            pagerState.animateScrollToPage(subTab)
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        onSubTabChanged(pagerState.currentPage)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                    color = MaterialTheme.colorScheme.primary,
                    height = 3.dp
                )
            }
        ) {
            Tab(
                selected = pagerState.currentPage == 0,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(0)
                    }
                },
                text = {
                    Text(
                        "GPA",
                        fontWeight = if (pagerState.currentPage == 0) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 16.sp
                    )
                },
                modifier = Modifier.testTag("gpa_sub_tab")
            )
            Tab(
                selected = pagerState.currentPage == 1,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                },
                text = {
                    Text(
                        "CGPA",
                        fontWeight = if (pagerState.currentPage == 1) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 16.sp
                    )
                },
                modifier = Modifier.testTag("cgpa_sub_tab")
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            if (page == 0) {
                GpaCalculatorScreen(viewModel, onNavigateToGpaResult)
            } else {
                CgpaCalculatorScreen(viewModel, onNavigateToCgpaResult)
            }
        }
    }
}

@Composable
fun GpaCalculatorScreen(
    viewModel: CalculatorViewModel,
    onNavigateToGpaResult: () -> Unit
) {
    val context = LocalContext.current
    val numSubjects by viewModel.numberOfSubjects.collectAsState()
    var dropdownExpanded by remember { mutableStateOf(false) }
    val subjectsList = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Number of Subjects",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Box {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                            .clickable(
                                indication = null,
                                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                            ) { dropdownExpanded = true }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .testTag("choose_subjects_dropdown"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (numSubjects == 0) "Choose" else numSubjects.toString(),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = if (dropdownExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Dropdown Arrow",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false }
                    ) {
                        subjectsList.forEach { num ->
                            DropdownMenuItem(
                                text = { Text(num.toString(), fontWeight = FontWeight.Medium) },
                                onClick = {
                                    viewModel.setNumberOfSubjects(num)
                                    dropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (numSubjects > 0) {
            viewModel.courseInputs.forEachIndexed { index, course ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Course Index label
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (index + 1).toString(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }

                        // Obtained Marks Input
                        val marksVal = course.obtainedMarks.toDoubleOrNull()
                        val isObtError = course.obtainedMarks.isNotEmpty() && (marksVal == null || marksVal < 0 || marksVal > course.maxMarks)

                        OutlinedTextField(
                            value = course.obtainedMarks,
                            onValueChange = { valStr ->
                                val filtered = valStr.filterIndexed { idx, char ->
                                    char.isDigit() || (char == '.' && valStr.indexOf('.') == idx)
                                }
                                viewModel.updateCourseObtained(index, filtered)
                            },
                            label = { Text("Obt. Marks", fontSize = 12.sp) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            isError = isObtError,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(max = 62.dp)
                                .testTag("obt_marks_input_$index")
                        )

                        // Max Marks Dropdown Select
                        var maxMarksDropdownExpanded by remember { mutableStateOf(false) }
                        Box(modifier = Modifier.weight(1.2f)) {
                            OutlinedTextField(
                                value = "${course.maxMarks / 20}",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Credit Hours", fontSize = 12.sp) },
                                trailingIcon = {
                                    IconButton(onClick = { maxMarksDropdownExpanded = true }) {
                                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Open Max Marks Menu")
                                    }
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                                ),
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 62.dp)
                                    .clickable { maxMarksDropdownExpanded = true }
                                    .testTag("max_marks_select_$index")
                                    // Custom visual container style matching dynamic colors
                            )

                            DropdownMenu(
                                expanded = maxMarksDropdownExpanded,
                                onDismissRequest = { maxMarksDropdownExpanded = false }
                            ) {
                                listOf(20, 40, 60, 80).forEach { max ->
                                    DropdownMenuItem(
                                        text = { Text("${max / 20}") },
                                        onClick = {
                                            viewModel.updateCourseMax(index, max)
                                            maxMarksDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        IconButton(
                            onClick = { viewModel.removeCourseInput(index) },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Delete subject",
                                tint = Color(0xFFD32F2F)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = { viewModel.addCourseInput() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Subject Icon")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Subject Field", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val ok = viewModel.calculateGPA()
                    if (ok) {
                        onNavigateToGpaResult()
                    } else {
                        Toast.makeText(
                            context,
                            "Validation Error! Check all marks. They must be between 0 and maximum total.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("calculate_gpa_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Calculate GPA", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun CgpaCalculatorScreen(
    viewModel: CalculatorViewModel,
    onNavigateToCgpaResult: () -> Unit
) {
    val context = LocalContext.current
    val numSemesters by viewModel.numberOfSemesters.collectAsState()
    var dropdownExpanded by remember { mutableStateOf(false) }
    val semestersRange = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Number of Semesters",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Box {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                            .clickable(
                                indication = null,
                                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                            ) { dropdownExpanded = true }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .testTag("choose_semesters_dropdown"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (numSemesters == 0) "Choose" else numSemesters.toString(),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = if (dropdownExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Dropdown Arrow",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false }
                    ) {
                        semestersRange.forEach { num ->
                            DropdownMenuItem(
                                text = { Text(num.toString(), fontWeight = FontWeight.Medium) },
                                onClick = {
                                    viewModel.setNumberOfSemesters(num)
                                    dropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (numSemesters > 0) {
            viewModel.semesterInputs.forEachIndexed { index, sem ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Sem index label
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (index + 1).toString(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }

                        // Semester GPA text input
                        val gpaVal = sem.gpa.toDoubleOrNull()
                        val isGpaError = sem.gpa.isNotEmpty() && (gpaVal == null || gpaVal < 0.0 || gpaVal > 4.0)

                        OutlinedTextField(
                            value = sem.gpa,
                            onValueChange = { valStr ->
                                val filtered = valStr.filterIndexed { idx, char ->
                                    char.isDigit() || (char == '.' && valStr.indexOf('.') == idx)
                                }
                                viewModel.updateSemesterGpa(index, filtered)
                            },
                            label = { Text("Sem GPA", fontSize = 12.sp) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            isError = isGpaError,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(max = 62.dp)
                                .testTag("sem_gpa_input_$index")
                        )

                        // Credits input
                        val isCreditsError = sem.credits <= 0

                        OutlinedTextField(
                            value = if (sem.credits == 0) "" else sem.credits.toString(),
                            onValueChange = { valStr ->
                                val filtered = valStr.filter { it.isDigit() }
                                viewModel.updateSemesterCredits(index, filtered.toIntOrNull() ?: 0)
                            },
                            label = { Text("Total Credits", fontSize = 12.sp) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            isError = isCreditsError,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(max = 62.dp)
                                .testTag("sem_credits_input_$index")
                        )

                        // Delete button (Red cross)
                        IconButton(
                            onClick = { viewModel.removeSemesterInput(index) },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Delete semester",
                                tint = Color(0xFFD32F2F)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = { viewModel.addSemesterInput() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Semester Icon")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Semester Field", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val ok = viewModel.calculateCGPA()
                    if (ok) {
                        onNavigateToCgpaResult()
                    } else {
                        Toast.makeText(
                            context,
                            "Validation Error! All Semester GPAs must be between 0.00 and 4.00, and credits greater than 0.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("calculate_cgpa_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Calculate CGPA", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun RecordsTabScreen(
    viewModel: CalculatorViewModel,
    onEditGpa: (GpaRecord) -> Unit,
    onEditCgpa: (CgpaRecord) -> Unit
) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                    color = MaterialTheme.colorScheme.primary,
                    height = 3.dp
                )
            }
        ) {
            Tab(
                selected = pagerState.currentPage == 0,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(0)
                    }
                },
                text = {
                    Text(
                        "GPA Records",
                        fontWeight = if (pagerState.currentPage == 0) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 15.sp
                    )
                },
                modifier = Modifier.testTag("gpa_records_tab")
            )
            Tab(
                selected = pagerState.currentPage == 1,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                },
                text = {
                    Text(
                        "CGPA Records",
                        fontWeight = if (pagerState.currentPage == 1) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 15.sp
                    )
                },
                modifier = Modifier.testTag("cgpa_records_tab")
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            if (page == 0) {
                GpaRecordsList(viewModel, onEditGpa)
            } else {
                CgpaRecordsList(viewModel, onEditCgpa)
            }
        }
    }
}

@Composable
fun GpaRecordsList(viewModel: CalculatorViewModel, onEditGpa: (GpaRecord) -> Unit) {
    val context = LocalContext.current
    val gpaRecordsList by viewModel.gpaRecords.collectAsState()

    var recordToExport by remember { mutableStateOf<GpaRecord?>(null) }

    val createPdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf"),
        onResult = { uri ->
            val record = recordToExport
            if (uri != null && record != null) {
                val success = Exporter.exportGpaToPdf(context, uri, record)
                if (success) {
                    Toast.makeText(context, "PDF Report exported successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to export PDF!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    )

    val createPngLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("image/png"),
        onResult = { uri ->
            val record = recordToExport
            if (uri != null && record != null) {
                val success = Exporter.exportGpaToPng(context, uri, record)
                if (success) {
                    Toast.makeText(context, "PNG Report exported successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to export PNG!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    )

    if (gpaRecordsList.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = Color.LightGray
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "No GPA Records Saved Yet",
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    fontSize = 16.sp
                )
                Text(
                    text = "Run calculation on GPA tab and press 'Record'",
                    color = Color.LightGray,
                    fontSize = 14.sp
                )
            }
        }
    } else {
        var expandedCardId by remember { mutableStateOf<Int?>(null) }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            itemsIndexed(gpaRecordsList) { _, record ->
                val isExpanded = expandedCardId == record.id
                val dateStr = remember(record.timestamp) {
                    java.text.SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(java.util.Date(record.timestamp))
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            expandedCardId = if (isExpanded) null else record.id
                        },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = record.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = if (isExpanded) 2 else 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            Row {
                                IconButton(onClick = { onEditGpa(record) }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit record",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                                IconButton(onClick = { viewModel.deleteGpaRecord(record.id) }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete record",
                                        tint = Color(0xFFD32F2F)
                                    )
                                }
                                IconButton(onClick = {
                                    expandedCardId = if (isExpanded) null else record.id
                                }) {
                                    Icon(
                                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Expand info"
                                    )
                                }
                            }
                        }

                        // Slide down to show saved GPA details on click
                        AnimatedVisibility(
                            visible = isExpanded,
                            enter = expandVertically(
                                animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                                expandFrom = Alignment.Top
                            ) + fadeIn(),
                            exit = shrinkVertically(
                                animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                                shrinkTowards = Alignment.Top
                            ) + fadeOut()
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = dateStr,
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    "GPA Record Detail",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                // GPA detailed table
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("S#", modifier = Modifier.weight(0.4f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text("Ob.marks", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text("T.marks", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text("QP", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text("Grade", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }

                                record.getCourses().forEachIndexed { i, course ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text((i + 1).toString(), modifier = Modifier.weight(0.4f), fontSize = 12.sp)
                                        Text(String.format(Locale.getDefault(), "%.1f", course.obtainedMarks), modifier = Modifier.weight(1f), fontSize = 12.sp)
                                        Text(course.maxMarks.toString(), modifier = Modifier.weight(1f), fontSize = 12.sp)
                                        Text(String.format(Locale.getDefault(), "%.2f", course.qualityPoints), modifier = Modifier.weight(1f), fontSize = 12.sp)
                                        Text(course.grade, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    }
                                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
                                }

                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = String.format(Locale.getDefault(), "FINAL GPA: %.2f", record.gpa),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            recordToExport = record
                                            createPngLauncher.launch("GPA_Result_${record.name}.png")
                                        },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Download PNG", fontSize = 12.sp)
                                    }
                                    OutlinedButton(
                                        onClick = {
                                            recordToExport = record
                                            createPdfLauncher.launch("GPA_Report_${record.name}.pdf")
                                        },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Download PDF", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CgpaRecordsList(viewModel: CalculatorViewModel, onEditCgpa: (CgpaRecord) -> Unit) {
    val context = LocalContext.current
    val cgpaRecordsList by viewModel.cgpaRecords.collectAsState()

    var recordToExport by remember { mutableStateOf<CgpaRecord?>(null) }

    val createPdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf"),
        onResult = { uri ->
            val record = recordToExport
            if (uri != null && record != null) {
                val success = Exporter.exportCgpaToPdf(context, uri, record)
                if (success) {
                    Toast.makeText(context, "PDF Report exported successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to export PDF!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    )

    val createPngLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("image/png"),
        onResult = { uri ->
            val record = recordToExport
            if (uri != null && record != null) {
                val success = Exporter.exportCgpaToPng(context, uri, record)
                if (success) {
                    Toast.makeText(context, "PNG Report exported successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to export PNG!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    )

    if (cgpaRecordsList.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = Color.LightGray
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "No CGPA Records Saved Yet",
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    fontSize = 16.sp
                )
                Text(
                    text = "Calculate CGPA inside Calculator tab and record it",
                    color = Color.LightGray,
                    fontSize = 14.sp
                )
            }
        }
    } else {
        var expandedCardId by remember { mutableStateOf<Int?>(null) }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            itemsIndexed(cgpaRecordsList) { _, record ->
                val isExpanded = expandedCardId == record.id
                val dateStr = remember(record.timestamp) {
                    java.text.SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(java.util.Date(record.timestamp))
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            expandedCardId = if (isExpanded) null else record.id
                        },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = record.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = if (isExpanded) 2 else 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            Row {
                                IconButton(onClick = { onEditCgpa(record) }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit record",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                                IconButton(onClick = { viewModel.deleteCgpaRecord(record.id) }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete record",
                                        tint = Color(0xFFD32F2F)
                                    )
                                }
                                IconButton(onClick = {
                                    expandedCardId = if (isExpanded) null else record.id
                                }) {
                                    Icon(
                                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Expand info"
                                    )
                                }
                            }
                        }

                        // Slide down to show saved CGPA details on click
                        AnimatedVisibility(
                            visible = isExpanded,
                            enter = expandVertically(
                                animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                                expandFrom = Alignment.Top
                            ) + fadeIn(),
                            exit = shrinkVertically(
                                animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                                shrinkTowards = Alignment.Top
                            ) + fadeOut()
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = dateStr,
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    "CGPA Record Detail",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                // CGPA table columns
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Sem #", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text("GPA", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text("Credits", modifier = Modifier.weight(1.2f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }

                                record.getSemesters().forEachIndexed { index, sem ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Sem ${index + 1}", modifier = Modifier.weight(1f), fontSize = 12.sp)
                                        Text(String.format(Locale.getDefault(), "%.2f", sem.first), modifier = Modifier.weight(1f), fontSize = 12.sp)
                                        Text(sem.second.toString(), modifier = Modifier.weight(1.2f), fontSize = 12.sp)
                                    }
                                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
                                }

                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = String.format(Locale.getDefault(), "FINAL CGPA: %.2f", record.cgpa),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            recordToExport = record
                                            createPngLauncher.launch("CGPA_Result_${record.name}.png")
                                        },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Download PNG", fontSize = 12.sp)
                                    }
                                    OutlinedButton(
                                        onClick = {
                                            recordToExport = record
                                            createPdfLauncher.launch("CGPA_Report_${record.name}.pdf")
                                        },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Download PDF", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IntroductionScreen(
    viewModel: CalculatorViewModel,
    onFinish: () -> Unit
) {
    val context = LocalContext.current
    val isDark by viewModel.enableDarkMode.collectAsState()
    val skipIntroSet by viewModel.skipIntroduction.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        
        // Brand logo
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(ResultifyPrimary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.SettingsBackupRestore,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(54.dp)
            )
        }

        Text(
            text = "Resultify",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Set up your workspace and choose default preferences before calculating grades.",
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Preference Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Enable Dark Mode Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Enable Dark mode",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                        Text(
                            "Switches app to comfortable dark colors",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    Switch(
                        checked = isDark,
                        onCheckedChange = { viewModel.setDarkMode(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier.testTag("onboarding_dark_mode_switch")
                    )
                }

                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

                // Skip Introduction Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Skip Introduction",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                        Text(
                            "Skip this configuration screen on startup",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    Switch(
                        checked = skipIntroSet,
                        onCheckedChange = { viewModel.setSkipIntroduction(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier.testTag("onboarding_skip_intro_switch")
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onFinish,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("onboarding_continue_btn"),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Continue", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Text(
            text = "Developed by Saif Ur Rehman",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@Composable
fun InfoScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var ratingStars by remember { mutableIntStateOf(5) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        // App bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go back")
            }
            Text(
                "Resultify Info",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Brand icon logo
            Image(
                painter = painterResource(id = R.drawable.ic_result_foreground),
                contentDescription = "Resultify Logo",
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )

            Text(
                "Resultify",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Resultify is a simple to use tool which calculates GPA/CGPA of semester(s). You don't have to go through all the calculations to calculate your GPA/CGPA and quality points of all subjects individually. This simple tool will help you get through easily...",
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                textAlign = TextAlign.Justify,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            // Rating Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Please take a moment to rate us",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        for (star in 1..5) {
                            Icon(
                                imageVector = if (star <= ratingStars) Icons.Default.Star else Icons.Default.StarOutline,
                                contentDescription = "Star $star",
                                tint = if (star <= ratingStars) Color(0xFF0C50B5) else Color.LightGray,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clickable {
                                        ratingStars = star
                                        Toast.makeText(context, "Thank you for rating us $star stars!", Toast.LENGTH_SHORT).show()
                                    }
                            )
                        }
                    }
                }
            }

            // Restore / Reset card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                "Restore default",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                "App may reboot",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                    Button(
                        onClick = {
                            Toast.makeText(context, "App preferences restored to default!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Restore")
                    }
                }
            }

            // Share Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                "Share",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                "Share app with friends",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                    Button(
                        onClick = {
                            val shareIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "Calculate your GPA/CGPA with official GCUF regulations using Resultify grade tracker!")
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Resultify App"))
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Share")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Developer Profile Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_developer_avatar),
                        contentDescription = "Developer Profile Picture",
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(32.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Saif Ur Rehman",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Full Stack Developer",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Experienced in building clean, intuitive, and modern mobile and web applications with robust architectures.",
                            fontSize = 11.sp,
                            lineHeight = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Developed by Saif Ur Rehman",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}

@Composable
fun SettingsScreen(
    viewModel: CalculatorViewModel,
    onBack: () -> Unit,
    onGoToInfo: () -> Unit
) {
    val context = LocalContext.current
    var showResetDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        // App bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go back")
            }
            Text(
                "Settings",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Main configuration options Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // App version row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "App Version",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )
                        }
                        Text(
                            "1.0.4 (Stable)",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }

                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

                    // Data security row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "Data Security",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )
                        }
                        Text(
                            "Encrypted",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }

                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

                    // Reset app data row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showResetDialog = true }
                            .padding(vertical = 12.dp)
                            .testTag("reset_data_row"),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = null,
                                tint = Color(0xFFD32F2F),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "Reset App Data",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color(0xFFD32F2F)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Developed by Saif Ur Rehman",
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .clickable { onGoToInfo() }
            )
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset All Data?", fontWeight = FontWeight.Bold, color = Color(0xFFD32F2F)) },
            text = { Text("This will permanently clear all saved GPA/CGPA records and Keep in Mind notes from local storage. This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetAppData()
                        showResetDialog = false
                        Toast.makeText(context, "All local user data has been cleared!", Toast.LENGTH_LONG).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                    modifier = Modifier.testTag("confirm_reset_btn")
                ) {
                    Text("Clear Everything")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showResetDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

fun getGpaColor(gpa: Double): Color {
    return when {
        gpa >= 3.5 -> Color(0xFF2E7D32) // Lush green for Excellent
        gpa >= 3.0 -> Color(0xFF4CAF50) // Green for Good
        gpa >= 2.5 -> Color(0xFFFBC02D) // Gold/Yellow for Average
        gpa >= 2.0 -> Color(0xFFF57C00) // Orange for Below Average
        else -> Color(0xFFD32F2F) // Red for Poor
    }
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun GpaResultScreen(viewModel: CalculatorViewModel, onBack: () -> Unit) {
    val context = LocalContext.current
    val gpaResult by viewModel.calculatedGpaResult.collectAsState()
    val calculatedCourses by viewModel.calculatedCourses.collectAsState()

    var showSaveDialog by remember { mutableStateOf(false) }
    var recordName by remember { mutableStateOf("") }
    var selectedRecordToExport by remember { mutableStateOf<GpaRecord?>(null) }

    val createPdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf"),
        onResult = { uri ->
            val record = selectedRecordToExport
            if (uri != null && record != null) {
                val success = Exporter.exportGpaToPdf(context, uri, record)
                if (success) {
                    Toast.makeText(context, "PDF Exported Successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "PDF Export Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    )

    val createPngLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("image/png"),
        onResult = { uri ->
            val record = selectedRecordToExport
            if (uri != null && record != null) {
                val success = Exporter.exportGpaToPng(context, uri, record)
                if (success) {
                    Toast.makeText(context, "PNG Exported Successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "PNG Export Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    )

    val resultGpa = gpaResult ?: 0.0

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GPA Result", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Results Details Table
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Table Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("#", modifier = Modifier.weight(0.4f), fontWeight = FontWeight.Bold)
                        Text("Obt", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                        Text("Max", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                        Text("QP", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                        Text("Grade", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                        Text("%", modifier = Modifier.weight(0.8f), fontWeight = FontWeight.Bold)
                    }

                    calculatedCourses.forEachIndexed { i, r ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text((i + 1).toString(), modifier = Modifier.weight(0.4f))
                            Text(String.format(Locale.getDefault(), "%.1f", r.obtainedMarks), modifier = Modifier.weight(1f))
                            Text(r.maxMarks.toString(), modifier = Modifier.weight(1f))
                            Text(String.format(Locale.getDefault(), "%.2f", r.qualityPoints), modifier = Modifier.weight(1f))
                            Text(r.grade, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Text("${r.percentage}%", modifier = Modifier.weight(0.8f))
                        }
                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // GPA Score Box
            val scoreColor = getGpaColor(resultGpa)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                border = BorderStroke(2.dp, scoreColor)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "YOUR CALCULATED GPA",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = scoreColor.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(androidx.compose.foundation.shape.CircleShape)
                            .background(scoreColor.copy(alpha = 0.1f))
                            .border(2.dp, scoreColor, androidx.compose.foundation.shape.CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = String.format(Locale.getDefault(), "%.2f", resultGpa),
                            fontSize = 36.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = scoreColor
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Accurate GCUF Grade Point Table standard",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Actions Block
            Button(
                onClick = { showSaveDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("record_gpa_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Save Record", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Export Actions
            OutlinedButton(
                onClick = {
                    selectedRecordToExport = GpaRecord(
                        name = "GPA Academic Result",
                        timestamp = System.currentTimeMillis(),
                        gpa = resultGpa,
                        coursesData = GpaRecord.serializeCourses(calculatedCourses)
                    )
                    createPngLauncher.launch("GPA_Result_${System.currentTimeMillis()}.png")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("download_png_gpa"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Export as Image (PNG)", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = {
                    selectedRecordToExport = GpaRecord(
                        name = "GPA Academic Result",
                        timestamp = System.currentTimeMillis(),
                        gpa = resultGpa,
                        coursesData = GpaRecord.serializeCourses(calculatedCourses)
                    )
                    createPdfLauncher.launch("GPA_Report_${System.currentTimeMillis()}.pdf")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("download_pdf_gpa"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Export as PDF Report", fontWeight = FontWeight.Bold)
            }
        }
    }

    // Modal save dialog to get record name BEFORE saving
    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = { Text("Save GPA Record", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Enter custom name or identifier to save this GPA record offline:")
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = recordName,
                        onValueChange = { recordName = it },
                        placeholder = { Text("E.g. Ali - Semester 3") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("save_record_name_input")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val nameToSave = recordName.trim()
                        if (nameToSave.isEmpty()) {
                            Toast.makeText(context, "Name cannot be empty!", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.saveGpaRecord(nameToSave)
                            showSaveDialog = false
                            recordName = ""
                            Toast.makeText(context, "GPA Record Saved Successfully!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.testTag("save_record_confirm_btn")
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showSaveDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun CgpaResultScreen(viewModel: CalculatorViewModel, onBack: () -> Unit) {
    val context = LocalContext.current
    val cgpaResult by viewModel.calculatedCgpaResult.collectAsState()

    var showSaveDialog by remember { mutableStateOf(false) }
    var recordName by remember { mutableStateOf("") }
    var selectedRecordToExport by remember { mutableStateOf<CgpaRecord?>(null) }

    val createPdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf"),
        onResult = { uri ->
            val record = selectedRecordToExport
            if (uri != null && record != null) {
                val success = Exporter.exportCgpaToPdf(context, uri, record)
                if (success) {
                    Toast.makeText(context, "PDF CGPA Exported Successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "PDF Export Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    )

    val createPngLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("image/png"),
        onResult = { uri ->
            val record = selectedRecordToExport
            if (uri != null && record != null) {
                val success = Exporter.exportCgpaToPng(context, uri, record)
                if (success) {
                    Toast.makeText(context, "PNG CGPA Exported Successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "PNG Export Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    )

    val resultCgpa = cgpaResult ?: 0.0

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CGPA Result", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Details Table
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Semester", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                        Text("GPA", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                        Text("Credits", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, textAlign = TextAlign.End)
                    }

                    viewModel.semesterInputs.forEachIndexed { idx, sem ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Semester ${idx + 1}", modifier = Modifier.weight(1f))
                            Text(sem.gpa, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                            Text(sem.credits.toString(), modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                        }
                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // CGPA Score Box
            val scoreColor = getGpaColor(resultCgpa)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                border = BorderStroke(2.dp, scoreColor)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "YOUR CALCULATED CGPA",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = scoreColor.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(androidx.compose.foundation.shape.CircleShape)
                            .background(scoreColor.copy(alpha = 0.1f))
                            .border(2.dp, scoreColor, androidx.compose.foundation.shape.CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = String.format(Locale.getDefault(), "%.2f", resultCgpa),
                            fontSize = 36.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = scoreColor
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Semesters Stacked: ${viewModel.semesterInputs.size}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Actions Block
            Button(
                onClick = { showSaveDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("record_cgpa_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Save Record", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Export Actions
            OutlinedButton(
                onClick = {
                    val semPairs = viewModel.semesterInputs.map {
                        Pair(it.gpa.toDoubleOrNull() ?: 0.0, it.credits)
                    }
                    selectedRecordToExport = CgpaRecord(
                        name = "CGPA Academic Result",
                        timestamp = System.currentTimeMillis(),
                        cgpa = resultCgpa,
                        semestersData = CgpaRecord.serializeSemesters(semPairs)
                    )
                    createPngLauncher.launch("CGPA_Result_${System.currentTimeMillis()}.png")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("download_png_cgpa"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Export as Image (PNG)", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = {
                    val semPairs = viewModel.semesterInputs.map {
                        Pair(it.gpa.toDoubleOrNull() ?: 0.0, it.credits)
                    }
                    selectedRecordToExport = CgpaRecord(
                        name = "CGPA Academic Result",
                        timestamp = System.currentTimeMillis(),
                        cgpa = resultCgpa,
                        semestersData = CgpaRecord.serializeSemesters(semPairs)
                    )
                    createPdfLauncher.launch("CGPA_Report_${System.currentTimeMillis()}.pdf")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("download_pdf_cgpa"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Export as PDF Report", fontWeight = FontWeight.Bold)
            }
        }
    }

    // Modal save dialog to get record name BEFORE saving
    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = { Text("Save CGPA Record", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Enter custom name or identifier to save this cumulative CGPA record offline:")
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = recordName,
                        onValueChange = { recordName = it },
                        placeholder = { Text("E.g. Ali - Semester 1-5") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("save_cgpa_record_name_input")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val nameToSave = recordName.trim()
                        if (nameToSave.isEmpty()) {
                            Toast.makeText(context, "Name cannot be empty!", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.saveCgpaRecord(nameToSave)
                            showSaveDialog = false
                            recordName = ""
                            Toast.makeText(context, "CGPA Record Saved Successfully!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.testTag("save_record_confirm_btn")
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showSaveDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
