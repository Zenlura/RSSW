package de.radstation.werkstatt.presentation.auftraege.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import de.radstation.werkstatt.domain.model.Auftrag
import de.radstation.werkstatt.domain.model.AuftragStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AuftragDetailContent(
    auftrag: Auftrag?,
    isLoading: Boolean,
    error: String?,
    onBackClick: () -> Unit,
    onStatusChange: (AuftragStatus) -> Unit,
    onFlagChange: (angerufen: Boolean, mailbox: Boolean, bezahlt: Boolean) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Auftrag Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Zurück")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                error != null -> {
                    Text(
                        text = "Fehler: $error",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                auftrag != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Auftragsdaten (Scanner-System)
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Auftragsdaten",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                DetailRow("Auftragsnummer", auftrag.auftragsnummer)
                                DetailRow("Schlüsselnummer", auftrag.schluesselnummer)
                                DetailRow("Reparaturdatum", auftrag.formattedScanDate)
                                DetailRow("Erfasst am", auftrag.timestamp)
                            }
                        }

                        // Fahrraddaten
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Fahrraddaten",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                DetailRow("Marke", auftrag.fahrradmarke.ifEmpty { "Nicht angegeben" })
                            }
                        }

                        // Mängelbeschreibung
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Mängelbeschreibung",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = auftrag.maengelbeschreibung.ifEmpty { "Keine Beschreibung" },
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }

                        // Status & Flags
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Status & Bearbeitung",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                // Status Anzeige mit Icon
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Status:")
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = auftrag.status.icon,
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = auftrag.status.displayName,
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(auftrag.statusColor)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Status-Änderungs-Buttons
                                Text(
                                    text = "Status ändern:",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    AuftragStatus.entries.chunked(2).forEach { statusPair ->
                                        Column(modifier = Modifier.weight(1f)) {
                                            statusPair.forEach { status ->
                                                Button(
                                                    onClick = { onStatusChange(status) },
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(vertical = 4.dp),
                                                    enabled = auftrag.status != status,
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = if (auftrag.status == status)
                                                            MaterialTheme.colorScheme.primary
                                                        else
                                                            MaterialTheme.colorScheme.secondary
                                                    )
                                                ) {
                                                    Text("${status.icon} ${status.displayName}")
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))
                                Divider()
                                Spacer(modifier = Modifier.height(16.dp))

                                // Flags
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Angerufen:")
                                    Switch(
                                        checked = auftrag.angerufen,
                                        onCheckedChange = {
                                            onFlagChange(it, auftrag.mailbox, auftrag.bezahlt)
                                        }
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Mailbox:")
                                    Switch(
                                        checked = auftrag.mailbox,
                                        onCheckedChange = {
                                            onFlagChange(auftrag.angerufen, it, auftrag.bezahlt)
                                        }
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Bezahlt:")
                                    Switch(
                                        checked = auftrag.bezahlt,
                                        onCheckedChange = {
                                            onFlagChange(auftrag.angerufen, auftrag.mailbox, it)
                                        }
                                    )
                                }
                            }
                        }

                        // Fotos
                        if (auftrag.photos.isNotEmpty()) {
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "Fotos (${auftrag.photos.size})",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    auftrag.photos.forEach { photoUrl ->
                                        Text(
                                            text = "📷 $photoUrl",
                                            style = MaterialTheme.typography.bodySmall,
                                            modifier = Modifier.padding(vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // OCR Confidence (nur wenn relevant)
                        if (auftrag.confidence > 0.0) {
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "Scan-Qualität",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    DetailRow("Erkennungsgenauigkeit", "${(auftrag.confidence * 100).toInt()}%")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

// ViewModel-Wrapper für Navigation
@Composable
fun AuftragDetailScreen(
    auftragsnummer: String,
    navController: androidx.navigation.NavHostController
) {
    val viewModel: AuftragDetailViewModel = hiltViewModel()

    // Load auftrag when composable first launches
    LaunchedEffect(auftragsnummer) {
        viewModel.loadAuftrag(auftragsnummer)
    }

    val auftrag by viewModel.auftrag.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    AuftragDetailContent(
        auftrag = auftrag,
        isLoading = isLoading,
        error = error,
        onBackClick = {
            navController.popBackStack()
        },
        onStatusChange = { newStatus ->
            viewModel.updateStatus(newStatus)
        },
        onFlagChange = { angerufen, mailbox, bezahlt ->
            viewModel.updateFlags(angerufen, mailbox, bezahlt)
        }
    )
}