package de.radstation.werkstatt.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import de.radstation.werkstatt.presentation.auftraege.create.CreateAuftragScreen
import de.radstation.werkstatt.presentation.auftraege.detail.AuftragDetailScreen
import de.radstation.werkstatt.presentation.auftraege.list.AuftraegeListScreen
import de.radstation.werkstatt.presentation.dashboard.DashboardScreen
import de.radstation.werkstatt.presentation.teile.list.TeileListScreen

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object AuftraegeList : Screen("auftraege_list")
    object CreateAuftrag : Screen("create_auftrag")
    object AuftragDetail : Screen("auftrag_detail/{auftragsnummer}") {
        fun createRoute(auftragsnummer: String) = "auftrag_detail/$auftragsnummer"
    }
    object TeileList : Screen("teile_list")
}

@Composable
fun RadstationNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreen(navController = navController)
        }
        
        composable(Screen.AuftraegeList.route) {
            AuftraegeListScreen(navController = navController)
        }
        
        composable(Screen.CreateAuftrag.route) {
            CreateAuftragScreen(navController = navController)
        }
        
        composable(
            route = Screen.AuftragDetail.route,
            arguments = listOf(navArgument("auftragsnummer") { type = NavType.StringType })
        ) { backStackEntry ->
            val auftragsnummer = backStackEntry.arguments?.getString("auftragsnummer") ?: ""
            AuftragDetailScreen(
                auftragsnummer = auftragsnummer,
                navController = navController
            )
        }
        
        composable(Screen.TeileList.route) {
            TeileListScreen(navController = navController)
        }
    }
}
