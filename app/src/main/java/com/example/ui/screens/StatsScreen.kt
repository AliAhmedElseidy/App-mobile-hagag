package com.example.ui.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notification.HagagNotificationHelper
import com.example.ui.HagagViewModel
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentEmerald
import com.example.ui.theme.AccentFlame
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.PrimaryAmber
import com.example.ui.theme.SurfaceCard

@Composable
fun StatsScreen(
    viewModel: HagagViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val userName by viewModel.userName.collectAsState()
    val stats by viewModel.stats.collectAsState()
    val autoGreeting by viewModel.autoGreeting.collectAsState()
    val speechRate by viewModel.speechRate.collectAsState()
    val speechPitch by viewModel.speechPitch.collectAsState()

    val dailyReminderEnabled by viewModel.dailyReminderEnabled.collectAsState()
    val reminderHour by viewModel.reminderHour.collectAsState()
    val reminderMinute by viewModel.reminderMinute.collectAsState()

    var nameInput by remember(userName) { mutableStateOf(userName) }
    var currentPitch by remember(speechPitch) { mutableFloatStateOf(speechPitch) }
    var currentRate by remember(speechRate) { mutableFloatStateOf(speechRate) }

    var selectedHour by remember(reminderHour) { mutableIntStateOf(reminderHour) }
    var selectedMinute by remember(reminderMinute) { mutableIntStateOf(reminderMinute) }

    // Android 13+ Notification Permission Launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.updateDailyReminder(true, selectedHour, selectedMinute)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("stats_screen"),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Title
        item {
            Text(
                text = "📊 إحصائيات وإعدادات $userName",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 22.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "شوف تعبك وإنجازك بالأرقام يا بطل تانية إعدادي!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // 4 Main Stats Cards (2x2 Grid in Rows)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "جلسات النهاردة",
                    value = "${stats.sessionsToday} جلسات",
                    subtitle = if (stats.sessionsToday > 0) "عاش كمل!" else "لسه مابدأتش!",
                    icon = Icons.Default.Timer,
                    iconColor = AccentEmerald
                )

                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "جلسات الأسبوع",
                    value = "${stats.sessionsThisWeek} جلسة",
                    subtitle = "حصيلة 7 أيام",
                    icon = Icons.Default.CalendarMonth,
                    iconColor = AccentCyan
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "أكتر مادة ذاكرتها",
                    value = stats.mostStudiedSubject,
                    subtitle = "نجم المواد عندك",
                    icon = Icons.Default.School,
                    iconColor = PrimaryAmber
                )

                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "أيام الاستمرار",
                    value = "${stats.streakDays} أيام 🔥",
                    subtitle = if (stats.streakDays > 0) "السلسلة ولعانة!" else "العداد صفر!",
                    icon = Icons.Default.LocalFireDepartment,
                    iconColor = AccentFlame
                )
            }
        }

        // Daily Notification Setting Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "إشعار",
                                tint = PrimaryAmber
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "تذكير المذاكرة اليومي ⏰",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                        }

                        Switch(
                            checked = dailyReminderEnabled,
                            onCheckedChange = { isChecked ->
                                if (isChecked && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                                    !HagagNotificationHelper.hasNotificationPermission(context)
                                ) {
                                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                } else {
                                    viewModel.updateDailyReminder(isChecked, selectedHour, selectedMinute)
                                }
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.Black,
                                checkedTrackColor = PrimaryAmber
                            ),
                            modifier = Modifier.testTag("daily_reminder_switch")
                        )
                    }

                    Text(
                        text = "هنبعتلك إشعار مصري دمه خفيف في الميعاد ده عشان يفكرك تسيب الموبايل وتذاكر!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (dailyReminderEnabled) {
                        Text(
                            text = "الميعاد الحالي: الساعة ${String.format("%02d:%02d", selectedHour, selectedMinute)}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = PrimaryAmber
                        )

                        // Quick Time presets
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            TimeChip(label = "5:00 م", selected = selectedHour == 17 && selectedMinute == 0) {
                                selectedHour = 17
                                selectedMinute = 0
                                viewModel.updateDailyReminder(true, 17, 0)
                            }
                            TimeChip(label = "7:00 م", selected = selectedHour == 19 && selectedMinute == 0) {
                                selectedHour = 19
                                selectedMinute = 0
                                viewModel.updateDailyReminder(true, 19, 0)
                            }
                            TimeChip(label = "9:00 م", selected = selectedHour == 21 && selectedMinute == 0) {
                                selectedHour = 21
                                selectedMinute = 0
                                viewModel.updateDailyReminder(true, 21, 0)
                            }
                        }

                        // Test Notification button
                        Button(
                            onClick = {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                                    !HagagNotificationHelper.hasNotificationPermission(context)
                                ) {
                                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                } else {
                                    HagagNotificationHelper.createNotificationChannels(context)
                                    HagagNotificationHelper.showDailyReminderNotification(context, userName)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF334155),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().testTag("test_notification_button")
                        ) {
                            Icon(Icons.Default.Alarm, contentDescription = "تجربة الإشعار", modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("جرّب الإشعار دلوقتي في شريط الإشعارات 🔔", fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // Voice and Name Preferences
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = "صوت",
                            tint = PrimaryAmber
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "تعديل الاسم والصوت 🎛️",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }

                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = {
                            nameInput = it
                            viewModel.updateUserName(it)
                        },
                        label = { Text("اسم الطالب (حجاج)") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Person, contentDescription = "الاسم")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("stats_username_input"),
                        singleLine = true
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "التوبيخ التلقائي عند فتح التطبيق",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                            Text(
                                text = "يقول 'إزّيك يا حجاج ذاكر يا فاشل' أول لما يفتح",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = autoGreeting,
                            onCheckedChange = { viewModel.updateAutoGreeting(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.Black,
                                checkedTrackColor = PrimaryAmber
                            )
                        )
                    }

                    // Pitch Slider
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "درجة نبرة الصوت (Pitch)", color = Color.White, style = MaterialTheme.typography.bodySmall)
                            Text(text = String.format("%.2f", currentPitch), color = PrimaryAmber, style = MaterialTheme.typography.bodySmall)
                        }
                        Slider(
                            value = currentPitch,
                            onValueChange = {
                                currentPitch = it
                                viewModel.updateSpeechSettings(currentPitch, currentRate)
                            },
                            valueRange = 0.7f..1.5f,
                            colors = SliderDefaults.colors(
                                thumbColor = PrimaryAmber,
                                activeTrackColor = PrimaryAmber
                            )
                        )
                    }

                    // Speed Slider
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "سرعة الكلام (Speech Rate)", color = Color.White, style = MaterialTheme.typography.bodySmall)
                            Text(text = String.format("%.2f", currentRate), color = PrimaryAmber, style = MaterialTheme.typography.bodySmall)
                        }
                        Slider(
                            value = currentRate,
                            onValueChange = {
                                currentRate = it
                                viewModel.updateSpeechSettings(currentPitch, currentRate)
                            },
                            valueRange = 0.7f..1.4f,
                            colors = SliderDefaults.colors(
                                thumbColor = PrimaryAmber,
                                activeTrackColor = PrimaryAmber
                            )
                        )
                    }

                    Button(
                        onClick = { viewModel.shoutGreeting() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryAmber,
                            contentColor = Color.Black
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(imageVector = Icons.Default.VolumeUp, contentDescription = "تجربة")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("جرّب الصوت دلوقتي 🎤", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    iconColor: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                ),
                color = Color.White
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = iconColor
            )
        }
    }
}

@Composable
private fun TimeChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = PrimaryAmber,
            selectedLabelColor = Color.Black,
            containerColor = Color(0xFF1E293B),
            labelColor = Color.White
        )
    )
}
