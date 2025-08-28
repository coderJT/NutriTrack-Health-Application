package com.justin.id_34279075.nutritrack.ui.screen.home

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.justin.id_34279075.nutritrack.R
import com.justin.id_34279075.nutritrack.data.authentication.AuthManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(innerPaddingValues: PaddingValues, navController: NavHostController) {

    val context = LocalContext.current
    val application = context.applicationContext as Application
    val viewModel: HomeScreenViewModel = viewModel(
        factory = HomeScreenViewModel.HomeScreenViewModelFactory(application)
    )

    val totalHEIFAScore = viewModel.totalHEIFAScore.observeAsState(0f)
    val userID = AuthManager.getCurrentUserID()
    val userName = AuthManager.getCurrentUserName()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
            .padding(horizontal = 50.dp, vertical = 50.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
    ) {

        Text(text = "Hello,", fontSize = 20.sp)

        Text(
            text = userName.toString(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 32.sp,
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Text(
                text =
                "You've already filled in your Food intake Questionnaire, " +
                        "but you can change detail here:", modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .padding(end = 10.dp),
                fontSize = 16.sp
            )
            Button(
                onClick = {
                    navController.navigate("Questionnaire") {
                        popUpTo("Home") { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.purple_500),
                    contentColor = colorResource(R.color.white)
                ),
            ) {
                Text("Edit")
            }
        }

        Image(
            painter = painterResource(R.drawable.home_image),
            contentDescription = "Logo",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .aspectRatio(1f)
                .fillMaxSize(),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Text(
                text = "My Score",
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
            )
            Button(
                onClick = {
                    navController.navigate("Insights") {
                        popUpTo("Home") { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.purple_500),
                    contentColor = colorResource(R.color.white)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.AccountBox,
                    contentDescription = "Logout",
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text("See all scores")
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowUp,
                contentDescription = "Back",
            )

            Text(
                text = "Your Food Quality score",
                modifier = Modifier.padding(start = 15.dp)
            )

            Text(
                text = "${totalHEIFAScore.value}/100",
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f),
                color = Color(0xFF006400)
            )
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "What is the Food Quality Score?",
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text =
            "Your Food Quality Score provides a snapshot of how well your " +
                    "eating patterns align with established food guidelines, helping " +
                    "you identify both strengths and opportunities for improvement " +
                    "in your diet.",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text =
            "This personalized measurement considers various food groups " +
                    "including vegetables, fruits, whole grains, and proteins " +
                    "to give you practical insights for making healthier food choices.",
            fontSize = 16.sp
        )
    }
}
