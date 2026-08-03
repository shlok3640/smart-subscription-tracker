package com.shlok.tracker.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import com.shlok.tracker.data.local.Transaction
import com.shlok.tracker.domain.repository.TrackerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SmsReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repository: TrackerRepository

    private val receiverScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) return

        val pendingResult = goAsync()
        
        receiverScope.launch {
            try {
                val smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
                
                for (sms in smsMessages) {
                    val body = sms.displayMessageBody ?: continue
                    val sender = sms.displayOriginatingAddress ?: "Unknown"
                    val timestamp = sms.timestampMillis

                    val parsedData = SmsParser.parseBankSms(body)
                    
                    if (parsedData != null) {
                        val transaction = Transaction(
                            senderAddress = sender,
                            rawSmsBody = body,
                            amount = parsedData.amount,
                            currency = parsedData.currency,
                            timestamp = timestamp,
                            isSubscriptionCandidate = parsedData.isSubscriptionCandidate
                        )
                        repository.insertTransaction(transaction)
                    }
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
