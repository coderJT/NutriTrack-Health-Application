package com.justin.id_34279075.nutritrack.ui.screen.login

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.justin.id_34279075.nutritrack.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(innerPaddingValues: PaddingValues, navController: NavHostController)
{
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val viewModel: LoginScreenViewModel = viewModel(
        factory = LoginScreenViewModel.LoginScreenViewModelFactory(application)
    )

    val userID = viewModel.userID
    val password = viewModel.password

    val showExpandedUserIDs = viewModel.showExpandedUserIDs
    val showPassword = viewModel.showPassword

    val userIDs by viewModel.userIDs.observeAsState(emptyList())
    val errorMessage by viewModel.errorMessage.observeAsState("")

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column (
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal=60.dp, vertical=40.dp)
        ) {
            Text(
                text = stringResource(R.string.login_title),
                fontWeight = FontWeight.SemiBold,
                fontSize = 30.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenuBox(
                expanded = showExpandedUserIDs,
                onExpandedChange = { viewModel.toggleShowExpandedUserIDs() }
            ) {
                OutlinedTextField(
                    value = userID,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.userID_input_label)) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = showExpandedUserIDs)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = showExpandedUserIDs,
                    onDismissRequest = { viewModel.toggleShowExpandedUserIDs() }
                ) {
                    if (userIDs.isNotEmpty()) {
                        userIDs.forEach { id ->
                            DropdownMenuItem(
                                text = { Text(id) },
                                onClick = {
                                    viewModel.onUserIDChange(id)
                                    viewModel.toggleShowExpandedUserIDs()
                                }
                            )
                        }
                    } else {
                        DropdownMenuItem(
                            text = { Text("No registered user") },
                            onClick = {}
                        )
                    }

                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { newValue ->
                    val cleanedValue = newValue.filter { it.isLetterOrDigit() || it.isWhitespace().not() }
                    viewModel.onPasswordChange(cleanedValue)
                },
                label = { Text(stringResource(R.string.password_input_label)) },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (!showPassword) PasswordVisualTransformation() else VisualTransformation.None,
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

            Text(
                text = stringResource(R.string.login_notice),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        val isValid = viewModel.login()
                        if(isValid){
                            Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                        }
                        if (errorMessage == "") {
                            navController.navigate("Questionnaire")
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
                Text(text = "Continue", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(15.dp))

            Button(
                onClick = {
                    navController.navigate("Register")
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.purple_500),
                    contentColor = colorResource(R.color.white)
                )
            ) {
                Text("Register", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(15.dp))

            TextButton(
                onClick = {
                    navController.navigate("ResetPassword")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "Forgot your password?",
                    fontSize = 16.sp,
                )
            }
        }

        if (errorMessage != "") {
            AlertDialog(
                onDismissRequest = { viewModel.clearError() },
                title = { Text("Login Error:") },
                text = { Text(errorMessage.toString()) },
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
    }

}