package com.shlok.tracker.receiver

import org.junit.Assert.*
import org.junit.Test

class SmsParserTest {

    @Test
    fun `parseBankSms with valid INR amount and keyword returns correct data`() {
        val sms = "Your account was debited INR 499.00 for your Netflix subscription."
        val result = SmsParser.parseBankSms(sms)

        assertNotNull(result)
        assertEquals(499.0, result?.amount ?: 0.0, 0.0)
        assertEquals("INR", result?.currency)
        assertTrue(result?.isSubscriptionCandidate == true)
    }

    @Test
    fun `parseBankSms with generic non-banking message returns null`() {
        val sms = "Hey, are we still meeting for lunch at 1 PM?"
        val result = SmsParser.parseBankSms(sms)

        assertNull(result)
    }

    @Test
    fun `parseBankSms with USD currency returns correct currency`() {
        val sms = "You spent $12.99 on your monthly recurring mandate."
        val result = SmsParser.parseBankSms(sms)

        assertNotNull(result)
        assertEquals(12.99, result?.amount ?: 0.0, 0.0)
        assertEquals("USD", result?.currency)
        assertTrue(result?.isSubscriptionCandidate == true)
    }
}
