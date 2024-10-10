package com.example.project0def

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Body
import retrofit2.http.POST

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
    private const val BASE_URL = "http://192.168.1.213:3000"

    val api: PreguntasApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PreguntasApi::class.java)
    }
    interface EstadisticasApi {
        @POST("/api/agregar-estadistica")
        suspend fun agregarEstadistica(@Body estadistica: Estadistica): Response<Unit>
    }

    data class Estadistica(
        val respuestas_correctas: Int,
        val tiempo_terminado: Int
    )

    object RetrofitClientEstadisticas {
        private const val BASE_URL = "http://192.168.1.213:3000" // Asegúrate que este URL sea el correcto.

        val api: EstadisticasApi by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(EstadisticasApi::class.java)
        }
    }
}

