package com.rombsquare.randomcolors

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.rombsquare.randomcolors.ui.theme.RandomColorsTheme
import kotlinx.coroutines.delay


class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        setContent {
            RandomColorsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { it ->
                    Main()
                }
            }
        }
    }
}

@Composable
fun Rainbow() {
    val context = LocalContext.current

    var hue by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HSB(hue, 1f, 0.5f))
            .systemBarsPadding()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        (context as? Activity)?.finish()
                    }
                )
            },
    )

    LaunchedEffect(Unit) {
        while (true) {
            delay(1L)
            hue += .1f
            if (hue >= 360) {
                hue = 0f
            }
        }
    }
}

@Composable
fun Main() {
    var r by remember { mutableFloatStateOf(0f) }
    var g by remember { mutableFloatStateOf(0f) }
    var b by remember { mutableFloatStateOf(0f) }

    val rAnim by animateFloatAsState(targetValue = r, label = "r")
    val gAnim by animateFloatAsState(targetValue = g, label = "g")
    val bAnim by animateFloatAsState(targetValue = b, label = "b")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(rAnim.coerceIn(0f, 1f), gAnim.coerceIn(0f, 1f), bAnim.coerceIn(0f, 1f), 1f))
            .systemBarsPadding()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        r = Math.random().toFloat()
                        g = Math.random().toFloat()
                        b = Math.random().toFloat()
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text=if(r+g+b!=0f) RGBtoHEX(r, g, b) else "Tap to generate",
            color=if (getLuminance(r, g, b) > 0.5f) Color.Black else Color.White,
            fontSize=40.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

fun RGBtoHEX(r: Float, g: Float, b: Float): String {
    return String.format("#%02X%02X%02X", (r*255).toInt(), (g*255).toInt(), (b*255).toInt())
}

fun getLuminance(r: Float, g: Float, b: Float): Float {
    return 0.2126f * r + 0.7152f * g + 0.0722f * b
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RandomColorsTheme {
        Main()
    }
}

fun HSB(hue: Float, saturation: Float, brightness: Float): Color {
    val colorInt = ColorUtils.HSLToColor(floatArrayOf(hue, saturation, brightness))
    return Color(colorInt)
}