package com.example.data

data class RoastQuote(
    val id: String,
    val text: String,
    val spokenText: String,
    val category: String,
    val severityEmoji: String,
    val punchline: String
)

object RoastQuotesRepository {

    val quotes = listOf(
        RoastQuote(
            id = "q1",
            text = "ازيك يا حجاج، ذاكر يا فاشل! 📢",
            spokenText = "ازيك يا حجاج.. ذاكر يا فاشل!",
            category = "كلاسيكيات حجاج",
            severityEmoji = "🔥",
            punchline = "الدفعة كلها بتذاكر وأنت ماسك الموبايل!"
        ),
        RoastQuote(
            id = "q2",
            text = "كوباية الشاي رقم ٦ بتبرد وأنت لسه بتبص في الفهرس! ☕",
            spokenText = "كوباية الشاي رقم ستة بردت.. وأنت لسه بتبص في الفهرس يا حجاج! قوم ذاكر!",
            category = "توبيخ الشاي",
            severityEmoji = "☕",
            punchline = "الشاي خلص والمذاكرة لسه مابدأتش!"
        ),
        RoastQuote(
            id = "q3",
            text = "الدفعة خلصت المنهج وبتراجع، وأنت بتسأل الامتحان إم سي كيو ولا مقالي؟ 🤦‍♂️",
            spokenText = "الدفعة خلصت المنهج وبتراجع، وأنت بتسأل الامتحان إم سي كيو ولا مقالي؟",
            category = "صدمة الامتحانات",
            severityEmoji = "🚨",
            punchline = "فوق يا حجاج الامتحان الأسبوع الجاي!"
        ),
        RoastQuote(
            id = "q4",
            text = "مفيش كيمياء هتدخل دماغك بالتناضح! افتح الكتاب يا بني! 🧪",
            spokenText = "مفيش كيمياء هتدخل نفوخك بالتناضح، افتح الكتاب وبطل تبريقة!",
            category = "علمي مش هينفعك الكسل",
            severityEmoji = "🧪",
            punchline = "الكتاب مش بيعدي المعلومات بالبلوتوث!"
        ),
        RoastQuote(
            id = "q5",
            text = "السرير مش هيطلعك مهندس ولا دكتور يا فاشل! 🛏️❌",
            spokenText = "السرير مش هيطلعك مهندس ولا دكتور يا فاشل! سيب اللحاف وقوم عالمكتب!",
            category = "حرب السرير",
            severityEmoji = "⚡",
            punchline = "المكتب مستنيك بقاله شهر!"
        ),
        RoastQuote(
            id = "q6",
            text = "ألو؟ مستقبل حجاج بيضيع في الريلز والتيك توك.. حد يلحقه بملزمة! 📱",
            spokenText = "ألو؟ مستقبل حجاج بيضيع في الريلز والتيك توك.. حد يلحقه بملزمة!",
            category = "إدمان الموبايل",
            severityEmoji = "📱",
            punchline = "سكرول سكرول والدرجات بتطير!"
        ),
        RoastQuote(
            id = "q7",
            text = "الملازم بتعيط في الدرج يا حجاج.. ارحمها وبص فيها خمس دقايق! 📄",
            spokenText = "الملازم بتعيط في الدرج يا حجاج.. ارحمها وبص فيها خمس دقايق!",
            category = "ملازم منسية",
            severityEmoji = "😭",
            punchline = "الورق مصدوم من قلة الاهتمام!"
        ),
        RoastQuote(
            id = "q8",
            text = "المجموع مش هييجي بالدعاء والاستغفار بس.. اعقلها وتوكل وذاكر! 🤲",
            spokenText = "المجموع مش هييجي بالدعاء بس.. اعقلها وتوكل وافتح الكتاب يا حجاج!",
            category = "تذكرة",
            severityEmoji = "💡",
            punchline = "ربنا يوفق اللي بيسعى مش اللي نايم!"
        ),
        RoastQuote(
            id = "q9",
            text = "أهلك مستنيين يفرحوا بيك.. بلاش تخليهم يسألوا عن مصاريف الدروس راحت فين! 👨‍👩‍👦",
            spokenText = "أهلك مستنيين يفرحوا بيك.. بلاش تخليهم يسألوا مصاريف الدروس راحت فين يا حجاج!",
            category = "عقدة الضمير",
            severityEmoji = "💔",
            punchline = "فرح أمك وأبوك يا بطل!"
        ),
        RoastQuote(
            id = "q10",
            text = "بص في المراية كده.. دي ملامح واحد عنده امتحان بعد أسبوعين؟! 🪞",
            spokenText = "بص في المراية يا حجاج.. دي ملامح واحد عنده امتحان بعد أسبوعين؟ قوم ارمي الموبايل!",
            category = "مواجهة الحقيقة",
            severityEmoji = "👀",
            punchline = "الروقان ده هيتقلب صياح ليلة الامتحان!"
        ),
        RoastQuote(
            id = "q11",
            text = "عاش يا حجاج! إنت قدها وبطل.. بس سيب الكسل وهتبقى دحيح الدفعة! 🏆",
            spokenText = "عاش يا حجاج! إنت قدها وبطل.. بس سيب الكسل وهتبقى دحيح الدفعة بإذن الله!",
            category = "شحنة أمل",
            severityEmoji = "🌟",
            punchline = "أنت ذكي بس محتاج تركز!"
        ),
        RoastQuote(
            id = "q12",
            text = "كل دقيقة بتضيعها دلوقتي هتدفع تمنها توتر ليلة الامتحان! ⏳",
            spokenText = "كل دقيقة بتضيعها دلوقتي هتدفع تمنها توتر وسهر ليلة الامتحان.. الحق نفسك!",
            category = "إدارة الوقت",
            severityEmoji = "⏱️",
            punchline = "ساعة تركيز تريحك أسبوع!"
        )
    )

    fun getRandomQuote(): RoastQuote {
        return quotes.random()
    }
}
