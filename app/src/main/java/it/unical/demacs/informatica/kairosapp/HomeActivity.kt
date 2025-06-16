package it.unical.demacs.informatica.kairosapp

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material3.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import io.swagger.client.models.EventDTO
import io.swagger.client.models.SectorDTO
import it.unical.demacs.informatica.kairosapp.ui.theme.KairosAppTheme
import it.unical.demacs.informatica.kairosapp.viewmodels.HomeViewModel
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeActivity(
    onNavigateToLogin: () -> Unit,
    onNavigateToAdmin: () -> Unit,
    onNavigateToProfile: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val lazyListState = rememberLazyListState()
    val coroutineScope = androidx.compose.runtime.rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.mipmap.ic_launcher_monochrome),
                            contentDescription = stringResource(R.string.kairos_logo),
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.title))
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToAdmin) {
                        Icon(
                            Icons.Default.AdminPanelSettings,
                            contentDescription = stringResource(R.string.admin_panel)
                        )
                    }
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(
                            Icons.Default.AccountCircle,
                            contentDescription = stringResource(R.string.user_profile)
                        )
                    }
                    IconButton(onClick = onNavigateToLogin) {
                        Icon(
                            Icons.AutoMirrored.Filled.Login,
                            contentDescription = stringResource(R.string.login)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            state = lazyListState,
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(vertical = 32.dp, horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.welcome_message),
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.site_description),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(24.dp))
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                lazyListState.animateScrollToItem(index = 1)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(stringResource(R.string.explore_events))
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            item {
                if (uiState.isLoading) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = stringResource(R.string.events_loading),
                            modifier = Modifier.padding(top = 8.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else if (uiState.errorMessage != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = uiState.errorMessage!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.fetchEventsFromNetwork() },
                            modifier = Modifier.fillMaxWidth(0.6f)
                        ) {
                            Text(stringResource(R.string.retry))
                        }
                    }
                } else if (uiState.events.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_events_available),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.fetchEventsFromNetwork() },
                            modifier = Modifier.fillMaxWidth(0.6f)
                        ) {
                            Text(stringResource(R.string.refresh))
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            if (!uiState.isLoading && uiState.events.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.upcoming_events),
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                    )
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(uiState.events.sortedBy { it.dateTime }.take(10)) { event ->
                            EventCard(event = event) {
                                Log.d("HomeActivity", "Clicked on upcoming event: ${event.title}")
                            }
                        }
                    }
                    Spacer(Modifier.height(24.dp))
                }

                uiState.categorizedEvents.forEach { (category, eventsInCategory) ->
                    item {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = category,
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                    bottom = 8.dp
                                )
                            )
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(eventsInCategory) { event ->
                                    EventCard(event = event) {
                                        Log.d(
                                            "HomeActivity",
                                            "Clicked on categorized event: ${event.title}"
                                        )
                                    }
                                }
                            }
                            Spacer(Modifier.height(24.dp))
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventCard(event: EventDTO, onDetailsClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(300.dp),
        onClick = onDetailsClick,
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                val imageUrl = event.images?.firstOrNull()

                Image(
                    painter = rememberAsyncImagePainter(
                        model = imageUrl,
                        error = painterResource(id = R.drawable.ic_image_placeholder),
                        placeholder = painterResource(id = R.drawable.ic_image_placeholder)
                    ),
                    contentDescription = "${stringResource(R.string.event_image_description)}: ${event.title}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                val firstSectorPrice = event.sectors.firstOrNull()?.price
                val priceText = if (firstSectorPrice != null && firstSectorPrice > 0) {
                    "%.2f €".format(firstSectorPrice)
                } else {
                    stringResource(R.string.free)
                }
                val chipColors = if (firstSectorPrice != null && firstSectorPrice > 0) {
                    AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        labelColor = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        labelColor = MaterialTheme.colorScheme.onSecondary
                    )
                }

                AssistChip(
                    onClick = { },
                    label = {
                        Text(
                            priceText,
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    colors = chipColors
                )
            }
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxHeight()
            ) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))

                val formattedDate = try {
                    val dateTimeObj = OffsetDateTime.parse(event.dateTime)
                    dateTimeObj.format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm"))
                } catch (e: DateTimeParseException) {
                    Log.e("EventCard", "Parsing date error: ${event.dateTime}", e)
                    stringResource(R.string.na)
                } catch (_: NullPointerException) {
                    stringResource(R.string.na)
                }

                Text(
                    text = "${stringResource(R.string.date)}: $formattedDate",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${stringResource(R.string.max_participants)}: ${event.maxParticipants}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = event.description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = onDetailsClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.details))
                }
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
fun HomeActivityPreview() {
    KairosAppTheme {
        HomeActivity(
            onNavigateToLogin = {},
            onNavigateToAdmin = {},
            onNavigateToProfile = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EventCardPreview() {
    KairosAppTheme {
        EventCard(
            event = EventDTO(
                id = "1",
                title = "Concerto Rock Estivo - Anteprima",
                description = "Una serata indimenticabile con le migliori band locali e un'atmosfera unica.",
                category = "Musica",
                dateTime = OffsetDateTime.now().plusDays(7).toString(),
                maxParticipants = 500,
                organizerId = "101",
                structureId = "201",
                sectors = listOf(SectorDTO(name = "Parterre", price = 25.0f)),
                images = listOf("https://picsum.photos/seed/picsum/200/300")
            ),
            onDetailsClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EventCardFreePreview() {
    KairosAppTheme {
        EventCard(
            event = EventDTO(
                id = "2",
                title = "Mostra d'Arte Moderna",
                description = "Esplora le ultime tendenze dell'arte contemporanea in questa mostra imperdibile.",
                category = "Arte",
                dateTime = OffsetDateTime.now().plusMonths(1).toString(),
                maxParticipants = 100,
                organizerId = "102",
                structureId = "202",
                sectors = listOf(SectorDTO(name = "Ingresso Libero", price = 0.0f)),
                images = listOf("https://picsum.photos/seed/art/200/300")
            ),
            onDetailsClick = {}
        )
    }
}
