package com.justin.id_34279075.nutritrack.ui.screen.clinician

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.justin.id_34279075.nutritrack.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ClinicianScreen(innerPaddingValues: PaddingValues, navController: NavHostController) {

    val context = LocalContext.current
    val application = context.applicationContext as Application
    val viewModel: ClinicianScreenViewModel = viewModel(
        factory = ClinicianScreenViewModel.ClinicianScreenViewModelFactory(application)
    )

    val password = viewModel.password
    val showPassword = viewModel.showPassword

    val maleScore by viewModel.averageMaleHEIFAScore.observeAsState()
    val femaleScore by viewModel.averageFemaleHEIFAScore.observeAsState()
    val message by viewModel.message.observeAsState()
    val adminAuthenticated by viewModel.adminAuthenticated.observeAsState()
    val errorMessage by viewModel.errorMessage.observeAsState("")
    val isLoading by viewModel.isLoading.observeAsState(false)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(adminAuthenticated) {
        if (adminAuthenticated == true) {
            viewModel.togglePassword("")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 50.dp, vertical = 30.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (adminAuthenticated != true) {

                Text("Clinician Login", fontWeight = FontWeight.Bold, fontSize = 30.sp)

                Spacer(modifier = Modifier.height(120.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { newValue ->
                        val cleanedValue = newValue.filter { it.isLetterOrDigit() || it.isWhitespace().not() }
                        viewModel.togglePassword(cleanedValue)
                    },
                    label = { Text("Enter your clinician key") },
                    visualTransformation = if (!showPassword) PasswordVisualTransformation() else VisualTransformation.None,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),

                    trailingIcon = {
                        var icon = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff

                        val description = if (showPassword) "Hide Password" else "Show Password"

                        IconButton(onClick = { viewModel.toggleShowPassword() }) {
                            Icon(imageVector = icon, contentDescription = description)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        viewModel.authenticateAdmin(password)
                        if(errorMessage == ""){
                            Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                        }
                    }, modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.purple_500),
                        contentColor = colorResource(R.color.white))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Logout",
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Clinician Login", fontSize = 18.sp)
                }

                if (errorMessage != "") {
                    AlertDialog(
                        onDismissRequest = { viewModel.clearError() },
                        title = { Text("Clinician Login Error:") },
                        text = { Text(errorMessage) },
                        confirmButton = {
                            Button(
                                onClick = { viewModel.clearError() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(R.color.purple_500)
                                )
                            ) {
                                Text("OK")
                            }
                        }
                    )
                }

            } else {

                Text(
                    text = "Clinician Dashboard",
                    modifier = Modifier
                        .padding(bottom = 8.dp),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 2.dp,
                                color = colorResource(R.color.purple_500),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Average HEIFA Score (Male): ${maleScore ?: "Loading..."}",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 2.dp,
                                color = colorResource(R.color.purple_500),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Average HEIFA Score (Female): ${femaleScore ?: "Loading..."}",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                viewModel.getPatternsFromAllPatientScores()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.purple_500),
                            contentColor = colorResource(R.color.white)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Logout",
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Find Data Patterns", fontSize = 18.sp)
                    }

                    if (isLoading) {
                        Spacer(modifier = Modifier.height(16.dp))
                        CircularProgressIndicator()
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    if (!message.isNullOrEmpty()) {

                        Log.d("Message", message.toString())

                        Text(
                            text = "Patterns in Data:",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Split message into sections specifically for any numbered sections (1. **) or for
                        // ("Important...")   
                        val sectionRegex = Regex("""(?=(\d+\. \*\*|\*\*Important))""")
                        val sections =
                            message!!.trim().split(sectionRegex).filter { it.isNotBlank() }

                        Column(modifier = Modifier.padding(8.dp)) {
                            sections.forEach { section ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                                ) {
                                    val annotatedText = buildAnnotatedString {
                                        val boldTextRegex = Regex("""\*\*(.*?)\*\*""")
                                        var stopIndex = 0

                                        // Extract each section based on previously designed regex
                                        boldTextRegex.findAll(section).forEach { match ->
                                            val range = match.range
                                            val before = section.substring(stopIndex, range.first)
                                            append(before)

                                            // Makes the section title more attractive
                                            withStyle(
                                                style = SpanStyle(
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 18.sp
                                                )
                                            ) {
                                                append(match.groupValues[1])
                                            }

                                            // Add spacing between title and contents
                                            append("\n")
                                            append("\n")

                                            // Mark last position of used raw text
                                            stopIndex = range.last + 1
                                        }

                                        // Add remaining texts
                                        if (stopIndex < section.length) {
                                            append(section.substring(stopIndex).trim())
                                        }
                                    }

                                    Text(
                                        text = annotatedText,
                                        fontSize = 16.sp,
                                        textAlign = TextAlign.Start,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                    )
                                }
                            }
                        }
                    }

                    if (message != null) {
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    val file = withContext(Dispatchers.IO) {
                                        viewModel.generatePDF(context)
                                    }

                                    if (file != null) {
                                        Toast.makeText(
                                            context,
                                            "Report saved at: ${file.absolutePath}",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Error encountered while trying to save PDF",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.purple_500),
                                contentColor = colorResource(R.color.white)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = "Download",
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Download as PDF", fontSize = 18.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
        Button(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(36.dp)
                .height(50.dp),
            elevation = ButtonDefaults.buttonElevation(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.purple_500),
                contentColor = colorResource(R.color.white)),
            onClick = {
                navController.navigate("Settings") {
                    popUpTo("Settings") { inclusive = true }
                }
            },
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Logout",
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text("Done", fontSize = 16.sp)
        }
    }
}
