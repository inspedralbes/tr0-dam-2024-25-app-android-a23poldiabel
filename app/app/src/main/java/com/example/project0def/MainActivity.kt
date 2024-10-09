package com.example.project0def

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.project0def.Network.PreguntasScreen
import com.example.project0def.ui.theme.Project0DEFTheme

class MainActivity : ComponentActivity() {
    private val viewModel: PreguntasViewModel by viewModels() // Obtén el ViewModel

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Project0DEFTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "inicio") {
                    composable("inicio") { InicioScreen(navController) }
                    composable("preguntas") { PreguntasScreen(viewModel) }
                    composable("resultados") { ResultadosScreen() }
                }
            }
        }
    }
}

@Composable
fun InicioScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Bienvenido a la práctica de autoescuela")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("preguntas") }) {
                Text(text = "Iniciar preguntas")
            }
        }
    }
}

@Composable
fun ResultadosScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Resultados de la práctica")
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Project0DEFTheme {
        Greeting("Android")
    }
}