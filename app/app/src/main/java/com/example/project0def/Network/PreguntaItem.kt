package com.example.project0def.Network

import android.os.Build
import android.util.Log
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
import com.example.project0def.RetrofitClient
import kotlinx.coroutines.delay



@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun PreguntasScreen(viewModel: PreguntasViewModel) {
    val preguntas by viewModel.preguntas.collectAsState()
    val isLoading = preguntas.isEmpty()
    val error by viewModel.error.collectAsState() // Captura el error

    // Variables para el cuestionario
    var tiempoTranscurrido by remember { mutableStateOf(0) }
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var respuestasSeleccionadas by remember { mutableStateOf(mutableListOf<String>()) }
    var cuestionarioFinalizado by remember { mutableStateOf(false) }

    // Efecto para incrementar el cronómetro
    LaunchedEffect(tiempoTranscurrido) {
        if (!cuestionarioFinalizado) {
            delay(1000)
            tiempoTranscurrido++
        }
    }

    // Manejo de la carga de preguntas
    if (error != null) {
        Text(text = error!!, modifier = Modifier.fillMaxSize())
    } else if (isLoading) {
        Text(text = "Cargando preguntas...", modifier = Modifier.fillMaxSize())
    } else if (preguntas.isEmpty()) {
        Text(text = "No se encontraron preguntas.", modifier = Modifier.fillMaxSize())
    } else {
        if (!cuestionarioFinalizado) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text("Tiempo transcurrido: $tiempoTranscurrido segundos")
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
            // Cuando se acaban las preguntas, pasamos el tiempo a ResultadosScreen
            ResultadosScreen(
                respuestasSeleccionadas,
                preguntas,
                tiempoTranscurrido,
                onReiniciarCuestionario = {
                    // Lógica para reiniciar el cuestionario
                    tiempoTranscurrido = 0
                    currentQuestionIndex = 0
                    respuestasSeleccionadas.clear()
                    cuestionarioFinalizado = false
                }
            )
        }
    }
}


@Composable
fun ResultadosScreen(
    respuestasSeleccionadas: MutableList<String>,
    preguntas: List<Pregunta>,
    tiempoTranscurrido: Int,
    onReiniciarCuestionario: () -> Unit
) {
    // Calcular el puntaje
    val puntaje = respuestasSeleccionadas.mapIndexed { index, respuesta ->
        if (respuesta == preguntas[index].respostes[preguntas[index].resposta_correcta]) 1 else 0
    }.sum()

    // Crear el objeto Estadistica para enviar al servidor
    val estadistica = RetrofitClient.Estadistica(
        respuestas_correctas = puntaje,
        tiempo_terminado = tiempoTranscurrido
    )

    // Envía la estadística cuando el cuestionario finaliza
    LaunchedEffect(Unit) {
        try {
            // Llamada a la API para enviar la estadística
            val response = RetrofitClient.RetrofitClientEstadisticas.api.agregarEstadistica(estadistica)
            if (response.isSuccessful) {
                Log.d("ResultadosScreen", "Estadística enviada correctamente")
            } else {
                Log.e("ResultadosScreen", "Error al enviar la estadística: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("ResultadosScreen", "Error al enviar la estadística: ${e.message}")
        }
    }

    // Interfaz de usuario mostrando resultados
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Cuestionario finalizado", style = MaterialTheme.typography.titleLarge)
        Text(text = "Tu puntaje: $puntaje de ${preguntas.size}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Tiempo total: $tiempoTranscurrido segundos", style = MaterialTheme.typography.bodyMedium)

        Button(
            onClick = {
                onReiniciarCuestionario()
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
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
