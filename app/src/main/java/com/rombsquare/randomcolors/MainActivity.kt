package com.rombsquare.randomcolors

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import java.lang.Math.pow
import kotlin.math.pow
import kotlin.math.sqrt


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

fun getColorDist(color1: Color, color2: Color): Float {
    return sqrt((color2.red - color1.red).pow(2) + (color2.green - color1.green).pow(2) + (color2.blue - color1.blue).pow(2))
}

fun getColorName(targetColor: Color): String {

    if ((targetColor.red + targetColor.green + targetColor.blue)/3 > .95) {
        return "White"
    }

    if ((targetColor.red + targetColor.green + targetColor.blue)/3 < .05) {
        return "Black"
    }

    val colors = mapOf(
        "Red" to Color(255, 0, 0),
        "Orange" to Color(255, 128, 0),
        "Yellow" to Color(255, 255, 0),
        "Lime" to Color(128, 255, 0),
        "Green" to Color(0, 255, 0),
        "Turquoise" to Color(0, 255, 180, 255),
        "Cyan" to Color(0, 255, 255),
        "Sky" to Color(0, 128, 255),
        "Blue" to Color(0, 0, 255),
        "Violet" to Color(128, 0, 255),
        "Pink" to Color(255, 0, 255),
        "Rose" to Color(255, 0, 128),

//        "Brown" to Color(64, 0, 0),
//        "Dark Orange" to Color(64, 32, 0),
//        "Dark Yellow" to Color(64, 64, 0),
//        "Dark Lime" to Color(32, 64, 0),
//        "Dark Green" to Color(0, 64, 0),
//        "Dark Turquoise" to Color(0, 64, 32),
//        "Dark Cyan" to Color(0, 64, 64),
//        "Night Sky" to Color(0, 32, 64),
//        "Dark Blue" to Color(0, 0, 64),
//        "Dark Violet" to Color(32, 0, 64),
//        "Dark Pink" to Color(64, 0, 64),
//        "Dark Rose" to Color(64, 0, 32),

//        "Light Red" to Color(255, 128, 128),
//        "Beige" to Color(255, 201, 146),
//        "Light Yellow" to Color(255, 255, 128),
//        "Light Lime" to Color(213, 255, 171),
//        "Light Green" to Color(128, 255, 128),
//        "Light Turquoise" to Color(137, 255, 194),
//        "Light Cyan" to Color(128, 255, 255),
//        "Light Sky" to Color(123, 190, 255),
//        "Light Blue" to Color(128, 128, 255),
//        "Light Violet" to Color(190, 128, 255),
//        "Light Pink" to Color(255, 128, 255),
//        "Light Rose" to Color(255, 142, 199, 255),
    )

    var closest_color = "Black"
    var closest_distance = 2f

    for ((name, color) in colors) {
        val dist = getColorDist(targetColor, color)
        if (dist < closest_distance) {
            closest_distance = dist
            closest_color = name
        }
    }

    return closest_color
}

@Composable
fun Main() {
    var r by remember { mutableFloatStateOf(0f) }
    var g by remember { mutableFloatStateOf(0f) }
    var b by remember { mutableFloatStateOf(0f) }

    val rAnim by animateFloatAsState(targetValue = r, label = "r", animationSpec = tween(durationMillis = 500))
    val gAnim by animateFloatAsState(targetValue = g, label = "g", animationSpec = tween(durationMillis = 500))
    val bAnim by animateFloatAsState(targetValue = b, label = "b", animationSpec = tween(durationMillis = 500))

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
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text=if(r+g+b!=0f) getColorName(Color(r, g, b)) else "Tap to generate",
                color=if (getLuminance(r, g, b) > 0.5f) Color(0, 0, 0, 164) else Color(255, 255, 255, 164),
                fontSize=if(r+g+b==0f) 40.sp else 60.sp,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text=if(r+g+b!=0f) RGBtoHEX(r, g, b) else "",
                color=if (getLuminance(r, g, b) > 0.5f) Color(0, 0, 0, 164) else Color(255, 255, 255, 164),
                fontSize=20.sp,
                fontWeight = FontWeight.Bold,
            )

        }

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