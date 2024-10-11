package com.example.project0def

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Body
import retrofit2.http.POST

data class Pregunta(
    val id: Int,
    val pregunta: String,
    val imatge: String?,
    val respostes: List<String>,
    val resposta_correcta: Int
)

interface PreguntasApi {
    @GET("/api/preguntes")
    suspend fun obtenerPreguntas(): List<Pregunta>
}

object RetrofitClient {
    private val BASE_URL = "http://${IPGloval.ipGeneral}:3000" // Usando la variable global IPGloval.ipGeneral

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
        private val BASE_URL = "http://${IPGloval.ipGeneral}:3000"

        val api: EstadisticasApi by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(EstadisticasApi::class.java)
        }
    }
}
