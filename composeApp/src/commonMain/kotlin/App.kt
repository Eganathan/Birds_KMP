import Configs.baseURL
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun App() {
    MaterialTheme {
        val birdsViewModel: BirdsViewModel =
            getViewModel(Unit, viewModelFactory { BirdsViewModel() })

        val uiState by birdsViewModel.uiState.collectAsState()
        LaunchedEffect(birdsViewModel) {
            birdsViewModel.fetchImages()
        }

        if (uiState.content == null) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center).fillMaxSize(0.6f)
                )
            }
        } else {
            BirdsListPage(content = uiState.content.orEmpty())
        }
    }
}

@Composable
fun BirdsListPage(content: List<BirdImage>) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(180.dp), content = {
            items(items = content) {
                BirdItem(it.path)
            }
        })
}

@Composable
fun BirdItem(imageUrl: String) {
    KamelImage(
        modifier = Modifier.fillMaxWidth().aspectRatio(1f),
        resource = asyncPainterResource(data = "$baseURL/$imageUrl"),
        contentAlignment = Alignment.Center,
        contentScale = ContentScale.Crop,
        contentDescription = "",
        onLoading = {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    )
}