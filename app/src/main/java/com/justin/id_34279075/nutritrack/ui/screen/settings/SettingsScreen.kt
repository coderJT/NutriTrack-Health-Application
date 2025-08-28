package com.justin.id_34279075.nutritrack.ui.screen.settings

import android.app.Application
import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.justin.id_34279075.nutritrack.R
import com.justin.id_34279075.nutritrack.data.authentication.AuthManager

@Composable
fun SettingsScreen(innerPaddingValues: PaddingValues, navController: NavHostController) {

    val context = LocalContext.current

    val application = context.applicationContext as Application
    val viewModel: SettingsScreenViewModel = viewModel(
        factory = SettingsScreenViewModel.SettingsScreenViewModelFactory(application)
    )

    val userBasicInfo by viewModel.userBasicInfo.observeAsState()

    val darkMode = context.getSharedPreferences("darkMode", Context.MODE_PRIVATE)

    val isDarkMode = remember {
        mutableStateOf(darkMode.getBoolean("darkMode", false))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
            .padding(horizontal = 50.dp, vertical = 50.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Settings",
            modifier = Modifier
                .padding(bottom = 8.dp)
                .align(Alignment.Start),
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(40.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Account Info",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )

            Button(
                onClick = {
                    navController.navigate("ChangeDetails") {
                        popUpTo("Settings") { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.purple_500),
                    contentColor = colorResource(R.color.white)
                ),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
            ) {
                Text("Update Details", fontSize = 16.sp)
            }
        }


        HorizontalDivider(thickness = 2.dp)

        Spacer(modifier = Modifier.height(24.dp))

        userBasicInfo?.let { info ->
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.AccountBox,
                        contentDescription = "User ID",
                        tint = colorResource(R.color.purple_500)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "User ID: ${info.userID}", fontSize = 20.sp)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Phone,
                        contentDescription = "Phone",
                        tint = colorResource(R.color.purple_500)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Phone: ${info.phoneNumber}", fontSize = 20.sp)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Name",
                        tint = colorResource(R.color.purple_500)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Name: ${info.name}", fontSize = 20.sp)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Face,
                        contentDescription = "Sex",
                        tint = colorResource(R.color.purple_500)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Sex: ${info.sex}", fontSize = 20.sp)
                }
            }
        } ?: Text(
            text = "Loading user info...",
            fontSize = 18.sp,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Other Settings",
            textAlign = TextAlign.Left,
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )

        HorizontalDivider(thickness = 2.dp)

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .border(
                    width = 2.dp,
                    color = colorResource(R.color.purple_500),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(16.dp)
                .clickable {
                    AuthManager.logout()
                    navController.navigate("Welcome") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = "Logout",
                tint = colorResource(R.color.purple_500),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Logout",
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Navigate",
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .border(
                    width = 2.dp,
                    color = colorResource(R.color.purple_500),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(16.dp)
                .clickable {
                    navController.navigate("Clinician") {
                        popUpTo("Settings") { inclusive = true }
                    }
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Logout",
                tint = colorResource(R.color.purple_500),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Clinician Login",
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Navigate",
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .border(
                    width = 2.dp,
                    color = colorResource(R.color.purple_500),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(16.dp)
                .clickable {
                    val prefs = context.getSharedPreferences("darkMode", Context.MODE_PRIVATE)
                    prefs.edit().putBoolean("darkMode", !isDarkMode.value).apply()
                    isDarkMode.value = !isDarkMode.value
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isDarkMode.value) Icons.Default.LightMode else Icons.Default.DarkMode,
                contentDescription = "Switch to Dark Mode",
                tint = colorResource(R.color.purple_500),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = if (isDarkMode.value) "To Light Mode" else "To Dark Mode",
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Navigate",
            )
        }
    }
}
