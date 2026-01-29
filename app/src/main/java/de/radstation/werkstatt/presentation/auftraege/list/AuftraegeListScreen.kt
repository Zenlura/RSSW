package de.radstation.werkstatt.presentation.auftraege.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import de.radstation.werkstatt.domain.model.Auftrag
import de.radstation.werkstatt.domain.model.AuftragStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AuftraegeListContent(
    auftraege: List<Auftrag>,
    isLoading: Boolean,
    error: String?,
    onAuftragClick: (String) -> Unit,
    onCreateClick: () -> Unit,
    onRefresh: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Aufträge") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateClick) {
                Icon(Icons.Default.Add, contentDescription = "Neuer Auftrag")
            }
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
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Fehler: $error",
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onRefresh) {
                            Text("Erneut versuchen")
                        }
                    }
                }
                auftraege.isEmpty() -> {
                    Text(
                        text = "Keine Aufträge vorhanden",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(auftraege) { auftrag ->
                            AuftragItem(
                                auftrag = auftrag,
                                onClick = { onAuftragClick(auftrag.auftragsnummer) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AuftragItem(
    auftrag: Auftrag,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Auftragsnummer als Titel
            Text(
                text = "Nr: ${auftrag.auftragsnummer}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Schlüsselnummer und Fahrradmarke
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Schlüssel: ${auftrag.schluesselnummer}",
                    style = MaterialTheme.typography.bodyMedium
                )
                if (auftrag.fahrradmarke.isNotEmpty()) {
                    Text(
                        text = auftrag.fahrradmarke,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Status und Datum
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusChip(status = auftrag.status)
                Text(
                    text = auftrag.formattedScanDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Flags (wenn gesetzt)
            if (auftrag.angerufen || auftrag.mailbox || auftrag.bezahlt) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (auftrag.angerufen) {
                        Text("📞", style = MaterialTheme.typography.bodySmall)
                    }
                    if (auftrag.mailbox) {
                        Text("📧", style = MaterialTheme.typography.bodySmall)
                    }
                    if (auftrag.bezahlt) {
                        Text("💰", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@Composable
fun StatusChip(status: AuftragStatus) {
    val color = when (status) {
        AuftragStatus.NICHT_BEGONNEN -> MaterialTheme.colorScheme.error
        AuftragStatus.IN_BEARBEITUNG -> MaterialTheme.colorScheme.tertiary
        AuftragStatus.FERTIG -> MaterialTheme.colorScheme.secondary
        AuftragStatus.ABGEHOLT -> MaterialTheme.colorScheme.surfaceVariant
    }

    Surface(
        color = color,
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = status.icon,
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = status.displayName,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimary
            )
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
fun AuftraegeListScreen(
    navController: androidx.navigation.NavHostController
) {
    val viewModel: AuftraegeListViewModel = hiltViewModel()
    val auftraege by viewModel.auftraege.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    AuftraegeListContent(
        auftraege = auftraege,
        isLoading = isLoading,
        error = error,
        onAuftragClick = { auftragsnummer ->
            navController.navigate(de.radstation.werkstatt.presentation.navigation.Screen.AuftragDetail.createRoute(auftragsnummer))
        },
        onCreateClick = {
            navController.navigate(de.radstation.werkstatt.presentation.navigation.Screen.CreateAuftrag.route)
        },
        onRefresh = {
            viewModel.loadAuftraege()
        }
    )
}