package com.example.data.remote

import com.squareup.moshi.Json

data class GeminiGenerateRequest(
    @Json(name = "contents") val contents: List<GeminiContent>,
    @Json(name = "systemInstruction") val systemInstruction: GeminiContent? = null,
    @Json(name = "generationConfig") val generationConfig: GeminiGenerationConfig? = null
)

data class GeminiContent(
    @Json(name = "role") val role: String? = null,
    @Json(name = "parts") val parts: List<GeminiPart>
)

data class GeminiPart(
    @Json(name = "text") val text: String? = null,
    @Json(name = "inlineData") val inlineData: GeminiInlineData? = null
)

data class GeminiInlineData(
    @Json(name = "mimeType") val mimeType: String,
    @Json(name = "data") val data: String
)

data class GeminiGenerationConfig(
    @Json(name = "temperature") val temperature: Float? = 0.4f,
    @Json(name = "topP") val topP: Float? = 0.95f,
    @Json(name = "topK") val topK: Int? = 40,
    @Json(name = "responseMimeType") val responseMimeType: String? = null,
    @Json(name = "responseSchema") val responseSchema: Map<String, Any>? = null
)

data class GeminiGenerateResponse(
    @Json(name = "candidates") val candidates: List<GeminiCandidate>? = null,
    @Json(name = "error") val error: GeminiError? = null
)

data class GeminiCandidate(
    @Json(name = "content") val content: GeminiContent? = null,
    @Json(name = "finishReason") val finishReason: String? = null
)

data class GeminiError(
    @Json(name = "code") val code: Int? = null,
    @Json(name = "message") val message: String? = null
)

data class GeminiMealAnalysisJson(
    @Json(name = "is_food_detected") val isFoodDetected: Boolean? = true,
    @Json(name = "no_food_reason") val noFoodReason: String? = null,
    @Json(name = "identified_foods") val identifiedFoods: List<GeminiFoodItemJson>? = null,
    @Json(name = "totals") val totals: GeminiTotalsJson? = null,
    @Json(name = "allergens") val allergens: List<String>? = null,
    @Json(name = "health_notes") val healthNotes: List<String>? = null,
    @Json(name = "feedback") val feedback: GeminiFeedbackJson? = null
)

data class GeminiFoodItemJson(
    @Json(name = "name") val name: String? = null,
    @Json(name = "estimated_grams") val estimatedGrams: Int? = null,
    @Json(name = "calories") val calories: Int? = null,
    @Json(name = "protein_g") val proteinG: Int? = null,
    @Json(name = "carbs_g") val carbsG: Int? = null,
    @Json(name = "fat_g") val fatG: Int? = null,
    @Json(name = "fiber_g") val fiberG: Int? = null,
    @Json(name = "notes") val notes: String? = null
)

data class GeminiTotalsJson(
    @Json(name = "calories") val calories: Int? = null,
    @Json(name = "carbs_g") val carbsG: Int? = null,
    @Json(name = "protein_g") val proteinG: Int? = null,
    @Json(name = "fat_g") val fatG: Int? = null,
    @Json(name = "fiber_g") val fiberG: Int? = null
)

data class GeminiFeedbackJson(
    @Json(name = "title") val title: String? = null,
    @Json(name = "paragraph_1") val paragraph1: String? = null,
    @Json(name = "paragraph_2") val paragraph2: String? = null
)
