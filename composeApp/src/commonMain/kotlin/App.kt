import Configs.baseURL
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun App() {
    MaterialTheme {
        val birdsViewModel: BirdsViewModel =getViewModel(Unit, viewModelFactory { BirdsViewModel() })
        val uiState by birdsViewModel.uiState.collectAsState()

        LaunchedEffect(birdsViewModel) {
            birdsViewModel.fetchImages()
        }
        Scaffold (
            topBar = {
                TopAppBar(
                    title = {
                        Text("Just Birds ")
                        Text("by eknath.dev", style = MaterialTheme.typography.subtitle1, fontSize = 10.sp, modifier = Modifier.offset(y = 5.dp))
                    },
                    navigationIcon = {
                        IconButton(onClick = {}){Icon(Icons.Default.Menu,"")}},
                    backgroundColor = MaterialTheme.colors.background
                )
            }
        ){
           Surface (modifier = Modifier.padding(it).fillMaxSize()){
               if (uiState.content == null) {
                   Box(modifier = Modifier.fillMaxSize()) {
                       CircularProgressIndicator(
                           modifier = Modifier.align(Alignment.Center).fillMaxSize(0.6f)
                       )
                   }
               } else {
                   BirdsListPage(
                       content = if(uiState.selectedCategory == null)uiState.content.orEmpty() else uiState.selectedContent.orEmpty(),
                       categorys = uiState.categories,
                       onSelection = {birdsViewModel.selectCategory(it)},
                       selectedCategory = uiState.selectedCategory)
               }
           }
        }
    }
}

@Composable
fun BirdsListPage(content: List<BirdImage>, categorys:Set<String>, onSelection:(String)->Unit, selectedCategory:String?) {
    Column (modifier = Modifier.fillMaxSize()){
        Row{
            for(item in categorys){
                Button(
                    onClick = {onSelection(item)},
                    colors = ButtonDefaults.buttonColors(backgroundColor = if(item == selectedCategory) MaterialTheme.colors.primary else MaterialTheme.colors.secondary),
                    modifier = Modifier.weight(1.0f).padding(horizontal = 2.5.dp)
                ){
                    Text(item)
                }
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Adaptive(180.dp), content = {
                items(items = content) {
                    BirdItem(it.path)
                }
            })
    }
}

@Composable
fun BirdItem(imageUrl: String) {
    KamelImage(
        modifier = Modifier.fillMaxWidth().aspectRatio(1f),
        resource = asyncPainterResource(data = "$baseURL/$imageUrl"),
        contentAlignment = Alignment.Center,
        contentScale = ContentScale.Crop,
        contentDescription = "",
        animationSpec = tween(
            easing = LinearOutSlowInEasing,
            delayMillis = 350,
            durationMillis = 500
        ),
        onLoading = {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(0.2f).aspectRatio(1f)
            )
        }
    )
}