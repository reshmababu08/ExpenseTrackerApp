package com.example.expensetracker.ui.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.R
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensetracker.ui.auth.AuthViewModel
import com.example.expensetracker.ui.components.CommonTopBar
import com.example.expensetracker.ui.theme.AppBackground
import com.example.expensetracker.ui.theme.ButtonGreen
import com.example.expensetracker.ui.theme.DarkGreen
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->

        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken

            if (idToken != null) {
                authViewModel.signInWithGoogle(
                    idToken,
                    onSuccess = onLoginSuccess,
                    onError = { errorMsg = it }
                )
            }

        } catch (e: Exception) {
            errorMsg = e.message ?: "Google sign-in failed"
        }
    }

    val googleSignInClient = remember {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("930236194459-35m707s2janf998mp440ld7hn6sd4pmi.apps.googleusercontent.com")
            .requestEmail()
            .build()

        GoogleSignIn.getClient(context, gso)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {

        CommonTopBar(title = "Login")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {

            // EMAIL
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                isError = emailError.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )

            if (emailError.isNotEmpty()) Text(emailError, color = Color.Red)

            Spacer(modifier = Modifier.height(16.dp))

            // PASSWORD
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                isError = passwordError.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )

            if (passwordError.isNotEmpty()) Text(passwordError, color = Color.Red)

            Spacer(modifier = Modifier.height(24.dp))

            if (errorMsg.isNotEmpty()) {
                Text(errorMsg, color = Color.Red)
                Spacer(modifier = Modifier.height(8.dp))
            }
            if (authViewModel.isLoading.value) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
            }

            // LOGIN BUTTON
            Button(
                onClick = {

                    emailError = ""
                    passwordError = ""
                    errorMsg = ""

                    var valid = true

                    if (email.isBlank()) {
                        emailError = "Email required"
                        valid = false
                    }

                    if (password.length < 6) {
                        passwordError = "Password must be 6+ chars"
                        valid = false
                    }

                    if (!valid) return@Button

                    authViewModel.login(
                        email,
                        password,
                        onSuccess = onLoginSuccess,
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
                Text("LOGIN")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔥 GOOGLE LOGIN BUTTON (NEW)
            Button(
                onClick = {
                    launcher.launch(googleSignInClient.signInIntent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text("Continue with Google")
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onRegisterClick) {
                Text("Create Account", color = DarkGreen)
            }
        }
    }
}