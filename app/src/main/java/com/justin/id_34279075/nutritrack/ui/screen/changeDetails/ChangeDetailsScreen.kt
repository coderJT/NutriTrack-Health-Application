package com.justin.id_34279075.nutritrack.ui.screen.changeDetails

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
import kotlinx.coroutines.launch

@Composable
fun ChangeDetailsScreen(innerPaddingValues: PaddingValues, navController: NavHostController) {

    val context = LocalContext.current
    val application = context.applicationContext as Application
    val viewModel: ChangeDetailsScreenViewModel = viewModel(
        factory = ChangeDetailsScreenViewModel.ChangeDetailsScreenViewModelFactory(application)
    )

    val errorMessage by viewModel.errorMessage.observeAsState("")

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .padding(innerPaddingValues)
            .padding(horizontal = 20.dp, vertical = 30.dp)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 40.dp, vertical = 40.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Text(
                text = "Update Details",
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.Start),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = viewModel.userName,
                onValueChange = { newValue ->
                    if (newValue.all { it.isLetter() || it.isWhitespace()  }) {
                        viewModel.onUserNameChange(newValue)
                    }
                },
                label = { Text("Update Your Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = viewModel.currentPassword,
                onValueChange = { newValue ->
                    val cleanedValue = newValue.filter { it.isLetterOrDigit() || it.isWhitespace().not() }
                    viewModel.onCurrentPasswordChange(cleanedValue)
                },
                label = { Text("Your Current Password") },
                visualTransformation = if (!viewModel.showCurrentPassword) PasswordVisualTransformation() else VisualTransformation.None,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),

                trailingIcon = {
                    val icon = if (viewModel.showCurrentPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff

                    val description = if (viewModel.showCurrentPassword) "Hide password" else "Show Password"

                    IconButton(onClick = { viewModel.toggleShowCurrentPassword() }) {
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

                    val description = if (viewModel.showPassword) "Hide password" else "Show Password"

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

                    val description = if (viewModel.showConfirmPassword) "Hide password" else "Show Password"

                    IconButton(onClick = { viewModel.toggleShowConfirmPassword() }) {
                        Icon(imageVector = icon, contentDescription = description)
                    }
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        val canUpdate = viewModel.canBeUpdated()

                        if (errorMessage == "" && canUpdate) {
                            viewModel.updateDetails()
                            if (errorMessage == "") {
                                Toast.makeText(context, "Change successful", Toast.LENGTH_SHORT).show()
                                navController.navigate("Settings")
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
                Text("Update Information", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    navController.navigate("Settings")
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
