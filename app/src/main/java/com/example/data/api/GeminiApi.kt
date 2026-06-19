package com.example.data.api

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String
)

object GeminiApi {
    private const val TAG = "GeminiApi"
    private const val MODEL_NAME = "gemini-3.5-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL_NAME:generateContent"

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    suspend fun generateContent(prompt: String, systemInstruction: String? = null): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            Log.e(TAG, "Gemini API key is not configured or is placeholder!")
            return@withContext "API_KEY_ERROR"
        }

        try {
            val jsonBody = JSONObject().apply {
                val contentsArray = JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", prompt)
                            })
                        })
                    })
                }
                put("contents", contentsArray)

                if (systemInstruction != null) {
                    put("systemInstruction", JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", systemInstruction)
                            })
                        })
                    })
                }
            }

            val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val errBody = response.body?.string() ?: ""
                    Log.e(TAG, "Unsuccessful response from Gemini API: ${response.code} $errBody")
                    return@withContext "Error: Request failed with status ${response.code}"
                }

                val responseString = response.body?.string() ?: ""
                val responseJson = JSONObject(responseString)
                val candidates = responseJson.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val firstCandidate = candidates.getJSONObject(0)
                    val content = firstCandidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        return@withContext parts.getJSONObject(0).optString("text")
                    }
                }
                return@withContext "Error: No text in response content."
            }
        } catch (e: IOException) {
            Log.e(TAG, "Network IO Exception during Gemini call", e)
            return@withContext "Network Error: ${e.localizedMessage}. Please check internet connection."
        } catch (e: Exception) {
            Log.e(TAG, "Exception during Gemini call", e)
            return@withContext "Error: ${e.localizedMessage}"
        }
    }

    suspend fun generateQuiz(moduleName: String): List<QuizQuestion> = withContext(Dispatchers.IO) {
        val prompt = """
            You are an expert tutor for OFPPT (Office de la Formation Professionnelle et de la Promotion du Travail).
            Generate a set of exactly 4 challenging educational multiple-choice quiz questions for the module: "$moduleName".
            Output strictly in JSON format. The response must be a single JSON object containing a "questions" array.
            Each question object must strictly contain:
            1. "question": string, the text of the question
            2. "options": array of exactly 4 strings, represent multiple choices (A, B, C, D)
            3. "correctAnswerIndex": integer (0 to 3), representing the zero-based index of the correct option
            4. "explanation": string, a helpful explanation of why this option is correct.

            Do not wrap the response in ```json ``` or any other formatting codes. Return ONLY valid JSON, starting with { and ending with }.
        """.trimIndent()

        val systemInstruction = "You are a professional quiz generator that outputs strictly legal JSON matching the specified format."

        val responseText = generateContent(prompt, systemInstruction)
        if (responseText == "API_KEY_ERROR" || responseText.startsWith("Error:") || responseText.startsWith("Network")) {
            return@withContext getLocalFallbackQuiz(moduleName)
        }

        try {
            // Clean response text from backticks just in case model ignores instruction
            val cleaned = responseText.trim()
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()

            val responseJson = JSONObject(cleaned)
            val questionsArray = responseJson.getJSONArray("questions")
            val quizQuestions = mutableListOf<QuizQuestion>()

            for (i in 0 until questionsArray.length()) {
                val qObj = questionsArray.getJSONObject(i)
                val questionText = qObj.getString("question")
                val optionsArray = qObj.getJSONArray("options")
                val options = mutableListOf<String>()
                for (j in 0 until optionsArray.length()) {
                    options.add(optionsArray.getString(j))
                }
                val correctIndex = qObj.getInt("correctAnswerIndex")
                val explanation = qObj.optString("explanation", "Correct Answer!")
                quizQuestions.add(QuizQuestion(questionText, options, correctIndex, explanation))
            }

            if (quizQuestions.isEmpty()) {
                return@withContext getLocalFallbackQuiz(moduleName)
            }
            return@withContext quizQuestions
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse quiz JSON response from Gemini, using fallbacks. Response text: $responseText", e)
            return@withContext getLocalFallbackQuiz(moduleName)
        }
    }

    private fun getLocalFallbackQuiz(moduleName: String): List<QuizQuestion> {
        return when (moduleName.lowercase().trim()) {
            "react" -> listOf(
                QuizQuestion(
                    "What is the hook used to manage state components in React?",
                    listOf("useState", "useEffect", "useContext", "useReducer"),
                    0,
                    "useState is the standard and most simple Hook used to add state to functional components in React."
                ),
                QuizQuestion(
                    "Which lifecycle emulator hook runs after the component renders?",
                    listOf("useLayoutEffect", "useMemo", "useEffect", "useCallback"),
                    2,
                    "useEffect runs asynchronously after the render phase, making it perfect for custom side-effects, data fetching and subscriptions."
                ),
                QuizQuestion(
                    "What represents the 'Virtual DOM' in React?",
                    listOf("A direct copy of the HTML head", "An in-memory lightweight representation of the real DOM", "A browser extension for Chrome", "A security framework"),
                    1,
                    "The Virtual DOM is React's local conceptual copy of the UI, which it syncs dynamically using reconciliation."
                ),
                QuizQuestion(
                    "What is the purpose of 'keys' in React Lists?",
                    listOf("To style elements uniquely", "To encrypt list data", "To help React identify which items have changed, been added, or been removed", "To link components to backend"),
                    2,
                    "Keys provide list elements with a stable identity so React can efficiently locate and update changes during diffing."
                )
            )
            "laravel" -> listOf(
                QuizQuestion(
                    "What is the default templating engine used in Laravel?",
                    listOf("Twig", "Blade", "Smarty", "Mustache"),
                    1,
                    "Blade is Laravel's intuitive and lightweight built-in templating system, compiling to cached PHP code."
                ),
                QuizQuestion(
                    "Which command-line interface helper tool is bundled with Laravel?",
                    listOf("Artisan", "Composer", "NPM", "Maven"),
                    0,
                    "Artisan is laravel's interactive CLI helper tool loaded with features to scaffold controllers, models, and run migrations."
                ),
                QuizQuestion(
                    "What is Eloquent in Laravel?",
                    listOf("The route controller engine", "A code minifier module", "An Object-Relational Mapper (ORM) system", "A user access manager"),
                    2,
                    "Eloquent ORM is Laravel's ActiveRecord implementation for relational database tables, letting developers query with fluent models."
                ),
                QuizQuestion(
                    "Which class represents the HTTP web traffic router entry point in Laravel?",
                    listOf("Kernel", "Router", "Route", "Controller"),
                    2,
                    "Routes are defined using the Route class facade in files like web.php and api.php."
                )
            )
            "mongodb" -> listOf(
                QuizQuestion(
                    "MongoDB is classified as which type of database?",
                    listOf("Relational, SQL-based", "Document-oriented NoSQL", "Key-Value cache store", "Graph-relational database"),
                    1,
                    "MongoDB is a leading document-oriented NoSQL database that stores data as flexible, JSON-like BSON records."
                ),
                QuizQuestion(
                    "What formatting format represents documents internally saved in MongoDB?",
                    listOf("JSON", "XML", "BSON", "YAML"),
                    2,
                    "BSON (Binary JSON) is MongoDB's internal serialized binary-encoded format that extends standard JSON types."
                ),
                QuizQuestion(
                    "What is the equivalent concept of a SQL 'Row' in MongoDB?",
                    listOf("Document", "Collection", "Primary Index", "Field"),
                    0,
                    "A Document is equivalent to a database Row, while a Collection is equivalent to a SQL Table."
                ),
                QuizQuestion(
                    "Which aggregation operator is used to filter documents in a pipeline?",
                    listOf("\$project", "\$match", "\$group", "\$sort"),
                    1,
                    "\$match is used to filter document streams, passing only matched records down the aggregation pipeline stages."
                )
            )
            else -> listOf(
                QuizQuestion(
                    "What is Agile development focused on?",
                    listOf("Detailed micro-management", "Inflexible sequential execution", "Iterative progress, customer collaboration, and adapting to change", "Comprehensive initial blueprinting"),
                    2,
                    "Agile focus is on continuous delivery, quick adaptive iterations, cross-functional collaboration and feedback loops."
                ),
                QuizQuestion(
                    "In UML, what does an Use Case Diagram describe?",
                    listOf("System database indexes layout", "The interactions between actors and the system's operational cases", "Code library interface functions", "Server cluster hardware deployment"),
                    1,
                    "An Use Case diagram serves to illustrate how actors (users or external modules) interact with system-level functional components."
                ),
                QuizQuestion(
                    "What represents the cloud computing deployment model where services are hosted publically?",
                    listOf("Private Cloud", "On-Premises server", "Public Cloud", "Hybrid Local center"),
                    2,
                    "Public clouds (like AWS, Azure, GCP) offer scalable elastic resources managed over the internet by third-party providers."
                ),
                QuizQuestion(
                    "What does UML stand for?",
                    listOf("Unified Modeling Language", "Universal Markup Layout", "Unit Module Line", "Unique Method Loader"),
                    0,
                    "UML stands for Unified Modeling Language, which is the standardized visual blueprinting language for software architectures."
                )
            )
        }
    }
}
