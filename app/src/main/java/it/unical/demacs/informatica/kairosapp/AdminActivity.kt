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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import io.swagger.client.models.UserDTO
import it.unical.demacs.informatica.kairosapp.model.UserRole
import it.unical.demacs.informatica.kairosapp.ui.theme.KairosAppTheme
import it.unical.demacs.informatica.kairosapp.viewmodels.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminActivity(viewModel: AdminViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val currentUsername by viewModel.currentUsername.collectAsState()

    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null) {
            Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(uiState.deleteSuccessMessage) {
        if (uiState.deleteSuccessMessage != null) {
            Toast.makeText(context, uiState.deleteSuccessMessage, Toast.LENGTH_SHORT).show()
            viewModel.dismissDeleteDialog()
        }
    }

    LaunchedEffect(uiState.roleUpdateSuccessMessage) {
        if (uiState.roleUpdateSuccessMessage != null) {
            Toast.makeText(context, uiState.roleUpdateSuccessMessage, Toast.LENGTH_SHORT).show()
            viewModel.dismissRoleChangeDialog()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.ic_launcher_monochrome),
                    contentDescription = stringResource(R.string.kairos_logo),
                    modifier = Modifier.size(80.dp)
                )
                Text(
                    text = stringResource(R.string.title),
                    style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            }
            IconButton(onClick = viewModel::fetchUsers, enabled = !uiState.isLoading) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = stringResource(R.string.refresh_users),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        Spacer(Modifier.height(24.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(48.dp))
            Text(stringResource(R.string.loading_users), modifier = Modifier.padding(top = 16.dp))
        } else if (uiState.users.isEmpty()) {
            Text(
                text = uiState.errorMessage ?: stringResource(R.string.no_users_found),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 24.dp)
            )
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(uiState.users, key = { it.id }) { user ->
                    val isCurrentUser = user.username == currentUsername
                    UserCard(
                        user = user,
                        isCurrentUser = isCurrentUser,
                        onEditRoleClick = { viewModel.prepareForRoleChange(it) },
                        onDeleteClick = { viewModel.confirmDeleteUser(it) }
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = viewModel::goToPreviousPage,
                    enabled = !uiState.isLoading && uiState.currentPage > 0
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.previous_page)
                    )
                }
                Text(
                    stringResource(
                        R.string.page_info,
                        uiState.currentPage + 1,
                        uiState.totalPages
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                IconButton(
                    onClick = viewModel::goToNextPage,
                    enabled = !uiState.isLoading && uiState.currentPage < uiState.totalPages - 1
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = stringResource(R.string.next_page)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.items_per_page_label),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.width(8.dp))
                PageSizeDropdown(
                    selectedSize = uiState.pageSize,
                    onSizeSelected = { viewModel.setPageSize(it) },
                    enabled = !uiState.isLoading
                )
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.sort_by_label),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(Modifier.width(8.dp))

                SortByFieldDropdown(
                    currentSortBy = uiState.sortBy,
                    onSortBySelected = { newSortBy -> viewModel.setSortBy(newSortBy) },
                    enabled = !uiState.isLoading
                )
                Spacer(Modifier.width(8.dp))
                IconButton(
                    onClick = viewModel::toggleSortDirection,
                    enabled = !uiState.isLoading
                ) {
                    val iconVector =
                        if (uiState.sortDirection == "ASC") Icons.Default.ArrowUpward else Icons.Default.ArrowDownward
                    val description =
                        if (uiState.sortDirection == "ASC") R.string.sort_direction_asc else R.string.sort_direction_desc
                    Icon(
                        imageVector = iconVector,
                        contentDescription = stringResource(description),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }

    if (uiState.showDeleteDialog) {
        val userToDelete = uiState.userToDelete
        if (userToDelete != null) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissDeleteDialog() },
                title = { Text(stringResource(R.string.confirm_delete_user_title)) },
                text = {
                    Text(
                        stringResource(
                            R.string.confirm_delete_user_message,
                            userToDelete.username
                        )
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { viewModel.deleteUser() },
                        enabled = !uiState.isLoading
                    ) {
                        Text(stringResource(R.string.delete))
                    }
                },
                dismissButton = {
                    Button(onClick = { viewModel.dismissDeleteDialog() }) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            )
        }
    }

    if (uiState.showRoleChangeDialog) {
        val user = uiState.userToEditRole
        if (user != null) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissRoleChangeDialog() },
                title = {
                    Text(
                        stringResource(
                            R.string.change_role_title,
                            user.username
                        )
                    )
                },
                text = { Text(stringResource(R.string.change_role_message)) },
                confirmButton = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = {
                                viewModel.updateUserRole(
                                    user.id,
                                    UserRole.ADMIN
                                )
                            },
                            enabled = !uiState.isLoading && !user.role.equals(
                                UserRole.ADMIN.removePrefix("ROLE_"),
                                ignoreCase = true
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(stringResource(R.string.role_admin))
                        }
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = {
                                viewModel.updateUserRole(
                                    user.id,
                                    UserRole.ORGANIZER
                                )
                            },
                            enabled = !uiState.isLoading && !user.role.equals(
                                UserRole.ORGANIZER.removePrefix("ROLE_"),
                                ignoreCase = true
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(stringResource(R.string.role_organizer))
                        }
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.dismissRoleChangeDialog() },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Text(stringResource(R.string.cancel))
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun UserCard(
    user: UserDTO,
    isCurrentUser: Boolean,
    onEditRoleClick: (UserDTO) -> Unit,
    onDeleteClick: (UserDTO) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (!user.profileImage?.photoUrl.isNullOrBlank()) {
                AsyncImage(
                    model = user.profileImage?.photoUrl,
                    contentDescription = stringResource(R.string.profile_picture),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = stringResource(R.string.default_profile_picture),
                    modifier = Modifier.size(50.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${user.firstName} ${user.lastName}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = user.username,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .clip(AbsoluteRoundedCornerShape(4.dp))
                        .background(
                            when (user.role.uppercase()) {
                                "ADMIN" -> MaterialTheme.colorScheme.errorContainer
                                "ORGANIZER" -> MaterialTheme.colorScheme.tertiaryContainer
                                else -> MaterialTheme.colorScheme.secondaryContainer
                            }
                        )
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = user.role,
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = when (user.role.uppercase()) {
                            "ADMIN" -> MaterialTheme.colorScheme.onErrorContainer
                            "ORGANIZER" -> MaterialTheme.colorScheme.onTertiaryContainer
                            else -> MaterialTheme.colorScheme.onSecondaryContainer
                        }
                    )
                }
            }

            Row {
                IconButton(
                    onClick = { onEditRoleClick(user) },
                    enabled = !isCurrentUser
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.edit_role),
                        tint = if (isCurrentUser) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f) else MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(
                    onClick = { onDeleteClick(user) },
                    enabled = !isCurrentUser
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete_user),
                        tint = if (isCurrentUser) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f) else MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun PageSizeDropdown(selectedSize: Int, onSizeSelected: (Int) -> Unit, enabled: Boolean) {
    val items = listOf(5, 10, 20, 30)
    val expanded = remember { mutableStateOf(false) }

    Column {
        Button(onClick = { expanded.value = true }, enabled = enabled) {
            Text("$selectedSize")
            Icon(
                Icons.Default.ArrowDropDown,
                contentDescription = stringResource(R.string.show_page_size_options)
            )
        }
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            items.forEach { size ->
                DropdownMenuItem(
                    text = { Text(size.toString()) },
                    onClick = {
                        onSizeSelected(size)
                        expanded.value = false
                    }
                )
            }
        }
    }
}

@Composable
fun SortByFieldDropdown(
    currentSortBy: String,
    onSortBySelected: (String) -> Unit,
    enabled: Boolean
) {
    val sortOptions = mapOf(
        "username" to stringResource(R.string.username),
        "firstName" to stringResource(R.string.first_name),
        "lastName" to stringResource(R.string.last_name),
        "email" to stringResource(R.string.email)
    )
    val expanded = remember { mutableStateOf(false) }

    Column {
        Button(onClick = { expanded.value = true }, enabled = enabled) {
            Text(sortOptions[currentSortBy] ?: currentSortBy)
            Icon(
                Icons.Default.ArrowDropDown,
                contentDescription = stringResource(R.string.show_sort_options)
            )
        }
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            sortOptions.forEach { (sortByValue, sortByLabel) ->
                DropdownMenuItem(
                    text = { Text(sortByLabel) },
                    onClick = {
                        onSortBySelected(sortByValue)
                        expanded.value = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
fun AdminActivityPreview() {
    KairosAppTheme {
        AdminActivity()
    }
}
