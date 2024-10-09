package com.example.project0def

import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class PreguntasViewModel : ViewModel() {

    private val _preguntas = MutableStateFlow<List<Pregunta>>(emptyList())
    val preguntas: StateFlow<List<Pregunta>> = _preguntas

    init {
        cargarPreguntas()
    }

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun cargarPreguntas() {
        viewModelScope.launch {
            try {
                Log.d("PreguntasViewModel", "Cargando preguntas...")
                val preguntasResponse = RetrofitClient.api.obtenerPreguntas()
                Log.d("PreguntasViewModel", "Preguntas recibidas: $preguntasResponse")
                _preguntas.value = preguntasResponse
                _error.value = null // Reseteamos el error si se carga correctamente
            } catch (e: HttpException) {
                Log.e("PreguntasViewModel", "Error HTTP: ${e.message}", e)
                _error.value = "Error al cargar preguntas: ${e.message}"
            } catch (e: IOException) {
                Log.e("PreguntasViewModel", "Error de red: ${e.message}", e)
                _error.value = "Error de red: ${e.message}"
            } catch (e: Exception) {
                Log.e("PreguntasViewModel", "Error inesperado", e)
                _error.value = "Error inesperado: ${e.message}"
            }
        }
    }



}
