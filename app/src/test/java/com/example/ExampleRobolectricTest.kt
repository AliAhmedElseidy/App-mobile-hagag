package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
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
  fun `verify roast quotes are populated`() {
    val quotes = RoastQuotesRepository.quotes
    assertTrue(quotes.isNotEmpty())
    val firstQuote = quotes.first()
    assertNotNull(firstQuote.text)
    assertTrue(firstQuote.text.contains("حجاج"))
  }
}
