package com.example.expensetracker.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.expensetracker.data.model.Expense

class ExpenseViewModel : ViewModel() {
    private val _expenses = mutableStateListOf<Expense>()
    val expenses: List<Expense> = _expenses
    fun addExpense(expense: Expense) {
        _expenses.add(expense)
    }

    fun deleteExpense(expense: Expense) {
        _expenses.remove(expense)
    }

    fun updateExpense(updatedExpense: Expense) {

        val index = _expenses.indexOfFirst {
            it.id == updatedExpense.id
        }

        if (index != -1) {
            _expenses[index] = updatedExpense
        }
    }
}
