package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.StudyTask
import com.example.ui.HagagViewModel
import com.example.ui.components.GeniusMeterCard
import com.example.ui.components.HagagHeader
import com.example.ui.components.SpeechBubbleCard
import com.example.ui.theme.AccentEmerald
import com.example.ui.theme.AccentFlame
import com.example.ui.theme.PrimaryAmber
import com.example.ui.theme.SurfaceCard

@Composable
fun HomeScreen(
    viewModel: HagagViewModel,
    onNavigateToTimer: () -> Unit,
    onNavigateToTasks: () -> Unit,
    onNavigateToQuiz: () -> Unit,
    onNavigateToRewards: () -> Unit,
    onNavigateToChat: () -> Unit,
    modifier: Modifier = Modifier
) {
    val userName by viewModel.userName.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()
    val quote by viewModel.currentQuote.collectAsState()
    val stats by viewModel.stats.collectAsState()
    val tasks by viewModel.tasks.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen")
    ) {
        HagagHeader(
            userName = userName,
            levelTitle = stats.currentLevelTitle,
            isSpeaking = isSpeaking,
            onShoutClick = { viewModel.shoutGreeting() },
            onStatsClick = { viewModel.selectTab(6) }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Streak Broken Egyptian Alert
            if (stats.streakBrokenAlert != null) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF450A0A))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "تحذير",
                                tint = AccentFlame,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "قطعت السلسلة يا حجاج! 💔",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = stats.streakBrokenAlert ?: "",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFFECACA)
                                )
                            }
                            IconButton(onClick = { viewModel.dismissStreakBrokenAlert() }) {
                                Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = Color.White)
                            }
                        }
                    }
                }
            }

            // Main Hero Speech Bubble Card
            item {
                SpeechBubbleCard(
                    quote = quote,
                    userName = userName,
                    isSpeaking = isSpeaking,
                    onShoutGreeting = { viewModel.shoutGreeting() },
                    onRoastAgain = { viewModel.roastMeAgain() }
                )
            }

            // Quick Focus Session Banner
            item {
                QuickFocusBanner(
                    onStartFocus = {
                        viewModel.setDuration(25)
                        viewModel.startTimer()
                        onNavigateToTimer()
                    }
                )
            }

            // Quick Rewards Callout Banner
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onNavigateToRewards)
                        .testTag("home_rewards_callout"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryAmber.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🎁", fontSize = 24.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "صندوق مكافآت حجاج 🎮🍫",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White
                                )
                                Text(
                                    text = "خلص جلسات بومودورو وافتح 30 دقيقة لعب أو حلوى!",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = PrimaryAmber
                                )
                            }
                        }

                        TextButton(onClick = onNavigateToRewards) {
                            Text("فتح 🎁", color = PrimaryAmber, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Quick 2nd Prep Quiz Callout
            item {
                QuickQuizCallout(onNavigateToQuiz = onNavigateToQuiz)
            }

            // Quick AI Tutor Callout
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onNavigateToChat)
                        .testTag("home_chat_callout"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1B4B))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF38BDF8).copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "👨‍🏫", fontSize = 22.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "محادثة صوتية لايف مع مستر حجاج (Gemini Live) 🎙️🧠",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White
                                )
                                Text(
                                    text = "اتكلم معاه بصوتك يشرحلك أي مسألة في تانية إعدادي ويهزر معاك!",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFBAE6FD)
                                )
                            }
                        }

                        TextButton(onClick = onNavigateToChat) {
                            Text("تحدث 🎙️", color = Color(0xFF38BDF8), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Genius Meter
            item {
                GeniusMeterCard(stats = stats)
            }

            // Tasks Preview Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "جدول مواد تانية إعدادي 📝",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    TextButton(onClick = onNavigateToTasks) {
                        Text(
                            text = "عرض كل المواد (${tasks.size})",
                            color = PrimaryAmber,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Top 3 Tasks
            val previewTasks = tasks.take(3)
            items(previewTasks.size) { index ->
                val task = previewTasks[index]
                QuickTaskItem(
                    task = task,
                    onToggle = { viewModel.toggleTask(task.id) }
                )
            }

            // Funny Egyptian Study Tip Card
            item {
                EgyptianHumorCard(userName = userName)
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
private fun QuickFocusBanner(
    onStartFocus: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("quick_focus_banner"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(AccentFlame.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = "مؤقت",
                        tint = AccentFlame,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "جلسة بومودورو 25 دقيقة ⏱️",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Text(
                        text = "اقفل تيك توك وسيب اللحاف وقوم!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Button(
                onClick = onStartFocus,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryAmber,
                    contentColor = Color.Black
                ),
                modifier = Modifier.testTag("start_quick_focus_button")
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "ابدأ",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "ابدأ فوراً",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun QuickQuizCallout(
    onNavigateToQuiz: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onNavigateToQuiz),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1B4B))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(PrimaryAmber.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = "كويز",
                        tint = PrimaryAmber,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "كويز الفهامة لتانية إعدادي 🧠",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Text(
                        text = "10 أسئلة سريعة أوفلاين في كل مادة!",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFCBD5E1)
                    )
                }
            }

            TextButton(onClick = onNavigateToQuiz) {
                Text("جرّب مخك 💡", color = PrimaryAmber, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun QuickTaskItem(
    task: StudyTask,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
            .testTag("task_item_${task.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(if (task.isCompleted) AccentEmerald else Color(0xFF334155)),
                contentAlignment = Alignment.Center
            ) {
                if (task.isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "تم",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = if (task.isCompleted) FontWeight.Normal else FontWeight.SemiBold,
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    ),
                    color = if (task.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${task.subject} • ${task.estimatedMinutes} دقيقة",
                    style = MaterialTheme.typography.labelSmall,
                    color = PrimaryAmber
                )
            }
        }
    }
}

@Composable
private fun EgyptianHumorCard(userName: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1B4B))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "💡", fontSize = 28.sp, modifier = Modifier.padding(end = 12.dp))
            Column {
                Text(
                    text = "نصيحة الدحيحة لـ $userName",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = PrimaryAmber
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "المذاكرة 25 دقيقة أسهل بكتير من إحساس الندم لما تشوف ورقة امتحان تانية إعدادي!",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White
                )
            }
        }
    }
}
