package com.justin.id_34279075.nutritrack.ui.screen.questionnaire

import android.app.Application
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.justin.id_34279075.nutritrack.R
import com.justin.id_34279075.nutritrack.data.authentication.AuthManager
import com.justin.id_34279075.nutritrack.data.foodIntake.FoodCategory
import com.justin.id_34279075.nutritrack.data.foodIntake.Persona
import com.justin.id_34279075.nutritrack.data.foodIntake.TimingQuestion
import com.justin.id_34279075.nutritrack.data.foodIntake.TimingQuestionWithAnswer
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionnaireScreen(innerPaddingValues: PaddingValues, navController: NavHostController) {

    val context = LocalContext.current
    val application = context.applicationContext as Application
    val viewModel: QuestionnaireScreenViewModel = viewModel(
        factory = QuestionnaireScreenViewModel.QuestionnaireScreenViewModelFactory(application)
    )

    val userID = AuthManager.getCurrentUserID()

    val selectedFoodCategories by viewModel.selectedFoodCategories.observeAsState()
    val selectedPersona by viewModel.selectedPersona.observeAsState(null)
    val selectedTimings by viewModel.selectedTimings.observeAsState(null)
    val errorMessage by viewModel.errorMessage.observeAsState("")
    val showTimePickerForQuestion = viewModel.showTimePickerForQuestion
    val isLoadingQuestionnaireValues by viewModel.isLoadingQuestionnaireValues.observeAsState(false)

    Scaffold(
        modifier = Modifier.fillMaxSize().padding(horizontal=20.dp),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Food Intake Questionnaire") },
                navigationIcon = {
                    IconButton(onClick = {
                        AuthManager.logout()
                        navController.navigate("Login")
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if(!isLoadingQuestionnaireValues) {
            Column(
                modifier = Modifier.padding(innerPadding).verticalScroll(rememberScrollState())
            ) {

                Text(
                    text = "Tick all the food categories you can eat",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 5.dp)
                )

                FoodCategorySelection(selectedFoodCategories, viewModel::toggleFoodCategorySelection)

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Your Persona",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 5.dp)
                )

                Text("People can be broadly classified into 6 different types based on their eating preferences." +
                        "Click on each button below to find out the different types, and select the " +
                        "type that best fits you!"
                )

                Spacer(modifier = Modifier.height(10.dp))

                PersonaList(viewModel)

                viewModel.showDialog.value?.let { persona ->
                    DialogWithImage(
                        imageName = persona.personaName,
                        imageDescription = persona.description,
                        imageId = persona.image,
                        onDismiss = { viewModel.showDialog.value = null }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Which persona best fits you?",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )

                PersonaDropdownMenu(selectedPersona, viewModel::togglePersonaSelection, viewModel)

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Timings",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )

                TimingSelection(selectedTimings, viewModel)

                showTimePickerForQuestion?.let { question ->
                    TimePickerDialog(
                        currentAnswers = selectedTimings ?: emptyList(),
                        currentQuestion = question,
                        onTimeSelected = { time ->
                            viewModel.toggleTimingSelection(question, time)
                            viewModel.setShowTimePickerQuestion(null)
                        },
                        onDismiss = {
                            viewModel.setShowTimePickerQuestion(null)
                        }
                    )
                }

                Button(
                    onClick = {
                        if (userID != null) {
                            Log.d("Update", "Updating patient $userID")
                            viewModel.storePatientFoodIntake(userID)
                        }
                        if (errorMessage == "") {
                            Toast.makeText(context, "Save successful", Toast.LENGTH_SHORT).show()
                            navController.navigate("Home") {
                                popUpTo("Home") { inclusive = true }
                            }
                        }

                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.purple_500),
                        contentColor = colorResource(R.color.white)
                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally).fillMaxWidth(0.5f)

                ) {
                    Text("Save")
                }

                Spacer(modifier = Modifier.height(50.dp))

                if (errorMessage != "") {
                    AlertDialog(
                        onDismissRequest = { viewModel.clearError() },
                        title = { Text("Questionnaire Error:") },
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
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Loading Questionnaire Values...")
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator()
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun PersonaList(viewModel: QuestionnaireScreenViewModel) {

    Column {
        Persona.entries.chunked(3).forEach { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            ) {
                rowItems.forEach { persona ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {

                        Button(
                            onClick = { viewModel.showDialog.value = persona },
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(3.dp),
                            colors = ButtonDefaults
                                .buttonColors(
                                    containerColor = colorResource(R.color.purple_500),
                                    contentColor = colorResource(R.color.white)
                                )
                        ) {
                            Text(
                                text = persona.personaName,
                                modifier = Modifier.align(Alignment.CenterVertically),
                                maxLines = 2,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * This is the food category selection list.
 */
@Composable
fun FoodCategorySelection(
    selectedCategories: List<FoodCategory>?,
    onToggleCategory: (FoodCategory) -> Unit
) {
    Column(
        modifier = Modifier.padding(0.dp)
    ) {
        FoodCategory.entries.chunked(3).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                rowItems.forEach { category ->
                    Box(
                        modifier = Modifier
                            .weight(1.1f)
                            .padding(4.dp)
                    ) {
                        val isSelected = selectedCategories?.contains(category) == true
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = {
                                onToggleCategory(category)
                            },
                            modifier = Modifier.align(Alignment.CenterStart),
                            colors = CheckboxDefaults.colors(checkedColor = colorResource(R.color.purple_500))
                        )
                        Text(
                            text = category.label,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .padding(start = 40.dp)
                                .align(Alignment.CenterStart)
                        )
                    }
                }
            }
        }
    }
}

/**
 * This is the dropdown menu to choose personas.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PersonaDropdownMenu(
    selectedPersona: Persona?,
    onPersonaSelected: (Persona) -> Unit,
    viewModel: QuestionnaireScreenViewModel
) {
    val expanded = viewModel.expandedDropdownMenu

    Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
        OutlinedButton(
            onClick = { expanded.value = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.purple_500),
                contentColor = colorResource(R.color.white))
        ) {
            Text(text = selectedPersona?.personaName ?: "Select Persona")
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown Arrow",
                modifier = Modifier.size(24.dp)
            )
        }

        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            Persona.entries.forEach { persona ->
                DropdownMenuItem(
                    text = { Text(persona.personaName) },
                    onClick = {
                        onPersonaSelected(persona)
                        expanded.value = false
                    }
                )
            }
        }
    }
}

/**
 * This component consists of the entire timing selection portion of the questionnaire.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimingSelection(
    selectedTimings: List<TimingQuestionWithAnswer>?,
    viewModel: QuestionnaireScreenViewModel
) {
    val formatter = DateTimeFormatter.ofPattern("hh:mm a")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        selectedTimings?.forEach { timing ->
            TimingQuestionItem(
                question = timing.question.question,
                selectedTime = if (timing.answer.isNotEmpty())
                    LocalTime.parse(timing.answer).format(formatter)
                else "Select Time",
                onTimeClick = { viewModel.setShowTimePickerQuestion(timing.question) }
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimingQuestionItem(
    question: String,
    selectedTime: String,
    onTimeClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = question,
            modifier = Modifier.fillMaxWidth(0.6f)
        )
        Button(
            onClick = onTimeClick,
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.purple_500), contentColor = colorResource(R.color.white))

        ) {
            Text(
                text = selectedTime.format(DateTimeFormatter.ofPattern("hh:mm a"))
            )
        }
    }
}


/**
 * This represents the time picker dialog component.
 */
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimePickerDialog(
    currentAnswers: List<TimingQuestionWithAnswer>,
    currentQuestion: TimingQuestion,
    onTimeSelected: (LocalTime) -> Unit,
    onDismiss: () -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = LocalTime.now().hour,
        initialMinute = LocalTime.now().minute,
        is24Hour = false
    )

    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .then(
                    Modifier
                        .widthIn(max = 600.dp)
                        .heightIn(max = 600.dp)
                )
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Pick a time",
                )

                TimePicker(state = timePickerState,
                    modifier = Modifier.graphicsLayer(
                        scaleX = 0.8f,
                        scaleY = 0.8f,
                        transformOrigin = TransformOrigin.Center
                    ))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    TextButton(onClick = {
                        val selectedTime = LocalTime.of(timePickerState.hour, timePickerState.minute)

                        val isConflict = currentAnswers.any {
                            it.question != currentQuestion &&
                                    it.answer.isNotEmpty() &&
                                    LocalTime.parse(it.answer) == selectedTime
                        }

                        val sleepTime = currentAnswers
                            .find { it.question == TimingQuestion.SLEEP_TIME && it.answer.isNotBlank() }
                            ?.answer?.let { LocalTime.parse(it) }

                        val wakeUpTime = currentAnswers
                            .find { it.question == TimingQuestion.WAKE_UP_TIME && it.answer.isNotBlank() }
                            ?.answer?.let { LocalTime.parse(it) }

                        val isBiggestMeal = currentQuestion == TimingQuestion.BIGGEST_MEAL_TIME

                        val biggestMealTimeInvalid = if (isBiggestMeal && sleepTime != null && wakeUpTime != null) {
                            if (sleepTime.isBefore(wakeUpTime)) {
                                selectedTime.isAfter(sleepTime) && selectedTime.isBefore(wakeUpTime)
                            } else {
                                selectedTime.isAfter(sleepTime) || selectedTime.isBefore(wakeUpTime)
                            }
                        } else false

                        when {
                            isConflict -> Toast.makeText(
                                context,
                                "Time set conflicts with another time. Choose another.",
                                Toast.LENGTH_LONG
                            ).show()

                            isBiggestMeal && (sleepTime == null || wakeUpTime == null) -> Toast.makeText(
                                context,
                                "Please enter your sleeping times before your biggest meal time.",
                                Toast.LENGTH_LONG
                            ).show()

                            biggestMealTimeInvalid -> Toast.makeText(
                                context,
                                "Biggest meal time should not fall within sleeping hours.",
                                Toast.LENGTH_LONG
                            ).show()

                            else -> {
                                onTimeSelected(selectedTime)
                                onDismiss()
                            }
                        }
                    }) {
                        Text("OK")
                    }
                }
            }
        }
    }
}





/**
 * This is the dialog shown when user clicks on a persona.
 */
@Composable
fun DialogWithImage(
    imageName: String,
    imageDescription: String,
    imageId: Int,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = imageName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Image(
                    painter = painterResource(id = imageId),
                    contentDescription = imageDescription,
                    modifier = Modifier.size(200.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = imageDescription,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = onDismiss) {
                    Text("Close")
                }
            }
        }
    }
}



