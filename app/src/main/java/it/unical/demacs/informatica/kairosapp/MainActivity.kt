package it.unical.demacs.informatica.kairosapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.swagger.client.apis.AuthenticationApi
import io.swagger.client.models.PasswordResetRequest
import it.unical.demacs.informatica.kairosapp.ui.theme.KairosAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.wait

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // FIXME Just testing
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = AuthenticationApi()
                val request = PasswordResetRequest(
                    usernameOrEmail = "sideroalessandro@gmail.com",
                )
                val result = api.forgotPassword(request)
                Log.d("ForgotPassword", "Success: $result")
            } catch (e: Exception) {
                Log.e("ForgotPassword", "Errore nella richiesta: ${e.message}")
            }
        }

        setContent {
            KairosAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = stringResource(R.string.app_name),
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KairosAppTheme {
        Greeting("Android")
    }
}
