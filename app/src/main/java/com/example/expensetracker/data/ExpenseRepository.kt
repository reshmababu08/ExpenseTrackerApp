package com.example.expensetracker.data

import androidx.compose.runtime.mutableStateListOf
import com.example.expensetracker.data.model.Expense

object ExpenseRepository {
    val expenseList = mutableStateListOf<Expense>()

}