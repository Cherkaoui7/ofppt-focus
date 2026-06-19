package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.StudyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    var isRegisterMode by remember { mutableStateOf(false) }
    var isForgotPasswordMode by remember { mutableStateOf(false) }

    var nameInput by remember { mutableStateOf("") }
    var emailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var statusMessage by remember { mutableStateOf("") }

    val isDark by viewModel.isDarkMode.collectAsState()

    // Smooth gradient backgrounds matching selected modes
    val bgGradients = if (isDark) {
        listOf(CyberDarkBg, Color(0xFF1E1E38))
    } else {
        listOf(CyberLightBg, Color(0xFFE2E8F0))
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(bgGradients)),
        contentAlignment = Alignment.Center
    ) {
        // Aesthetic layered background circle accents
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-120).dp, y = (-200).dp)
                .clip(RoundedCornerShape(150.dp))
                .background(Brush.radialGradient(listOf(TechTeal.copy(alpha = 0.2f), Color.Transparent)))
        )
        Box(
            modifier = Modifier
                .size(350.dp)
                .offset(x = 150.dp, y = 250.dp)
                .clip(RoundedCornerShape(175.dp))
                .background(Brush.radialGradient(listOf(TechTeal.copy(alpha = 0.15f), Color.Transparent)))
        )

        // Glassmorphism login container card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .widthIn(max = 420.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(
                    if (isDark) Color(0xFF1E293B).copy(alpha = 0.85f)
                    else Color.White.copy(alpha = 0.9f)
                )
                .border(
                    width = 1.2.dp,
                    color = (if (isDark) CyberDarkBorder else CyberLightBorder).copy(alpha = 0.8f),
                    shape = RoundedCornerShape(32.dp)
                )
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Elegant brand heading
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = "Graduation Cap Logo",
                    tint = TechTeal,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "OFPPT Focus",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.SansSerif,
                        letterSpacing = 0.5.sp,
                        color = TechTeal
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your Smart Academic Companion",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = if (isDark) Color.LightGray else Color.Gray,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Sub-heading labels depending on current status states
            Text(
                text = when {
                    isForgotPasswordMode -> "Reset Password"
                    isRegisterMode -> "Create Student Account"
                    else -> "Student Portal Log In"
                },
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) Color.White else CyberDarkBg
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Form Fields
            AnimatedVisibility(
                visible = isRegisterMode && !isForgotPasswordMode,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    label = { Text("Full Name") },
                    leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = "Name") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .testTag("register_name_input"),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            if (!isForgotPasswordMode) {
                OutlinedTextField(
                    value = emailInput,
                    onValueChange = { emailInput = it },
                    label = { Text("E-mail Address") },
                    leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = "Email") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .testTag("login_email_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = passwordInput,
                    onValueChange = { passwordInput = it },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = "Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .testTag("login_password_input"),
                    shape = RoundedCornerShape(12.dp)
                )
            } else {
                OutlinedTextField(
                    value = emailInput,
                    onValueChange = { emailInput = it },
                    label = { Text("Registered E-mail") },
                    leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = "Email") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Quick forgot password switch row
            if (!isRegisterMode && !isForgotPasswordMode) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    TextButton(
                        onClick = { isForgotPasswordMode = true },
                        modifier = Modifier.minimumInteractiveComponentSize()
                    ) {
                        Text(
                            "Forgot Password?",
                            style = MaterialTheme.typography.bodySmall.copy(color = TechTeal)
                        )
                    }
                }
            }

            if (statusMessage.isNotEmpty()) {
                Text(
                    text = statusMessage,
                    color = if (statusMessage.startsWith("Success")) ElectricGreen else AlarmRed,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(vertical = 8.dp),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Primary Action Button
            Button(
                onClick = {
                    statusMessage = ""
                    when {
                        isForgotPasswordMode -> {
                            if (emailInput.isBlank()) {
                                statusMessage = "Please enter your registered e-mail."
                            } else {
                                statusMessage = "Success: Password reset link dispatched to $emailInput"
                            }
                        }
                        isRegisterMode -> {
                            if (nameInput.isBlank() || emailInput.isBlank() || passwordInput.isBlank()) {
                                statusMessage = "Please fill in all registration fields."
                            } else {
                                viewModel.login(nameInput, emailInput)
                            }
                        }
                        else -> {
                            // Default Login
                            if (emailInput.isBlank() || passwordInput.isBlank()) {
                                statusMessage = "Please provide email and password details."
                            } else {
                                // Simulate success login
                                val index = emailInput.indexOf("@")
                                val computedName = if (index != -1) {
                                    emailInput.substring(0, index)
                                        .replace(".", " ")
                                        .replaceFirstChar { it.uppercase() }
                                } else {
                                    "OFPPT Student"
                                }
                                viewModel.login(computedName, emailInput)
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("primary_auth_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TechTeal,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = when {
                        isForgotPasswordMode -> "Send Recovery Link"
                        isRegisterMode -> "Sign Up"
                        else -> "Log In Securely"
                    },
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Text toggles below main buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when {
                        isForgotPasswordMode -> "Remember password?"
                        isRegisterMode -> "Already registered?"
                        else -> "First time studying here?"
                    },
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (isDark) Color.Gray else Color.DarkGray
                    )
                )
                TextButton(
                    onClick = {
                        statusMessage = ""
                        when {
                            isForgotPasswordMode -> {
                                isForgotPasswordMode = false
                                isRegisterMode = false
                            }
                            isRegisterMode -> {
                                isRegisterMode = false
                            }
                            else -> {
                                isRegisterMode = true
                            }
                        }
                    },
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Text(
                        text = when {
                            isForgotPasswordMode -> "Login"
                            isRegisterMode -> "Sign In"
                            else -> "Register"
                        },
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TechTeal,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}
