package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.ai.ChatMessage
import com.example.ai.LiveState
import com.example.ui.HagagViewModel
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentEmerald
import com.example.ui.theme.AccentFlame
import com.example.ui.theme.PrimaryAmber
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceDark

@Composable
fun ChatScreen(
    viewModel: HagagViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedModeTab by remember { mutableIntStateOf(0) } // 0: Gemini Live Voice, 1: Text Chat

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("chat_screen")
    ) {
        // Top Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1B4B))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(PrimaryAmber),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "👨‍🏫", fontSize = 24.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "مستر حجاج الفرفوش (Gemini Live) 🧠🎙️",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Text(
                            text = "مدرس تانية إعدادي.. في المذاكرة بس بهزار خفيف وتشجيع!",
                            style = MaterialTheme.typography.bodySmall,
                            color = PrimaryAmber
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Mode Tabs: Live Voice vs Text Chat
                TabRow(
                    selectedTabIndex = selectedModeTab,
                    containerColor = Color(0xFF0F172A),
                    contentColor = PrimaryAmber,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedModeTab]),
                            color = PrimaryAmber
                        )
                    },
                    modifier = Modifier.clip(RoundedCornerShape(12.dp))
                ) {
                    Tab(
                        selected = selectedModeTab == 0,
                        onClick = { selectedModeTab = 0 },
                        text = {
                            Text(
                                text = "🎙️ محادثة صوتية لايف",
                                fontWeight = if (selectedModeTab == 0) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedModeTab == 0) PrimaryAmber else Color.LightGray
                            )
                        },
                        modifier = Modifier.testTag("tab_live_voice")
                    )
                    Tab(
                        selected = selectedModeTab == 1,
                        onClick = { selectedModeTab = 1 },
                        text = {
                            Text(
                                text = "💬 دردشة كتابية",
                                fontWeight = if (selectedModeTab == 1) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedModeTab == 1) PrimaryAmber else Color.LightGray
                            )
                        },
                        modifier = Modifier.testTag("tab_text_chat")
                    )
                }
            }
        }

        // Screen Body depending on mode
        if (selectedModeTab == 0) {
            GeminiLiveVoicePane(
                viewModel = viewModel,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            )
        } else {
            GeminiTextChatPane(
                viewModel = viewModel,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            )
        }
    }
}

/**
 * Full Interactive Gemini Live Voice Screen Pane with "مستر حجاج الفرفوش"
 */
