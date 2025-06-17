package it.unical.demacs.informatica.kairosapp

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import it.unical.demacs.informatica.kairosapp.ui.theme.KairosAppTheme
import it.unical.demacs.informatica.kairosapp.viewmodels.PasswordChangeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordChangeActivity(
    onNavigateBack: () -> Unit,
    viewModel: PasswordChangeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.passwordChangeSuccess) {
        if (uiState.passwordChangeSuccess) {
            Toast.makeText(context, R.string.password_change_success, Toast.LENGTH_SHORT).show()
            viewModel.resetState()
            onNavigateBack()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null) {
            Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher_monochrome),
                contentDescription = stringResource(R.string.kairos_logo),
                modifier = Modifier.size(100.dp)
            )
            Text(
                text = stringResource(R.string.title),
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )
        }
        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = uiState.currentPassword,
            onValueChange = viewModel::updateCurrentPassword,
            label = { Text(stringResource(R.string.current_password)) },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = if (uiState.isCurrentPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image =
                    if (uiState.isCurrentPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description =
                    if (uiState.isCurrentPasswordVisible) stringResource(R.string.hide_password) else stringResource(
                        R.string.show_password
                    )
                IconButton(onClick = { viewModel.toggleCurrentPasswordVisibility() }) {
                    Icon(imageVector = image, contentDescription = description)
                }
            },
            singleLine = true,
            isError = uiState.currentPasswordError != null,
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        )
        if (uiState.currentPasswordError != null) {
            Text(
                text = uiState.currentPasswordError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 4.dp)
            )
        }
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.newPassword,
            onValueChange = viewModel::updateNewPassword,
            label = { Text(stringResource(R.string.new_password)) },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = if (uiState.isNewPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image =
                    if (uiState.isNewPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description =
                    if (uiState.isNewPasswordVisible) stringResource(R.string.hide_password) else stringResource(
                        R.string.show_password
                    )
                IconButton(onClick = { viewModel.toggleNewPasswordVisibility() }) {
                    Icon(imageVector = image, contentDescription = description)
                }
            },
            singleLine = true,
            isError = uiState.newPasswordError != null,
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        )
        if (uiState.newPasswordError != null) {
            Text(
                text = uiState.newPasswordError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 4.dp)
            )
        }
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.confirmNewPassword,
            onValueChange = viewModel::updateConfirmNewPassword,
            label = { Text(stringResource(R.string.confirm_new_password)) },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = if (uiState.isConfirmNewPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image =
                    if (uiState.isConfirmNewPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description =
                    if (uiState.isConfirmNewPasswordVisible) stringResource(R.string.hide_password) else stringResource(
                        R.string.show_password
                    )
                IconButton(onClick = { viewModel.toggleConfirmNewPasswordVisibility() }) {
                    Icon(imageVector = image, contentDescription = description)
                }
            },
            singleLine = true,
            isError = uiState.confirmNewPasswordError != null,
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        )
        if (uiState.confirmNewPasswordError != null) {
            Text(
                text = uiState.confirmNewPasswordError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 4.dp)
            )
        }
        Spacer(Modifier.height(24.dp))

        Button(
            onClick = viewModel::changePassword,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = !uiState.isLoading
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(stringResource(R.string.change_password_button))
            }
        }
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onNavigateBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = !uiState.isLoading
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.back)
            )
            Spacer(Modifier.width(8.dp))
            Text(stringResource(R.string.back))
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
fun PasswordChangeActivityPreview() {
    KairosAppTheme {
        PasswordChangeActivity(onNavigateBack = {})
    }
}
