package com.example.project0def

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// Modelo de datos para la pregunta
data class Pregunta(
    val id: Int,
    val pregunta: String,
    val imatge: String?,
    val respostes: List<String>,
    val resposta_correcta: Int
)

// Interfaz del API
interface PreguntasApi {
    @GET("/api/preguntes")
    suspend fun obtenerPreguntas(): List<Pregunta>
}

// Configuración de Retrofit
object RetrofitClient {
    private const val BASE_URL = "http://192.168.0.157:3000"

    val api: PreguntasApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PreguntasApi::class.java)
    }
}

