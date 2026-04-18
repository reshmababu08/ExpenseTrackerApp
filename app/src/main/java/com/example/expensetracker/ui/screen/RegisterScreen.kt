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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensetracker.ui.auth.AuthViewModel
import com.example.expensetracker.ui.components.CommonTopBar
import com.example.expensetracker.ui.theme.AppBackground
import com.example.expensetracker.ui.theme.ButtonGreen
import com.example.expensetracker.ui.theme.DarkGreen

@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onBackToLoginClick: () -> Unit
) {

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var errorMsg by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {

        CommonTopBar(title = "Register")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (errorMsg.isNotEmpty()) {
                Text(errorMsg, color = Color.Red)
                Spacer(modifier = Modifier.height(8.dp))
            }
            if (authViewModel.isLoading.value) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
            }

            Button(
                onClick = {

                    if (email.isBlank() || password.length < 6) {
                        errorMsg = "Enter valid email & password (6+ chars)"
                        return@Button
                    }

                    authViewModel.register(
                        email,
                        password,
                        onSuccess = onRegisterSuccess,
                        onError = { errorMsg = it }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ButtonGreen,
                    contentColor = Color.White
                )
            ) {
                Text("REGISTER")
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onBackToLoginClick) {
                Text("Back to Login", color = DarkGreen)
            }
        }
    }
}