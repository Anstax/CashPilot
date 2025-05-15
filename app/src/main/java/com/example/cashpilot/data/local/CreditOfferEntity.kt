package com.example.cashpilot.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "credit_offer")
data class CreditOfferEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val bankName: String,
    val minAmount:  String,
    val term:  String,
    val creditType: String
)