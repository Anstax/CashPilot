package com.example.cashpilot.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CreditOfferDao {

    @Query("SELECT * FROM credit_offer")
    fun getAll(): Flow<List<CreditOfferEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(offers: List<CreditOfferEntity>)

    @Query("DELETE FROM credit_offer")
    suspend fun clearAll()
}
