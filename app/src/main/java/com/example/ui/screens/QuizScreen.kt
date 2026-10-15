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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.QuizRepository
import com.example.ui.HagagViewModel
import com.example.ui.theme.AccentEmerald
import com.example.ui.theme.AccentFlame
import com.example.ui.theme.PrimaryAmber
import com.example.ui.theme.SurfaceCard

@Composable
fun QuizScreen(
    viewModel: HagagViewModel,
    modifier: Modifier = Modifier
) {
    val userName by viewModel.userName.collectAsState()
    val selectedSubject by viewModel.selectedQuizSubject.collectAsState()
    val currentIndex by viewModel.currentQuizIndex.collectAsState()
    val selectedOption by viewModel.selectedOption.collectAsState()
    val isSubmitted by viewModel.isAnswerSubmitted.collectAsState()
    val score by viewModel.quizScore.collectAsState()
    val feedbackComment by viewModel.currentFeedbackComment.collectAsState()

    val questions = QuizRepository.getQuestionsBySubject(selectedSubject)
    val currentQuestion = questions.getOrNull(currentIndex)
    val subjects = listOf("رياضيات", "علوم", "لغة عربية", "إنجليزي")

    val isQuizFinished = isSubmitted && currentIndex == questions.size - 1

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("quiz_screen"),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "🧠 كويز الفهامة لتانية إعدادي",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "نشوفك دحيح يا $userName ولا بتهبد!",
                        style = MaterialTheme.typography.bodySmall,
                        color = PrimaryAmber
                    )
                }

                // Score Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF1E293B))
                        .border(1.dp, PrimaryAmber.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "السكور: $score / ${questions.size}",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
            }
        }

        // Subject selector row
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(subjects) { subject ->
                    FilterChip(
                        selected = selectedSubject == subject,
                        onClick = { viewModel.selectQuizSubject(subject) },
                        label = {
                            val emoji = when (subject) {
                                "رياضيات" -> "📐"
                                "علوم" -> "🧪"
                                "لغة عربية" -> "📖"
                                else -> "🇬🇧"
                            }
                            Text("$emoji $subject")
                        },
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

        if (currentQuestion != null && !isQuizFinished) {
            // Question Progress
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "السؤال ${currentIndex + 1} من ${questions.size}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "مادة ${currentQuestion.subject}",
                        style = MaterialTheme.typography.labelSmall,
                        color = PrimaryAmber
                    )
                }
            }

            // Question Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = currentQuestion.question,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                lineHeight = 26.sp
                            ),
                            color = Color.White
                        )
                    }
                }
            }

            // Options
            items(currentQuestion.options.size) { optIndex ->
                val optionText = currentQuestion.options[optIndex]
                val isSelected = selectedOption == optIndex
                val isCorrectAnswer = optIndex == currentQuestion.correctIndex

                val cardBg = when {
                    !isSubmitted && isSelected -> PrimaryAmber.copy(alpha = 0.2f)
                    isSubmitted && isCorrectAnswer -> AccentEmerald.copy(alpha = 0.25f)
                    isSubmitted && isSelected && !isCorrectAnswer -> AccentFlame.copy(alpha = 0.25f)
                    else -> SurfaceCard
                }

                val borderColor = when {
                    !isSubmitted && isSelected -> PrimaryAmber
                    isSubmitted && isCorrectAnswer -> AccentEmerald
                    isSubmitted && isSelected && !isCorrectAnswer -> AccentFlame
                    else -> Color.Transparent
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.5.dp, borderColor, RoundedCornerShape(16.dp))
                        .clickable(enabled = !isSubmitted) {
                            viewModel.selectQuizOption(optIndex)
                        }
                        .testTag("option_$optIndex"),
                    colors = CardDefaults.cardColors(containerColor = cardBg)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Letter / status icon
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isSubmitted && isCorrectAnswer -> AccentEmerald
                                        isSubmitted && isSelected && !isCorrectAnswer -> AccentFlame
                                        isSelected -> PrimaryAmber
                                        else -> Color(0xFF334155)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSubmitted && isCorrectAnswer) {
                                Icon(Icons.Default.Check, contentDescription = "صح", tint = Color.White, modifier = Modifier.size(18.dp))
                            } else if (isSubmitted && isSelected && !isCorrectAnswer) {
                                Icon(Icons.Default.Close, contentDescription = "غلط", tint = Color.White, modifier = Modifier.size(18.dp))
                            } else {
                                val char = when (optIndex) {
                                    0 -> "أ"
                                    1 -> "ب"
                                    2 -> "ج"
                                    else -> "د"
                                }
                                Text(
                                    text = char,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.Black else Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Text(
                            text = optionText,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 16.sp
                            ),
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Egyptian Commentary on Answer
            item {
                AnimatedVisibility(visible = isSubmitted && feedbackComment != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedOption == currentQuestion.correctIndex) Color(0xFF064E3B) else Color(0xFF450A0A)
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = feedbackComment ?: "",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                ),
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "💡 الشرح: ${currentQuestion.explanation}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFE2E8F0)
                            )
                        }
                    }
                }
            }

            // Action Button: Submit or Next
            item {
                Spacer(modifier = Modifier.height(8.dp))
                if (!isSubmitted) {
                    Button(
                        onClick = { viewModel.submitQuizAnswer() },
                        enabled = selectedOption != null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("submit_answer_button"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryAmber,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            text = "تأكيد الإجابة 🚀",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                } else {
                    Button(
                        onClick = { viewModel.nextQuizQuestion() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("next_question_button"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryAmber,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            text = "السؤال اللي بعده ➡️",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }

        // Quiz Completed Screen
        if (isQuizFinished) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "كأس",
                            tint = PrimaryAmber,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "خلصت كويز $selectedSubject! 🎉",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black),
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "جبت $score من أصل ${questions.size}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryAmber
                            )
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        val evaluation = when {
                            score >= 8 -> "دحيح تانية إعدادي المتوج يا $userName! ارفع راسك وسط أهلك والمستر هيديك الشيكولاتة! 🍫🎓"
                            score >= 5 -> "ماشي حالك يا $userName.. بس محتاج تفرم وتراجع النقط اللي غلطت فيها عشان الامتحان ميرحمش! ☕"
                            else -> "إيه الخيبة دي يا $userName؟! ده أداء واحد كان نايم طول الحصة! قوم افتح الملزمة حالا! 😭🚨"
                        }

                        Text(
                            text = evaluation,
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = { viewModel.restartQuiz() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimaryAmber,
                                contentColor = Color.Black
                            )
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = "إعادة")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("حل كويز تاني من الأول 🔄", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
