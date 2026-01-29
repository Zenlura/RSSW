package de.radstation.werkstatt.presentation.auftraege.create

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun CreateAuftragScreen(
    navController: NavController,
    viewModel: CreateAuftragViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    
    // Kamera-Permission
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    
    // Foto aufnehmen
    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            viewModel.addPhoto()
        }
    }
    
    // Auftragsnummer generieren
    val generatedAuftragsnummer = remember {
        System.currentTimeMillis().toString().takeLast(6)
    }
    
    LaunchedEffect(Unit) {
        viewModel.setAuftragsnummer(generatedAuftragsnummer)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Neuer Auftrag") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Zurück")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Auftragsnummer (automatisch)
            OutlinedTextField(
                value = state.auftragsnummer,
                onValueChange = {},
                label = { Text("Auftragsnummer") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                leadingIcon = { Icon(Icons.Default.Info, null) }
            )
            
            // Schlüsselnummer
            OutlinedTextField(
                value = state.schluesselnummer,
                onValueChange = { viewModel.setSchluesselnummer(it) },
                label = { Text("Schlüsselnummer *") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Key, null) },
                isError = state.schluesselnummer.isBlank() && state.showErrors
            )
            
            // Fahrradmarke
            OutlinedTextField(
                value = state.fahrradmarke,
                onValueChange = { viewModel.setFahrradmarke(it) },
                label = { Text("Fahrradmarke") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Text("🚴") }
            )
            
            // Mängelbeschreibung
            OutlinedTextField(
                value = state.maengelbeschreibung,
                onValueChange = { viewModel.setMaengelbeschreibung(it) },
                label = { Text("Mängelbeschreibung") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            )
            
            Divider()
            
            // Fotos
            Text(
                "Fotos",
                style = MaterialTheme.typography.titleMedium
            )
            
            if (state.photoUris.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.photoUris) { uri ->
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(8.dp))
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(uri),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            
                            IconButton(
                                onClick = { viewModel.removePhoto(uri) },
                                modifier = Modifier.align(Alignment.TopEnd)
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    "Löschen",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
            
            OutlinedButton(
                onClick = {
                    if (cameraPermissionState.status.isGranted) {
                        val uri = viewModel.createImageUri(context)
                        takePictureLauncher.launch(uri)
                    } else {
                        cameraPermissionState.launchPermissionRequest()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.CameraAlt, null)
                Spacer(Modifier.width(8.dp))
                Text("Foto aufnehmen")
            }
            
            Spacer(Modifier.height(16.dp))
            
            // Erstellen Button
            Button(
                onClick = { viewModel.createAuftrag() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Icon(Icons.Default.Check, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Auftrag erstellen")
                }
            }
            
            // Fehler anzeigen
            state.error?.let { error ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        error,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
    
    // Navigation nach Erfolg
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            navController.popBackStack()
        }
    }
}
