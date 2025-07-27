package com.example.chronos.ui.components.screens.login

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.chronos.R
import com.example.chronos.ui.theme.Shapes
import com.example.chronos.utils.Constants.LIST_SCREEN
import com.example.chronos.utils.Constants.LOGIN_SCREEN
import com.example.chronos.utils.Constants.WEB_CLIENT_ID
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun LoginScreen(navController: NavHostController) {

    val context = LocalContext.current
    val googleSignInOptions = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(WEB_CLIENT_ID)
            .requestEmail()
            .build()
    }

    val googleSignInClient = remember {
        GoogleSignIn.getClient(context, googleSignInOptions)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.result
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            Firebase.auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Google Sign-In Successful", Toast.LENGTH_SHORT)
                            .show()
                        navController.navigate(LIST_SCREEN) {
                            popUpTo(LOGIN_SCREEN) { inclusive = true }
                        }
                    } else {
                        Toast.makeText(context, "Google Sign-In Failed", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        } catch (e:Exception) {
            Toast.makeText(context, "Google Sign-In Failed: ${e.message}", Toast.LENGTH_SHORT)
                .show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 140.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .size(150.dp),
            painter = painterResource(id = R.drawable.ic_chronos_logo),
            contentDescription = "Logo"
        )

        Text(
            modifier = Modifier
                .padding(top = 20.dp, bottom = 60.dp),
            text = "Welcome to Chronos",
            style = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.Normal
            )
        )
//        GoogleSignInButton(
//            text = "Sign Up with Google",
//            loadingText = "Signing In...",
//            onClicked = {
//                val signInIntent = googleSignInClient.signInIntent
//                launcher.launch(signInIntent)
//            },
//            modifier = Modifier
//        )

        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            factory = {context ->
                SignInButton(context).apply {
                    setSize(SignInButton.SIZE_STANDARD)
                    setOnClickListener {
                        val signInIntent = googleSignInClient.signInIntent
                        launcher.launch(signInIntent)
                    }
                }
            }
        )
    }
}

@Composable
fun GoogleSignInButton(
    modifier: Modifier = Modifier,
    text: String = "",
    loadingText: String = "",
    onClicked: () -> Unit
) {
    var clicked by remember { mutableStateOf(false) }

    Surface(
        onClick = {clicked = !clicked},
        shape = Shapes.medium,
        border = BorderStroke(width = 1.dp,color = Color.LightGray),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row (
            modifier = modifier
                .padding(
                    start = 12.dp,
                    end = 16.dp,
                    top = 12.dp,
                    bottom = 12.dp
                )
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {

            Icon(
                painter = painterResource(id = R.drawable.ic_google_icon),
                contentDescription = "Google sign button",
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(text = if (clicked) loadingText else text)

            if (clicked){
                Spacer(modifier = Modifier.width(16.dp))
                CircularProgressIndicator(
                    modifier = Modifier
                        .height(16.dp)
                        .width(16.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                )
                onClicked()
            }
        }
    }
}