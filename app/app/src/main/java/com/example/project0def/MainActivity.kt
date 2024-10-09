package com.example.project0def

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.project0def.Network.PreguntasScreen
import com.example.project0def.ui.theme.Project0DEFTheme

class MainActivity : ComponentActivity() {
    private val viewModel: PreguntasViewModel by viewModels() // Obtén el ViewModel

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Project0DEFTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    PreguntasScreen(viewModel) // Pasa el ViewModel aquí
                }
            }
        }
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
