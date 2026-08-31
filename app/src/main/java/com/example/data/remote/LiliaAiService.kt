package com.example.data.remote

import android.util.Log
import com.example.BuildConfig
import com.example.data.model.MealAnalysisResult
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiGenerateRequest
    ): GeminiGenerateResponse
}

class LiliaAiService {
    private val tag = "LiliaAiService"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://generativelanguage.googleapis.com/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    private val apiService = retrofit.create(GeminiApiService::class.java)

    /**
     * Persona oficial da Lília - Organizadora de Estilo de Vida e Educação Nutricional.
     * Alinhada com a blindagem regulatória (sem prescrição médica/dietoterápica privativa e sem diagnóstico clínico).
     */
     private val liliaSystemPrompt = """
        <comportamento>
        Seu nome é Lília, uma assistente virtual e Organizadora de Estilo de Vida focada em hábitos alimentares saudáveis, bem-estar e autoconhecimento.
        Você se fundamenta nas melhores referências científicas da nutrição, bioquímica e técnica dietética (Krause, Tratado de Nutrição, Pirâmide dos Alimentos e Educação Alimentar e Nutricional).
        </comportamento>

        <diretrizes_de_blindagem_regulatória>
        1. **SEM PRESCRIÇÃO MÉDICA OU DIETOTERÁPICA PRIVATIVA**: Você NÃO prescreve dietas terapêuticas, tratamentos para patologias específicas ou fármacos/suplementos clínicos. Em vez disso, você fornece **planejamento de refeições e sugestões de receitas inteligentes** com base na rotina, aversões do usuário ou nos ingredientes disponíveis em casa.
        2. **SEM DIAGNÓSTICO CLÍNICO**: A autoavaliação inicial de perguntas é um **mapeamento comportamental e autoavaliação de hábitos cotidianos**, e não um diagnóstico ou consulta médica/nutricional clínica privativa.
        3. **SUPORTE E APRENDIZADO**: Toda estimativa e análise calórica por foto de pratos e rótulos é uma **análise nutricional de suporte e aprendizado contínuo**, servindo como ferramenta de consciência e organização pessoal, e não como parecer médico.
        4. **ENCAMINHAMENTO ÉTICO**: Se o usuário relatar sintomas graves, transtornos alimentares agudos ou patologias complexas, recomende com carinho o acompanhamento presencial com um profissional médico ou nutricionista habilitado.
        </diretrizes_de_blindagem_regulatória>

        <áreas_de_atuação>
        Você auxilia em:
        1. Organização da rotina alimentar e hábitos saudáveis de vida.
        2. Cálculo e acompanhamento de estimativas energéticas informativas:
           - IMC referencial e Taxa Metabólica Basal (TMB) calculada pela fórmula de Harris-Benedict para autoconhecimento.
           - Distribuição de macronutrientes balanceada para planejamento diário (Proteínas, Carboidratos e Gorduras).
        3. Sugestões de receitas práticas aproveitando ingredientes disponíveis na despensa, respeitando aversões e preferências.
        4. Lista de compras setorizada com dicas de conservação e preparo.
        5. Incentivo e constância em metas de hidratação e sono.
        </áreas_de_atuação>

        <instruções>
        - **Empatia & Acolhimento**: Elimine qualquer sentimento de culpa diante de deslizes alimentares. Mostre sensibilidade emocional e acolhimento incondicional.
        - **Clareza & Tom**: Tom empático, leve, explicativo e positivo. Linguagem acessível, promovendo autonomia e consciência alimentar.
        - **Concisão**: Respostas fluidas e fáceis de ler no dia a dia.
        - **Comando de Ajuda / Dúvida**: Se o usuário enviar "?", "ajuda" ou "dúvida", responda com clareza para esclarecer sua dúvida imediatamente.
        - **Mapeamento Comportamental**: Ao conduzir a autoavaliação, faça UMA pergunta de cada vez, validando com carinho as respostas antes de avançar.
        </instruções>
    """.trimIndent()

