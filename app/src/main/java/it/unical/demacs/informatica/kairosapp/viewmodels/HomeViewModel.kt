package it.unical.demacs.informatica.kairosapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import io.swagger.client.apis.EventControllerApi
import io.swagger.client.models.EventDTO
import io.swagger.client.models.PageEventDTO
import io.swagger.client.models.Pageable
import io.swagger.client.models.SectorDTO
import it.unical.demacs.informatica.kairosapp.R
import it.unical.demacs.informatica.kairosapp.model.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.OffsetDateTime
import kotlin.random.Random

data class HomeUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val events: List<EventDTO> = emptyList(),
    val categorizedEvents: Map<String, List<EventDTO>> = emptyMap()
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val _application = application
    private val _uiState = MutableStateFlow(HomeUiState())
    private val _eventApi = EventControllerApi()
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            AppDatabase.getInstance(context = _application.applicationContext).eventDao()
                .getAllEvents().collect { eventsFromDb ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            events = eventsFromDb,
                            categorizedEvents = eventsFromDb.groupBy { it.category }
                        )
                    }
                }
        }
        fetchEventsFromNetwork()
    }

    fun fetchEventsFromNetwork() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val pageableRequest = Pageable()
                val fetchedPage: PageEventDTO =
                    withContext(Dispatchers.IO) { _eventApi.getAllEvents(pageable = pageableRequest) }

                val fetchedEvents: List<EventDTO> = (fetchedPage.content as? List<*>)
                    ?.filterIsInstance<EventDTO>()
                    ?: emptyList()

                if (fetchedEvents.isEmpty()) {
                    val sampleEvents = generateSampleEvents()
                    AppDatabase.getInstance(context = _application.applicationContext).eventDao()
                        .insertEvents(sampleEvents)
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            errorMessage = _application.getString(R.string.message_empty_network_response_showing_samples),
                            categorizedEvents = sampleEvents.groupBy { it.category }
                        )
                    }
                } else {
                    AppDatabase.getInstance(context = _application.applicationContext).eventDao()
                        .insertEvents(fetchedEvents)
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            categorizedEvents = fetchedEvents.groupBy { it.category }
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching events from network: ${e.message}", e)
                val currentEvents = _uiState.value.events
                val errorMessage = if (currentEvents.isNotEmpty()) {
                    _application.getString(R.string.message_empty_network_response_showing_samples)
                } else {
                    _application.getString(R.string.error_network_generic)
                }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = errorMessage
                    )
                }
            }
        }
    }

    private fun generateSampleEvents(): List<EventDTO> {
        val sampleList = mutableListOf<EventDTO>()
        val titles = listOf(
            "Incontro di Calcio Amichevole",
            "Serata di Giochi da Tavolo",
            "Workshop di Pittura",
            "Corsa Podistica Mattutina",
            "Concerto Rock Estivo",
            "Festival del Cinema Indipendente",
            "Mostra d'Arte Moderna",
            "Lezione di Yoga all'Aperto"
        )
        val descriptions = listOf(
            "Partita amichevole tra appassionati, aperta a tutti.",
            "Divertiti con una vasta selezione di giochi da tavolo.",
            "Impara le basi della pittura ad acquerello con un artista esperto.",
            "Inizia la giornata con una corsa energizzante nel parco.",
            "Una serata indimenticabile con le migliori band locali e un'atmosfera unica.",
            "Rassegna dei migliori cortometraggi e documentari da tutto il mondo.",
            "Esplora le nuove tendenze dell'arte contemporanea.",
            "Ricarica le energie con una sessione di yoga nel verde."
        )
        val categories = listOf("Sport", "Giochi", "Arte", "Salute", "Musica", "Cinema")
        val sampleImages = listOf(
            "https://picsum.photos/seed/picsum/200/300",
            "https://picsum.photos/seed/picsum/200/300",
            "https://picsum.photos/seed/picsum/200/300",
            "https://picsum.photos/seed/picsum/200/300"
        )

        repeat(20) { i ->
            val randomTitle = titles.random()
            val randomDescription = descriptions.random()
            val randomCategory = categories.random()
            val randomImage = sampleImages.random()

            val sampleSectors = if (Random.nextBoolean()) {
                listOfNotNull(
                    SectorDTO(
                        name = "Standard",
                        price = Random.nextFloat() * 30 + 5
                    ),
                    SectorDTO(
                        name = "VIP",
                        price = Random.nextFloat() * 50 + 40
                    ).takeIf { Random.nextBoolean() }
                )
            } else {
                emptyList()
            }

            sampleList.add(
                EventDTO(
                    id = Random.nextLong(1000, 9999).toString(),
                    title = "$randomTitle - ${
                        OffsetDateTime.now().plusDays(i.toLong()).dayOfMonth
                    }",
                    description = randomDescription,
                    category = randomCategory,
                    dateTime = OffsetDateTime.now().plusDays(i.toLong())
                        .plusHours(Random.nextLong(9, 20)).toString(),
                    maxParticipants = Random.nextInt(10, 100),
                    organizerId = Random.nextLong(1, 5).toString(),
                    structureId = Random.nextLong(1, 5).toString(),
                    sectors = sampleSectors,
                    images = listOf(randomImage)
                )
            )
        }
        return sampleList
    }
}
