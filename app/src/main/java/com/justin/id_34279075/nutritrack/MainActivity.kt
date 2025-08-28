package com.justin.id_34279075.nutritrack

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.justin.id_34279075.nutritrack.data.authentication.AuthManager
import com.justin.id_34279075.nutritrack.data.database.NutriTrackDatabase
import com.justin.id_34279075.nutritrack.data.helpers.loadCSVIntoDatabase
import com.justin.id_34279075.nutritrack.ui.screen.changeDetails.ChangeDetailsScreen
import com.justin.id_34279075.nutritrack.ui.screen.clinician.ClinicianScreen
import com.justin.id_34279075.nutritrack.ui.screen.home.HomeScreen
import com.justin.id_34279075.nutritrack.ui.screen.insights.InsightsScreen
import com.justin.id_34279075.nutritrack.ui.screen.login.LoginScreen
import com.justin.id_34279075.nutritrack.ui.screen.nutricoach.NutriCoachScreen
import com.justin.id_34279075.nutritrack.ui.screen.questionnaire.QuestionnaireScreen
import com.justin.id_34279075.nutritrack.ui.screen.register.RegisterScreen
import com.justin.id_34279075.nutritrack.ui.screen.resetPassword.ResetPasswordScreen
import com.justin.id_34279075.nutritrack.ui.screen.settings.SettingsScreen
import com.justin.id_34279075.nutritrack.ui.screen.welcome.WelcomeScreen
import com.justin.id_34279075.nutritrack.ui.theme.NutritrackTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AuthManager.init(applicationContext)

        enableEdgeToEdge()

        val patientDAO = NutriTrackDatabase.getDatabase(this).patientDAO()
        /**
         * We will check whether we are on first launch, if so, load the csv data into room db.
         */
        val sharedPreferences = getSharedPreferences("launched", MODE_PRIVATE)
        val checkFirstLaunch = sharedPreferences.getBoolean("first", true)

        if (checkFirstLaunch) {
            CoroutineScope(Dispatchers.IO).launch {
                loadCSVIntoDatabase(this@MainActivity, patientDAO)
                sharedPreferences.edit().putBoolean("first", false).apply()
            }
        }

        setContent {

            val prefs = getSharedPreferences("darkMode", Context.MODE_PRIVATE)
            var isDarkMode by remember { mutableStateOf(prefs.getBoolean("darkMode", false)) }

            DisposableEffect(Unit) {

                // Switch to dark mode if sharedPreference's value is changed
                val themeListener = SharedPreferences.OnSharedPreferenceChangeListener {
                    _, key ->
                    if (key == "darkMode") {
                        isDarkMode = prefs.getBoolean("darkMode", false)
                    }
                }
                prefs.registerOnSharedPreferenceChangeListener(themeListener) // registers listener

                // Prevents memory leaks
                onDispose {
                    sharedPreferences.unregisterOnSharedPreferenceChangeListener(themeListener)
                }
            }


            NutritrackTheme(darkTheme = isDarkMode) {
                val userID = AuthManager.getCurrentUserID()

                // Ensures when userID changes, everything is recomposed cleanly
                key(userID) {
                    val navController = rememberNavController()
                    NutriAppScaffold(navController)
                }
            }
        }
    }
}

/**
 * This function creates a special Scaffold component so that only qualified routes can display
 * the bottom navigation bar.
 *
 * @param navController NavHostController used to control app navigation between destinations.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NutriAppScaffold(navController: NavHostController)
{
    // Holds information required about the current navigation state
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    // Retrieve current route information
    val currentDestination = navBackStackEntry?.destination?.route

    // These routes have no bottom bar
    val noBottomBarRoutes = listOf("Welcome", "Login", "Register", "Questionnaire", "ResetPassword")

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (currentDestination !in noBottomBarRoutes) {
                NutriAppBottomAppBar(navController)
            }
        }
    ) { innerPadding ->
        NutriAppNavHost(innerPadding, navController)
    }
}

/**
 * Main NavHost component that handles the route controller logic.
 *
 * @param innerPadding Inner padding values.
 * @param navController NavHostController used to control app navigation between destinations.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NutriAppNavHost(
    innerPadding: PaddingValues,
    navController: NavHostController,
) {
    val userID = AuthManager.getCurrentUserID()
    Log.d("Current user", userID.toString())

    // Redirect user to Questionnaire screen if authenticated, else redirect to Welcome screen.
    val startDestination = if (userID.isNullOrEmpty()) "Welcome" else "Questionnaire"

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable("Welcome") {
            WelcomeScreen(innerPadding, navController)
        }

        composable("Login") {
            LoginScreen(innerPadding, navController)
        }

        composable("Register") {
            RegisterScreen(innerPadding, navController)
        }
        composable("Home") {
            HomeScreen(innerPadding, navController)
        }

        composable("Questionnaire") {
            QuestionnaireScreen(innerPadding, navController)
        }

        composable("Insights") {
            InsightsScreen(innerPadding, navController)
        }

        composable("NutriCoach") {
            NutriCoachScreen(innerPadding, navController)
        }

        composable("Settings") {
            SettingsScreen(innerPadding, navController)
        }

        composable("Clinician") {
            ClinicianScreen(innerPadding,  navController)
        }

        composable("ChangeDetails") {
            ChangeDetailsScreen(innerPadding, navController)
        }

        composable("ResetPassword") {
            ResetPasswordScreen(innerPadding, navController)
        }
    }
}

/**
 * Bottom App Bar component that is responsible for the display and click mechanism.
 *
 * @param navController: NavHostController used to control app navigation between destinations.
 */
@Composable
fun NutriAppBottomAppBar(navController: NavHostController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // These routes will have an icon in the bottom app bar
    val items = listOf("Home", "Insights", "NutriCoach", "Settings")

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    when (item) {
                        "Home" -> Icon(Icons.Filled.Home, contentDescription = "Home")
                        "Insights" -> Icon(Icons.Filled.AccountBox, contentDescription = "Insights")
                        "NutriCoach" -> Icon(Icons.Filled.Face, contentDescription = "NutriCoach")
                        "Settings" -> Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                },
                label = { Text(item) },
                selected = currentRoute == item,
                onClick = {
                    navController.navigate(item) {
                        // Clear previous destinations above start (Questionnaire) excluding the current item
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true // save the state to be used in further restoration
                        }
                        launchSingleTop = true // wont recreate same screen at the top of the stack
                        restoreState = true // restores previous state
                    }
                }
            )
        }
    }
}
