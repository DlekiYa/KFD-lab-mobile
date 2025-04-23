package com.example.rickandmortycharacterview

import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Scaffold
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.room.Room
import androidx.room.migration.AutoMigrationSpec
import coil.compose.rememberImagePainter
import com.example.rickandmortycharacterview.common.database.Database
import com.example.rickandmortycharacterview.data.model.CharacterModel
import com.example.rickandmortycharacterview.data.repository.RickRepository
import com.example.rickandmortycharacterview.data.service.RickApiService
import com.example.rickandmortycharacterview.presentation.viewmodel.CharacterViewModel

import com.example.rickandmortycharacterview.ui.theme.RickAndMortyCharacterViewTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.rickandmortycharacterview.domain.entity.CharacterEntity


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val db : Database = Room.databaseBuilder(
            applicationContext,
            Database::class.java,
            "characters.db"
        ).build()

        val rep = RickRepository(RickApiService, db.charactersDAO())
        setContent {
            RickAndMortyCharacterViewTheme {
                CharactersScreen(rep = rep)
            }
        }
    }
}


sealed class CharactersScreenState {
    object Loading : CharactersScreenState()
    data class Error(val message: String) : CharactersScreenState()
    data class Content(val characters: List<CharacterEntity>) : CharactersScreenState()
}

@Composable
fun CharactersScreen(rep: RickRepository) {
    var screenState by remember { mutableStateOf<CharactersScreenState>(CharactersScreenState.Loading) }
    val coroutineScope = rememberCoroutineScope()

    fun loadCharacters(forceRefresh: Boolean) {
        coroutineScope.launch {

            screenState = CharactersScreenState.Loading
            try {
                val characters = rep.getAllCharacters(forceRefresh)
                screenState = CharactersScreenState.Content(characters)
            } catch (e: Exception) {
                Log.e("FUCK", "Error while loading characters")
                Log.e("LMAO", "I changed the sourse code")
                screenState = CharactersScreenState.Error(e.message ?: "Unknown error")
            }
        }
    }

    LaunchedEffect(Unit) {
        loadCharacters(false)
    }

    Scaffold(
        topBar = {
            Text("Rick and Morty Characters")
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { loadCharacters(true) },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh"
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = screenState) {
                is CharactersScreenState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is CharactersScreenState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { loadCharacters(false) }) {
                            Text("Retry")
                        }
                    }
                }
                is CharactersScreenState.Content -> {
                    val charVM = CharacterViewModel(rep)
                    CharacterList(charVM)
                }
            }
        }
    }
}

@Composable
fun MyLazyColumn(
    characterVM : CharacterViewModel
) {
    LaunchedEffect(Unit) {
        characterVM.loadCharacters()
    }
    val lazyListState = rememberLazyListState()

    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                if (visibleItems.isNotEmpty()) {
                    val lastVisibleItem = visibleItems.last()
                    val loadMoreThreshold = 5 // Load more when 5 items from end

                    if (lastVisibleItem.index >= characterVM.characters.value.size - loadMoreThreshold) {
                        characterVM.loadCharacters(false, true)
                    }
                }
            }
    }

    val state by characterVM.characters.collectAsState()
    LazyColumn(
        state = lazyListState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(state.size) { character ->
            CharacterItem(character = state[character])
            Divider()
        }
    }
}

@Composable
fun CharacterList(characterVM: CharacterViewModel) {
    MyLazyColumn(characterVM = characterVM)
}

@Composable
fun CharacterItem(character: CharacterEntity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberImagePainter(
                data = character.image,
                builder = {
                    size(64)
                    crossfade(true)
                }
            ),
            contentDescription = character.name,
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = character.name,
                style = MaterialTheme.typography.titleMedium
            )
            Row {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            color = when (character.status.lowercase()) {
                                "alive" -> Color.Green
                                "dead" -> Color.Red
                                else -> Color.Gray
                            },
                            shape = MaterialTheme.shapes.small
                        )
                        .align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(8.dp))
                if (character.status[0] == 'h') {
                    Log.v("ZAMN", character.toString())
                }
                Text(
                    text = "${character.status} - ${character.species}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
    }
}