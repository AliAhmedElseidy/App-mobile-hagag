package com.example.data

enum class QuoteCategory(val label: String, val emoji: String) {
    START_SESSION("بداية المذاكرة", "📢"),
    MID_SESSION("وسط الجلسة والتركيز", "⚡"),
    POST_SUCCESS("عاش يا دحيح! (مدح)", "🏆"),
    ON_FAILURE("توبيخ الهروب والكسل", "🚨"),
    STREAK_BROKEN("كسر السلسلة", "💔")
}

data class RoastQuote(
    val id: String,
    val text: String,
    val spokenText: String,
    val category: QuoteCategory,
    val severityEmoji: String,
    val punchline: String
)

object RoastQuotesRepository {

    val quotes: List<RoastQuote> = listOf(
        // ==========================================
        // 1. START_SESSION (بداية الجلسة وتوبيخ البداية) - 15 جملة
        // ==========================================
        RoastQuote(
            id = "start_1",
            text = "إزّيك يا حجاج.. ذاكر يا فاشل! 📢",
            spokenText = "إزّيك يا حجاج.. ذاكر يا فاشل!",
            category = QuoteCategory.START_SESSION,
            severityEmoji = "🔥",
            punchline = "الدفعة كلها بتذاكر وأنت ماسك الموبايل!"
        ),
        RoastQuote(
            id = "start_2",
            text = "كوباية الشاي رقم ستة بردت وأنت لسه بتبص في الفهرس! ☕",
            spokenText = "كوباية الشاي رقم ستة بردت وأنت لسه بتبص في الفهرس يا حجاج! افتح الدرس الأول!",
            category = QuoteCategory.START_SESSION,
            severityEmoji = "☕",
            punchline = "الشاي خلص والمذاكرة لسه مابدأتش!"
        ),
        RoastQuote(
            id = "start_3",
            text = "السرير مش هيطلعك مهندس ولا دكتور يا فاشل! 🛏️❌",
            spokenText = "السرير مش هيطلعك مهندس ولا دكتور يا فاشل! سيب اللحاف وقوم عالمكتب!",
            category = QuoteCategory.START_SESSION,
            severityEmoji = "⚡",
            punchline = "المكتب مستنيك بقاله شهر تراب!"
        ),
        RoastQuote(
            id = "start_4",
            text = "ألو؟ مستقبل حجاج بيضيع في الريلز والتيك توك.. حد يلحقه بملزمة! 📱",
            spokenText = "ألو؟ مستقبل حجاج بيضيع في الريلز والتيك توك.. حد يلحقه بملزمة علوم!",
            category = QuoteCategory.START_SESSION,
            severityEmoji = "📱",
            punchline = "سكرول سكرول والدرجات بتطير!"
        ),
        RoastQuote(
            id = "start_5",
            text = "الدفعة خلصت المنهج وبتراجع، وأنت بتسأل الامتحان إم سي كيو ولا مقالي؟ 🤦‍♂️",
            spokenText = "الدفعة خلصت المنهج وبتراجع، وأنت بتسأل الامتحان مقالي ولا إم سي كيو؟ فوق يا حجاج!",
            category = QuoteCategory.START_SESSION,
            severityEmoji = "🚨",
            punchline = "امتحان تانية إعدادي مش هيهزر معاك!"
        ),
        RoastQuote(
            id = "start_6",
            text = "مفيش كيمياء هتدخل نفوخك بالتناضح! افتح الكتاب وبطل تبريقة! 🧪",
            spokenText = "مفيش علوم ولا كيمياء هتدخل نفوخك بالتناضح! افتح الكتاب وبطل تبريقة!",
            category = QuoteCategory.START_SESSION,
            severityEmoji = "🧪",
            punchline = "الكتاب مش بينقل المعلومات بالبلوتوث!"
        ),
        RoastQuote(
            id = "start_7",
            text = "الملازم بتعيط في الدرج يا حجاج.. ارحمها وبص فيها ربع ساعة! 📄",
            spokenText = "الملازم بتعيط في الدرج يا حجاج.. ارحمها وبص فيها ربع ساعة!",
            category = QuoteCategory.START_SESSION,
            severityEmoji = "😭",
            punchline = "الورق مصدوم من قلة اهتمامك!"
        ),
        RoastQuote(
            id = "start_8",
            text = "أهلك دافعين دم قلبهم في الدروس وأنت مستني الإلهام ينزل عليك! 💸",
            spokenText = "أهلك دافعين دم قلبهم في الدروس، وأنت قاعد مستني وحي الإلهام!",
            category = QuoteCategory.START_SESSION,
            severityEmoji = "💔",
            punchline = "فرح أمك وأبوك وبطل دلع!"
        ),
        RoastQuote(
            id = "start_9",
            text = "قاعد بترتب المكتب وبتبري الأقلام بقالك ساعة.. ما تذاكر يا عم! ✏️",
            spokenText = "قاعد بترتب المكتب وبتبري الأقلام بقالك ساعة.. ما تفتح الملزمة يا حجاج!",
            category = QuoteCategory.START_SESSION,
            severityEmoji = "🎨",
            punchline = "طقوس ما قبل المذاكرة أطول من المذاكرة نفسها!"
        ),
        RoastQuote(
            id = "start_10",
            text = "بص في المراية كده.. دي ملامح طالب عنده امتحان كمان كام أسبوع؟! 🪞",
            spokenText = "بص في المراية يا حجاج.. دي ملامح طالب في تانية إعدادي عنده امتحانات؟",
            category = QuoteCategory.START_SESSION,
            severityEmoji = "👀",
            punchline = "الروقان ده هيتقلب صياح ليلة الامتحان!"
        ),
        RoastQuote(
            id = "start_11",
            text = "مستني الساعة تيجي سبعة بالظبط عشان تبدأ؟ أهي جت وبقت سبعة وربع! ⏰",
            spokenText = "مستني الساعة تيجي سبعة بالظبط؟ أهي جت وبقت سبعة وربع وأنت لسه مأنتخ!",
            category = QuoteCategory.START_SESSION,
            severityEmoji = "⌛",
            punchline = "تسويف بالدقيقة والثانية!"
        ),
        RoastQuote(
            id = "start_12",
            text = "قاعد تذاكر درس العربي ولا بتبص في السقف تحسب عدد اللمبات؟ 💡",
            spokenText = "قاعد تذاكر العربي ولا بتبص في السقف تعد اللمبات؟ ركز يا حجاج!",
            category = QuoteCategory.START_SESSION,
            severityEmoji = "🙄",
            punchline = "السقف مش هيجاوبلك في الامتحان!"
        ),
        RoastQuote(
            id = "start_13",
            text = "يلا يا حجاج سمّي الله واقفل باب الأوضة، النهاردة يوم البطولات! 🚪",
            spokenText = "يلا يا حجاج سمّي الله واقفل باب الأوضة، النهاردة يوم الجد والبطولات!",
            category = QuoteCategory.START_SESSION,
            severityEmoji = "🎯",
            punchline = "جلسة واحدة بتركيز هتفرق في مجموعك!"
        ),
        RoastQuote(
            id = "start_14",
            text = "لو فضلت كده هتدخل الامتحان تلاقي الأسئلة بتسلم عليك وأنت مش عارفها! 🤝",
            spokenText = "لو فضلت كده هتدخل الامتحان تلاقي الأسئلة بتسلم عليك وأنت مكسوف منها!",
            category = QuoteCategory.START_SESSION,
            severityEmoji = "😬",
            punchline = "الحق نفسك من دلوقتي!"
        ),
        RoastQuote(
            id = "start_15",
            text = "تانية إعدادي سنة تأسيس يا فالح.. مش سنة نوم وسهر للبلايستيشن! 🎮❌",
            spokenText = "تانية إعدادي سنة تأسيس يا فالح.. مش سنة نوم وسهر للبلايستيشن!",
            category = QuoteCategory.START_SESSION,
            severityEmoji = "📚",
            punchline = "تالتة إعدادي مستنياك على غلطة!"
        ),

        // ==========================================
        // 2. MID_SESSION (وسط الجلسة والتركيز ومنع السرحان) - 15 جملة
        // ==========================================
        RoastQuote(
            id = "mid_1",
            text = "عينك على الكتاب مش على إشعارات الواتساب يا حجاج! 📵",
            spokenText = "عينك على الكتاب مش على إشعارات الواتساب يا حجاج! اقفل النت وركز!",
            category = QuoteCategory.MID_SESSION,
            severityEmoji = "👀",
            punchline = "صاحبك مش هيشيل عنك درجات في الشهادة!"
        ),
        RoastQuote(
            id = "mid_2",
            text = "سيب القلم اللي بتلفه على صباعك ده وركز في المسألة! 🖊️",
            spokenText = "سيب القلم اللي بتلفه على صباعك ده وركز في مسألة الهندسة!",
            category = QuoteCategory.MID_SESSION,
            severityEmoji = "🌀",
            punchline = "البهلوانيات دي مش هتجيب درجات!"
        ),
        RoastQuote(
            id = "mid_3",
            text = "عدّى عشر دقايق بس وحاسس إنك في قاع المحيط؟ كمّل يا بطل! 🌊",
            spokenText = "عدّى عشر دقايق بس وحاسس إنك بتموت؟ كمّل يا حجاج إنت قدها!",
            category = QuoteCategory.MID_SESSION,
            severityEmoji = "💪",
            punchline = "عضلة التركيز محتاجة تمرين!"
        ),
        RoastQuote(
            id = "mid_4",
            text = "بتثاوب ليه؟ إنت لسه صاحي من ساعتين وشارب نسكافيه! 🥱",
            spokenText = "بتثاوب ليه يا حجاج؟ إنت لسه صاحي من ساعتين وشارب شاي!",
            category = QuoteCategory.MID_SESSION,
            severityEmoji = "☕",
            punchline = "الكسل شيطان بيوسوسلك تقفل الكتاب!"
        ),
        RoastQuote(
            id = "mid_5",
            text = "أوعى تفتح التليفزيون بحجة 'أنا بذاكر أحسن مع دوشة'! 📺❌",
            spokenText = "أوعى تفتح التليفزيون وتقول بذاكر أحسن مع الدوشة! دي كدبة قديمة!",
            category = QuoteCategory.MID_SESSION,
            severityEmoji = "🤫",
            punchline = "المخ بيسجل المسلسل ومش بيسجل المنهج!"
        ),
        RoastQuote(
            id = "mid_6",
            text = "شايفك وأنت بتمد إيدك للموبايل عشان تشوف الساعة كام.. بص في المؤقت هنا! ⏱️",
            spokenText = "شايفك وأنت بتمد إيدك للموبايل.. بص في مؤقت التطبيق وسيب الموبايل!",
            category = QuoteCategory.MID_SESSION,
            severityEmoji = "🤖",
            punchline = "حجة الساعة مكشوفة ومفقوسة!"
        ),
        RoastQuote(
            id = "mid_7",
            text = "المسألة صعبة؟ اقراها تلات مرات بالراحة وهتتحل يا عبقري! 📐",
            spokenText = "المسألة صعبة؟ اقراها تلات مرات بالراحة وهتتحل معاك يا حجاج!",
            category = QuoteCategory.MID_SESSION,
            severityEmoji = "🧠",
            punchline = "مفيش مسألة رياضيات ملهاش حل!"
        ),
        RoastQuote(
            id = "mid_8",
            text = "افتكر إن الدحيح بتاع الفصل مبيسبش الكتاب دلوقتي! 🥇",
            spokenText = "افتكر إن الدحيح بتاع فصلك مبيسبش الكتاب دلوقتي وقاعد يفرم المنهج!",
            category = QuoteCategory.MID_SESSION,
            severityEmoji = "🔥",
            punchline = "خلّيك أنت الأول المرة دي!"
        ),
        RoastQuote(
            id = "mid_9",
            text = "مش وقت تفكير في أحلام اليقظة وإنت شاري لامبورجيني! ركز في العلوم! 🏎️💭",
            spokenText = "مش وقت أحلام يقظة وأنت راكب عربية سباق! ركز في طبقات الغلاف الجوي!",
            category = QuoteCategory.MID_SESSION,
            severityEmoji = "🛸",
            punchline = "اللامبورجيني محتاجة مجموع الأول!"
        ),
        RoastQuote(
            id = "mid_10",
            text = "نص الوقت فات.. هانت يا حجاج! كمل الجلسة برجولة! ⏳",
            spokenText = "نص الوقت فات.. هانت يا حجاج! كمّل الجلسة برجولة وشطارة!",
            category = QuoteCategory.MID_SESSION,
            severityEmoji = "⏳",
            punchline = "فاضل تكة وتكسب البومودورو!"
        ),
        RoastQuote(
            id = "mid_11",
            text = "اقعد ضهرك مفرود.. القعدة المعوجة بتجيب نوم وخمول! 🪑",
            spokenText = "افرد ضهرك يا حجاج.. القعدة المنحنية بتجيب نوم وخمول!",
            category = QuoteCategory.MID_SESSION,
            severityEmoji = "📏",
            punchline = "طاقة المحارب بتبدأ من قعدة المكتب الصح!"
        ),
        RoastQuote(
            id = "mid_12",
            text = "بلاش تسرح في ماتش إمبارح.. الماتش خلص والامتحان لسه جاي! ⚽",
            spokenText = "بلاش تسرح في الماتش.. الماتش خلص والامتحان على الأبواب!",
            category = QuoteCategory.MID_SESSION,
            severityEmoji = "🥅",
            punchline = "ركز في جول المذاكرة دلوقتي!"
        ),
        RoastQuote(
            id = "mid_13",
            text = "الكلمات الإنجليزي دي لو حفظتها دلوقتي هتريحك طول الترم! 🇬🇧",
            spokenText = "الكلمات الإنجليزي دي لو حفظتها دلوقتي هتريحك طول الترم يا حجاج!",
            category = QuoteCategory.MID_SESSION,
            severityEmoji = "📖",
            punchline = "التكرار بيعلّم الشطار!"
        ),
        RoastQuote(
            id = "mid_14",
            text = "حاسس بملل؟ خُد نَفَس عميق ورش على وشك شوية مية ورجع كمل! 💦",
            spokenText = "حاسس بملل؟ خُد نَفَس عميق واشرب بؤ مية وكمل يا وحش!",
            category = QuoteCategory.MID_SESSION,
            severityEmoji = "💧",
            punchline = "الابطال بيكملوا لما الباقي يستسلم!"
        ),
        RoastQuote(
            id = "mid_15",
            text = "المؤقت بيعد تنازلي.. أثبت لنفسك إنك مش فاشل وقادر تركز! ⏱️🔥",
            spokenText = "المؤقت بيعد تنازلي.. أثبت لنفسك إنك راجل وقدها وقادر تركز!",
            category = QuoteCategory.MID_SESSION,
            severityEmoji = "🛡️",
            punchline = "التركيز قوة وتقدر عليها!"
        ),

        // ==========================================
        // 3. POST_SUCCESS (بعد الإنجاز - مدح حقيقي وتشجيع) - 15 جملة
        // ==========================================
        RoastQuote(
            id = "success_1",
            text = "عاش يا حجاج يا وحش! طلعت دحيح ومش فاشل أهو! 🏆✨",
            spokenText = "عاش يا حجاج يا وحش! طلعت دحيح وبطل ومش فاشل خالص!",
            category = QuoteCategory.POST_SUCCESS,
            severityEmoji = "🌟",
            punchline = "شغل عالي وفخر لكل العيلة!"
        ),
        RoastQuote(
            id = "success_2",
            text = "الله عليك يا فخر تانية إعدادي! الجلسة دي بمية جلسة! 🎓",
            spokenText = "الله عليك يا فخر تانية إعدادي! الجلسة دي بمية جلسة، تسلم إيدك يا بطل!",
            category = QuoteCategory.POST_SUCCESS,
            severityEmoji = "👑",
            punchline = "كده نرفع راسنا بيك في المدرسة!"
        ),
        RoastQuote(
            id = "success_3",
            text = "إيه الشطارة والروقان ده كله؟ كده المجموع في جيبك يا حجاج! 💯",
            spokenText = "إيه الشطارة دي كلها يا حجاج؟ كده المجموع والتفوق في جيبك بإذن الله!",
            category = QuoteCategory.POST_SUCCESS,
            severityEmoji = "💯",
            punchline = "الاستمرار ده هيخليك الأول على المدرسة!"
        ),
        RoastQuote(
            id = "success_4",
            text = "خلصت البومودورو بجد؟ قوم افرد ضهرك واشرب عصير.. تستاهل! 🧃",
            spokenText = "خلصت الجلسة بجد؟ قوم افرد ضهرك واشرب حاجة حلوة.. تستاهل يا برنس!",
            category = QuoteCategory.POST_SUCCESS,
            severityEmoji = "🍹",
            punchline = "استراحة خمس دقايق حلال بلال عليك!"
        ),
        RoastQuote(
            id = "success_5",
            text = "شفت بقى إن الموضوع سهل وإنت اللي كنت مكسل؟ برافو يا بطل! 👏",
            spokenText = "شفت بقى إن المذاكرة سهلة وإنت اللي كنت مكسل؟ برافو عليك يا حجاج!",
            category = QuoteCategory.POST_SUCCESS,
            severityEmoji = "✨",
            punchline = "ثقتك في نفسك رجعت لمكانها الصح!"
        ),
        RoastQuote(
            id = "success_6",
            text = "وحش الرياضيات والعلوم قادم بقوة! أهلك هيفرحوا بيك جداً! 👨‍👩‍👦",
            spokenText = "وحش تانية إعدادي قادم بقوة! أهلك هيفرحوا بيك ويفتخروا بنتيجتك!",
            category = QuoteCategory.POST_SUCCESS,
            severityEmoji = "❤️",
            punchline = "فرحة النجاح مفيش أحلى منها!"
        ),
        RoastQuote(
            id = "success_7",
            text = "المستر لو شاف تركيزك ده هيديك الدرجة النهائية من غير ما يمتحنك! 📝",
            spokenText = "المستر لو شاف شطارتك دي هيديك الدرجة النهائية من غير امتحان!",
            category = QuoteCategory.POST_SUCCESS,
            severityEmoji = "🎖️",
            punchline = "إنجاز حقيقي تستحق عليه التحية!"
        ),
        RoastQuote(
            id = "success_8",
            text = "كسرت حاجز الكسل والملل.. أنت النهاردة في حتة تانية خالص! 🚀",
            spokenText = "كسرت حاجز الكسل والملل.. أنت النهاردة في حتة تانية خالص يا حجاج!",
            category = QuoteCategory.POST_SUCCESS,
            severityEmoji = "🚀",
            punchline = "انطلاق نحو القمة!"
        ),
        RoastQuote(
            id = "success_9",
            text = "الدروس المتراكمة بدأت تنهار قدام عزيمتك! عاش يا مقاتل! ⚔️",
            spokenText = "الدروس المتراكمة بدأت تنهار قدام شطارتك! عاش يا مقاتل تانية إعدادي!",
            category = QuoteCategory.POST_SUCCESS,
            severityEmoji = "🛡️",
            punchline = "قاهر الملازم المتراكمة!"
        ),
        RoastQuote(
            id = "success_10",
            text = "سجلت نقطة جديدة في رصيد الدحيحة.. كمل على نفس المعدل! 📊",
            spokenText = "سجلت نقطة جديدة في رصيد الدحيحة.. كمل يا حجاج على نفس المستوى العالي!",
            category = QuoteCategory.POST_SUCCESS,
            severityEmoji = "📈",
            punchline = "مؤشر الدحيح بيولّع أخضر!"
        ),
        RoastQuote(
            id = "success_11",
            text = "دماغك ألماظ لما بتشتغل.. فخورين بيك يا حجاج من القلب! 💎",
            spokenText = "دماغك ألماظ لما بتشتغل.. فخورين بيك يا حجاج من القلب وبجد!",
            category = QuoteCategory.POST_SUCCESS,
            severityEmoji = "💎",
            punchline = "ذكائك لما بيتوظف صح محدش بيعرف يوقفك!"
        ),
        RoastQuote(
            id = "success_12",
            text = "الخطوة دي قربتك خطوة عملاقة من المركز الأول على الدفعة! 🥇",
            spokenText = "الخطوة دي قربتك خطوة عملاقة من المركز الأول على الدفعة يا شاطر!",
            category = QuoteCategory.POST_SUCCESS,
            severityEmoji = "🥇",
            punchline = "النجاح تراكم خطوات صغيرة!"
        ),
        RoastQuote(
            id = "success_13",
            text = "كده ضميرك مرتاح وتنام وأنت حاسس برجولة الإنجاز! 😴✨",
            spokenText = "كده ضميرك مرتاح وتنام وأنت راضي عن نفسك وعن يومك يا حجاج!",
            category = QuoteCategory.POST_SUCCESS,
            severityEmoji = "🌙",
            punchline = "نومة المذاكر أحلى نومة في الدنيا!"
        ),
        RoastQuote(
            id = "success_14",
            text = "يا واد يا عبقري.. مين ده اللي كان بيقول عليك مش بتذاكر؟! 🌟",
            spokenText = "يا واد يا عبقري.. مين ده اللي كان بيقول عليك مش بتذاكر؟ ده إنت أستاذ!",
            category = QuoteCategory.POST_SUCCESS,
            severityEmoji = "🎩",
            punchline = "أثبت لكل الناس إنك تقدر!"
        ),
        RoastQuote(
            id = "success_15",
            text = "عاش يا حجاج.. الجلسة الجاية هتكون أسهل وأقوى كمان! 💪",
            spokenText = "عاش يا حجاج.. الجلسة الجاية هتكون أسهل وأقوى كمان، استمر يا بطل!",
            category = QuoteCategory.POST_SUCCESS,
            severityEmoji = "🔥",
            punchline = "الاستمرارية سر كل الأوائل!"
        ),

        // ==========================================
        // 4. ON_FAILURE (بعد الفشل والهروب من المذاكرة) - 10 جمل
        // ==========================================
        RoastQuote(
            id = "fail_1",
            text = "قفلت المذاكرة وهربت؟ تانية إعدادي مش هتستناك يا حجاج! 🏃‍♂️❌",
            spokenText = "قفلت المذاكرة وهربت؟ تانية إعدادي مش هتستناك يا حجاج! ارجع للكتاب!",
            category = QuoteCategory.ON_FAILURE,
            severityEmoji = "🚨",
            punchline = "الهروب مش هيمسح أسئلة الامتحان!"
        ),
        RoastQuote(
            id = "fail_2",
            text = "رايح فين وسامع صوت التيك توك بيناديك؟ ارجع اقعد على حيلك! 📱",
            spokenText = "رايح فين وسامع صوت التيك توك؟ ارجع اقعد على حيلك وذاكر!",
            category = QuoteCategory.ON_FAILURE,
            severityEmoji = "⚠️",
            punchline = "التيك توك مش هيديك درجات في الشهادة!"
        ),
        RoastQuote(
            id = "fail_3",
            text = "استسلمت بعد خمس دقايق بس؟! ده فنجان القهوة لسه سخن! ☕🤦‍♂️",
            spokenText = "استسلمت بعد خمس دقايق بس يا فاشل؟! ده فنجان الشاي لسه سخن!",
            category = QuoteCategory.ON_FAILURE,
            severityEmoji = "🤦‍♂️",
            punchline = "فين العزيمة والرجولة اللي كنت بتتكلم عنها؟"
        ),
        RoastQuote(
            id = "fail_4",
            text = "بتهرب من المذاكرة ليه؟ هو الكتاب بيعض يا حجاج؟! 📖👀",
            spokenText = "بتهرب من المذاكرة ليه؟ هو الكتاب بيعض يا حجاج؟! قوم افتحه!",
            category = QuoteCategory.ON_FAILURE,
            severityEmoji = "😂",
            punchline = "الكتاب غلبان وعايز مصلحتك!"
        ),
        RoastQuote(
            id = "fail_5",
            text = "بتقول 'هبدأ بكرة'؟ بكرة ده هييجي يوم الامتحان الصبح وتعيّط! 😭",
            spokenText = "بتقول هبدأ بكرة؟ بكرة ده هييجي يوم الامتحان وتعيّط وتقول ياريتني!",
            category = QuoteCategory.ON_FAILURE,
            severityEmoji = "😢",
            punchline = "مفيش وقت للتأجيل، الوقت بيجري!"
        ),
        RoastQuote(
            id = "fail_6",
            text = "ألو؟ شرطة مكافحة الكسل.. امسكوا حجاج سايب الملازم وطالع للصالة! 🚔",
            spokenText = "ألو؟ شرطة الكسل.. امسكوا حجاج سايب الملازم وطالع للصالة ياكل!",
            category = QuoteCategory.ON_FAILURE,
            severityEmoji = "🚨",
            punchline = "التلاجة مش هتغير حاجة في المنهج!"
        ),
        RoastQuote(
            id = "fail_7",
            text = "كل ما تفتح كتاب تفتكر إنك جعان وعطشان وعايز تنام؟ حركات قديمة! 🥪",
            spokenText = "كل ما تفتح كتاب تفتكر إنك جعان وعطشان؟ الحركات دي مفقوسة ومكشوفة يا حجاج!",
            category = QuoteCategory.ON_FAILURE,
            severityEmoji = "🍕",
            punchline = "المخ بيهرب للحيل السهلة.. اغلبه!"
        ),
        RoastQuote(
            id = "fail_8",
            text = "دروس العلوم والعربي بتعيط في الزاوية.. متكسرش خاطرها! 📚",
            spokenText = "دروس العلوم والعربي بتعيط في الزاوية.. متكسرش خاطرها يا حجاج وارجع كمل!",
            category = QuoteCategory.ON_FAILURE,
            severityEmoji = "🥺",
            punchline = "ذاكر صفحتين بس وهتلاقي الموضوع اتحل!"
        ),
        RoastQuote(
            id = "fail_9",
            text = "وقفت الجلسة ليه؟ الكسل رجع ركبك تاني ولا إيه؟! 💤",
            spokenText = "وقفت الجلسة ليه؟ الكسل رجع ركبك تاني؟ فوق يا حجاج واستعذ بالله!",
            category = QuoteCategory.ON_FAILURE,
            severityEmoji = "⚡",
            punchline = "شوية مية باردة على وشك ورجع وحش!"
        ),
        RoastQuote(
            id = "fail_10",
            text = "مش عيب تقع.. العيب إنك تفضل مأنتخ وتقول مش قادر! قوم يا حجاج! ✊",
            spokenText = "مش عيب تقع.. العيب إنك تفضل مأنتخ وتقول مش قادر! قوم يا حجاج وأثبت نفسك!",
            category = QuoteCategory.ON_FAILURE,
            severityEmoji = "🔥",
            punchline = "ابدأ جلسة جديدة دلوقتي واغسل خيبة اللي فات!"
        ),

        // ==========================================
        // 5. STREAK_BROKEN (قطع الأيام المتتالية والتريقة عليها) - 8 جمل
        // ==========================================
        RoastQuote(
            id = "streak_1",
            text = "قطعت السلسلة يا حجاج؟! رجعت لنقطة الصفر زي ما رجعت لدروس تانية إعدادي! 📉💔",
            spokenText = "قطعت السلسلة يا حجاج؟! رجعت لنقطة الصفر زي ما رجعت لدروس تانية إعدادي!",
            category = QuoteCategory.STREAK_BROKEN,
            severityEmoji = "💔",
            punchline = "العداد رجع صفر والكسل كسب الجولة!"
        ),
        RoastQuote(
            id = "streak_2",
            text = "يوم كامل ملمستش فيه كتاب؟! السلسلة ماتت وشبعت موت يا فاشل! 🪦",
            spokenText = "يوم كامل ملمستش فيه كتاب؟ السلسلة ماتت وشبعت موت يا حجاج.. شد حيلك!",
            category = QuoteCategory.STREAK_BROKEN,
            severityEmoji = "😭",
            punchline = "ابنِ سلسلة جديدة من النهاردة ومتكررهاش!"
        ),
        RoastQuote(
            id = "streak_3",
            text = "كنت ماشي كويس.. إيه اللي حصل؟ الشاي خلص ولا النت رجع؟! ☕📶",
            spokenText = "كنت ماشي كويس.. إيه اللي حصل يا حجاج؟ الشاي خلص ولا النت اشتغل؟! السلسلة طارت!",
            category = QuoteCategory.STREAK_BROKEN,
            severityEmoji = "🤷‍♂️",
            punchline = "الاستمرار أصعب من البداية بس هو اللي بيعمل الدحيح!"
        ),
        RoastQuote(
            id = "streak_4",
            text = "مبروك! حصلت على لقب 'ملك السرحان والانقطاع' بعد كسر السلسلة! 👑😴",
            spokenText = "مبروك يا حجاج! حصلت على لقب ملك السرحان والانقطاع بعد ما قطعت السلسلة!",
            category = QuoteCategory.STREAK_BROKEN,
            severityEmoji = "😴",
            punchline = "اللقب ده تريقة مش فخر.. قوم ذاكر!"
        ),
        RoastQuote(
            id = "streak_5",
            text = "السلسلة اتقطعت عشان نمت وسبت المذاكرة؟ والله تانية إعدادي زعلانة منك! 🥺",
            spokenText = "السلسلة اتقطعت عشان نمت وسبت المذاكرة؟ والله تانية إعدادي زعلانة منك يا حجاج!",
            category = QuoteCategory.STREAK_BROKEN,
            severityEmoji = "📉",
            punchline = "صالحها بجلسة بومودورو 25 دقيقة دلوقتي!"
        ),
        RoastQuote(
            id = "streak_6",
            text = "رجعنا للصفر تاني يا أبو علي.. كأنك معملتش أي حاجة الأسبوع اللي فات! 🔄",
            spokenText = "رجعنا للصفر تاني يا أبو علي.. كأنك معملتش حاجة! شد حيلك وابدأ من جديد!",
            category = QuoteCategory.STREAK_BROKEN,
            severityEmoji = "🔄",
            punchline = "المهم تبدأ النهاردة ومتستسلمش!"
        ),
        RoastQuote(
            id = "streak_7",
            text = "أصحابك في المدرسة سلسلتهم شغالة وأنت سلسلتك بتستغيث! 🆘",
            spokenText = "أصحابك في المدرسة مكملين مذاكرة، وأنت سلسلتك بتستغيث يا حجاج!",
            category = QuoteCategory.STREAK_BROKEN,
            severityEmoji = "🆘",
            punchline = "انقذ ما يمكن انقاذه والتحق بالدحيحة!"
        ),
        RoastQuote(
            id = "streak_8",
            text = "العداد رجع صفر بس إنت لسه عندك فرصة تكون بطل.. ابدأ جلسة فوراً! ⚡",
            spokenText = "العداد رجع صفر بس إنت لسه عندك فرصة تكون بطل.. ابدأ جلسة فوراً يا حجاج!",
            category = QuoteCategory.STREAK_BROKEN,
            severityEmoji = "💪",
            punchline = "البطل الحقيقي هو اللي بيقوم بعد ما يقع!"
        )
    )

    fun getRandomQuote(category: QuoteCategory? = null): RoastQuote {
        val filtered = if (category != null) quotes.filter { it.category == category } else quotes
        return if (filtered.isNotEmpty()) filtered.random() else quotes.random()
    }
}
