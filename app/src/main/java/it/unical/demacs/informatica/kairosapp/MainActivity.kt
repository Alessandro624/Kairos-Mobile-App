package it.unical.demacs.informatica.kairosapp

import android.content.Intent
import android.net.Uri
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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
    private val _navigationEvent = MutableStateFlow<String?>(null)
    val navigationEvent: StateFlow<String?> = _navigationEvent.asStateFlow()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _authManager = AuthManager.getInstance(this)
        handleIntent(intent)

        setContent {
            KairosAppTheme {
                val navController = rememberNavController()
                val context = LocalContext.current

                LaunchedEffect(Unit) {
                    if (_authManager.isLoggedIn()) {
                        val delay = _authManager.getNextRefreshDelayMillis()
                        if (delay > 0) {
                            TokenRefreshWorker.schedule(context, delay)
                        } else {
                            Log.w("MainActivity", "Token near expiry. Forcing logout.")
                            _authManager.clearTokens()
                            TokenRefreshWorker.cancel(context)
                        }
                    }
                }

                LaunchedEffect(navigationEvent) {
                    navigationEvent.collect { route ->
                        route?.let {
                            Log.d("MainActivity", "Navigating to $it from deep link.")
                            navController.navigate(it) {
                                popUpTo(0) { inclusive = true }
                            }
                            _navigationEvent.value = null
                        }
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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        val appLinkData: Uri? = intent?.data
        appLinkData?.let { uri ->
            Log.d("MainActivity", "Deep link received: $uri")

            val accessToken = uri.getQueryParameter("token")
            val refreshToken = uri.getQueryParameter("refreshToken")

            if (!accessToken.isNullOrBlank() && !refreshToken.isNullOrBlank()) {
                _authManager.saveTokens(accessToken, refreshToken)
                Log.d("MainActivity", "Tokens received via deep link and saved!")
                _navigationEvent.update { Routes.HOME }
            } else {
                Log.w(
                    "MainActivity",
                    "Deep link did not contain valid login success data or tokens."
                )
                _navigationEvent.update { Routes.LOGIN }
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
    val currentRoute by navController.currentRoute()
    val isLoggedIn by authManager.isLoggedIn.collectAsState()
    val isAdmin by authManager.isAdmin.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            SimpleTopBarActivity(
                currentRoute = currentRoute,
                showBackButton = currentRoute != Routes.HOME && navController.previousBackStackEntry != null,
                isLoggedIn = isLoggedIn,
                isAdmin = isAdmin,
                onBackClick = { navController.popBackStack() },
                onAdminClick = { navController.navigateSingleTop(Routes.ADMIN, currentRoute) },
                onProfileClick = { navController.navigateSingleTop(Routes.PROFILE, currentRoute) },
                onLoginClick = { navController.navigateSingleTop(Routes.LOGIN, currentRoute) },
                onLogoutClick = {
                    authManager.clearTokens()
                    TokenRefreshWorker.cancel(context)
                    navController.navigateSingleTop(Routes.HOME, currentRoute)
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.HOME) { HomeActivity(Modifier.fillMaxSize()) }

            composable(Routes.LOGIN) {
                LoginActivity(
                    onNavigateToRegistration = {
                        navController.navigateSingleTop(
                            Routes.REGISTRATION,
                            currentRoute
                        )
                    },
                    onNavigateToForgotPassword = {
                        navController.navigateSingleTop(
                            Routes.FORGOT_PASSWORD,
                            currentRoute
                        )
                    },
                    onLoginSuccess = {
                        val delay = authManager.getNextRefreshDelayMillis()
                        TokenRefreshWorker.schedule(context, delay)
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.REGISTRATION) {
                RegistrationActivity(
                    onNavigateToLogin = {
                        navController.navigateSingleTop(
                            Routes.LOGIN,
                            currentRoute
                        )
                    },
                    onRegistrationSuccess = {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.REGISTRATION) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.FORGOT_PASSWORD) {
                ForgotPasswordActivity(
                    onNavigateToLogin = {
                        navController.navigateSingleTop(
                            Routes.LOGIN,
                            currentRoute
                        )
                    }
                )
            }

            composable(Routes.PROFILE) {
                if (isLoggedIn) {
                    ProfileActivity()
                } else {
                    Log.w("Nav", "Unauthorized PROFILE access.")
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = false }
                    }
                }
            }

            composable(Routes.ADMIN) {
                if (isLoggedIn && isAdmin) {
                    AdminActivity()
                } else {
                    Log.w("Nav", "Unauthorized ADMIN access.")
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            }
        }
    }
}

@Composable
fun NavHostController.currentRoute(): State<String?> {
    val navBackStackEntry by currentBackStackEntryAsState()
    return rememberUpdatedState(navBackStackEntry?.destination?.route)
}

fun NavHostController.navigateSingleTop(route: String, currentRoute: String?) {
    if (currentRoute != route) {
        navigate(route) {
            launchSingleTop = true
        }
    }
}
