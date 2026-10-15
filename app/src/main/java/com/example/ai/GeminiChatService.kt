package com.example.ai

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class ChatMessage(
    val id: String,
    val sender: String, // "user" or "model"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

object GeminiChatService {

    private const val MODEL_NAME = "gemini-3.8-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL_NAME:generateContent"

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private const val SYSTEM_PROMPT = """
أنت "مستر حجاج الفرفوش"، مدرس مصري خبير في مناهج تانية إعدادي (رياضيات، علوم، لغة عربية، إنجليزي).
تتحدث بالعامية المصرية المرحة مع هزار خفيف وتشجيع دائم لطالب تانية إعدادي اسمه حجاج.
تشرح أي درس أو مسألة يطلبها بأسلوب مبسط جداً وبالأمثلة المصرية الواقعية، وتشجعه على فتح المكافآت وجمع جلسات البومودورو.
أجوبتك مختصرة، مفيدة، ودمها خفيف!
    """

    suspend fun sendMessage(
        history: List<ChatMessage>,
        userMessage: String,
        studentName: String = "حجاج"
    ): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (_: Exception) {
            ""
        }

        // If key is empty or default template placeholder, return smart offline fallback
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY" || apiKey == "AIzaSyDummyKeyForBuild") {
            return@withContext getOfflineSmartResponse(userMessage, studentName)
        }

        try {
            val contentsArray = JSONArray()

            // System instruction or initial context turn
            val systemObj = JSONObject().apply {
                put("role", "user")
                put("parts", JSONArray().put(JSONObject().put("text", "تعليمات: $SYSTEM_PROMPT الطالب اسمه $studentName")))
            }
            contentsArray.put(systemObj)

            val modelAck = JSONObject().apply {
                put("role", "model")
                put("parts", JSONArray().put(JSONObject().put("text", "يا هلا بيك وبحبيب قلبي $studentName دحيح تانية إعدادي القادم! اؤمرني يا بطل!")))
            }
            contentsArray.put(modelAck)

            // Add previous recent history turns (limit last 6)
            val recentHistory = history.takeLast(6)
            for (msg in recentHistory) {
                val turn = JSONObject().apply {
                    put("role", if (msg.sender == "user") "user" else "model")
                    put("parts", JSONArray().put(JSONObject().put("text", msg.text)))
                }
                contentsArray.put(turn)
            }

            // Current message
            val currentTurn = JSONObject().apply {
                put("role", "user")
                put("parts", JSONArray().put(JSONObject().put("text", userMessage)))
            }
            contentsArray.put(currentTurn)

            val requestJson = JSONObject().apply {
                put("contents", contentsArray)
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.7)
                    put("maxOutputTokens", 600)
                })
            }

            val requestBody = requestJson.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (response.isSuccessful && !responseBody.isNullOrEmpty()) {
                val json = JSONObject(responseBody)
                val candidates = json.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val content = candidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        return@withContext parts.getJSONObject(0).optString("text", "عاش يا $studentName! إجابة تمام!")
                    }
                }
            }

            // Fallback if network failed
            getOfflineSmartResponse(userMessage, studentName)
        } catch (_: Exception) {
            getOfflineSmartResponse(userMessage, studentName)
        }
    }

    private fun getOfflineSmartResponse(query: String, studentName: String): String {
        val q = query.lowercase()
        return when {
            q.contains("رياضيات") || q.contains("جذر") || q.contains("مثلث") -> {
                "يا هلا يا $studentName! في رياضيات تانية إعدادي افتكر دايماً: الجذر التكعيبي بيحافظ على الإشارة السالبة، ونقطة تقاطع المتوسطات بتقسم بنسبة 1 : 2 من جهة القاعدة! زاكر مسألتين وتعال افتح مكافأة البلايستيشن! 📐🎮"
            }
            q.contains("علوم") || q.contains("جدول") || q.contains("أوزون") -> {
                "العلوم أسهل مادة يا $studentName! افتكر مندليف رتب حسب الوزن الذري وموزلي حسب العدد الذري، وطبقة الأوزون في الستراتوسفير بتحمينا من الأشعة فوق البنفسجية! ذاكر ربع ساعة واشرب عصير! 🧪✨"
            }
            q.contains("عربي") || q.contains("نحو") || q.contains("معرب") -> {
                "النحو في جيبك يا باشا! الجمل بعد النكرات صفات وبعد المعارف أحوال، والأسماء الموصولة مبنية ما عدا اللذان واللتان معربان إعراب المثنى! فخور بيك يا دحيح! 📖"
            }
            q.contains("انجليزي") || q.contains("إنجليزي") || q.contains("english") -> {
                "English is very easy ya $studentName! Remember: While + Past Continuous (was/were + v-ing), and First Conditional (If + present simple -> will + inf)! Keep going ya champion! 🇬🇧"
            }
            q.contains("مكافأة") || q.contains("لعب") || q.contains("هدية") -> {
                "عايز مكافأة يا سيدي؟ خلص جلسة بومودورو 25 دقيقة دلوقتي وشوف قائمة المكافآت هتفتحلك شيكولاتة أو 30 دقيقة لعب فوراً! المذاكرة بتبقى أحلى بالمكافأة الملموسة! 🎁🍫"
            }
            q.contains("كسلان") || q.contains("مش قادر") || q.contains("تعبان") -> {
                "سيب الدلع يا $studentName! قوم اغسل وشك بماية ساقعة، افتح الشباك، واقعد 10 دقايق بس على المكتب.. هتلاقي الحماس رجعلك وتانية إعدادي في جيبك! 🔥"
            }
            else -> {
                "الله عليك يا $studentName! سؤال في الجون! أهم حاجة تذاكر بومودورو بتركيز بدون تشتيت، وبعدها تروح تفتح المكافأة اللي تستاهلها! اسألني في أي درس واقف معاك في تانية إعدادي وأنا أشرحهولك بالمللي! 💡👏"
            }
        }
    }
}
