package com.justin.id_34279075.nutritrack.ui.screen.nutricoach

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import com.justin.id_34279075.nutritrack.R
import com.justin.id_34279075.nutritrack.data.authentication.AuthManager
import com.justin.id_34279075.nutritrack.data.network.FruitResponse

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NutriCoachScreen(innerPaddingValues: PaddingValues, navController: NavHostController) {

    val context = LocalContext.current
    val application = context.applicationContext as Application
    val viewModel: NutriCoachScreenViewModel = viewModel(
        factory = NutriCoachScreenViewModel.NutriCoachScreenViewModelFactory(application)
    )

    val userID = AuthManager.getCurrentUserID()

    val query = viewModel.query
    val showTips = viewModel.showTips

    val fruitScoreOptimal by viewModel.fruitScoreOptimal.observeAsState(false)
    val fruit by viewModel.fruit.observeAsState()
    val fruitName by viewModel.fruitName.observeAsState()
    val imageUrl by viewModel.imageUrl.observeAsState()
    val tips by viewModel.tips.observeAsState()
    val errorMessage by viewModel.errorMessage.observeAsState("")
    val tip by viewModel.tip.observeAsState()

    val isFruitLoading by viewModel.isFruitLoading.observeAsState(false)
    val isTipLoading by viewModel.isTipLoading.observeAsState(false)

    var isImageLoading by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddingValues)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 50.dp, vertical = 50.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "NutriCoach",
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.Start),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (fruitScoreOptimal == false) {

                Text(
                    text = "Enter the fruit name:",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Left,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = query,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isLetter() || it.isWhitespace() }) {
                            viewModel.toggleQuery(newValue)
                        }
                    },
                    label = { Text("example: banana") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(15.dp))

                Button(
                    onClick = { viewModel.searchFruit(query) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = query.isNotBlank() && !isFruitLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.purple_500),
                        contentColor = colorResource(R.color.white))
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Logout",
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text("Search Fruit Details", fontSize = 18.sp)
                }

                if (isFruitLoading) {
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator()
                }

                Spacer(modifier = Modifier.height(18.dp))

                if (fruit != null) {
                    FruitInfoCard(fruit, fruitName)
                }

                Spacer(modifier = Modifier.height(20.dp))

            } else {

                if (isImageLoading) {
                    Text("Loading Image...")
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator()
                }

                imageUrl?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = "Random Image",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onState = {
                            isImageLoading = it is AsyncImagePainter.State.Loading
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            Button(
                onClick = {
                    if (userID != null) {
                        viewModel.sendPrompt(userID)
                    }
                },
                enabled = !isTipLoading,
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 12.dp,
                ),
                modifier = Modifier
                    .align(Alignment.End)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.purple_500),
                    contentColor = colorResource(R.color.white)),
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Logout",
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    "Click for Motivational Message!",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                )
            }

            if (isTipLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator()
            }

            Spacer(modifier = Modifier.height(18.dp))

            tip?.let {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    Text(
                        text = "AI Response:",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.purple_500)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val formattedText = buildAnnotatedString {
                            var currentIndex = 0

                            // Regex to capture bolded contents
                            val regex = Regex("""\*(.*?)\*""")

                            regex.findAll(tip!!).forEach { matchResult ->

                                // Start and end index of the bolded content
                                val start = matchResult.range.first
                                val end = matchResult.range.last + 1

                                // Append text before bold text
                                append(tip!!.substring(currentIndex, start))

                                // Use bolded styles on bolded texts
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(matchResult.groupValues[1])
                                }

                                // Update the ending pointer
                                currentIndex = end
                            }

                            // Append remaining text
                            if (currentIndex < tip!!.length) {
                                append(tip!!.substring(currentIndex))
                            }
                        }

                        Text(
                            text = formattedText,
                            color = colorResource(R.color.white),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(50.dp))

            Box(modifier = Modifier.fillMaxSize().padding(top = 10.dp)) {
                Column(
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                    if (showTips) {
                        AlertDialog(
                            onDismissRequest = { viewModel.toggleShowTips() },
                            modifier = Modifier.padding(vertical = 30.dp),
                            title = {
                                Text(
                                    text = "NutriCoach Tips",
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            },
                            text = {
                                Column(
                                    modifier = Modifier.verticalScroll(rememberScrollState()),
                                ) {
                                    tips?.let { nutriCoachTip ->
                                        if (nutriCoachTip.tips.isNotEmpty()) {
                                            nutriCoachTip.tips.forEach { tip ->
                                                Card(modifier = Modifier.padding(vertical = 4.dp).border(
                                                    width = 1.dp,
                                                    color = colorResource(R.color.purple_500),
                                                    shape = RoundedCornerShape(8.dp)
                                                )) {
                                                    Column(modifier = Modifier.padding(16.dp)) {
                                                        Text(text = tip.text)
                                                        Text(
                                                            text = "Created at: ${tip.dateTime}",
                                                            style = MaterialTheme.typography.labelSmall,
                                                            modifier = Modifier.padding(top = 4.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        } else {
                                            Text(text = "No tips available.")
                                        }
                                    } ?: Text(text = "No tips available.")
                                }
                            },
                            confirmButton = {
                                Button(
                                    onClick = { viewModel.toggleShowTips() },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = colorResource(
                                            R.color.purple_500
                                        ),
                                        contentColor = colorResource(R.color.white)
                                    )
                                ) {
                                    Text("Close")
                                }
                            }
                        )
                    }
                }

                if (errorMessage != "") {
                    AlertDialog(
                        onDismissRequest = { viewModel.clearError() },
                        title = { Text("Error:") },
                        text = { Text(errorMessage ?: "") },
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

        Button(
            onClick = { viewModel.toggleShowTips() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(36.dp)
                .height(50.dp),
            elevation = ButtonDefaults.buttonElevation(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.purple_500),
                contentColor = colorResource(R.color.white)),
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "Logout",
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = if (showTips) "Hide All Tips" else "Show All Tips")
        }
    }

}
@Composable
fun FruitInfoCard(fruit: FruitResponse?, fruitName: String?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "Fruit information for $fruitName:",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            modifier = Modifier.fillMaxWidth().border(
                width = 2.dp,
                color = colorResource(R.color.purple_500),
                shape = RoundedCornerShape(8.dp)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                val rows = listOf(
                    "Family" to (fruit?.family ?: "N/A"),
                    "Calories" to "${fruit?.nutritions?.calories ?: "N/A"} kcal",
                    "Fat" to "${fruit?.nutritions?.fat ?: "N/A"} g",
                    "Sugar" to "${fruit?.nutritions?.sugar ?: "N/A"} g",
                    "Carbohydrates" to "${fruit?.nutritions?.carbohydrates ?: "N/A"} g",
                    "Protein" to "${fruit?.nutritions?.protein ?: "N/A"} g"
                )

                rows.forEachIndexed { index, (label, value) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "$label:",
                            modifier = Modifier.weight(1f),
                        )
                        Text(
                            text = value,
                            textAlign = TextAlign.End,
                            modifier = Modifier.weight(1f),
                        )
                    }

                    if (index < rows.lastIndex) {
                        HorizontalDivider(
                            color = colorResource(R.color.purple_500),
                            thickness = 1.dp,
                        )
                    }
                }
            }
        }
    }
}
