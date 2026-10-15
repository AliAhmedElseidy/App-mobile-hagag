package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.QuoteCategory
import com.example.data.QuizRepository
import com.example.data.RoastQuotesRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("ذاكر يا حجاج", appName)
  }

  @Test
  fun `verify roast quotes has at least 60 quotes and covers all categories`() {
    val quotes = RoastQuotesRepository.quotes
    assertTrue("Quotes count must be at least 60, found ${quotes.size}", quotes.size >= 60)

    val categories = quotes.map { it.category }.toSet()
    assertTrue(categories.contains(QuoteCategory.START_SESSION))
    assertTrue(categories.contains(QuoteCategory.MID_SESSION))
    assertTrue(categories.contains(QuoteCategory.POST_SUCCESS))
    assertTrue(categories.contains(QuoteCategory.ON_FAILURE))
    assertTrue(categories.contains(QuoteCategory.STREAK_BROKEN))

    quotes.forEach { quote ->
      assertNotNull(quote.text)
      assertNotNull(quote.spokenText)
      assertNotNull(quote.punchline)
    }
  }

  @Test
  fun `verify quiz repository has 10 questions per subject for second prep`() {
    val subjects = listOf("رياضيات", "علوم", "لغة عربية", "إنجليزي")
    subjects.forEach { subject ->
      val list = QuizRepository.getQuestionsBySubject(subject)
      assertEquals("Subject $subject should have 10 questions", 10, list.size)
      list.forEach { q ->
        assertEquals(4, q.options.size)
        assertTrue(q.correctIndex in 0..3)
        assertTrue(q.correctEgyptianComment.isNotEmpty())
        assertTrue(q.wrongEgyptianComment.isNotEmpty())
      }
    }
  }

  @Test
  fun `verify rewards repository initialization and claiming`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repo = com.example.data.TaskRepository(context)
    val rewards = repo.rewards.value
    assertTrue("Rewards should have default rewards loaded", rewards.isNotEmpty())
    val gameReward = rewards.find { it.title.contains("لعب") || it.emoji == "🎮" }
    assertNotNull("Gaming reward should exist", gameReward)
    
    val rewardId = rewards.first().id
    repo.claimReward(rewardId)
    val claimedReward = repo.rewards.value.find { it.id == rewardId }
    assertTrue(claimedReward?.isClaimed == true)
  }

  @Test
  fun `verify GeminiChatService provides Egyptian study responses offline`() {
    val mathResponse = com.example.ai.GeminiChatService.getOfflineSmartResponse("مسألة رياضيات في الجذر التكعيبي", "حجاج")
    assertTrue(mathResponse.contains("حجاج"))
    assertTrue(mathResponse.contains("رياضيات") || mathResponse.contains("الجذر"))

    val scienceResponse = com.example.ai.GeminiChatService.getOfflineSmartResponse("جدول مندليف والعلوم", "حجاج")
    assertTrue(scienceResponse.contains("حجاج"))
    assertTrue(scienceResponse.contains("مندليف") || scienceResponse.contains("العلوم"))

    val motivationResponse = com.example.ai.GeminiChatService.getOfflineSmartResponse("أنا كسلان ومش قادر أذاكر", "حجاج")
    assertTrue(motivationResponse.contains("حجاج"))
  }
}