@Composable
private fun GeminiLiveVoicePane(
    viewModel: HagagViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val liveState by viewModel.liveState.collectAsState()
    val userSpokenText by viewModel.liveUserSpokenText.collectAsState()
    val assistantReplyText by viewModel.liveAssistantReplyText.collectAsState()
    val soundLevel by viewModel.liveSoundLevel.collectAsState()
    val userName by viewModel.userName.collectAsState()

    // Permission launcher for RECORD_AUDIO
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.startLiveListening()
        }
    }

    // Pulse animation for avatar sphere
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_sphere")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val quickLiveTopics = listOf(
        "📐 الجذر التكعيبي ومتوسطات المثلث" to "اشرحلي يا مستر الجذر التكعيبي وتقسيم متوسطات المثلث في تانية إعدادي ببساطة",
        "🧪 جدول مندليف وطبقة الأوزون" to "اشرحلي يا مستر جدول مندليف وموزلي وطبقة الأوزون في العلوم بهزار خفيف",
        "📖 المعرب والمبني والنعت والحال" to "فهمني يا مستر النحو في تانية إعدادي وإزاي أفرق بين النعت والحال",
        "🇬🇧 قاعدة While وأزمنة الإنجليزي" to "اشرحلي قاعدة While في الماضي المستمر للإنجليزي تانية إعدادي",
        "🎁 امتى أفتح مكافأة اللعب؟" to "ازاي افتح مكافأة الـ 30 دقيقة لعب أو الحلوى من جلسات المذاكرة؟",
        "🔥 كسلان وشكلي هفشل شجعني!" to "أنا كسلان ومش قادر أذاكر النهاردة يا مستر حجاج، شجعني بأسلوبك الفرفوش!"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("gemini_live_pane"),
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Permission Missing Alert Banner
        if (liveState is LiveState.NoPermission) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF450A0A))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.MicOff, contentDescription = "إذن الميكروفون", tint = AccentFlame, modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "محتاجين إذن الميكروفون يا حجاج! 🎙️",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = (liveState as LiveState.NoPermission).message,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFFECACA),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = {
                                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentFlame)
                        ) {
                            Text("السماح بالميكروفون 🎙️", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Offline Alert Banner
        if (liveState is LiveState.Offline) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF3B1D11))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.WifiOff, contentDescription = "إنترنت غير متوفر", tint = PrimaryAmber, modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "يا حجاج النت فاصل عندك! 🌐",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = (liveState as LiveState.Offline).message,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFFED7AA),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { viewModel.resetLiveState() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF475569))
                            ) {
                                Text("إغلاق التنبيه", color = Color.White)
                            }
                            Button(
                                onClick = {
                                    val hasPerm = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                                    if (hasPerm) viewModel.startLiveListening() else permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryAmber)
                            ) {
                                Text("إعادة المحاولة 🔄", color = Color.Black, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Hero Live Sphere / Visualizer
        item {
            val isListening = liveState is LiveState.Listening
            val isThinking = liveState is LiveState.Thinking
            val isSpeaking = liveState is LiveState.Speaking

            val sphereGradient = when {
                isListening -> Brush.radialGradient(listOf(Color(0xFF10B981), Color(0xFF047857), Color(0xFF064E3B)))
                isThinking -> Brush.radialGradient(listOf(Color(0xFF8B5CF6), Color(0xFF6D28D9), Color(0xFF2E1065)))
                isSpeaking -> Brush.radialGradient(listOf(PrimaryAmber, Color(0xFFD97706), Color(0xFF78350F)))
                else -> Brush.radialGradient(listOf(Color(0xFF38BDF8), Color(0xFF0284C7), Color(0xFF0C4A6E)))
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Outer glowing circle
                Box(
                    modifier = Modifier
                        .size(170.dp)
                        .scale(if (isListening || isSpeaking) pulseScale else 1f)
                        .clip(CircleShape)
                        .background(sphereGradient)
                        .border(
                            width = 4.dp,
                            color = if (isListening) Color(0xFF34D399) else if (isSpeaking) PrimaryAmber else Color(0xFF7DD3FC),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (isSpeaking) "🗣️" else if (isThinking) "🤔" else if (isListening) "🎙️" else "👨‍🏫",
                            fontSize = 44.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = when {
                                isListening -> "سامعك يا بطل..."
                                isThinking -> "بيفكر في الرد..."
                                isSpeaking -> "بيتكلم..."
                                else -> "مستر حجاج"
                            },
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Egyptian Status Badge
                val statusText = when {
                    isListening -> "🟢 مستر حجاج سامعك.. اتكلم واسأله يا بطل!"
                    isThinking -> "🟡 مستر حجاج بيفكر في رد مبسط ومرح..."
                    isSpeaking -> "🔵 مستر حجاج بيشرحلك صوتياً دلوقتي!"
                    liveState is LiveState.NoPermission -> "🔴 مطلوب إذن الميكروفون للبدء"
                    liveState is LiveState.Offline -> "🟠 النت فاصل.. وصّل النت أو اختر سؤالاً جاهزاً"
                    else -> "⚪ اضغط على زرار المايك واتكلم بصوتك!"
                }

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
                ) {
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Main Action Mic Button
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isSpeaking) {
                        Button(
                            onClick = { viewModel.stopLiveSpeaking() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier
                                .height(56.dp)
                                .testTag("stop_speaking_button")
                        ) {
                            Icon(Icons.Default.Stop, contentDescription = "إيقاف", tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("إيقاف الصوت ⏹️", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    } else if (isListening) {
                        Button(
                            onClick = { viewModel.stopLiveListening() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA580C)),
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier
                                .height(56.dp)
                                .testTag("stop_listening_button")
                        ) {
                            Icon(Icons.Default.Stop, contentDescription = "إنهاء الكلام", tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("خلصت كلامي.. جاوبني يا مستر! 🚀", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    } else {
                        Button(
                            onClick = {
                                val hasPerm = ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.RECORD_AUDIO
                                ) == PackageManager.PERMISSION_GRANTED

                                if (hasPerm) {
                                    viewModel.startLiveListening()
                                } else {
                                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryAmber),
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier
                                .height(56.dp)
                                .testTag("start_live_voice_button")
                        ) {
                            Icon(Icons.Default.Mic, contentDescription = "تحدث صوتياً", tint = Color.Black)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("اضغط وتكلم مع مستر حجاج 🎙️", fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 16.sp)
                        }
                    }
                }
            }
        }

        // Live Captions Cards: User query and Assistant Reply
        if (userSpokenText.isNotBlank()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A8A))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(text = "🎒", fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "أنت سألت ($userName):",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFF93C5FD)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = userSpokenText,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        if (assistantReplyText.isNotBlank()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "👨‍🏫", fontSize = 22.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "رد مستر حجاج الفرفوش:",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = PrimaryAmber
                                )
                            }

                            IconButton(
                                onClick = { viewModel.speakPhrase(assistantReplyText) },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                    contentDescription = "إعادة النطق",
                                    tint = PrimaryAmber
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = assistantReplyText,
                            style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 24.sp),
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Quick Voice Prompts for 2nd Prep
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "أو اضغط لسؤال مستر حجاج فوراً بصوتك ⚡",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                quickLiveTopics.forEach { (label, prompt) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                viewModel.sendLiveVoicePrompt(prompt)
                            },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                color = Color.White
                            )
                            Icon(
                                Icons.Default.PlayArrow,
                                contentDescription = "تشغيل السؤال",
                                tint = PrimaryAmber,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Text Chat Pane with "مستر حجاج الفرفوش"
 */
@Composable
private fun GeminiTextChatPane(
    viewModel: HagagViewModel,
    modifier: Modifier = Modifier
) {
    val messages by viewModel.chatMessages.collectAsState()
    val isLoading by viewModel.isChatLoading.collectAsState()

    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    val starterPrompts = listOf(
        "اشرحلي الجذر التكعيبي بسهولة 📐",
        "إيه طبقات الغلاف الجوي؟ 🧪",
        "إزاي أفرق بين النعت والحال؟ 📖",
        "شجعني أخلص الجلسة وافتح مكافأة! 🎁"
    )

    Column(modifier = modifier) {
        // Quick Starter Chips
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(starterPrompts) { prompt ->
                FilterChip(
                    selected = false,
                    onClick = { viewModel.sendChatMessage(prompt) },
                    label = { Text(prompt, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color(0xFF1E293B),
                        labelColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }

        // Messages List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                ChatBubbleItem(
                    message = msg,
                    onSpeak = { viewModel.speakPhrase(msg.text) }
                )
            }

            if (isLoading) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 12.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = PrimaryAmber,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "مستر حجاج بيفكر ويكتبلك الرد...",
                            style = MaterialTheme.typography.bodySmall,
                            color = PrimaryAmber
                        )
                    }
                }
            }
        }

        // Input Area
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E293B))
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text("اسأل مستر حجاج في أي مادة أو مسألة...") },
                modifier = Modifier
                    .weight(1f)
                    .testTag("chat_input_field"),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryAmber,
                    unfocusedBorderColor = Color(0xFF475569),
                    focusedContainerColor = Color(0xFF0F172A),
                    unfocusedContainerColor = Color(0xFF0F172A),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                maxLines = 3
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    if (inputText.isNotBlank()) {
                        viewModel.sendChatMessage(inputText)
                        inputText = ""
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (inputText.isNotBlank()) PrimaryAmber else Color(0xFF334155))
                    .testTag("send_chat_button"),
                enabled = inputText.isNotBlank() && !isLoading
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "إرسال",
                    tint = if (inputText.isNotBlank()) Color.Black else Color.Gray,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
private fun ChatBubbleItem(
    message: ChatMessage,
    onSpeak: () -> Unit
) {
    val isUser = message.sender == "user"

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(PrimaryAmber),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "👨‍🏫", fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Card(
            shape = RoundedCornerShape(
                topStart = 18.dp,
                topEnd = 18.dp,
                bottomStart = if (isUser) 18.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 18.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (isUser) Color(0xFF1E3A8A) else SurfaceCard
            ),
            modifier = Modifier.fillMaxWidth(0.85f)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 15.sp,
                        lineHeight = 22.sp
                    ),
                    color = Color.White
                )

                if (!isUser) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = onSpeak,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "نطق الرد",
                                tint = PrimaryAmber,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
