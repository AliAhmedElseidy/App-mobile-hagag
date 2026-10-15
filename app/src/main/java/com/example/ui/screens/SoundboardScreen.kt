package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.HagagViewModel
import com.example.ui.theme.AccentFlame
import com.example.ui.theme.PrimaryAmber
import com.example.ui.theme.SurfaceCard

data class SoundboardItem(
    val title: String,
    val phrase: String,
    val icon: String
)

@Composable
fun SoundboardScreen(
    viewModel: HagagViewModel,
    modifier: Modifier = Modifier
) {
    val userName by viewModel.userName.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()
    val autoGreeting by viewModel.autoGreeting.collectAsState()
    val speechRate by viewModel.speechRate.collectAsState()
    val speechPitch by viewModel.speechPitch.collectAsState()

    var nameInput by remember(userName) { mutableStateOf(userName) }
    var currentPitch by remember(speechPitch) { mutableFloatStateOf(speechPitch) }
    var currentRate by remember(speechRate) { mutableFloatStateOf(speechRate) }

    val soundItems = listOf(
        SoundboardItem("توبيخ الافتتاح 📢", "ازيك يا $userName.. ذاكر يا فاشل!", "🔥"),
        SoundboardItem("كوباية الشاي ☕", "كوباية الشاي رقم ستة بردت وأنت لسه بتبص في الفهرس يا $userName!", "☕"),
        SoundboardItem("صدمة الامتحان 🚨", "الدفعة خلصت المنهج وبتراجع، وأنت بتسأل الامتحان مقالي ولا إم سي كيو؟", "😱"),
        SoundboardItem("التناضح 🧪", "مفيش كيمياء هتدخل نفوخك بالتناضح.. افتح الكتاب يا فاشل!", "🧪"),
        SoundboardItem("سيب السرير 🛏️", "السرير مش هيطلعك مهندس ولا دكتور يا $userName.. سيب اللحاف وقوم!", "⚡"),
        SoundboardItem("المستقبل بيضيع 📱", "ألو؟ مستقبل $userName بيضيع في التيك توك.. حد يلحقه بملزمة!", "📱"),
        SoundboardItem("ارحم الملازم 📄", "الملازم بتعيط في الدرج يا $userName.. ارحمها وبص فيها خمس دقايق!", "😭"),
        SoundboardItem("شحنة أمل 🌟", "عاش يا $userName! إنت بطل وقدها وهتبقى دحيح الدفعة!", "🏆")
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("soundboard_screen"),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Text(
                text = "🔊 صاوند بورد توبيخ وتحفيز $userName",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 22.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "دوس على أي زرار عشان تسمع التوبيخ بصوت عالي وفوري!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Stop audio button if speaking
        if (isSpeaking) {
            item {
                Button(
                    onClick = { viewModel.stopSpeaking() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentFlame,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(imageVector = Icons.Default.Stop, contentDescription = "إيقاف")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("إيقاف الصوت الآن", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Soundboard Grid
        item {
            Text(
                text = "الأصوات السريعة ⚡",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = PrimaryAmber
            )
        }

        // 2-column Soundboard grid inside items
        val chunkedItems = soundItems.chunked(2)
        items(chunkedItems.size) { rowIndex ->
            val row = chunkedItems[rowIndex]
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                for (item in row) {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.speakPhrase(item.phrase) }
                            .testTag("sound_item_${item.title}"),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = item.icon,
                                fontSize = 32.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                ),
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                if (row.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        // Voice and Name Settings
        item {
            Spacer(modifier = Modifier.height(10.dp))
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
                            contentDescription = "إعدادات",
                            tint = PrimaryAmber
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "إعدادات الصوت والاسم 🎛️",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }

                    // Change Name
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
                            .testTag("username_input"),
                        singleLine = true
                    )

                    // Auto Greeting on Open Toggle
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
                                text = "يقول 'ازيك يا حجاج ذاكر يا فاشل' أول لما يفتح",
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
                            ),
                            modifier = Modifier.testTag("auto_greeting_switch")
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

                    // Test Voice Button
                    Button(
                        onClick = { viewModel.shoutGreeting() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryAmber,
                            contentColor = Color.Black
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("test_voice_button"),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(imageVector = Icons.Default.VolumeUp, contentDescription = "تجربة")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("تجربة الصوت الآن 🎤", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
