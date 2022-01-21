package com.helloanwar.vendure.ui.auth

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.github.ajalt.timberkt.Timber
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.helloanwar.vendure.GoogleAuthenticateMutation
import com.helloanwar.vendure.NativeAuthenticateMutation
import com.helloanwar.vendure.R
import com.helloanwar.vendure.VendureScreen
import com.helloanwar.vendure.ui.auth.components.ContinueButtonSection
import com.helloanwar.vendure.ui.auth.components.RedirectSection
import com.helloanwar.vendure.ui.auth.components.SignupTextField
import com.helloanwar.vendure.ui.auth.components.TitleSection
import com.helloanwar.vendure.ui.auth.data.textWhite

@Composable
fun LoginScreen(
    navController: NavController? = null,
    authViewModel: AuthViewModel? = null,
    scaffoldState: ScaffoldState? = null
) {
    // Configure Google Sign In
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(stringResource(R.string.google_web_client_id))
        .requestEmail()
        .build()

    val context = LocalContext.current

    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    val startForResult = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            //do something here
            val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                Timber.d { "firebaseAuthWithGoogle:" + account.id }
                authViewModel?.googleLogin(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Timber.e(e)
            }
        }
    }

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val loading = remember { mutableStateOf(false) }

    val authUiState = authViewModel?.authUiState?.observeAsState()
    loading.value = authUiState?.value is AuthUiState.Loading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            TitleSection("Welcome\nBack", "Login to your account using email")
        }

        when (authUiState?.value) {
            is AuthUiState.Error -> {
                Text(text = "Error")
            }
            AuthUiState.Loading -> {

            }
            is AuthUiState.Success -> {
                when (val data = (authUiState.value as AuthUiState.Success).data) {
                    is GoogleAuthenticateMutation.Data -> {
                        data.authenticate.onCurrentUser?.let {
                            println("success => ${it.id}, ${it.identifier}")
                            navController?.navigateUp()
                        }
                    }
                    is NativeAuthenticateMutation.Data -> {
                        data.login.onCurrentUser?.let {
                            println("success => ${it.id}, ${it.identifier}")
                            navController?.navigateUp()
                        }
                    }
                }
            }
            null -> {}
        }

        LoginSection("Email address", "Password", email, password)
        ContinueButtonSection(
            text = "Login",
            icon = R.drawable.ic_round_arrow_forward,
            iconDescription = null,
            loading = loading
        ) {
            //native login
            //TODO:check validation
            authViewModel?.login(userName = email.value, password = password.value)
        }
        OtherOptionSection {
            startForResult.launch(googleSignInClient.signInIntent)
        }
        RedirectSection("Sign up", true, onForget = {

        }) {
            navController?.navigate(VendureScreen.SignUpScreen.name)
        }
    }
}

@Composable
fun LoginSection(
    hintEmail: String,
    hintPassword: String,
    email: MutableState<String>,
    password: MutableState<String>
) {
    Column {
        SignupTextField(email.value, { email.value = it }, "", hintEmail)
        Spacer(modifier = Modifier.height(12.dp))
        SignupTextField(password.value, { password.value = it }, "", hintPassword)
    }
}

@Composable
fun OtherOptionSection(
    onGoogleSignIn: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        OptionItem(R.drawable.google, null) {
            onGoogleSignIn()
        }
    }
}

@Composable
fun OptionItem(
    icon: Int,
    iconDescription: String?,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .border(2.dp, textWhite, CircleShape)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = iconDescription,
                modifier = Modifier.size(32.dp)
            )
        }
        Text(text = "Sign in with Google")
    }
}

@Preview
@Composable
fun LoginPreview() {
    LoginScreen()
}