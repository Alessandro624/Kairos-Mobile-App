package it.unical.demacs.informatica.kairosapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.unical.demacs.informatica.kairosapp.ui.theme.KairosAppTheme

object Routes {
    const val HOME = "home"
    const val LOGIN = "login"
    const val REGISTRATION = "registration"
    const val FORGOT_PASSWORD = "forgot_password"
    const val PROFILE = "profile"
    const val ADMIN = "admin"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KairosAppTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigationHost(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun AppNavigationHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        modifier = modifier
    ) {
        composable(Routes.HOME) {
            HomeActivity(
                onNavigateToLogin = { navController.navigate(Routes.LOGIN) },
                onNavigateToAdmin = { navController.navigate(Routes.ADMIN) },
                onNavigateToProfile = { navController.navigate(Routes.PROFILE) }
            )
        }
        composable(Routes.LOGIN) {
            LoginActivity(
                onNavigateToRegistration = { navController.navigate(Routes.REGISTRATION) },
                onNavigateToForgotPassword = { navController.navigate(Routes.FORGOT_PASSWORD) },
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.REGISTRATION) {
            RegistrationActivity(
                onNavigateToLogin = { navController.navigate(Routes.LOGIN) },
                onRegistrationSuccess = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTRATION) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.FORGOT_PASSWORD) {
            ForgotPasswordActivity(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToLogin = { navController.navigate(Routes.LOGIN) }
            )
        }
        composable(Routes.PROFILE) {
            ProfileActivity(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Routes.ADMIN) {
            AdminActivity(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