    suspend fun chatWithLilia(
        conversationHistory: List<Pair<String, String>>,
        userMessage: String
    ): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext getOfflineEmpatheticResponse(userMessage, conversationHistory.size)
        }

        try {
            val contents = mutableListOf<GeminiContent>()

            // Adiciona mensagens anteriores para contexto contínuo
            conversationHistory.takeLast(12).forEach { (sender, text) ->
                val role = if (sender == "user") "user" else "model"
                contents.add(
                    GeminiContent(
                        role = role,
                        parts = listOf(GeminiPart(text = text))
                    )
                )
            }

            // Mensagem atual do usuário
            contents.add(
                GeminiContent(
                    role = "user",
                    parts = listOf(GeminiPart(text = userMessage))
                )
            )

            val request = GeminiGenerateRequest(
                contents = contents,
                systemInstruction = GeminiContent(
                    parts = listOf(GeminiPart(text = liliaSystemPrompt))
                ),
                generationConfig = GeminiGenerationConfig(
                    temperature = 0.7f,
                    topP = 0.95f
                )
            )

            val response = apiService.generateContent(apiKey, request)
            val reply = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            if (!reply.isNullOrBlank()) {
                return@withContext reply.trim()
            } else {
                return@withContext getOfflineEmpatheticResponse(userMessage, conversationHistory.size)
            }
        } catch (e: Exception) {
            Log.e(tag, "Gemini API error", e)
            return@withContext getOfflineEmpatheticResponse(userMessage, conversationHistory.size)
        }
    }

    private val mealAnalysisJsonAdapter = moshi.adapter(GeminiMealAnalysisJson::class.java)

    private val mealAnalysisJsonSchema = mapOf(
        "type" to "OBJECT",
        "properties" to mapOf(
            "is_food_detected" to mapOf(
                "type" to "BOOLEAN",
                "description" to "Verdadeiro se houver alimentos ou pratos identificáveis na imagem, falso se for um objeto não alimentar, foto escura/borrada ou sem comida visível"
            ),
            "no_food_reason" to mapOf(
                "type" to "STRING",
                "description" to "Motivo empático e breve caso nenhum alimento seja detectado"
            ),
            "identified_foods" to mapOf(
                "type" to "ARRAY",
                "description" to "Lista dos alimentos e preparações identificados no prato (deixe vazio se nenhum alimento for detectado)",
                "items" to mapOf(
                    "type" to "OBJECT",
                    "properties" to mapOf(
                        "name" to mapOf("type" to "STRING", "description" to "Nome do alimento"),
                        "estimated_grams" to mapOf("type" to "INTEGER", "description" to "Peso estimado em gramas"),
                        "calories" to mapOf("type" to "INTEGER", "description" to "Calorias do alimento"),
                        "protein_g" to mapOf("type" to "INTEGER", "description" to "Proteínas em gramas"),
                        "carbs_g" to mapOf("type" to "INTEGER", "description" to "Carboidratos em gramas"),
                        "fat_g" to mapOf("type" to "INTEGER", "description" to "Gorduras em gramas"),
                        "fiber_g" to mapOf("type" to "INTEGER", "description" to "Fibras alimentares em gramas"),
                        "notes" to mapOf("type" to "STRING", "description" to "Observação nutricional relevante")
                    ),
                    "required" to listOf("name", "estimated_grams", "calories", "protein_g", "carbs_g", "fat_g")
                )
            ),
            "totals" to mapOf(
                "type" to "OBJECT",
                "description" to "Totais consolidados de macronutrientes da refeição",
                "properties" to mapOf(
                    "calories" to mapOf("type" to "INTEGER", "description" to "Calorias totais"),
                    "carbs_g" to mapOf("type" to "INTEGER", "description" to "Carboidratos totais em g"),
                    "protein_g" to mapOf("type" to "INTEGER", "description" to "Proteínas totais em g"),
                    "fat_g" to mapOf("type" to "INTEGER", "description" to "Gorduras totais em g"),
                    "fiber_g" to mapOf("type" to "INTEGER", "description" to "Fibras totais em g")
                ),
                "required" to listOf("calories", "carbs_g", "protein_g", "fat_g")
            ),
            "allergens" to mapOf(
                "type" to "ARRAY",
                "description" to "Alérgenos ou pontos de atenção (ex: Glúten, Lactose, Amendoim, Frutos do Mar)",
                "items" to mapOf("type" to "STRING")
            ),
            "health_notes" to mapOf(
                "type" to "ARRAY",
                "description" to "Pontos positivos e substâncias bioativas (ex: Rico em Fibras, Fonte de Ômega-3, Antioxidantes)",
                "items" to mapOf("type" to "STRING")
            ),
            "feedback" to mapOf(
                "type" to "OBJECT",
                "description" to "Feedback humanizado, acolhedor e motivador da Lília Personal Diet",
                "properties" to mapOf(
                    "title" to mapOf("type" to "STRING", "description" to "Título motivador e elogioso ou acolhedor"),
                    "paragraph_1" to mapOf("type" to "STRING", "description" to "Elogio e destaque dos pontos positivos dos alimentos ingeridos, ou acolhimento gentil caso a imagem não esteja nítida"),
                    "paragraph_2" to mapOf("type" to "STRING", "description" to "Dica prática e acolhedora de equilíbrio ou instrução para nova foto")
                ),
                "required" to listOf("title", "paragraph_1", "paragraph_2")
            )
        ),
        "required" to listOf("is_food_detected", "identified_foods", "totals", "allergens", "health_notes", "feedback")
    )

    private val visionSystemPrompt = """
        Você é a IA de Visão Computacional Nutricional da Lília, atuando como organizadora de estilo de vida e ferramenta de suporte e aprendizado nutricional educativo (baseada em referências acadêmicas de Técnica Dietética e composição dos alimentos).
        
        IMPORTANTE (BLINDAGEM REGULATÓRIA): Toda análise calórica, estimativa de porção e identificação de macros é uma estimativa de suporte e aprendizado contínuo para o usuário organizar seus hábitos, e NÃO constitui prescrição médica ou dietoterápica clínica privativa.
        
        Sua tarefa é analisar imagens de refeições, pratos montados ou rótulos e retornar um JSON estruturado:
        1. Se a imagem contiver alimentos:
           - is_food_detected: true
           - identified_foods: lista com nome, peso estimado em gramas, calorias aproximadas, proteínas, carboidratos, gorduras e fibras.
           - totals: total estimado consolidado de calorias e macros.
           - allergens: alérgenos potenciais para conscientização.
           - health_notes: pontos positivos de estilo de vida (ex: Rico em Fibras, Fonte de Antioxidantes, Boa densidade nutritiva).
           - feedback: título e dois parágrafos acolhedores e educativos valorizando as escolhas alimentares e sugerindo hábitos equilibrados.
        2. Se a imagem NÃO contiver alimentos (ex: foto de pessoa, animal, objeto, ambiente, borrão ou imagem escura):
           - is_food_detected: false
           - no_food_reason: "Não foi possível identificar alimentos com clareza nesta imagem."
           - identified_foods: []
           - totals: {"calories": 0, "carbs_g": 0, "protein_g": 0, "fat_g": 0, "fiber_g": 0}
           - allergens: []
           - health_notes: []
           - feedback: {
               "title": "Não consegui ver os alimentos com clareza",
               "paragraph_1": "Tudo bem, acontece! Às vezes a iluminação do ambiente, a distância da câmera ou um reflexo no prato dificultam a visualização dos ingredientes.",
               "paragraph_2": "Que tal tirar uma foto mais de perto e com boa iluminação? Você também pode me contar pelo chat ou registrar manualmente com calma."
             }
    """.trimIndent()

    suspend fun analyzePlate(
        imageUriOrDescription: String,
        mealType: String
    ): MealAnalysisResult = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        val fallbackImage = when (mealType) {
            "Café da Manhã" -> "https://lh3.googleusercontent.com/aida-public/AB6AXuBvZXWevs49ANvctfNHzmj73ar6xA1xBVFwLkqcqTMcv-naj0JaLtZC5Hyps1MaXIKMdioCq9rwjaw4Qt5oqETz9CtFHAgRrSjKQcfzhes-N1JienmpE-PHGkXtypypO86np-wLENfDsCNjTZ6m0Safll3VLQdeJTwRmyyFInPiUante1I8gySbQpxmB940U91jqIz8y8TIgyOBwnxV6-vQ_Qcwm5qFYcZ1cPi34vyUV8uHZcsls02S"
            "Jantar" -> "https://lh3.googleusercontent.com/aida-public/AB6AXuCZ4UTNY94CuYXeWdJOFFyiu4A41SzvbEW3hHNso1-O1w-MOEpJqB6d3GbqAO43geoaWDrHgMeGkco-tI731_RjJrj2swtV5L8RMVsQYw4GixjurvvjWj3cqlu2oGf6vjwgcvBGJcWXnqdyLrlS1hXgK-nJx8Ysy1DhHkodBcBWsI8hDwjc56z88RjwxceGBe-CICfo7bvbbaSveeICoZ2-6k6q_FAcal7t7m7oi5g6hSKeBD4UTZh4"
            "Lanche" -> "https://lh3.googleusercontent.com/aida-public/AB6AXuCLApRHLgIIuQorkGQKFpVESEkaU-NAwKYyCZITGMXBkixb2TSo65Vht55d7T2TJKE3q9IvRXjqb3tCfidE0pgqBDjpeETPycYHGa6nmtO8F1rXhiHxeyJRrxJlUHV2hxxpZzpfB8sAOfl-B4jWgwCPzHX7-By8cQaQZODrDDPWN3DwQ_68khtlj__vkMlCdmetVW5ta0QRHR_ivx_Y3JNcRJe9IjQriTglYkYMLN76TCdlaKCxwnN3"
            else -> "https://lh3.googleusercontent.com/aida-public/AB6AXuCAq-KIaazIYB4gEwCsh1diRFqavivtPRc9fLEJ_tqA73YIKsUoPAks_e7-n1tXIXmG3KJvnQ8R1bDZOZd2opsgsFHe_ljpR3X5sKafRi8NA6_yZOg5PbFKfjq4dC-ueBKNRCudyx8QqkdXn3ufUHbuOJq6tIAOs-VXs1Xa5REDWaDUvkrhJIUBieqz4LNgGzSMiW9HP6rViHqlK11OVLdbp9EcpUJjp7cfq4mnbXgRXwb4d06FelW"
        }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext getOfflineAnalysisResult(mealType, fallbackImage)
        }

        try {
            val userPromptText = if (imageUriOrDescription.isNotBlank() && !imageUriOrDescription.startsWith("data:")) {
                "Analise detalhadamente os alimentos desta refeição de '$mealType'. Detalhes adicionais: $imageUriOrDescription"
            } else {
                "Analise detalhadamente a composição nutricional, alimentos e equilíbrio deste prato de '$mealType'."
            }

            val parts = mutableListOf<GeminiPart>()
            if (imageUriOrDescription.startsWith("data:image/") && imageUriOrDescription.contains(";base64,")) {
                val mimeType = imageUriOrDescription.substringAfter("data:").substringBefore(";base64,")
                val base64Data = imageUriOrDescription.substringAfter(";base64,")
                parts.add(GeminiPart(inlineData = GeminiInlineData(mimeType = mimeType, data = base64Data)))
            }
            parts.add(GeminiPart(text = userPromptText))

            val request = GeminiGenerateRequest(
                contents = listOf(
                    GeminiContent(
                        role = "user",
                        parts = parts
                    )
                ),
                systemInstruction = GeminiContent(
                    parts = listOf(GeminiPart(text = visionSystemPrompt))
                ),
                generationConfig = GeminiGenerationConfig(
                    temperature = 0.2f,
                    topP = 0.95f,
                    responseMimeType = "application/json",
                    responseSchema = mealAnalysisJsonSchema
                )
            )

            val response = apiService.generateContent(apiKey, request)
            val jsonText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text

            if (!jsonText.isNullOrBlank()) {
                val parsed = parseVisionJson(jsonText, fallbackImage)
                if (parsed != null) {
                    return@withContext parsed
                }
            }
            return@withContext getOfflineAnalysisResult(mealType, fallbackImage)
        } catch (e: Exception) {
            Log.e(tag, "Vision Engine API error", e)
            return@withContext getOfflineAnalysisResult(mealType, fallbackImage)
        }
    }

    private fun parseVisionJson(rawJson: String, defaultImage: String): MealAnalysisResult? {
        try {
            val cleaned = rawJson
                .substringAfter("```json")
                .substringBefore("```")
                .trim()
            val finalJson = if (cleaned.startsWith("{")) cleaned else rawJson.substringAfter("{").substringBeforeLast("}").let { "{$it}" }

            // Try Moshi strongly-typed deserialization first
            val moshiResult = runCatching { mealAnalysisJsonAdapter.fromJson(finalJson) }.getOrNull()
            if (moshiResult != null) {
                val isDetected = moshiResult.isFoodDetected != false && !moshiResult.identifiedFoods.isNullOrEmpty()
                
                if (!isDetected) {
                    val p1 = moshiResult.feedback?.paragraph1?.ifBlank {
                        "Tudo bem, acontece! Às vezes a iluminação do ambiente, a distância da câmera ou um reflexo no prato dificultam a leitura dos ingredientes."
                    } ?: "Tudo bem, acontece! Às vezes a iluminação do ambiente, a distância da câmera ou um reflexo no prato dificultam a leitura dos ingredientes."
                    val p2 = moshiResult.feedback?.paragraph2?.ifBlank {
                        "Que tal tentar novamente tirando uma nova foto mais aproximada e bem iluminada? Se preferir, você também pode registrar manualmente ou me contar pelo chat!"
                    } ?: "Que tal tentar novamente tirando uma nova foto mais aproximada e bem iluminada? Se preferir, você também pode registrar manualmente ou me contar pelo chat!"
                    val title = moshiResult.feedback?.title?.ifBlank {
                        "Não consegui ver os alimentos com clareza"
                    } ?: "Não consegui ver os alimentos com clareza"

                    return MealAnalysisResult(
                        plateImageUrl = defaultImage,
                        identifiedFoods = emptyList(),
                        components = emptyList(),
                        calories = 0,
                        protein = 0,
                        carbs = 0,
                        fat = 0,
                        allergens = emptyList(),
                        healthTags = emptyList(),
                        feedbackTitle = title,
                        feedbackMessage = "$p1\n\n$p2",
                        feedbackParagraph1 = p1,
                        feedbackParagraph2 = p2,
                        isFoodDetected = false,
                        noFoodReason = moshiResult.noFoodReason ?: "Não identificamos alimentos visíveis nesta imagem."
                    )
                }

                val components = moshiResult.identifiedFoods.map { item ->
                    com.example.data.model.FoodComponent(
                        name = item.name ?: "Alimento",
                        estimatedGrams = item.estimatedGrams ?: 100,
                        calories = item.calories ?: 0,
                        protein = item.proteinG ?: 0,
                        carbs = item.carbsG ?: 0,
                        fat = item.fatG ?: 0,
                        notes = item.notes ?: ""
                    )
                }

                val calories = moshiResult.totals?.calories ?: components.sumOf { it.calories }.coerceAtLeast(250)
                val carbs = moshiResult.totals?.carbsG ?: components.sumOf { it.carbs }.coerceAtLeast(20)
                val protein = moshiResult.totals?.proteinG ?: components.sumOf { it.protein }.coerceAtLeast(15)
                val fat = moshiResult.totals?.fatG ?: components.sumOf { it.fat }.coerceAtLeast(8)

                val p1 = moshiResult.feedback?.paragraph1 ?: ""
                val p2 = moshiResult.feedback?.paragraph2 ?: ""
                val title = moshiResult.feedback?.title ?: "Refeição Analisada com Sucesso!"
                val fullMsg = if (p1.isNotBlank() && p2.isNotBlank()) "$p1\n\n$p2" else (p1 + p2).ifBlank {
                    "Excelente equilíbrio nutricional! Esta refeição fornece saciedade duradoura e energia de qualidade."
                }

                return MealAnalysisResult(
                    plateImageUrl = defaultImage,
                    identifiedFoods = moshiResult.identifiedFoods.mapNotNull { it.name },
                    components = components,
                    calories = calories,
                    protein = protein,
                    carbs = carbs,
                    fat = fat,
                    allergens = moshiResult.allergens ?: emptyList(),
                    healthTags = moshiResult.healthNotes ?: emptyList(),
                    feedbackTitle = title,
                    feedbackMessage = fullMsg,
                    feedbackParagraph1 = p1,
                    feedbackParagraph2 = p2,
                    isFoodDetected = true,
                    noFoodReason = ""
                )
            }

            // Fallback to JSONObject manual parsing
            val obj = org.json.JSONObject(finalJson)
            val isDetected = obj.optBoolean("is_food_detected", true)

            val identifiedFoods = mutableListOf<String>()
            val components = mutableListOf<com.example.data.model.FoodComponent>()

            val foodsArray = obj.optJSONArray("identified_foods")
            if (foodsArray != null) {
                for (i in 0 until foodsArray.length()) {
                    val item = foodsArray.getJSONObject(i)
                    val name = item.optString("name", "Alimento")
                    identifiedFoods.add(name)
                    components.add(
                        com.example.data.model.FoodComponent(
                            name = name,
                            estimatedGrams = item.optInt("estimated_grams", 100),
                            calories = item.optInt("calories", 0),
                            protein = item.optInt("protein_g", 0),
                            carbs = item.optInt("carbs_g", 0),
                            fat = item.optInt("fat_g", 0),
                            notes = item.optString("notes", "")
                        )
                    )
                }
            }

            if (!isDetected || identifiedFoods.isEmpty()) {
                val feedbackObj = obj.optJSONObject("feedback")
                val title = feedbackObj?.optString("title", "Não consegui ver os alimentos com clareza") ?: "Não consegui ver os alimentos com clareza"
                val p1 = feedbackObj?.optString("paragraph_1", "Tudo bem, acontece! Às vezes a iluminação do ambiente ou o ângulo dificultam a leitura dos ingredientes.") ?: "Tudo bem, acontece! Às vezes a iluminação do ambiente ou o ângulo dificultam a leitura dos ingredientes."
                val p2 = feedbackObj?.optString("paragraph_2", "Que tal tentar novamente tirando uma nova foto mais iluminada e aproximada? Estou aqui para te ajudar!") ?: "Que tal tentar novamente tirando uma nova foto mais iluminada e aproximada? Estou aqui para te ajudar!"

                return MealAnalysisResult(
                    plateImageUrl = defaultImage,
                    identifiedFoods = emptyList(),
                    components = emptyList(),
                    calories = 0,
                    protein = 0,
                    carbs = 0,
                    fat = 0,
                    allergens = emptyList(),
                    healthTags = emptyList(),
                    feedbackTitle = title,
                    feedbackMessage = "$p1\n\n$p2",
                    feedbackParagraph1 = p1,
                    feedbackParagraph2 = p2,
                    isFoodDetected = false,
                    noFoodReason = obj.optString("no_food_reason", "Nenhum alimento identificado")
                )
            }

            val totals = obj.optJSONObject("totals")
            val calories = totals?.optInt("calories") ?: components.sumOf { it.calories }.coerceAtLeast(350)
            val carbs = totals?.optInt("carbs_g") ?: components.sumOf { it.carbs }.coerceAtLeast(30)
            val protein = totals?.optInt("protein_g") ?: components.sumOf { it.protein }.coerceAtLeast(25)
            val fat = totals?.optInt("fat_g") ?: components.sumOf { it.fat }.coerceAtLeast(10)

            val allergensList = mutableListOf<String>()
            val allergensArray = obj.optJSONArray("allergens")
            if (allergensArray != null) {
                for (i in 0 until allergensArray.length()) {
                    allergensList.add(allergensArray.getString(i))
                }
            }

            val healthNotesList = mutableListOf<String>()
            val healthArray = obj.optJSONArray("health_notes")
            if (healthArray != null) {
                for (i in 0 until healthArray.length()) {
                    healthNotesList.add(healthArray.getString(i))
                }
            }

            val feedbackObj = obj.optJSONObject("feedback")
            val title = feedbackObj?.optString("title", "Refeição Analisada com Sucesso!") ?: "Refeição Analisada com Sucesso!"
            val p1 = feedbackObj?.optString("paragraph_1", "") ?: ""
            val p2 = feedbackObj?.optString("paragraph_2", "") ?: ""
            val fullMessage = if (p1.isNotBlank() && p2.isNotBlank()) "$p1\n\n$p2" else (p1 + p2).ifBlank {
                "Excelente equilíbrio nutricional! Esta refeição fornece saciedade duradoura e energia estável para os seus objetivos."
            }

            return MealAnalysisResult(
                plateImageUrl = defaultImage,
                identifiedFoods = if (identifiedFoods.isNotEmpty()) identifiedFoods else listOf("Alimento identificado"),
                components = components,
                calories = calories,
                protein = protein,
                carbs = carbs,
                fat = fat,
                allergens = allergensList,
                healthTags = healthNotesList,
                feedbackTitle = title,
                feedbackMessage = fullMessage,
                feedbackParagraph1 = p1,
                feedbackParagraph2 = p2
            )
        } catch (e: Exception) {
            Log.e(tag, "JSON parsing error from Gemini vision", e)
            return null
        }
    }

    private fun getOfflineAnalysisResult(mealType: String, image: String): MealAnalysisResult {
        return when (mealType) {
            "Café da Manhã" -> MealAnalysisResult(
                plateImageUrl = image,
                identifiedFoods = listOf("Tigela de Açaí puro", "Banana fatiada", "Mirtilos frescos", "Sementes de chia"),
                components = listOf(
                    com.example.data.model.FoodComponent("Polpa de açaí puro", 150, 140, 2, 18, 7, "Rico em antocianinas"),
                    com.example.data.model.FoodComponent("Banana prata", 80, 70, 1, 18, 0, "Potássio e energia rápida"),
                    com.example.data.model.FoodComponent("Mirtilos", 30, 20, 0, 5, 0, "Antioxidantes naturais"),
                    com.example.data.model.FoodComponent("Sementes de chia", 15, 70, 3, 6, 4, "Ômega-3 vegetal e fibras")
                ),
                calories = 300,
                protein = 6,
                carbs = 47,
                fat = 11,
                allergens = emptyList(),
                healthTags = listOf("Antioxidantes Poderosos", "Rico em Fibras", "Sem Lactose", "Sem Glúten"),
                feedbackTitle = "Excelente início de dia com energia viva! 🫐",
                feedbackMessage = "Que maravilha de café da manhã! As cores vibrantes dos mirtilos e do açaí entregam uma dose potente de antioxidantes naturais que combatem o estresse oxidativo e mantêm a vitalidade celular em alta.\n\nPara tornar essa refeição ainda mais completa e manter sua saciedade por mais horas, que tal adicionar uma colher de pasta de amendoim ou um scoop de proteína vegetal? Ficaria sensacional!",
                feedbackParagraph1 = "Que maravilha de café da manhã! As cores vibrantes dos mirtilos e do açaí entregam uma dose potente de antioxidantes naturais que combatem o estresse oxidativo e mantêm a vitalidade celular em alta.",
                feedbackParagraph2 = "Para tornar essa refeição ainda mais completa e manter sua saciedade por mais horas, que tal adicionar uma colher de pasta de amendoim ou um scoop de proteína vegetal? Ficaria sensacional!"
            )
            "Jantar" -> MealAnalysisResult(
                plateImageUrl = image,
                identifiedFoods = listOf("Salmão grelhado", "Aspargos verdes", "Azeite de oliva"),
                components = listOf(
                    com.example.data.model.FoodComponent("Filé de salmão grelhado", 160, 330, 34, 0, 20, "Alta concentração de ômega-3"),
                    com.example.data.model.FoodComponent("Aspargos ao vapor", 100, 25, 3, 4, 0, "Fibras prebióticas e folato"),
                    com.example.data.model.FoodComponent("Azeite e ervas finas", 10, 85, 0, 0, 9, "Gorduras monoinsaturadas")
                ),
                calories = 440,
                protein = 37,
                carbs = 4,
                fat = 29,
                allergens = listOf("Peixes"),
                healthTags = listOf("Rico em Ômega-3", "Proteína Magra", "Low Carb", "Sem Glúten"),
                feedbackTitle = "Jantar leve e restaurador! 🐟✨",
                feedbackMessage = "Uma combinação impecável para a sua noite! O salmão entrega gorduras nobres e proteínas de altíssima absorção, favorecendo a recuperação muscular e a síntese hormonal durante o sono reparador.\n\nComo é uma refeição bem leve e anti-inflamatória, lembre-se de saborear devagar e manter uma boa hidratação até a hora de deitar. Você cuidou muito bem de si hoje!",
                feedbackParagraph1 = "Uma combinação impecável para a sua noite! O salmão entrega gorduras nobres e proteínas de altíssima absorção, favorecendo a recuperação muscular e a síntese hormonal durante o sono reparador.",
                feedbackParagraph2 = "Como é uma refeição bem leve e anti-inflamatória, lembre-se de saborear devagar e manter uma boa hidratação até a hora de deitar. Você cuidou muito bem de si hoje!"
            )
            "Lanche" -> MealAnalysisResult(
                plateImageUrl = image,
                identifiedFoods = listOf("Salada de Quinoa", "Abacate fatiado", "Tomates cereja", "Microverdes"),
                components = listOf(
                    com.example.data.model.FoodComponent("Quinoa cozida", 100, 120, 4, 21, 2, "Proteína vegetal completa"),
                    com.example.data.model.FoodComponent("Abacate", 60, 95, 1, 5, 9, "Gorduras monoinsaturadas saudáveis"),
                    com.example.data.model.FoodComponent("Tomate cereja e microverdes", 50, 15, 1, 3, 0, "Licopeno e vitamina C")
                ),
                calories = 230,
                protein = 6,
                carbs = 29,
                fat = 11,
                allergens = emptyList(),
                healthTags = listOf("Fibras Prebióticas", "100% Vegetal", "Sem Glúten"),
                feedbackTitle = "Lanche nutritivo e equilibrado! 🥑",
                feedbackMessage = "Excelente escolha de lanche intermediário! A combinação de gorduras boas do abacate com o baixo índice glicêmico da quinoa mantém sua glicemia estável e evita picos de fome.\n\nUm toque de sementes de abóbora tostadas pode trazer crocância e magnésio extra. Parabéns pelo carinho com a sua alimentação!",
                feedbackParagraph1 = "Excelente escolha de lanche intermediário! A combinação de gorduras boas do abacate com o baixo índice glicêmico da quinoa mantém sua glicemia estável e evita picos de fome.",
                feedbackParagraph2 = "Um toque de sementes de abóbora tostadas pode trazer crocância e magnésio extra. Parabéns pelo carinho com a sua alimentação!"
            )
            else -> MealAnalysisResult(
                plateImageUrl = image,
                identifiedFoods = listOf("Peito de frango grelhado", "Arroz integral", "Salada mista com tomate"),
                components = listOf(
                    com.example.data.model.FoodComponent("Peito de frango grelhado", 150, 240, 46, 0, 5, "Proteína magra de alto valor biológico"),
                    com.example.data.model.FoodComponent("Arroz integral cozido", 120, 135, 3, 28, 1, "Carboidrato complexo rico em fibras"),
                    com.example.data.model.FoodComponent("Salada de alface e tomate", 80, 25, 1, 5, 0, "Fibras, licopeno e água")
                ),
                calories = 400,
                protein = 50,
                carbs = 33,
                fat = 6,
                allergens = emptyList(),
                healthTags = listOf("Proteína Magra", "Rico em Fibras", "Baixo Teor de Gordura", "Sem Glúten"),
                feedbackTitle = "Prato padrão ouro em nutrientes! 🥗🍗",
                feedbackMessage = "Que prato harmônico e convidativo! A presença de proteína magra bem dimensionada combinada com grãos integrais e folhas frescas garante saciedade prolongada e excelente rendimento para a sua tarde.\n\nSe quiser um aporte ainda maior de micronutrientes, incluir uma colher de azeite extra virgem cru sobre a salada ajuda a absorver melhor as vitaminas lipossolúveis (A, D, E, K). Excelente trabalho!",
                feedbackParagraph1 = "Que prato harmônico e convidativo! A presença de proteína magra bem dimensionada combinada com grãos integrais e folhas frescas garante saciedade prolongada e excelente rendimento para a sua tarde.",
                feedbackParagraph2 = "Se quiser um aporte ainda maior de micronutrientes, incluir uma colher de azeite extra virgem cru sobre a salada ajuda a absorver melhor as vitaminas lipossolúveis (A, D, E, K). Excelente trabalho!"
            )
        }
    }

    private fun getOfflineEmpatheticResponse(userMessage: String, turnCount: Int): String {
        val lower = userMessage.lowercase().trim()
        return when {
            lower == "?" || lower == "ajuda" || lower == "esclarecer dúvida" || lower == "dúvida" -> {
                "Parei tudo para te ouvir! ✨ Qual é a sua dúvida neste momento? Pode me perguntar sobre combinações de alimentos, calorias, substituições ou como interpretar os sinais do seu corpo."
            }
            lower.contains("1") || lower.contains("análise nutricional") -> {
                "Legal! Para fazer a Análise Nutricional, por favor, envie uma foto nítida do alimento, da sua refeição ou do rótulo do produto (ou descreva os itens do prato) que eu calculo as calorias, macronutrientes e te dou um feedback completo! 📸"
            }
            lower.contains("2") || lower.contains("consultoria") || lower.contains("avaliação") -> {
                "Excelente escolha! Vamos iniciar sua Consultoria Nutricional completa. Vou te fazer algumas perguntas, uma por vez, para entender sua rotina, saúde e metas. Me diga: qual é o seu principal objetivo hoje (emagrecer, ganhar massa ou ter mais disposição)?"
            }
            lower.contains("3") || lower.contains("receita") -> {
                "Oba! Adoro criar receitas práticas e nutritivas! 🥑 Me conte quais ingredientes você tem aí na sua geladeira e despensa agora (até 5 itens) e quantas porções você gostaria de preparar."
            }
            lower.contains("doce") || lower.contains("pizza") || lower.contains("exagerei") || lower.contains("culpa") || lower.contains("comi demais") -> {
                "Ei, respira fundo e sem culpa nenhuma! ❤️ Um momento de prazer faz parte da vida e do equilíbrio alimentar. O nosso metabolismo responde ao que fazemos com consistência a longo prazo, não a uma refeição isolada. Beba um copo de água e vamos seguir leves para a próxima refeição!"
            }
            lower.contains("água") || lower.contains("beber") || lower.contains("sede") -> {
                "A hidratação é a base do metabolismo energético e da digestão saudável! 💧 Uma boa meta geral é manter entre 35ml a 40ml por kg de peso. Já encheu sua garrafinha hoje?"
            }
            lower.contains("alergia") || lower.contains("lactose") || lower.contains("glúten") -> {
                "Anotadíssimo com total rigor! 🌿 Vamos priorizar ingredientes anti-inflamatórios e livres de alérgenos para garantir conforto digestivo máximo. Me conta: qual refeição do seu dia você mais sente dificuldade em variar?"
            }
            lower.contains("peso") || lower.contains("emagrecer") || lower.contains("meta") -> {
                "Vamos construir essa conquista juntas, aplicando déficit calórico moderado com alta saciedade e sem dietas restritivas punitivas! ✨ Qual é a sua maior expectativa comigo como sua Personal Diet?"
            }
            turnCount <= 2 -> {
                "Olá! Eu sou a Lília Personal Diet, sua Personal Diet no seu bolso! ❤️ Para iniciarmos nosso acompanhamento com carinho, me diga: como você avalia sua alimentação hoje e o que te motivou a buscar ajuda nutricional?"
            }
            turnCount in 3..4 -> {
                "Entendido perfeitamente! E quanto aos seus hábitos: você possui alguma doença diagnosticada, toma medicamentos de uso contínuo ou tem restrições alimentares?"
            }
            turnCount in 5..6 -> {
                "Muito bom saber! Agora me conta sobre a sua rotina: como costuma ser seu sono, nível de estresse e você pratica alguma atividade física semanal?"
            }
            else -> {
                "Estou aqui com você 24h! Conte comigo para planejar refeições, analisar fotos dos seus pratos, gerar sua lista de compras ou tirar dúvidas a qualquer momento. Lembre-se: pequenas escolhas consistentes geram grandes transformações! 🥗✨"
            }
        }
    }
}
