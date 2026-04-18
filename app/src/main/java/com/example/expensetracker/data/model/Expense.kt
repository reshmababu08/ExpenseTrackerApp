package com.example.expensetracker.data.model

import android.R

data class Expense(
    val id: String = "",
    val title: String = "",
    val amount: Double = 0.0,
    val category: String = "",
    val date: String = "",
    val userId: String = "",
    )
