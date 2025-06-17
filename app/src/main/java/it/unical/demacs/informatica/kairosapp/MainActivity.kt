package it.unical.demacs.informatica.kairosapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import it.unical.demacs.informatica.kairosapp.security.AuthManager
import it.unical.demacs.informatica.kairosapp.security.TokenRefreshWorker
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
    private lateinit var _authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _authManager = AuthManager(this)

        setContent {
            KairosAppTheme {
                val navController = rememberNavController()
                val context = LocalContext.current

                if (_authManager.isLoggedIn()) {
                    val initialDelay = _authManager.getNextRefreshDelayMillis()
                    if (initialDelay > 0) {
                        TokenRefreshWorker.schedule(context, initialDelay)
                    } else {
                        Log.w(
                            "MainActivity",
                            "Token already expired or near expiration on app launch. Forcing re-login."
                        )
                        _authManager.clearTokens()
                        TokenRefreshWorker.cancel(context)
                    }
                }

                AppNavigationHost(
                    navController = navController,
                    authManager = _authManager,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigationHost(
    navController: NavHostController,
    authManager: AuthManager,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBackButton = currentRoute != Routes.HOME && navController.previousBackStackEntry != null
    val isLoggedInState by authManager.isLoggedIn.collectAsState()
    val isAdminState by authManager.isAdmin.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            SimpleTopBarActivity(
                currentRoute = currentRoute,
                showBackButton = showBackButton,
                isLoggedIn = isLoggedInState,
                isAdmin = isAdminState,
                onBackClick = { navController.popBackStack() },
                onAdminClick = { navController.navigate(Routes.ADMIN) },
                onProfileClick = { navController.navigate(Routes.PROFILE) },
                onLoginClick = { navController.navigate(Routes.LOGIN) },
                onLogoutClick = {
                    authManager.clearTokens()
                    TokenRefreshWorker.cancel(context)
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.HOME) {
                HomeActivity(
                    modifier = Modifier.fillMaxSize()
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
                        val initialDelay = authManager.getNextRefreshDelayMillis()
                        TokenRefreshWorker.schedule(context, initialDelay)
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
                    onNavigateToLogin = { navController.navigate(Routes.LOGIN) }
                )
            }
            composable(Routes.PROFILE) {
                if (isLoggedInState) {
                    ProfileActivity()
                } else {
                    Log.w(
                        "AppNavigationHost",
                        "Attempted to access PROFILE route without login. Redirecting."
                    )
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            }
            composable(Routes.ADMIN) {
                if (isLoggedInState && isAdminState) {
                    AdminActivity()
                } else {
                    Log.w(
                        "AppNavigationHost",
                        "Attempted to access ADMIN route without sufficient permissions. Redirecting."
                    )
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            }
        }
    }
}
