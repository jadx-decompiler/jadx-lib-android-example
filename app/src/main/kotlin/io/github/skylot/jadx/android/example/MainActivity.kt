package io.github.skylot.jadx.android.example

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import io.github.skylot.jadx.android.example.Decompile.DecompileMode
import io.github.skylot.jadx.android.example.Decompile.decompile

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                Column {
                    Decompile(
                        text = "Decompile MainActivity",
                        mode = DecompileMode.MAIN_ACTIVITY,
                        modifier = Modifier.padding(innerPadding)
                    )
                    Decompile(
                        text = "Decompile and save all",
                        mode = DecompileMode.SAVE_ALL,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Decompile(text: String, mode: DecompileMode, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var result: String by remember { mutableStateOf("") }
    Column {
        Button(onClick = {
            showMessage(context, message = "Decompiling...")
            result = decompile(context, mode)
        }) {
            Text(text)
        }
        if (result.isNotEmpty()) {
            Text(text = result, modifier = modifier)
        }
    }
}

fun showMessage(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

