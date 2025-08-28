package com.justin.id_34279075.nutritrack.ui.screen.insights

import android.app.Application
import android.content.Intent
import android.content.Intent.ACTION_SEND
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.justin.id_34279075.nutritrack.R
import com.justin.id_34279075.nutritrack.data.foodIntake.FoodCategories.addedSugars
import com.justin.id_34279075.nutritrack.data.foodIntake.FoodCategories.alcohol
import com.justin.id_34279075.nutritrack.data.foodIntake.FoodCategories.dairy
import com.justin.id_34279075.nutritrack.data.foodIntake.FoodCategories.discretionary
import com.justin.id_34279075.nutritrack.data.foodIntake.FoodCategories.fats
import com.justin.id_34279075.nutritrack.data.foodIntake.FoodCategories.fruits
import com.justin.id_34279075.nutritrack.data.foodIntake.FoodCategories.grains
import com.justin.id_34279075.nutritrack.data.foodIntake.FoodCategories.meat
import com.justin.id_34279075.nutritrack.data.foodIntake.FoodCategories.sodium
import com.justin.id_34279075.nutritrack.data.foodIntake.FoodCategories.vegetables
import com.justin.id_34279075.nutritrack.data.foodIntake.FoodCategories.water
import com.justin.id_34279075.nutritrack.data.foodIntake.FoodCategoryInfo
import com.justin.id_34279075.nutritrack.data.patient.toLabeledMap

@Composable
fun InsightsScreen(innerPaddingValues: PaddingValues, navController: NavHostController) {

    val context = LocalContext.current
    val application = context.applicationContext as Application
    val viewModel: InsightsScreenViewModel = viewModel(
        factory = InsightsScreenViewModel.InsightsScreenViewModelFactory(application)
    )

    val scores by viewModel.scores.observeAsState()
    val totalHEIFAScore by viewModel.totalHEIFAScore.observeAsState()
    val foodInfo = viewModel.foodInfo.observeAsState()

    val labelledScores = scores?.toLabeledMap()

    labelledScores?.let { data ->
    Column(
        modifier = Modifier
            .padding(innerPaddingValues)
            .fillMaxSize()
            .padding(horizontal = 50.dp, vertical = 50.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Food Score",
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.Start),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            data.forEach { (label, score) ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 8.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = label,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .weight(1f),
                            softWrap = true,
                            maxLines = 2
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Button(
                            onClick = {
                                allCategories[label]?.let { info ->
                                    viewModel.selectFoodInfo(info)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = colorResource(R.color.purple_500),
                            ),
                            contentPadding = PaddingValues(0.dp),
                            shape = RoundedCornerShape(14.dp),
                            elevation = ButtonDefaults.buttonElevation(0.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .background(
                                        color = colorResource(R.color.purple_500),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Info",
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Info",
                                    fontSize = 13.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Slider(
                        value = (score / getMaxScore(label)).coerceIn(0f, 1f),
                        onValueChange = {},
                        enabled = false,
                        valueRange = 0f..1f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp),
                        colors = SliderDefaults.colors(
                            disabledThumbColor = colorResource(R.color.purple_500)
                        )
                    )

                    Text(
                        text = "$score / ${getMaxScore(label)}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.purple_500),
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                }
            }

            if (foodInfo.value != null) {
                FoodCategoryDialog(info = foodInfo.value!!, onDismiss = { viewModel.selectFoodInfo(null) })
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 8.dp)
            ) {
                Text(
                    text = "Total Food Quality Score:",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                totalHEIFAScore?.let { score ->
                    Column {
                        Slider(
                            value = score / 100f,
                            onValueChange = {},
                            enabled = false,
                            valueRange = 0f..1f,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp),
                            colors = SliderDefaults.colors(
                                disabledThumbColor = colorResource(R.color.purple_500)
                            )
                        )

                        Text(
                            text = "$score / 100",
                            fontSize = 18.sp,
                            color = colorResource(R.color.purple_500),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                modifier = Modifier.height(50.dp).fillMaxWidth(),
                onClick = {
                    val shareIntent = Intent(ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "Hi! I've got a HEIFA score of: $totalHEIFAScore"
                        )
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share text via"))
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.purple_500),
                    contentColor = colorResource(R.color.white)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Logout",
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text("Share with someone", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                modifier = Modifier.height(50.dp).fillMaxWidth(),
                onClick = {
                    navController.navigate("NutriCoach") {
                        launchSingleTop = true
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.purple_500),
                    contentColor = colorResource(R.color.white)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Build,
                    contentDescription = "Logout",
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text("Improve my diet", fontSize = 16.sp)
            }



        }
    } ?:  Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Text("Loading Insights Values...")
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
        }
    }


}

/**
 * Function that sets the maximum scores for each score
 */
fun getMaxScore(label: String): Float {
    return when (label) {
        "Vegetables" -> 10f
        "Fruits" -> 10f
        "Grains & Cereals" -> 5f
        "Whole Grains" -> 5f
        "Meat & Alternatives" -> 10f
        "Dairy" -> 10f
        "Water" -> 5f
        "Saturated Fats" -> 5f
        "Unsaturated Fats" -> 5f
        "Sodium" -> 10f
        "Sugar" -> 10f
        "Alcohol" -> 5f
        "Discretionary Foods" -> 10f
        "Total Food Quality" -> 100f
        else -> 1f
    }
}

val allCategories = mapOf(
    "Discretionary Foods" to discretionary,
    "Vegetables" to vegetables,
    "Fruits" to fruits,
    "Grains & Cereals" to grains,
    "Whole Grains" to grains,
    "Meat & Alternatives" to meat,
    "Dairy" to dairy,
    "Water" to water,
    "Saturated Fats" to fats,
    "Unsaturated Fats" to fats,
    "Sodium" to sodium,
    "Sugar" to addedSugars,
    "Alcohol" to alcohol
)

@Composable
fun FoodCategoryDialog(
    info: FoodCategoryInfo,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(text = info.categoryName, fontSize = 18.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(10.dp))

                Text("Energy per Serve:", fontWeight = FontWeight.SemiBold)
                Text(info.energyPerServe)

                Spacer(modifier = Modifier.height(10.dp))

                Text("Criteria for Maximum Score:", fontWeight = FontWeight.SemiBold)
                info.maxScoreCriteria.forEach { criterion ->
                    Text("• $criterion")
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text("Criteria for 0 Score:", fontWeight = FontWeight.SemiBold)
                info.zeroScoreCriteria.forEach { criterion ->
                    Text("• $criterion")
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text("Details:", fontWeight = FontWeight.SemiBold)
                Text(info.details)

                Spacer(modifier = Modifier.height(10.dp))

                if (info.examples.isNotEmpty()) {
                    Text("Examples:", fontWeight = FontWeight.SemiBold)
                    info.examples.forEach { example ->
                        Text("• $example")
                    }
                }
            }
        },
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp)
    )
}
