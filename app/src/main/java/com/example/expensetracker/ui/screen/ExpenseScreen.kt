package com.example.expensetracker.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensetracker.data.ExpenseRepository
import com.example.expensetracker.data.ExpenseRepository.expenseList
import com.example.expensetracker.data.model.Expense
import com.example.expensetracker.ui.auth.AuthViewModel
import com.example.expensetracker.ui.components.CommonTopBar
import com.example.expensetracker.ui.theme.AppBackground
import com.example.expensetracker.ui.theme.ButtonGreen
import com.example.expensetracker.ui.theme.CategoryHeaderBackground
import com.example.expensetracker.ui.theme.CategoryHeaderColor
import com.example.expensetracker.ui.theme.TotalAmountColor
import com.example.expensetracker.ui.theme.TotalBoxBackground
import com.example.expensetracker.viewmodel.ExpenseViewModel
import kotlin.math.exp

@Composable
fun ExpenseScreen(
    viewModel: ExpenseViewModel,
    authViewModel: AuthViewModel,
    onLogout:() -> Unit,
    onAddExpenseClick: () -> Unit,
    onEditExpenseClick: (Expense) -> Unit,
    onDeleteExpenseClick: (Expense) -> Unit
) {

    val expenses = viewModel.expenses
    val groupedExpenses = expenses.groupBy { it.category }
    val totalAmount = expenses.sumOf { it.amount }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            CommonTopBar(title = "Expenses",
                onLogoutClick = {
                    authViewModel.logout()
                    onLogout()
                })

            Button(
                onClick = {
                    authViewModel.logout()
                    onLogout()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                )
            ) {
                Text("Logout")
            }
        }

        if (authViewModel.isLoading.value) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {



            Column(
                modifier = Modifier
                    .shadow(4.dp, shape = MaterialTheme.shapes.medium)

                    .background(
                        TotalBoxBackground,
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(horizontal = 16.dp, vertical = 10.dp),

                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "TOTAL",
                    style = MaterialTheme.typography.labelSmall,
                    color = TotalAmountColor
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "₹ ${"%.2f".format(totalAmount)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TotalAmountColor
                )
            }
        }



        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onAddExpenseClick() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = ButtonGreen,
                contentColor = Color.White
            )
        ) {
            Text("Add Expense")
        }
        Spacer(modifier = Modifier.height(16.dp))
        val groupedExpenses = viewModel.expenses.groupBy { it.category }



        LazyColumn {
            groupedExpenses.forEach { (category, categoryExpenses) ->
                val categoryTotal = categoryExpenses.sumOf { it.amount }

                item {
                    Column {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(CategoryHeaderBackground)
                                .padding(14.dp),

                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = category.uppercase(),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = CategoryHeaderColor
                            )
                            Text(
                                text = "₹ ${"%.2f".format(categoryTotal)}",
                                fontWeight = FontWeight.Bold,
                                color = CategoryHeaderColor
                            )
                        }
                        Divider()
                    }
                }

                items(categoryExpenses) { expense ->
                    ExpenseItem(
                        expense = expense,
                        onEditClick = { expenseToEdit ->
                            onEditExpenseClick(expenseToEdit)
                        },
                        onDeleteExpenseClick = { expenseToDelete ->
                            onDeleteExpenseClick(expenseToDelete)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun ExpenseItem(
    expense: Expense,
    onEditClick: (Expense) -> Unit,
    onDeleteExpenseClick: (Expense) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding( 8.dp)
                .background(Color.White, shape = MaterialTheme.shapes.medium)
                .padding(12.dp),

            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,

            ) {
            Column(modifier = Modifier.weight(1f))
            {
                Text(
                    text = expense.title,
                    style = MaterialTheme.typography.titleSmall
                )

            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${"%.2f".format(expense.amount)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(8.dp))

                Box {
                    IconButton(
                        onClick = {
                            expanded = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options"
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text("Edit")
                            },
                            onClick = {
                                expanded = false
                                onEditClick(expense)
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text("Delete")
                            },
                            onClick = {
                                expanded = false
                                onDeleteExpenseClick(expense)
                            }
                        )
                    }
                }
            }
        }
        Divider(
            color = Color.LightGray.copy(alpha = 0.3f),
            thickness = 1.dp
        )
    }
}



//@Preview(showBackground = true)
//@Composable
//fun ExpenseScreenPreview(){
//    ExpenseScreen(onAddExpenseClick = {})
//}