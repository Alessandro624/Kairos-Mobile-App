package it.unical.demacs.informatica.kairosapp

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import it.unical.demacs.informatica.kairosapp.ui.theme.KairosAppTheme
import it.unical.demacs.informatica.kairosapp.viewmodels.UserProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileActivity(
    onNavigateToChangePassword: () -> Unit,
    viewModel: UserProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            Toast.makeText(context, R.string.profile_update_success, Toast.LENGTH_SHORT).show()
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
        Spacer(Modifier.height(12.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(48.dp))
            Text(
                stringResource(R.string.loading_profile_data),
                modifier = Modifier.padding(top = 16.dp)
            )
        } else {
            if (!uiState.profileImageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = uiState.profileImageUrl,
                    contentDescription = stringResource(R.string.profile_picture),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = stringResource(R.string.default_profile_picture),
                    modifier = Modifier.size(120.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = uiState.firstName,
                onValueChange = viewModel::updateFirstName,
                label = { Text(stringResource(R.string.first_name)) },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                isError = uiState.firstNameError != null,
                enabled = uiState.isEditing,
                readOnly = !uiState.isEditing,
                modifier = Modifier.fillMaxWidth()
            )
            if (uiState.firstNameError != null) {
                Text(
                    text = uiState.firstNameError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 4.dp)
                )
            }
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.lastName,
                onValueChange = viewModel::updateLastName,
                label = { Text(stringResource(R.string.last_name)) },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                isError = uiState.lastNameError != null,
                enabled = uiState.isEditing,
                readOnly = !uiState.isEditing,
                modifier = Modifier.fillMaxWidth()
            )
            if (uiState.lastNameError != null) {
                Text(
                    text = uiState.lastNameError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 4.dp)
                )
            }
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.username,
                onValueChange = { },
                label = { Text(stringResource(R.string.username)) },
                leadingIcon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
                singleLine = true,
                readOnly = true,
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.email,
                onValueChange = { },
                label = { Text(stringResource(R.string.email)) },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                singleLine = true,
                readOnly = true,
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.phoneNumber.orEmpty(),
                onValueChange = viewModel::updatePhoneNumber,
                label = { Text(stringResource(R.string.phone_number_optional)) },
                leadingIcon = { Icon(Icons.Default.Call, contentDescription = null) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                isError = uiState.phoneNumberError != null,
                enabled = uiState.isEditing,
                readOnly = !uiState.isEditing,
                modifier = Modifier.fillMaxWidth()
            )
            if (uiState.phoneNumberError != null) {
                Text(
                    text = uiState.phoneNumberError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 4.dp)
                )
            }
            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                if (uiState.isEditing) {
                    Button(
                        onClick = viewModel::saveUserProfile,
                        enabled = !uiState.isLoading,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Icon(
                                Icons.Default.Save,
                                contentDescription = stringResource(R.string.save)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(stringResource(R.string.save))
                        }
                    }
                    Spacer(Modifier.width(16.dp))
                    Button(
                        onClick = viewModel::toggleEditMode,
                        enabled = !uiState.isLoading,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                } else {
                    Button(
                        onClick = viewModel::toggleEditMode,
                        enabled = !uiState.isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = stringResource(R.string.edit_profile)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.edit_profile))
                    }
                }
            }
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onNavigateToChangePassword,
                enabled = !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = stringResource(R.string.change_password_button)
                )
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.change_password_button))
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
fun ProfileActivityPreview() {
    KairosAppTheme {
        ProfileActivity(onNavigateToChangePassword = {})
    }
}
