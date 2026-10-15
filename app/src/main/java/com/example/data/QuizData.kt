package com.example.data

data class QuizQuestion(
    val id: String,
    val subject: String,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val correctEgyptianComment: String,
    val wrongEgyptianComment: String
)

object QuizRepository {

    val questions: List<QuizQuestion> = listOf(
        // ==========================================
        // 1. رياضيات تانية إعدادي (10 أسئلة)
        // ==========================================
        QuizQuestion(
            id = "math_1",
            subject = "رياضيات",
            question = "الجذر التكعيبي للعدد ( - 27 ) يساوي:",
            options = listOf("3", "-3", "9", "-9"),
            correctIndex = 1,
            explanation = "الجذر التكعيبي يحافظ على الإشارة السالبة: (-3) × (-3) × (-3) = -27.",
            correctEgyptianComment = "الله عليك يا حجاج يا باشمهندس! إجابة مية مية 📐✨",
            wrongEgyptianComment = "إيه العك ده يا حجاج؟! نسيت السالب وإنت صغير ولا إيه؟! 😂"
        ),
        QuizQuestion(
            id = "math_2",
            subject = "رياضيات",
            question = "نقطة تقاطع متوسطات المثلث تقسم كلاً منها بنسبة ...... من جهة القاعدة:",
            options = listOf("1 : 2", "2 : 1", "1 : 3", "3 : 1"),
            correctIndex = 0,
            explanation = "تقسم بنسبة 1 : 2 من جهة القاعدة (أو 2 : 1 من جهة الرأس).",
            correctEgyptianComment = "وحش الهندسة المتوج! دي قاعدة ذهبية في تانية إعدادي 🥇",
            wrongEgyptianComment = "اتلخبطت بين القاعدة والرأس يا فالح؟! ركز في السؤال! 🤦‍♂️"
        ),
        QuizQuestion(
            id = "math_3",
            subject = "رياضيات",
            question = "مجموعة حل المعادلة س² + 25 = 0 في ح (مجموعة الأعداد الحقيقية) هي:",
            options = listOf("{5}", "{-5}", "{5, -5}", "فاي (∅)"),
            correctIndex = 3,
            explanation = "س² = -25 ولا يوجد جذر تربيعي لعدد سالب في مجموعة الأعداد الحقيقية ح.",
            correctEgyptianComment = "يا واد يا دحيح! المقلب مأكلش معاك عشان مفيش جذر لسالب في ح! 🧠",
            wrongEgyptianComment = "لبست المقلب يا حجاج! مفيش جذر لسالب في الأعداد الحقيقية! 🕳️"
        ),
        QuizQuestion(
            id = "math_4",
            subject = "رياضيات",
            question = "إذا كان طولا ضلعين في مثلث 3 سم و 7 سم، فإن طول الضلع الثالث يمكن أن يكون:",
            options = listOf("3 سم", "4 سم", "7 سم", "10 سم"),
            correctIndex = 2,
            explanation = "متباينة المثلث: طول الضلع الثالث ينتمي للفترة (7-3، 7+3) = (4، 10)، والـ 7 تنتمي إليها.",
            correctEgyptianComment = "معلم ومسيطر على متباينة المثلث يا حجاج! 📏",
            wrongEgyptianComment = "متباينة المثلث زعلانة منك في الزاوية.. راجع مجموع الضلعين! 📐"
        ),
        QuizQuestion(
            id = "math_5",
            subject = "رياضيات",
            question = "تحليل المقدار: س² - 49 هو:",
            options = listOf("(س - 7)(س - 7)", "(س - 7)(س + 7)", "(س + 7)(س + 7)", "س(س - 49)"),
            correctIndex = 1,
            explanation = "فرق بين مربعين: جذر الأول ناقص جذر الثاني في جذر الأول زائد جذر الثاني.",
            correctEgyptianComment = "تسلم إيدك يا فنان! فرق بين مربعين أسهل درس في الجبر! 👏",
            wrongEgyptianComment = "فرق بين مربعين يا راجل وضيعته؟! المستر هيعيّط منك! 😭"
        ),
        QuizQuestion(
            id = "math_6",
            subject = "رياضيات",
            question = "المثلث الذي قياسا زاويتين فيه 50° و 80° يكون مثلثاً:",
            options = listOf("مختلف الأضلاع", "متساوي الساقين", "متساوي الأضلاع", "قائم الزاوية"),
            correctIndex = 1,
            explanation = "الزاوية الثالثة = 180 - (50 + 80) = 50°، وبما أن زاويتان متساويتان فهو متساوي الساقين.",
            correctEgyptianComment = "شاطر جداً! حسبت الزاوية التالتة ولقيتها 50° زيهم! 🎯",
            wrongEgyptianComment = "احسب الزاوية التالتة يا حجاج قبل ما تهبد! 🧮"
        ),
        QuizQuestion(
            id = "math_7",
            subject = "رياضيات",
            question = "الفترة [2 ، 5] فرق المجموعة {2}:",
            options = listOf("]2 ، 5]", "[2 ، 5[", "]2 ، 5[", "[3 ، 5]"),
            correctIndex = 0,
            explanation = "حذف العنصر 2 يفتح القوس عند الـ 2 لتصبح ]2 ، 5].",
            correctEgyptianComment = "برافو يا حجاج! فتحت القوس عند الـ 2 زي الكتاب ما قال! 🔓",
            wrongEgyptianComment = "الأقواس المفتوحة والمقفولة لعبت بيك يا بطل! 😅"
        ),
        QuizQuestion(
            id = "math_8",
            subject = "رياضيات",
            question = "مكعب حجمه 64 سم³، فإن طول حرفه يساوي:",
            options = listOf("4 سم", "8 سم", "16 سم", "32 سم"),
            correctIndex = 0,
            explanation = "طول الحرف = الجذر التكعيبي للحجم = الجذر التكعيبي لـ 64 = 4 سم.",
            correctEgyptianComment = "الله ينور عليك! 4 × 4 × 4 = 64 بالتمام والكمال! 🧊",
            wrongEgyptianComment = "أنت قَسَمت على الاتنين ولا إيه؟! ده مكعب يا حجاج! 🤦‍♂️"
        ),
        QuizQuestion(
            id = "math_9",
            subject = "رياضيات",
            question = "في المثلث القائم الزاوية، طول الضلع المقابل للزاوية التي قياسها 30° يساوي:",
            options = listOf("طول الوتر", "نصف طول الوتر", "ضعف طول الوتر", "ثلث طول الوتر"),
            correctIndex = 1,
            explanation = "نتيجة مشهورة: طول الضلع المقابل للزاوية 30 في المثلث القائم = نصف طول الوتر.",
            correctEgyptianComment = "نتيجة الثلاثيني ستيني في جيبك يا باشا! عاش! 📐",
            wrongEgyptianComment = "المقابل للزاوية 30 نص الوتر علطول.. احفظها يا فالح! ⚠️"
        ),
        QuizQuestion(
            id = "math_10",
            subject = "رياضيات",
            question = "العدد غير النسبي المحصور بين 2 و 3 هو:",
            options = listOf("جذر 3", "جذر 7", "جذر 10", "2.5"),
            correctIndex = 1,
            explanation = "2 = جذر 4 و 3 = جذر 9، إذاً جذر 7 محصور بينهما وغير نسبي.",
            correctEgyptianComment = "عيني عليك باردة! ربعت الأطراف وقفشت جذر 7! 🚀",
            wrongEgyptianComment = "2.5 ده عدد نسبي وجذر 3 أقل من 2.. ركز يا حجاج! 🧐"
        ),

        // ==========================================
        // 2. علوم تانية إعدادي (10 أسئلة)
        // ==========================================
        QuizQuestion(
            id = "science_1",
            subject = "علوم",
            question = "رتب العالم مندليف العناصر في جدوله تصاعدياً حسب:",
            options = listOf("أعدادها الذرية", "أوزانها الذرية", "كثافتها", "سالبّيتها"),
            correctIndex = 1,
            explanation = "مندليف رتب حسب الوزن الذري، بينما موزلي رتب حسب العدد الذري.",
            correctEgyptianComment = "صح الصح! مندليف وزن ذري، وموزلي عدد ذري! 🧪✨",
            wrongEgyptianComment = "اتلخبطت بين مندليف وموزلي يا حجاج! دول أساس المنهج! 🔬"
        ),
        QuizQuestion(
            id = "science_2",
            subject = "علوم",
            question = "الجدول الدوري الحديث يتكون من ...... دورات أفقية:",
            options = listOf("5 دورات", "6 دورات", "7 دورات", "8 دورات"),
            correctIndex = 2,
            explanation = "يتكون من 7 دورات أفقية و 18 مجموعة رأسية.",
            correctEgyptianComment = "يا دحيح العلوم! 7 دورات زي أيام الأسبوع بالظبط! 📅",
            wrongEgyptianComment = "7 دورات يا حجاج.. متزودش من عندك دورات! 😂"
        ),
        QuizQuestion(
            id = "science_3",
            subject = "علوم",
            question = "توجد طبقة الأوزون في إحدى طبقات الغلاف الجوي وهي:",
            options = listOf("التروبوسفير", "الستراتوسفير", "الميزوسفير", "الثيرموسفير"),
            correctIndex = 1,
            explanation = "توجد في الستراتوسفير على ارتفاع من 20 إلى 40 كم فوق سطح البحر.",
            correctEgyptianComment = "تمام التمام! الستراتوسفير غلاف الأوزون الحامي للأرض! 🛡️",
            wrongEgyptianComment = "التروبوسفير دي اللي فيها تقلبات الجو والأمطار! فوق شوية يا حجاج! ☁️"
        ),
        QuizQuestion(
            id = "science_4",
            subject = "علوم",
            question = "تصل كثافة الماء لأعلى قيمة لها عند درجة حرارة:",
            options = listOf("صفر مئوي", "4 درجات مئوية", "100 درجة مئوية", "-4 درجات مئوية"),
            correctIndex = 1,
            explanation = "شذوذ الماء: أقصى كثافة للماء النقي عند 4 درجات مئوية.",
            correctEgyptianComment = "شاطر جداً! خاصية شذوذ الماء دي اللي بتحافظ على الكائنات المائية! 🐟",
            wrongEgyptianComment = "عند الصفر بيتجمد وحجمه بيزيد وكثافته بتقل! الإجابة 4 مئوية! 🧊"
        ),
        QuizQuestion(
            id = "science_5",
            subject = "علوم",
            question = "من الغازات الدفيئة المسؤولة عن ظاهرة الاحتباس الحراري:",
            options = listOf("الأكسجين", "النيتروجين", "غاز الميثان", "الهيليوم"),
            correctIndex = 2,
            explanation = "الميثان، وثاني أكسيد الكربون، وأكسيد النيتروز، وبخار الماء من الغازات الدفيئة.",
            correctEgyptianComment = "الله ينور عليك! الميثان وثاني أكسيد الكربون حابسين الحرارة! 🔥",
            wrongEgyptianComment = "الأكسجين والنيتروجين دي غازات الجو العادية مش دفيئة! 💨"
        ),
        QuizQuestion(
            id = "science_6",
            subject = "علوم",
            question = "حفرية كائن كامل مدفون في مادة صمغية تحافظ عليه هي حفرية:",
            options = listOf("الماموث في الجليد", "الكهرمان", "الأمونيت", "الترايلوبيت"),
            correctIndex = 1,
            explanation = "الكهرمان مادة صمغية أفرزتها أشجار صنوبرية قديمة حفظت الحشرات بداخلها كحفرية كائن كامل.",
            correctEgyptianComment = "عاش يا عالم الآثار! الكهرمان الصمغي حفظ الحشرات بالمللي! 🐜",
            wrongEgyptianComment = "الماموث كان في الجليد.. والصمغ يعني كهرمان يا بطل! 🌲"
        ),
        QuizQuestion(
            id = "science_7",
            subject = "علوم",
            question = "أبرد طبقات الغلاف الجوي وتصل حرارتها إلى (-90°م) هي:",
            options = listOf("التروبوسفير", "الستراتوسفير", "الميزوسفير", "الثيرموسفير"),
            correctIndex = 2,
            explanation = "الميزوسفير هي الطبقة المتوسطة وأبرد الطبقات حيث تحترق فيها الشهب.",
            correctEgyptianComment = "صح! الميزوسفير فريزر الغلاف الجوي وبتحرق الشهب! 🌠",
            wrongEgyptianComment = "الثيرموسفير أسخن طبقة (1200°م).. الأبرد هي الميزوسفير! 🥶"
        ),
        QuizQuestion(
            id = "science_8",
            subject = "علوم",
            question = "الصوت عبارة عن موجات:",
            options = listOf("كهرومغناطيسية مستعرضة", "ميكانيكية طولية", "كهرومغناطيسية طولية", "ميكانيكية مستعرضة"),
            correctIndex = 1,
            explanation = "الصوت موجات ميكانيكية (تحتاج لوسط مادي) وطولية (تتكون من تضاغطات وتخلخلات).",
            correctEgyptianComment = "ميكانيكية طولية يا معلم! صوتك واصل ومفهوم! 📢",
            wrongEgyptianComment = "الصوت مش كهرومغناطيسي، عشان كده مبينتقلش في الفراغ! 🌌"
        ),
        QuizQuestion(
            id = "science_9",
            subject = "علوم",
            question = "إذا كان تردد جسم مهتز 50 هرتز، فإن زمنه الدوري يساوي:",
            options = listOf("0.02 ثانية", "50 ثانية", "2 ثانية", "0.5 ثانية"),
            correctIndex = 0,
            explanation = "الزمن الدوري = 1 ÷ التردد = 1 ÷ 50 = 0.02 ثانية.",
            correctEgyptianComment = "وحش الفيزياء! مقلوب التردد على طول = 1/50 = 0.02! ⚡",
            wrongEgyptianComment = "الزمن الدوري مقلوب التردد مش بتضربه يا حجاج! 🤦‍♂️"
        ),
        QuizQuestion(
            id = "science_10",
            subject = "علوم",
            question = "العنصر الأكبر في الحجم الذري في الجدول الدوري يقع في المجموعة 1A وهو:",
            options = listOf("الفلور", "السيزيوم", "الصوديوم", "الليثيوم"),
            correctIndex = 1,
            explanation = "السيزيوم أنشط الفلزات وأكبرها حجماً ذرياً، بينما الفلور أصغرها حجماً وأعلاها سالبية.",
            correctEgyptianComment = "يا واد يا عملاق! السيزيوم ملك الحجم الذري في الجدول! 👑",
            wrongEgyptianComment = "الفلور هو الأصغر حجماً.. السيزيوم هو الأكبر يا حجاج! 📏"
        ),

        // ==========================================
        // 3. لغة عربية تانية إعدادي (10 أسئلة)
        // ==========================================
        QuizQuestion(
            id = "arabic_1",
            subject = "لغة عربية",
            question = "كل الأسماء التالية مبنية ما عدا:",
            options = listOf("هؤلاء", "الذي", "اللذان", "أنت"),
            correctIndex = 2,
            explanation = "الأسماء الموصولة مبنية ما عدا اللذان واللتان فهما معربتان إعراب المثنى.",
            correctEgyptianComment = "الله يفتح عليك! اللذان واللتان وهذان وهاتان معربان إعراب المثنى! 📖",
            wrongEgyptianComment = "نسيت استثناء المثنى يا حجاج؟! اللذان بتترفع بالألف! ⚠️"
        ),
        QuizQuestion(
            id = "arabic_2",
            subject = "لغة عربية",
            question = "يبنى الفعل المضارع على السكون إذا اتصلت به:",
            options = listOf("واو الجماعة", "ياء المخاطبة", "نون النسوة", "نون التوكيد"),
            correctIndex = 2,
            explanation = "نون النسوة تسكّن الفعل (يبنى على السكون)، ونون التوكيد تفتحه (يبنى على الفتح).",
            correctEgyptianComment = "قاعدة نحوية صلبة! نون النسوة تسكّن ونون التوكيد تفتح! ✍️",
            wrongEgyptianComment = "نون التوكيد بتبنيه على الفتح.. نون النسوة على السكون يا حجاج! 🤦‍♂️"
        ),
        QuizQuestion(
            id = "arabic_3",
            subject = "لغة عربية",
            question = "في جملة 'استمعت إلى خطيبٍ كلامُه مؤثّرٌ'، جملة (كلامه مؤثر) في محل:",
            options = listOf("نصب حال", "جر نعت", "رفع خبر", "جر مضاف إليه"),
            correctIndex = 1,
            explanation = "القاعدة الشهيرة: الجمل بعد النكرات صفات (نعت)، وخطيب مجرور إذاً في محل جر نعت.",
            correctEgyptianComment = "الجمل بعد النكرات صفات وبعد المعارف أحوال! برنس النحو! 🌟",
            wrongEgyptianComment = "خطيب نكرة يا حجاج.. يبقى الجملة نعت مش حال! ركز! 📝"
        ),
        QuizQuestion(
            id = "arabic_4",
            subject = "لغة عربية",
            question = "كرمت المدرسة الطالبين ...... (اختر التوكيد المعنوي المناسب):",
            options = listOf("كلاهما", "كليهما", "كلتاهما", "كلتيهما"),
            correctIndex = 1,
            explanation = "الطالبين مفعول به منصوب بالياء ومذكر، إذاً يتبعه 'كليهما' منصوب بالياء.",
            correctEgyptianComment = "صح الصح! توكيد منصوب بالياء للمذكر = كليهما! 🎯",
            wrongEgyptianComment = "الطالبين منصوبة بالياء.. كلاهما دي مرفوعة بالألف يا فالح! 😅"
        ),
        QuizQuestion(
            id = "arabic_5",
            subject = "لغة عربية",
            question = "حرف العطف الذي يفيد الإضراب (العدول عن الحكم الأول وإثباته للثاني) هو:",
            options = listOf("ثم", "أو", "بل", "لا"),
            correctIndex = 2,
            explanation = "'بل' تفيد الإضراب إذا سبقت بكلام مثبت (مثل: صاحب ذكياً بل عبقرياً).",
            correctEgyptianComment = "بل تفيد الإضراب يا أستاذ البلاغة والنحو! ممتاز! 👏",
            wrongEgyptianComment = "ثم للترتيب والتراخي، وأو للشك والتخيير.. بل هي بتاعة الإضراب! 🛑"
        ),
        QuizQuestion(
            id = "arabic_6",
            subject = "لغة عربية",
            question = "في موضوع 'كبرياء طفل'، رفض التلميذ مصطفى كامل أن يذكر أمام الخديوي كلمة:",
            options = listOf("سيدي", "عبدك", "مولاي", "حضرتك"),
            correctIndex = 1,
            explanation = "رفض أن يقول كلمة 'عبدك' وقال: لم أكن عبداً ولا كان أبي عبداً.",
            correctEgyptianComment = "عاش مصطفى كامل وعاش حجاج الدحيح! لم أكن عبداً ولا كان أبي عبداً! 🇪🇬",
            wrongEgyptianComment = "درس كبرياء طفل مشهور يا حجاج! كلمة 'عبدك' اللي رفضها بعزة نفس! 🦁"
        ),
        QuizQuestion(
            id = "arabic_7",
            subject = "لغة عربية",
            question = "عالم مصري نال قلادة العلوم الوطنية الأمريكية لأبحاثه في علاج السرطان بجزيئات الذهب النانوية:",
            options = listOf("أحمد زويل", "مجدي يعقوب", "مصطفى السيد", "فاروق الباز"),
            correctIndex = 2,
            explanation = "الدكتور مصطفى السيد بطل درس القراءة 'عالم من ذهب'.",
            correctEgyptianComment = "الدكتور مصطفى السيد وعالم من ذهب! فخر مصر وفخرك بالمعلومة! 🏅",
            wrongEgyptianComment = "زويل نوبل في الفيمتو ثانية.. ده الدكتور مصطفى السيد يا حجاج! 🔬"
        ),
        QuizQuestion(
            id = "arabic_8",
            subject = "لغة عربية",
            question = "في نص 'نصائح غالية'، قوله تعالى: (إنّ أنكرَ الأصواتِ لصوتُ الحمير) تصوير يفيد:",
            options = listOf("المدح والثناء", "التحقير والتنفير من رفع الصوت", "الدعاء والرجاء", "التعجب والاستحسان"),
            correctIndex = 1,
            explanation = "تشبيه من يرفع صوته بصوت الحمار للتنفير والتحذير من علو الصوت بغير فائدة.",
            correctEgyptianComment = "تذوق بلاغي ممتاز! تنفير وتحذير من علو الصوت بلا مبرر! 📢",
            wrongEgyptianComment = "تنفير يا حجاج عشان محدش يجعّر ويزعّق على الفاضي! 🔕"
        ),
        QuizQuestion(
            id = "arabic_9",
            subject = "لغة عربية",
            question = "جاء الطلاب ...... مسرورين (اختر الرابط والحال المناسب):",
            options = listOf("وهم", "وهمْ", "وهم مسرورون", "وهم مسرورين"),
            correctIndex = 2,
            explanation = "جملة الحال: (وهم مسرورون) هم: مبتدأ، ومسرورون: خبر مرفوع بالواو، والجملة في محل نصب حال.",
            correctEgyptianComment = "لقطة نحوية صعبة وجبتها! هم مبتدأ ومسرورون خبر مرفوع! 🎩",
            wrongEgyptianComment = "الخبر بعد المبتدأ بيترفع بالواو مش بالياء يا حجاج! 🤦‍♂️"
        ),
        QuizQuestion(
            id = "arabic_10",
            subject = "لغة عربية",
            question = "مرادف كلمة (قصد) في قوله تعالى لقمان لابنه: (واقصِد في مشيك):",
            options = listOf("أسرع واجرِ", "اعتدل وتوسط", "تبختر وافرح", "قف ولا تتحرك"),
            correctIndex = 1,
            explanation = "واقصد في مشيك أي اعتدل في سيرك بين الإسراع والبطء.",
            correctEgyptianComment = "تفسير ومعجم لغوي متميز! اعتدل وتوسط في كل أمورك! ✨",
            wrongEgyptianComment = "القصد يعني التوسط والاعتدال لا بطيء ولا بتجري! 🚶‍♂️"
        ),

        // ==========================================
        // 4. لغة إنجليزية تانية إعدادي (10 أسئلة)
        // ==========================================
        QuizQuestion(
            id = "english_1",
            subject = "إنجليزي",
            question = "While I ...... my English lesson, my friend knocked on the door.",
            options = listOf("study", "studied", "was studying", "am studying"),
            correctIndex = 2,
            explanation = "Rule: While + past continuous (was/were + v-ing), past simple.",
            correctEgyptianComment = "Perfect grammar ya Hagag! While was studying knock knock! 🇬🇧✨",
            wrongEgyptianComment = "While بيجي بعدها past continuous يا برنس! was studying! 🤦‍♂️"
        ),
        QuizQuestion(
            id = "english_2",
            subject = "إنجليزي",
            question = "If you don't sleep early tonight, you ...... tired tomorrow.",
            options = listOf("will be", "are", "were", "would be"),
            correctIndex = 0,
            explanation = "First Conditional: If + Present Simple (don't sleep) -> will + infinitive (will be).",
            correctEgyptianComment = "First conditional in the bag! Will be tired like Hagag! 😂👏",
            wrongEgyptianComment = "قاعدة if الأولى: مضارع بسيط يليه will والمصدر! ركز! ⚠️"
        ),
        QuizQuestion(
            id = "english_3",
            subject = "إنجليزي",
            question = "A person who does operations on patients in a hospital is called a:",
            options = listOf("dentist", "surgeon", "nurse", "chemist"),
            correctIndex = 1,
            explanation = "Surgeon (جراح): a doctor who performs operations.",
            correctEgyptianComment = "Doctor Hagag the famous surgeon! ممتاز في الكلمات! 🩺",
            wrongEgyptianComment = "Surgeon يعني جراح بيعمل عمليات! Dentist ده بتاع السنان! 🦷"
        ),
        QuizQuestion(
            id = "english_4",
            subject = "إنجليزي",
            question = "You ...... use your mobile phone during exams; it is strictly forbidden!",
            options = listOf("must", "mustn't", "should", "don't have to"),
            correctIndex = 1,
            explanation = "Mustn't expresses prohibition (تحريم ومنع قاطع).",
            correctEgyptianComment = "Mustn't for sure! ممنوع الموبايل في اللجنة يا فاشل! 📵",
            wrongEgyptianComment = "Mustn't يا باشا عشان ده ممنوع وقانون رسمي مش نصيحة! 🚫"
        ),
        QuizQuestion(
            id = "english_5",
            subject = "إنجليزي",
            question = "The school ...... I study is near my house.",
            options = listOf("which", "who", "where", "when"),
            correctIndex = 2,
            explanation = "Where is used as a relative pronoun for places (المكان).",
            correctEgyptianComment = "Great job! Where for places where you learn! 🏫",
            wrongEgyptianComment = "School مكان بنعمل فيه نشاط يبقى where يا حجاج! 📍"
        ),
        QuizQuestion(
            id = "english_6",
            subject = "إنجليزي",
            question = "Using solar panels and wind energy produces ...... energy.",
            options = listOf("fossil", "dirty", "renewable", "polluted"),
            correctIndex = 2,
            explanation = "Renewable energy (طاقة متجددة وصديقة للبيئة).",
            correctEgyptianComment = "Renewable energy green and clean! تسلم يا دحيح! ☀️🍃",
            wrongEgyptianComment = "Solar wind يعني طاقة متجددة renewable مش تلوث! 💨"
        ),
        QuizQuestion(
            id = "english_7",
            subject = "إنجليزي",
            question = "My grandfather ...... play football when he was young, but now he is old.",
            options = listOf("used to", "use to", "uses to", "is used to"),
            correctIndex = 0,
            explanation = "Used to + infinitive expresses past habits that are no longer true.",
            correctEgyptianComment = "Used to play! عادة في الماضي وانتهت! برنس! ⚽",
            wrongEgyptianComment = "الماضي من use to هو used to بالـ d يا فالح! 📜"
        ),
        QuizQuestion(
            id = "english_8",
            subject = "إنجليزي",
            question = "You look sick and exhausted. I think you ...... see a doctor.",
            options = listOf("mustn't", "should", "shouldn't", "don't"),
            correctIndex = 1,
            explanation = "Should is used for giving advice (إعطاء نصيحة).",
            correctEgyptianComment = "You should see a doctor! نصيحة في محلها! 💊",
            wrongEgyptianComment = "بننصحه يروح للدكتور يبقى should يا حجاج! 🤒"
        ),
        QuizQuestion(
            id = "english_9",
            subject = "إنجليزي",
            question = "Spending too many hours on social media can lead to internet ...... :",
            options = listOf("addiction", "prediction", "tradition", "invention"),
            correctIndex = 0,
            explanation = "Addiction (إدمان): internet addiction is spending unhealthy time online.",
            correctEgyptianComment = "Internet addiction like your TikTok addiction! 😂 برافو!",
            wrongEgyptianComment = "Addiction يعني إدمان.. اللي حجاج بيعمله مع الموبايل! 📱"
        ),
        QuizQuestion(
            id = "english_10",
            subject = "إنجليزي",
            question = "Look at those dark clouds! It ...... rain very soon.",
            options = listOf("will", "is going to", "rained", "rains"),
            correctIndex = 1,
            explanation = "Going to is used for future predictions based on present evidence (clouds).",
            correctEgyptianComment = "Evidence (dark clouds) means 'is going to rain'! شطارة لغات! 🌧️",
            wrongEgyptianComment = "فيه دليل في الجو (غيوم سودة) يبقى going to مش will! ☁️"
        )
    )

    fun getQuestionsBySubject(subject: String): List<QuizQuestion> {
        return if (subject == "الكل") questions else questions.filter { it.subject == subject }
    }
}
