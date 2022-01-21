package com.helloanwar.vendure.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.helloanwar.vendure.R
import com.helloanwar.vendure.RegisterNativeMutation
import com.helloanwar.vendure.ui.auth.components.ContinueButtonSection
import com.helloanwar.vendure.ui.auth.components.RedirectSection
import com.helloanwar.vendure.ui.auth.components.SignupTextField
import com.helloanwar.vendure.ui.auth.components.TitleSection

@Composable
fun SignupScreen(
    navController: NavController? = null,
    authViewModel: AuthViewModel? = null,
    scaffoldState: ScaffoldState? = null
) {

    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    val registerUiState = authViewModel?.createCustomerState?.observeAsState()

    val loading = remember { mutableStateOf(false) }
    loading.value = registerUiState?.value is AuthUiState.Loading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            /*BackButton(R.drawable.ic_round_arrow_back, null) {
                navController?.navigateUp()
            }
            Spacer(modifier = Modifier.height(32.dp))*/
            TitleSection("Welcome", "Lets get started by creating your account with email")
        }

        when (registerUiState?.value) {
            is AuthUiState.Error -> {
                Text(text = "Error")
            }
            AuthUiState.Loading -> {
                CircularProgressIndicator()
            }
            is AuthUiState.Success -> {
                val data = (registerUiState.value as AuthUiState.Success)
                    .data as RegisterNativeMutation.Data
                data.registerCustomerAccount.onSuccess?.let {
                    if (it.success) {
                        //redirect to home
                        Text(text = "Registered successfully")
                    }
                }
                data.registerCustomerAccount.onErrorResult?.let {
                    Text(text = it.message)
                }
                data.registerCustomerAccount.onNativeAuthStrategyError?.let {
                    Text(text = it.message)
                }
                data.registerCustomerAccount.onMissingPasswordError?.let {
                    Text(text = it.message)
                }
            }
            null -> {

            }
        }

        SignupSection("Name", "Email address", "Password", name, email, password)
        ContinueButtonSection(
            text = "Sign up",
            icon = R.drawable.ic_round_arrow_forward,
            iconDescription = null,
            loading = loading
        ) {
            //signup using email
            authViewModel?.createCustomer(
                firstName = name.value,
                email = email.value,
                password = password.value
            )
        }
        RedirectSection("Login", onForget = {}) {
            navController?.navigateUp()
        }
    }
}

@Composable
fun SignupSection(
    hintName: String,
    hintEmail: String,
    hintPassword: String,
    name: MutableState<String>,
    email: MutableState<String>,
    password: MutableState<String>
) {
    Column {
        SignupTextField(name.value, { name.value = it }, "", hintName)
        Spacer(modifier = Modifier.height(12.dp))
        SignupTextField(email.value, { email.value = it }, "", hintEmail)
        Spacer(modifier = Modifier.height(12.dp))
        SignupTextField(password.value, { password.value = it }, "", hintPassword)
    }
}

@Preview
@Composable
fun Preview() {
    SignupScreen()
}
