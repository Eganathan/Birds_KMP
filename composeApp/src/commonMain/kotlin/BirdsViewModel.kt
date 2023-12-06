import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BirdsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(BirdsViewUIState())
    val uiState = _uiState.asStateFlow()

    val apiURL = "https://sebi.io/demo-image-api/pictures.json"
    val httpClient: HttpClient = HttpClient() {
        install(ContentNegotiation) {
            json()
        }
    }

    fun fetchImages() {
        viewModelScope.launch {
            val images = getImages()
            _uiState.update {
                it.copy(content = images)
            }
        }
    }

    private suspend fun getImages(): List<BirdImage> = httpClient.get(apiURL).body()


    override fun onCleared() {
        httpClient.close()
        super.onCleared()
    }
}