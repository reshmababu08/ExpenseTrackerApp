package com.example.expensetracker.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toString
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.expensetracker.data.ExpenseRepository
import com.example.expensetracker.data.model.Expense
import com.example.expensetracker.ui.components.CommonTopBar
import com.example.expensetracker.ui.theme.AppBackground
import com.example.expensetracker.ui.theme.ButtonGreen
import com.example.expensetracker.viewmodel.ExpenseViewModel

@Composable
fun AddExpenseScreen(
    viewModel: ExpenseViewModel,
    expenseToEdit: Expense?,
    onSaveClick: () -> Unit
) {

    var title by remember { mutableStateOf(expenseToEdit?.title?: "") }
    var amount by remember { mutableStateOf(expenseToEdit?.amount?.toString()?:"") }
    var category by remember { mutableStateOf(expenseToEdit?.category?:"") }

    var titleError by remember { mutableStateOf("") }
    var amountError by remember { mutableStateOf("") }
    var categoryError by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ){
        CommonTopBar(
            title = if(expenseToEdit == null)"Add Expense" else "Edit Expense" )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {

            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    titleError = ""
                },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth(),
                isError = titleError.isNotEmpty()

            )
            if (titleError.isNotEmpty()) {
                Text(text = titleError, color = Color.Red)
            }
            Spacer(modifier = Modifier.height(16.dp))


            OutlinedTextField(
                value = amount,
                onValueChange = {
                    amount = it
                    amountError = ""
                },
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth(),
                isError = amountError.isNotEmpty()

            )
            if (amountError.isNotEmpty()) {
                Text(text = amountError, color = Color.Red)
            }
            Spacer(modifier = Modifier.height(16.dp))


            OutlinedTextField(
                value = category,
                onValueChange = {
                    category = it
                    categoryError = ""
                },
                label = { Text("Category") },
                modifier = Modifier.fillMaxWidth(),
                isError = categoryError.isNotEmpty()
            )
            if (categoryError.isNotEmpty()) {
                Text(text = categoryError, color = Color.Red)
            }
            Spacer(modifier = Modifier.height(24.dp))


            Button(
                onClick = {

                    var isValid = true

                    // TITLE VALIDATION
                    if (title.isBlank()) {
                        titleError = "Title cannot be empty"
                        isValid = false
                    } else {
                        titleError = ""
                    }

                    // AMOUNT VALIDATION
                    val amountValue = amount.toDoubleOrNull()
                    if (amount.isBlank()) {
                        amountError = "Amount cannot be empty"
                        isValid = false
                    } else if (amountValue == null || amountValue <= 0) {
                        amountError = "Enter valid amount"
                        isValid = false
                    } else {
                        amountError = ""
                    }

                    // CATEGORY VALIDATION
                    if (category.isBlank()) {
                        categoryError = "Category cannot be empty"
                        isValid = false
                    } else {
                        categoryError = ""
                    }

                    // SAVE ONLY IF VALID
                    if (isValid) {

                        if (expenseToEdit == null) {
                            val newExpense = Expense(
                                id = System.currentTimeMillis().toString(),
                                title = title,
                                amount = amountValue ?: 0.0,
                                category = category
                            )
                            viewModel.addExpense(newExpense)

                        } else {
                            val updatedExpense = expenseToEdit.copy(
                                title = title,
                                amount = amountValue ?: 0.0,
                                category = category
                            )
                            viewModel.updateExpense(updatedExpense)
                        }

                        onSaveClick()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ButtonGreen,
                    contentColor = Color.White
                )
            ) {
                Text("SAVE")
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun AddExpenseScreenPreview(){
//    AddExpenseScreen(onSaveClick = {}, expenseToEdit = )
//}
