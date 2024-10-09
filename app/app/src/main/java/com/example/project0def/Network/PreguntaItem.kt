package com.example.project0def.Network

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.project0def.Pregunta
import com.example.project0def.PreguntasViewModel
import kotlinx.coroutines.delay

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun PreguntasScreen(viewModel: PreguntasViewModel) {
    val preguntas by viewModel.preguntas.collectAsState()
    val isLoading = preguntas.isEmpty()

    // Variables para el cuestionario
    var tiempoRestante by remember { mutableStateOf(60) } // 60 segundos
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var respuestasSeleccionadas by remember { mutableStateOf(mutableListOf<String>()) }
    var cuestionarioFinalizado by remember { mutableStateOf(false) }

    // Efecto para decrementar el temporizador
    LaunchedEffect(tiempoRestante) {
        if (tiempoRestante > 0 && !cuestionarioFinalizado) {
            delay(1000)
            tiempoRestante--
        }
    }

    // Manejo de la carga de preguntas
    if (isLoading) {
        Text(text = "Cargando preguntas...", modifier = Modifier.fillMaxSize())
    } else if (preguntas.isEmpty()) {
        Text(text = "No se encontraron preguntas.", modifier = Modifier.fillMaxSize())
    } else {
        if (tiempoRestante > 0 && !cuestionarioFinalizado) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Text("Tiempo restante: $tiempoRestante segundos")
                PreguntaItem(preguntas[currentQuestionIndex]) { respuestaSeleccionada ->
                    // Guardar la respuesta seleccionada
                    respuestasSeleccionadas.add(respuestaSeleccionada)

                    // Pasar a la siguiente pregunta
                    if (currentQuestionIndex < preguntas.size - 1) {
                        currentQuestionIndex++
                    } else {
                        // Finalizar el cuestionario
                        cuestionarioFinalizado = true
                    }
                }
            }
        } else {
            // Cuando se acaba el tiempo o se han respondido todas las preguntas
            ResultadosScreen(respuestasSeleccionadas, preguntas)
        }
    }
}

@Composable
fun ResultadosScreen(respuestasSeleccionadas: List<String>, preguntas: List<Pregunta>) {
    // Calcular el puntaje
    val puntaje = respuestasSeleccionadas.mapIndexed { index, respuesta ->
        if (respuesta == preguntas[index].respostes[preguntas[index].resposta_correcta]) 1 else 0
    }.sum()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Cuestionario finalizado", style = MaterialTheme.typography.titleLarge)
        Text(text = "Tu puntaje: $puntaje de ${preguntas.size}", style = MaterialTheme.typography.bodyMedium)

        // Aquí puedes mostrar una lista de respuestas correctas
        Text(text = "Respuestas:", style = MaterialTheme.typography.titleMedium)
        preguntas.forEachIndexed { index, pregunta ->
            Text(text = "${pregunta.pregunta}: ${pregunta.respostes[pregunta.resposta_correcta]} (Tu respuesta: ${respuestasSeleccionadas[index]})")
        }

        // Botón para reiniciar el cuestionario o salir
        Button(onClick = { /* Lógica para reiniciar el cuestionario */ }, modifier = Modifier.padding(top = 16.dp)) {
            Text("Reiniciar Cuestionario")
        }
    }
}

@Composable
fun PreguntaItem(pregunta: Pregunta, onRespuestaSeleccionada: (String) -> Unit) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = pregunta.pregunta)
        pregunta.imatge?.let {
            Image(painter = rememberImagePainter(it), contentDescription = null)
        }
        pregunta.respostes.forEach { respuesta ->
            Button(onClick = { onRespuestaSeleccionada(respuesta) }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Text(text = respuesta)
            }
        }
    }
}
