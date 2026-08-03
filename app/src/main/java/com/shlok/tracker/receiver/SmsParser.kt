package com.shlok.tracker.receiver

object SmsParser {
    private val amountRegex = Regex("""(?:Rs\.?|INR|\$)\s*([\d,]+\.?\d*)|([\d,]+\.?\d*)\s*(?:INR|USD)""", RegexOption.IGNORE_CASE)
    
    private val subscriptionKeywords = listOf(
        "autopay", "mandate", "recurring", "subscription", "auto-debit", "scheduled"
    )

    data class ParsedData(
        val amount: Double,
        val currency: String,
        val isSubscriptionCandidate: Boolean
    )

    fun parseBankSms(smsBody: String): ParsedData? {
        val lowerCaseBody = smsBody.lowercase()
        
        if (!lowerCaseBody.contains("debited") && !lowerCaseBody.contains("spent") && !lowerCaseBody.contains("paid")) {
            return null
        }

        val matchResult = amountRegex.find(smsBody)
        var amount = 0.0
        var currency = "INR"

        if (matchResult != null) {
            val amountStr = (matchResult.groups[1]?.value ?: matchResult.groups[2]?.value)?.replace(",", "")
            amount = amountStr?.toDoubleOrNull() ?: 0.0
            
            if (matchResult.value.contains("$", ignoreCase = true) || matchResult.value.contains("USD", ignoreCase = true)) {
                currency = "USD"
            }
        } else {
            return null
        }

        val isSubscriptionCandidate = subscriptionKeywords.any { lowerCaseBody.contains(it) }

        return ParsedData(amount, currency, isSubscriptionCandidate)
    }
}
