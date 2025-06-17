package it.unical.demacs.informatica.kairosapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTopBarActivity(
    currentRoute: String?,
    showBackButton: Boolean,
    isLoggedIn: Boolean,
    isAdmin: Boolean,
    onBackClick: () -> Unit,
    onAdminClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLoginClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier,
        title = { TopBarTitle(currentRoute) },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        },
        actions = {
            if (isLoggedIn) {
                if (isAdmin && currentRoute != Routes.ADMIN) {
                    IconButton(onClick = onAdminClick) {
                        Icon(
                            Icons.Default.AdminPanelSettings,
                            contentDescription = stringResource(R.string.admin_panel)
                        )
                    }
                }

                if (currentRoute != Routes.PROFILE) {
                    IconButton(onClick = onProfileClick) {
                        Icon(
                            Icons.Default.AccountCircle,
                            contentDescription = stringResource(R.string.user_profile)
                        )
                    }
                }

                IconButton(onClick = onLogoutClick) {
                    Icon(
                        Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = stringResource(R.string.logout),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            } else if (currentRoute != Routes.LOGIN && currentRoute != Routes.REGISTRATION && currentRoute != Routes.FORGOT_PASSWORD) {
                IconButton(onClick = onLoginClick) {
                    Icon(
                        Icons.AutoMirrored.Filled.Login,
                        contentDescription = stringResource(R.string.login)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground
        )
    )
}

@Composable
private fun TopBarTitle(currentRoute: String?) {
    when (currentRoute) {
        Routes.HOME -> Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher_monochrome),
                contentDescription = stringResource(R.string.kairos_logo),
                modifier = Modifier.size(40.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(stringResource(R.string.title))
        }

        Routes.LOGIN -> Text(stringResource(R.string.login))
        Routes.REGISTRATION -> Text(stringResource(R.string.registration_title))
        Routes.FORGOT_PASSWORD -> Text(stringResource(R.string.forgot_password_title))
        Routes.PROFILE -> Text(stringResource(R.string.profile_title))
        Routes.ADMIN -> Text(stringResource(R.string.admin_panel_title))
        else -> Text(stringResource(R.string.title))
    }
}

@Preview(showBackground = true)
@Composable
fun SimpleTopBarActivityPreviewLoggedInHome() {
    SimpleTopBarActivity(
        currentRoute = Routes.HOME,
        showBackButton = false,
        isLoggedIn = true,
        isAdmin = false,
        onBackClick = {},
        onAdminClick = {},
        onProfileClick = {},
        onLoginClick = {},
        onLogoutClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun SimpleTopBarActivityPreviewNotLoggedInLogin() {
    SimpleTopBarActivity(
        currentRoute = Routes.LOGIN,
        showBackButton = true,
        isLoggedIn = false,
        isAdmin = false,
        onBackClick = {},
        onAdminClick = {},
        onProfileClick = {},
        onLoginClick = {},
        onLogoutClick = {}
    )
}
