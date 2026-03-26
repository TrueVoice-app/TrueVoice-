package com.truevoice.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.compose.foundation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    // ✅ MODERN PERMISSION HANDLER
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                // Permission denied → app may not show contact names
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ REQUEST CONTACT PERMISSION HERE
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }

        setContent {
            TrueVoiceApp()
        }
    }
}

enum class AppState { IDLE, RINGING, RESULT }

@Composable
fun TrueVoiceApp() {

    var state by remember { mutableStateOf(AppState.IDLE) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(16.dp)
    ) {

        Column {

            AppHeader()

            Spacer(modifier = Modifier.height(16.dp))

            when (state) {

                AppState.IDLE -> IdleScreen {
                    scope.launch {
                        state = AppState.RINGING
                        delay(1500)
                        state = AppState.RESULT
                    }
                }

                AppState.RINGING -> RingingScreen()

                AppState.RESULT -> ResultScreen {
                    state = AppState.IDLE
                }
            }
        }
    }
}

@Composable
fun AppHeader() {
    Row(verticalAlignment = Alignment.CenterVertically) {

        Box(
            modifier = Modifier
                .size(44.dp)
                .background(Color(0xFF2563EB), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("📞", fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text("TrueVoice", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
            Text("Caller Protection", fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun IdleScreen(onClick: () -> Unit) {

    Column {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(16.dp))
                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
                .padding(20.dp)
        ) {

            Column {

                Text("You're Protected", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)

                Spacer(modifier = Modifier.height(6.dp))

                Text("Monitoring incoming calls in real-time", fontSize = 13.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SimpleStat("Blocked", "24")
                    SimpleStat("Spam", "12")
                    SimpleStat("Safe", "89")
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        ActionRow("📞 Simulate Call", "Test detection system", onClick)
        ActionRow("🚫 Blocked Numbers", "Manage blocked contacts") {}
        ActionRow("⚠ Report Spam", "Help improve detection") {}
        ActionRow("📊 Call History", "View analyzed calls") {}
    }
}

@Composable
fun RingingScreen() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        Text("Incoming Call", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)

        Spacer(modifier = Modifier.height(10.dp))

        Text("Analyzing...", color = Color.Gray)

        Spacer(modifier = Modifier.height(30.dp))

        CircularProgressIndicator(color = Color(0xFF2563EB))
    }
}

@Composable
fun ResultScreen(onEndCall: () -> Unit) {

    Column {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFEF3C7), RoundedCornerShape(16.dp))
                .padding(20.dp)
        ) {

            Column {

                Text(
                    "⚠ Suspicious Call",
                    color = Color(0xFF92400E),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    "This number shows spam-like behavior",
                    color = Color.DarkGray
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        ActionRow("🚫 Block", "Block this caller") { }
        ActionRow("⚠ Report", "Report as spam") { }
        ActionRow("✅ Mark Safe", "Trust this number") { }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onEndCall() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("End Call", color = Color.White)
        }
    }
}

@Composable
fun SimpleStat(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = Color.Black, fontWeight = FontWeight.Bold)
        Text(title, fontSize = 11.sp, color = Color.Gray)
    }
}

@Composable
fun ActionRow(title: String, subtitle: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .background(Color.White, RoundedCornerShape(14.dp))
            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(title, fontSize = 16.sp)

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = Color.Black, fontSize = 14.sp)
            Text(subtitle, fontSize = 12.sp, color = Color.Gray)
        }

        Text(">", color = Color.Gray)
    }
}