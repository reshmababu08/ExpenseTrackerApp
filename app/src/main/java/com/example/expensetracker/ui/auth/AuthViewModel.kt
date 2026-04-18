package com.example.expensetracker.ui.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    var isLoading = mutableStateOf(false)
        private set

    var errorMessage = mutableStateOf("")

    fun signInWithGoogle(
        idToken: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onError(task.exception?.message ?: "Google login failed")
                }
            }
    }

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        isLoading.value = true

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                isLoading.value = false

                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onError(task.exception?.message ?: "Login failed")
                }
            }
    }

    fun register(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        isLoading.value = true

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                isLoading.value = false

                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onError(task.exception?.message ?: "Registration failed")
                }
            }
    }
    fun logout(){
        auth.signOut()
    }
}