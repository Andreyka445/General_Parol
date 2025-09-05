package com.example.generalparol

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.generalparol.ui.theme.GeneralParolTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GeneralParolTheme {
                PasswordGeneratorApp()
            }
        }
    }
}

@Composable
fun PasswordGeneratorApp() {
    var currentLanguage by remember { mutableStateOf("RU") }
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        // Фон
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "фон",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // кнопка выбор языка
        Box(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopEnd)
                .clickable {
                    currentLanguage = if (currentLanguage == "RU") "EN" else "RU"
                }
                .background(Color.Black.copy(alpha = 0.8f), CircleShape)
                .size(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = currentLanguage,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        // рснрва
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            PasswordGeneratorScreen(currentLanguage = currentLanguage)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordGeneratorScreen(currentLanguage: String, modifier: Modifier = Modifier) {
    var password by remember { mutableStateOf("") }
    var passwordLength by remember { mutableStateOf(12) }
    var isExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val passwordLengths = listOf(4, 6, 8, 12, 16, 20, 24, 32)

    // тексты
    val texts = if (currentLanguage == "RU") {
        mapOf(
            "title" to "прпр от генерала пароля!",
            "length" to "длинна:",
            "hint" to "тут пароль",
            "generate" to "сделать пароль",
            "copy" to "ctrl+c",
            "copied" to "скопировал",
            "generate_first" to "сначала пароль брад",
            "made_by" to "создано VeroX prjct:",
            "telegram_error" to "лох"
        )
    } else {
        mapOf(
            "title" to "hi from general password!",
            "length" to "length:",
            "hint" to "your pass will be here",
            "generate" to "generate",
            "copy" to "ctrl+c",
            "copied" to "copied",
            "generate_first" to "first you need to generate password",
            "made_by" to "made by VeroX gacor prjct:",
            "telegram_error" to "fail"
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        // ZZZZZZZZZZZZZZZZZZZZZZZZZZagalovok
        Text(
            text = texts["title"] ?: "",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Red,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // vybor dlinny
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 13.dp)
        ) {
            Text(
                text = texts["length"] ?: "",
                fontSize = 14.sp,
                color = Color.Red,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { isExpanded = it }
            ) {
                TextField(
                    value = passwordLength.toString(),
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .width(90.dp)
                )

                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }
                ) {
                    passwordLengths.forEach { length ->
                        DropdownMenuItem(
                            text = { Text(text = length.toString()) },
                            onClick = {
                                passwordLength = length
                                isExpanded = false
                            }
                        )
                    }
                }
            }
        }

        // pole for pass
        SelectionContainer {
            Text(
                text = password.ifEmpty { texts["hint"] ?: "" },
                fontSize = 18.sp,
                color = Color.Red,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    password = generateRandomPassword(passwordLength)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = texts["generate"] ?: "")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if (password.isNotEmpty()) {
                        val clipboard = ContextCompat.getSystemService(context, ClipboardManager::class.java)
                        clipboard?.setPrimaryClip(ClipData.newPlainText("password", password))
                        Toast.makeText(context, texts["copied"], Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, texts["generate_first"], Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = texts["copy"] ?: "")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // тг линк
        Text(
            text = texts["made_by"] ?: "",
            fontSize = 12.sp,
            color = Color.Red,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = "https://t.me/Andreyka445real",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.primary,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .clickable {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/Andreyka445real"))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, texts["loh"], Toast.LENGTH_SHORT).show()
                    }
                }
        )
    }
}

fun generateRandomPassword(length: Int): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_+=*()?&:0"
    val random = java.util.Random()
    val sb = StringBuilder(length)

    for (i in 0 until length) {
        sb.append(chars[random.nextInt(chars.length)])
    }

    return sb.toString()
}