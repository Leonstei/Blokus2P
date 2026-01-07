package com.example.blokus2p

import android.util.Log
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.blokus2p.ui.bockusScreen.BlockusBoard
import com.example.blokus2p.ui.bockusScreen.BlockusScreen
import com.example.blokus2p.ui.bockusScreen.ZoomableBlockusBoard
import com.example.blokus2p.ui.theme.Blokus2PTheme
import com.example.blokus2p.viewModel.AppViewModel

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<AppViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AppViewModel() as T
                }
            }
        }

    )
    companion object {
        // Lädt die native Library (wird einmal beim Klassenladen ausgeführt)
        init {
            try {
                System.loadLibrary("native-lib")
                Log.d("NDK", "Native Library 'native-lib' erfolgreich geladen")
            } catch (e: UnsatisfiedLinkError) {
                Log.e("NDK", "Fehler beim Laden der native-lib", e)
            }
        }
    }
    // Deklaration der nativen Testfunktion
    external fun doubleNumber(number: Int): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Blokus2PTheme {
                // Hier rufen wir den NDK-Test genau EINMAL beim ersten Compose auf
                LaunchedEffect(Unit) {
                    testNativeFunction()
                }

                // Dein originaler Screen – bleibt unverändert!
                BlockusScreen()
            }
        }
    }

    // Separate Funktion für den Test – leicht zu finden und später zu entfernen
    private fun testNativeFunction() {
        try {
            val input = 21
            val result = doubleNumber(input)
            Log.d("NDK_TEST", "🎉 NDK-Test erfolgreich! doubleNumber($input) = $result")
            Log.d("NDK_TEST", "Erwartet: ${input * 2} | Erhalten: $result")
            if (result == input * 2) {
                Log.d("NDK_TEST", "✅ Native Integration funktioniert perfekt!")
            } else {
                Log.w("NDK_TEST", "⚠️ Ergebnis unerwartet")
            }
        } catch (e: UnsatisfiedLinkError) {
            Log.e("NDK_TEST", "❌ Native Funktion nicht gefunden", e)
        } catch (e: Exception) {
            Log.e("NDK_TEST", "❌ Unerwarteter Fehler beim Aufruf", e)
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




