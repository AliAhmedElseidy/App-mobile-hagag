package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.StudyReward
import com.example.ui.HagagViewModel
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentEmerald
import com.example.ui.theme.AccentFlame
import com.example.ui.theme.PrimaryAmber
import com.example.ui.theme.SurfaceCard

@Composable
fun RewardsScreen(
    viewModel: HagagViewModel,
    modifier: Modifier = Modifier
) {
    val userName by viewModel.userName.collectAsState()
    val rewards: List<StudyReward> by viewModel.rewards.collectAsState()
    val stats by viewModel.stats.collectAsState()
    val completedSessions = stats.sessionsToday.coerceAtLeast(stats.sessionsThisWeek)

    var selectedCategoryFilter by remember { mutableStateOf("الكل") }
    var showAddDialog by remember { mutableStateOf(false) }

    val categories = listOf("الكل", "ألعاب وترفيه", "حلويات وأكل", "سوشيال ميديا", "خروجات", "أفلام وأنيمي", "راحة وكسل", "مكافأة مخصصة")

    val filteredRewards = rewards.filter {
        selectedCategoryFilter == "الكل" || it.category == selectedCategoryFilter
    }

    val unlockedCount = rewards.count { completedSessions >= it.requiredSessions }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("rewards_screen"),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = PrimaryAmber,
                contentColor = Color.Black,
                shape = CircleShape,
                modifier = Modifier.testTag("add_reward_fab")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "إضافة مكافأة")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1B4B))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CardGiftcard,
                                contentDescription = "مكافأة",
                                tint = PrimaryAmber,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "صندوق مكافآت $userName 🎁",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Black,
                                        fontSize = 20.sp
                                    ),
                                    color = Color.White
                                )
                                Text(
                                    text = "ذاكر بومودورو وافتح دلع ولعب بدون تأنيب ضمير!",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = PrimaryAmber
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "جلسات المذاكرة المنجزة: $completedSessions جلسة ⏱️",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(AccentEmerald.copy(alpha = 0.2f))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "مفتوح لك: $unlockedCount مكافآت",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = AccentEmerald
                                )
                            }
                        }
                    }
                }
            }

            // Categories Filter Bar
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { cat ->
                        FilterChip(
                            selected = selectedCategoryFilter == cat,
                            onClick = { selectedCategoryFilter = cat },
                            label = { Text(cat) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PrimaryAmber,
                                selectedLabelColor = Color.Black,
                                containerColor = Color(0xFF1E293B),
                                labelColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }

            // Rewards List
            items(filteredRewards, key = { it.id }) { reward ->
                val isUnlocked = completedSessions >= reward.requiredSessions
                RewardCardItem(
                    reward = reward,
                    completedSessions = completedSessions,
                    isUnlocked = isUnlocked,
                    onClaim = { viewModel.claimReward(reward.id) },
                    onReset = { viewModel.resetReward(reward.id) },
                    onDelete = { viewModel.deleteReward(reward.id) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(70.dp))
            }
        }
    }

    if (showAddDialog) {
        AddRewardDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { title, desc, sessions, cat, emoji ->
                viewModel.addCustomReward(title, desc, sessions, cat, emoji)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun RewardCardItem(
    reward: StudyReward,
    completedSessions: Int,
    isUnlocked: Boolean,
    onClaim: () -> Unit,
    onReset: () -> Unit,
    onDelete: () -> Unit
) {
    val progress = (completedSessions.toFloat() / reward.requiredSessions.toFloat()).coerceIn(0f, 1f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("reward_card_${reward.id}"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked && !reward.isClaimed) Color(0xFF192C3D) else SurfaceCard
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isUnlocked) 6.dp else 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Large Emoji
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(if (isUnlocked) PrimaryAmber.copy(alpha = 0.2f) else Color(0xFF334155)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = reward.emoji, fontSize = 28.sp)
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = reward.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            ),
                            color = Color.White
                        )

                        // Lock / Unlock icon
                        Icon(
                            imageVector = if (isUnlocked) Icons.Default.LockOpen else Icons.Default.Lock,
                            contentDescription = if (isUnlocked) "مفتوحة" else "مقفولة",
                            tint = if (isUnlocked) AccentEmerald else AccentFlame,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = reward.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Progress bar toward unlocking
            if (!isUnlocked) {
                val needed = reward.requiredSessions - completedSessions
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "مطلوب ${reward.requiredSessions} جلسات (أنجزت $completedSessions)",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "فاضلك $needed جلسة وتفتح! ⏳",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = PrimaryAmber
                        )
                    }
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape),
                        color = PrimaryAmber,
                        trackColor = Color(0xFF1E293B),
                        strokeCap = StrokeCap.Round
                    )
                }
            } else {
                // Action Buttons for unlocked reward
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!reward.isClaimed) {
                        Button(
                            onClick = onClaim,
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("claim_reward_button_${reward.id}"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AccentEmerald,
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = "استلم المكافأة وعيش! 🎉",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = "تم", tint = AccentEmerald, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "تم الاستمتاع بيها اليوم! 🌟",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = AccentEmerald
                            )
                        }

                        TextButton(onClick = onReset) {
                            Icon(Icons.Default.Refresh, contentDescription = "إعادة", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("استلام تاني", color = PrimaryAmber, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AddRewardDialog(
    onDismiss: () -> Unit,
    onAdd: (title: String, desc: String, sessions: Int, category: String, emoji: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var sessions by remember { mutableIntStateOf(2) }
    var emoji by remember { mutableStateOf("🎮") }
    var category by remember { mutableStateOf("ألعاب وترفيه") }

    val presetEmojis = listOf("🎮", "🍫", "🍕", "📱", "🎬", "😴", "🎧", "⚽", "🍔", "🏖️")
    val presetCategories = listOf("ألعاب وترفيه", "حلويات وأكل", "سوشيال ميديا", "خروجات", "أفلام وأنيمي", "مكافأة مخصصة")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("إضافة مكافأة مذاكرة جديدة 🎁", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("اسم المكافأة") },
                    placeholder = { Text("مثال: ساعة لعبة فيفا أو كود") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("الوصف (هتعمل إيه بالضبط؟)") },
                    placeholder = { Text("مثال: العب مع أصحابي بعد ما أخلص الرياضيات") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Text("اختر الأيقونة:", style = MaterialTheme.typography.labelSmall, color = PrimaryAmber)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(presetEmojis) { em ->
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(if (emoji == em) PrimaryAmber.copy(alpha = 0.3f) else Color(0xFF1E293B))
                                .border(if (emoji == em) 1.5.dp else 0.dp, PrimaryAmber, CircleShape)
                                .clickable { emoji = em },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = em, fontSize = 20.sp)
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "مطلوب: $sessions جلسات بومودورو")
                    Row {
                        TextButton(onClick = { if (sessions > 1) sessions -= 1 }) { Text("-1") }
                        TextButton(onClick = { sessions += 1 }) { Text("+1") }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onAdd(title, desc.ifBlank { "مكافأة بعد إنهاء $sessions جلسات مذاكرة!" }, sessions, category, emoji)
                    }
                },
                enabled = title.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryAmber, contentColor = Color.Black)
            ) {
                Text("إضافة المكافأة 🚀", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("إلغاء") }
        }
    )
}
