package com.justin.id_34279075.nutritrack.ui.screen.resetPassword

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.justin.id_34279075.nutritrack.R
import com.justin.id_34279075.nutritrack.ui.screen.changeDetails.ChangeDetailsScreenViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(innerPaddingValues: PaddingValues, navController: NavHostController) {

    val context = LocalContext.current
    val application = context.applicationContext as Application
    val viewModel: ResetPasswordViewModel = viewModel(
        factory = ResetPasswordViewModel.ResetPasswordViewModelFactory(application)
        )

    val errorMessage by viewModel.errorMessage.observeAsState("")
    val userIDs by viewModel.userIDs.observeAsState(emptyList())
    val showExpandedUserIDs = viewModel.showExpandedUserIDs
    val userID = viewModel.userID

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .padding(innerPaddingValues)
            .padding(horizontal = 20.dp, vertical = 50.dp)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 40.dp, vertical = 40.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Text(
                text = "Reset Password",
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.Start),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(10.dp))

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

            OutlinedTextField(
                value = viewModel.phoneNumber,
                onValueChange = { newValue ->
                    val cleanedValue = newValue.filter { it.isLetterOrDigit() || it.isWhitespace().not() }
                    viewModel.onPhoneNumberChange(cleanedValue)
                },
                label = { Text("Your Phone Number") },
                visualTransformation = if (!viewModel.showPhoneNumber) PasswordVisualTransformation() else VisualTransformation.None,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),

                trailingIcon = {
                    val icon = if (viewModel.showPhoneNumber) Icons.Default.Visibility else Icons.Default.VisibilityOff

                    val description = if (viewModel.showPhoneNumber) "Hide Phone number" else "Show Phone number"

                    IconButton(onClick = { viewModel.toggleShowPhoneNumber() }) {
                        Icon(imageVector = icon, contentDescription = description)
                    }
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = viewModel.password,
                onValueChange = { newValue ->
                    val cleanedValue = newValue.filter { it.isLetterOrDigit() || it.isWhitespace().not() }
                    viewModel.onPasswordChange(cleanedValue)
                },
                label = { Text("Update Your Password") },
                visualTransformation = if (!viewModel.showPassword) PasswordVisualTransformation() else VisualTransformation.None,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),

                trailingIcon = {
                    val icon = if (viewModel.showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff

                    val description = if (viewModel.showPassword) "Hide Password" else "Show Password"

                    IconButton(onClick = { viewModel.toggleShowPassword() }) {
                        Icon(imageVector = icon, contentDescription = description)
                    }
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = viewModel.confirmPassword,
                onValueChange = { newValue ->
                    val cleanedValue = newValue.filter { it.isLetterOrDigit() || it.isWhitespace().not() }
                    viewModel.onConfirmPasswordChange(cleanedValue)
                },
                label = { Text("Confirm Your New Password") },
                visualTransformation = if (!viewModel.showConfirmPassword) PasswordVisualTransformation() else VisualTransformation.None,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),

                trailingIcon = {
                    val icon = if (viewModel.showConfirmPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff

                    val description = if (viewModel.showConfirmPassword) "Hide Password" else "Show Password"

                    IconButton(onClick = { viewModel.toggleShowConfirmPassword() }) {
                        Icon(imageVector = icon, contentDescription = description)
                    }
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        val canUpdate = viewModel.canBeReset()

                        if (errorMessage == "" && canUpdate) {
                            viewModel.updatePassword()
                            if (errorMessage == "") {
                                Toast.makeText(context, "Reset successful", Toast.LENGTH_SHORT).show()
                                navController.navigate("Login")
                            }
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
                Text("Reset Password", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    navController.navigate("Login")
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.purple_500),
                    contentColor = colorResource(R.color.white)
                )
            ) {
                Text("Back", fontSize = 18.sp)
            }

            if (errorMessage != "") {
                AlertDialog(
                    onDismissRequest = { viewModel.clearError() },
                    title = { Text("Registration Error:") },
                    text = { Text(errorMessage) },
                    confirmButton = {
                        Button(
                            onClick = {
                                viewModel.clearError()},
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.purple_500)
                            )
                        ) {
                            Text("OK", fontSize = 16.sp)
                        }
                    },
                )
            }
        }
    }
}
