package com.shlok.tracker.data.repository

import com.shlok.tracker.data.local.Subscription
import com.shlok.tracker.data.local.SubscriptionDao
import com.shlok.tracker.data.local.Transaction
import com.shlok.tracker.data.local.TransactionDao
import com.shlok.tracker.domain.repository.TrackerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackerRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
    private val subscriptionDao: SubscriptionDao
) : TrackerRepository {

    override suspend fun insertTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction)
    }

    override fun getAllTransactions(): Flow<List<Transaction>> {
        return transactionDao.getAllTransactions()
    }

    override fun getTransactionsForSubscription(subscriptionId: Long): Flow<List<Transaction>> {
        return transactionDao.getTransactionsForSubscription(subscriptionId)
    }

    override fun getSubscriptionCandidates(): Flow<List<Transaction>> {
        return transactionDao.getSubscriptionCandidates()
    }

    override suspend fun insertSubscription(subscription: Subscription): Long {
        return subscriptionDao.insertSubscription(subscription)
    }

    override suspend fun updateSubscription(subscription: Subscription) {
        subscriptionDao.updateSubscription(subscription)
    }

    override fun getActiveSubscriptions(): Flow<List<Subscription>> {
        return subscriptionDao.getActiveSubscriptions()
    }

    override suspend fun getUpcomingSubscriptions(startTime: Long, endTime: Long): List<Subscription> {
        return subscriptionDao.getUpcomingSubscriptions(startTime, endTime)
    }

    override suspend fun deleteSubscription(id: Long) {
        subscriptionDao.deleteSubscriptionById(id)
    }

    override suspend fun deleteDuplicateSubscriptions() {
        subscriptionDao.deleteDuplicateSubscriptions()
    }
}
