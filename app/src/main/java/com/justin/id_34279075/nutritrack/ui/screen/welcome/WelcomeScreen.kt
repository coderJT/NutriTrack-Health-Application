package com.justin.id_34279075.nutritrack.ui.screen.welcome

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.justin.id_34279075.nutritrack.R

@Composable
fun WelcomeScreen(innerPaddingValues: PaddingValues, navController: NavHostController) {

    Box(modifier = Modifier.fillMaxSize().padding(horizontal=60.dp, vertical=60.dp)) {

        // Align content to the center of the screen from top to bottom
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            // NutriTrack Logo
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = stringResource(R.string.logo_alt_text),
                modifier = Modifier
                    .width(500.dp)
                    .height(300.dp)
            )

            // App Description
            Text(
                text = stringResource(R.string.app_description),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Login Button
            Button(
                onClick = {
                    navController.navigate("Login")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.purple_500),
                    contentColor = colorResource(R.color.white)
                )
            ) {
                Text(
                    text = stringResource(R.string.login),
                    fontSize = 16.sp
                )
            }

            // Credits
            Text(
                text = stringResource(R.string.credits),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 40.dp),
            )
        }
    }
}