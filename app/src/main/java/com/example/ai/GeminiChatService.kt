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

    // Default supported model per AI Studio Gemini API guidelines
    private const val MODEL_NAME = "gemini-3.5-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL_NAME:generateContent"

    // OkHttpClient with 60-second timeouts per guidelines
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private const val SYSTEM_PROMPT = """
أنت "مستر حجاج الفرفوش"، مدرس مصري خفيف الظل متخصص في تدريس طالب في الصف الثاني الإعدادي (تانية إعدادي) اسمه حجاج.
المواد التي تساعده فيها: الرياضيات، العلوم، اللغة العربية، واللغة الإنجليزية.
قواعدك الأساسية:
1. تحدث دائماً بالعامية المصرية البسيطة والمرحة مع هزار خفيف وتشجيع دائم لطيف ("يا وحش"، "يا دحيح"، "يا بطل تانية إعدادي").
2. ردك يجب أن يكون قصيراً جداً ومبسطاً (من 2 إلى 4 جمل فقط) ليكون خفيفاً ومناسباً للاستماع الصوتي المباشر.
3. التزم فقط بالمذاكرة والدروس والمسائل والتشجيع على إنهاء جلسات البومودورو وفتح المكافآت.
4. استخدم تشبيهات مصرية مرحة وسهلة الفهم تناسب ولد في تانية إعدادي.
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

        // If key is empty or default template placeholder, return smart Egyptian offline response
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY" || apiKey == "AIzaSyDummyKeyForBuild") {
            return@withContext getOfflineSmartResponse(userMessage, studentName)
        }

        try {
            val contentsArray = JSONArray()

            // System context turn
            val systemObj = JSONObject().apply {
                put("role", "user")
                put("parts", JSONArray().put(JSONObject().put("text", "تعليمات الشخصية: $SYSTEM_PROMPT. اسم الطالب: $studentName. جاوب بالعامية المصرية في 2 أو 3 جمل فقط.")))
            }
            contentsArray.put(systemObj)

            val modelAck = JSONObject().apply {
                put("role", "model")
                put("parts", JSONArray().put(JSONObject().put("text", "يا هلا بحبيب قلبي $studentName دحيح تانية إعدادي! اسأل وأنا أجاوبك بالمللي وبدون كلكعة!")))
            }
            contentsArray.put(modelAck)

            // Add previous recent history turns (limit last 6 turns to keep context lightweight)
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
                    put("maxOutputTokens", 300)
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
                        val replyText = parts.getJSONObject(0).optString("text", "")
                        if (replyText.isNotBlank()) {
                            return@withContext replyText.trim()
                        }
                    }
                }
            }

            // Fallback if response was not ok or parse failed
            getOfflineSmartResponse(userMessage, studentName)
        } catch (_: Exception) {
            getOfflineSmartResponse(userMessage, studentName)
        }
    }

    fun getOfflineSmartResponse(query: String, studentName: String): String {
        val q = query.lowercase()
        return when {
            q.contains("رياضيات") || q.contains("جذر") || q.contains("تكعيب") || q.contains("متوسط") || q.contains("مثلث") -> {
                "يا وحش الرياضيات يا $studentName! افتكر في تانية إعدادي: الجذر التكعيبي لسالب 8 هو سالب 2، ونقطة تقاطع متوسطات المثلث بتقسم بنسبة 1 إلى 2 من جهة القاعدة! حل مسألة دلوقتي وافتح مكافأتك! 📐🎮"
            }
            q.contains("علوم") || q.contains("جدول") || q.contains("دوري") || q.contains("أوزون") || q.contains("غلاف") || q.contains("ذري") -> {
                "العلوم في جيبك يا $studentName! جدول مندليف رتب حسب الوزن الذري، وموزلي رتب حسب العدد الذري.. وطبقة الأوزون موجودة في الستراتوسفير عشان تحمينا! ركز 10 دقايق وهتبقى دحيح! 🧪✨"
            }
            q.contains("عربي") || q.contains("نحو") || q.contains("معرب") || q.contains("مبني") || q.contains("نعت") || q.contains("حال") -> {
                "النحو لعبتنا يا $studentName! الجمل بعد النكرات صفات (نعت) وبعد المعارف أحوال.. وكل الأسماء الموصولة مبنية ما عدا (اللذان واللتان) معربتان زي المثنى! شاطر ومصحصح دايماً! 📖"
            }
            q.contains("انجليزي") || q.contains("إنجليزي") || q.contains("english") || q.contains("while") || q.contains("grammar") -> {
                "إنجليزي تانية إعدادي سهل جداً يا $studentName! قاعدة While بيجي بعدها Past Continuous يعني was/were وفعل فيه ing.. ذاكر الدرس واشربلك عصير يا بطل! 🇬🇧☕"
            }
            q.contains("مكافأة") || q.contains("مكافات") || q.contains("لعب") || q.contains("بلايستيشن") || q.contains("حلوى") -> {
                "المكافأة مستنياك يا $studentName! خلص جلسة بومودورو 25 دقيقة تركيز من غير تشتيت، وروح فوراً لصندوق المكافآت وافتح 30 دقيقة لعب أو حلوى تستاهلها! 🎁🎮"
            }
            q.contains("كسلان") || q.contains("مش قادر") || q.contains("زهقان") || q.contains("تعبان") || q.contains("شجعني") -> {
                "ولا كسلان ولا حاجة يا $studentName! قوم اغسل وشك بماية باردة، اقعد على مكتبك وافتح المؤقت 15 دقيقة بس.. هتلاقي عقلك نور وتانية إعدادي بقت أسهل سنة عدت عليك! 🔥💪"
            }
            q.contains("امتحان") || q.contains("مستعد") || q.contains("خايف") -> {
                "الامتحان ده لعب عيال لواحد زيك يا $studentName! راجع الكويز السريع اللي في التطبيق واقرأ ملخص الدرس.. مستر حجاج واثق فيك وهتطلع من الأوائل إن شاء الله! 🏆"
            }
            else -> {
                "عاش يا $studentName! سؤال في الجون! أهم حاجة دلوقتي تقفل التشتيت وتركز في مادة واحدة، اسألني في أي مسألة أو قاعدة في تانية إعدادي وأنا أشرحهالك في ثواني وبدون تعقيد! 💡👏"
            }
        }
    }
}
