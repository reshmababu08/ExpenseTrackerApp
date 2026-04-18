package com.example.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.expensetracker.data.model.Expense
import com.example.expensetracker.ui.screen.AddExpenseScreen
import com.example.expensetracker.ui.screen.ExpenseScreen
import com.example.expensetracker.ui.screen.LoginScreen
import com.example.expensetracker.ui.screen.RegisterScreen
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme
import com.example.expensetracker.viewmodel.ExpenseViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensetracker.ui.auth.AuthViewModel
import com.example.expensetracker.ui.screen.SplashScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            val expenseViewModel: ExpenseViewModel = viewModel()
            val authViewModel: AuthViewModel = viewModel()

            var currentScreen by remember { mutableStateOf("splash") }
            var selectedExpense by remember { mutableStateOf<Expense?>(null) }

            ExpenseTrackerTheme {

                when (currentScreen) {

                    // 🔥 SPLASH SCREEN
                    "splash" -> {
                        SplashScreen(
                            onTimeout = {
                                currentScreen = "login"
                            }
                        )
                    }

                    // 🔥 LOGIN SCREEN (REAL AUTH)
                    "login" -> {
                        LoginScreen(
                            authViewModel = authViewModel,

                            onLoginSuccess = {
                                currentScreen = "expense"
                            },

                            onRegisterClick = {
                                currentScreen = "register"
                            }
                        )
                    }

                    // 🔥 REGISTER SCREEN
                    "register" -> {
                        RegisterScreen(
                            authViewModel = authViewModel,

                            onRegisterSuccess = {
                                currentScreen = "expense"
                            },

                            onBackToLoginClick = {
                                currentScreen = "login"
                            }
                        )
                    }

                    // 🔥 EXPENSE SCREEN
                    "expense" -> {
                        ExpenseScreen(
                            viewModel = expenseViewModel,
                            authViewModel = authViewModel,
                            onLogout = {
                                authViewModel.logout()
                                currentScreen = "login"
                            },
                            onAddExpenseClick = {
                                selectedExpense = null
                                currentScreen = "addExpense"
                            },
                            onEditExpenseClick = { expense ->
                                selectedExpense = expense
                                currentScreen = "addExpense"
                            },
                            onDeleteExpenseClick = { expense ->
                                expenseViewModel.deleteExpense(expense)
                            }
                        )
                    }

                    // 🔥 ADD EXPENSE SCREEN
                    "addExpense" -> {
                        AddExpenseScreen(
                            viewModel = expenseViewModel,
                            expenseToEdit = selectedExpense,
                            onSaveClick = {
                                selectedExpense = null
                                currentScreen = "expense"
                            }
                        )
                    }
                }
            }
        }
    }
}




