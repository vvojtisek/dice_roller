package com.example

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioFormat
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.blur
import kotlin.math.abs
import kotlin.random.Random
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.TextButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.automirrored.filled.VolumeMute
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import com.example.ui.theme.MyApplicationTheme
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.animation.core.animateFloat
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.animation.core.LinearEasing
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.draw.drawWithContent
import kotlin.math.cos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.sin
import kotlin.math.sqrt

// ==========================================
// AUDIO SYNTHESIZER UTILITY
// ==========================================
object AudioSynth {
    private const val SAMPLE_RATE = 44100

    private fun createAudioTrack(context: Context, bufferSize: Int): AudioTrack {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val builder = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(SAMPLE_RATE)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(bufferSize)
                .setTransferMode(AudioTrack.MODE_STATIC)
            
            if (android.os.Build.VERSION.SDK_INT >= 34) {
                builder.setContext(context)
            }
            builder.build()
        } else {
            @Suppress("DEPRECATION")
            AudioTrack(
                AudioManager.STREAM_MUSIC,
                SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize,
                AudioTrack.MODE_STATIC
            )
        }
    }

    private suspend fun playTone(context: Context, frequency: Double, durationMs: Int) = withContext(Dispatchers.Default) {
        val numSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
        val generatedSnd = ByteArray(2 * numSamples)
        
        for (i in 0 until numSamples) {
            val t = i.toDouble() / SAMPLE_RATE
            // Apply a minor envelope (fade-out) to avoid clicking sounds on termination
            val envelope = if (i > numSamples - 800) {
                (numSamples - i).toDouble() / 800.0
            } else {
                1.0
            }
            val sample = sin(2.0 * Math.PI * frequency * t) * envelope
            val valInt = (sample * 24000).toInt()
            generatedSnd[2 * i] = (valInt and 0x00ff).toByte()
            generatedSnd[2 * i + 1] = ((valInt and 0xff00) ushr 8).toByte()
        }

        try {
            val audioTrack = createAudioTrack(context, generatedSnd.size)
            audioTrack.write(generatedSnd, 0, generatedSnd.size)
            audioTrack.play()
            delay(durationMs.toLong() + 20)
            audioTrack.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    suspend fun playRollClick(context: Context) = withContext(Dispatchers.Default) {
        val durationMs = 15
        val numSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
        val generatedSnd = ByteArray(2 * numSamples)
        
        for (i in 0 until numSamples) {
            val t = i.toDouble() / SAMPLE_RATE
            // High-to-low fast sliding frequency mimicking physical dice roll click
            val freq = 500.0 - 400.0 * (t / (durationMs / 1000.0))
            val sample = sin(2.0 * Math.PI * freq * t) * (1.0 - t / (durationMs / 1000.0))
            val valInt = (sample * 12000).toInt()
            generatedSnd[2 * i] = (valInt and 0x00ff).toByte()
            generatedSnd[2 * i + 1] = ((valInt and 0xff00) ushr 8).toByte()
        }
        
        try {
            val audioTrack = createAudioTrack(context, generatedSnd.size)
            audioTrack.write(generatedSnd, 0, generatedSnd.size)
            audioTrack.play()
            delay(durationMs.toLong() + 5)
            audioTrack.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    suspend fun playResultSound(context: Context, number: Int) {
        when (number) {
            1 -> {
                // Flat, direct warning sound
                playTone(context, 220.0, 250) // A3
            }
            2 -> {
                // Pleasant interval
                playTone(context, 293.66, 100) // D4
                playTone(context, 349.23, 150) // F4
            }
            3 -> {
                // Short major scale triad
                playTone(context, 261.63, 70) // C4
                playTone(context, 329.63, 70) // E4
                playTone(context, 392.00, 150) // G4
            }
            4 -> {
                // Futuristic sci-fi beeps
                playTone(context, 440.00, 60) // A4
                playTone(context, 554.37, 60) // C#5
                playTone(context, 440.00, 60) // A4
                playTone(context, 659.25, 150) // E5
            }
            5 -> {
                // Ascending melodic flourish
                playTone(context, 349.23, 60) // F4
                playTone(context, 440.00, 60) // A4
                playTone(context, 523.25, 60) // C5
                playTone(context, 698.46, 180) // F5
            }
            6 -> {
                // Big Jackpot/Success arpeggio celebration!
                playTone(context, 523.25, 40) // C5
                playTone(context, 659.25, 40) // E5
                playTone(context, 784.00, 40) // G5
                playTone(context, 1046.50, 250) // C6
            }
        }
    }
}

// ==========================================
// MODELS AND STATE ENUMS
// ==========================================
data class RollRecord(val time: String, val values: List<Int>)

sealed interface FaceContent {
    val settleSnapDegrees: Float

    data class Pips(val value: Int) : FaceContent {
        override val settleSnapDegrees: Float = 90f
    }
    data class Numeral(val value: Int) : FaceContent {
        override val settleSnapDegrees: Float = 360f
    }
    data class Swatch(val color: Color, val symbol: Symbol?) : FaceContent {
        override val settleSnapDegrees: Float = 90f
    }
}

enum class Symbol { CIRCLE, TRIANGLE, SQUARE, DIAMOND, STAR, HEX, CROSS }

val android.content.Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "dice_profiles")

val PROFILE_STORE_KEY = stringPreferencesKey("profile_store")

private val jsonConfig = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
}

@Serializable
data class DicePreset(
    val id: String = "",
    val label: String = "",            // user-facing, e.g. "2× d6", "semafor", "2d10"
    val dieTypeId: String = "",        // must resolve against DiceTypes
    val count: Int = 1,               // 1..4
    val showSymbols: Boolean? = null,   // null = follow the global setting
)

@Serializable
data class DiceProfile(
    val id: String = "",
    val name: String = "",
    val presets: List<DicePreset> = emptyList(),
)

@Serializable
data class ProfileStore(
    val schemaVersion: Int = 1,
    val profiles: List<DiceProfile> = emptyList(),
    val activeProfileId: String? = null,
    val activePresetId: String? = null,
)

fun createDefaultProfileStore(): ProfileStore {
    val presets = listOf(
        DicePreset(id = "preset_pips", label = "Body (D6)", dieTypeId = "pips", count = 1),
        DicePreset(id = "preset_numeral", label = "Číselná (D6)", dieTypeId = "numeral", count = 1),
        DicePreset(id = "preset_d4", label = "D4", dieTypeId = "d4", count = 1),
        DicePreset(id = "preset_d8", label = "D8", dieTypeId = "d8", count = 1),
        DicePreset(id = "preset_d10", label = "D10", dieTypeId = "d10", count = 1),
        DicePreset(id = "preset_d12", label = "D12", dieTypeId = "d12", count = 1),
        DicePreset(id = "preset_d20", label = "D20", dieTypeId = "d20", count = 1),
        DicePreset(id = "preset_swatch", label = "Barevná", dieTypeId = "swatch_six_color", count = 1)
    )
    val defaultProfile = DiceProfile(
        id = "profile_zakladni",
        name = "Základní",
        presets = presets
    )
    return ProfileStore(
        schemaVersion = 1,
        profiles = listOf(defaultProfile),
        activeProfileId = "profile_zakladni",
        activePresetId = "preset_pips"
    )
}

suspend fun loadProfileStore(context: android.content.Context): ProfileStore {
    return try {
        val prefs = context.dataStore.data.first()
        val jsonStr = prefs[PROFILE_STORE_KEY]
        if (jsonStr != null) {
            val store = jsonConfig.decodeFromString<ProfileStore>(jsonStr)
            // Filter out invalid dieTypeIds and make sure we don't crash
            val validatedProfiles = store.profiles.map { profile ->
                profile.copy(
                    presets = profile.presets.filter { preset ->
                        val isValid = preset.dieTypeId in listOf(
                            "pips", "numeral", "d4", "d8", "d10", "d12", "d20",
                            "swatch_two_color", "swatch_three_color", "swatch_six_color"
                        )
                        if (!isValid) {
                            android.util.Log.w("DiceRoller", "Neplatné dieTypeId: ${preset.dieTypeId}, vynecháno.")
                        }
                        isValid
                    }
                )
            }
            // If active preset or active profile got deleted / invalid, adjust them
            val updatedStore = store.copy(profiles = validatedProfiles)
            val activeProf = updatedStore.profiles.find { it.id == updatedStore.activeProfileId }
            if (activeProf == null) {
                val firstProf = updatedStore.profiles.firstOrNull()
                updatedStore.copy(
                    activeProfileId = firstProf?.id,
                    activePresetId = firstProf?.presets?.firstOrNull()?.id
                )
            } else {
                val activePres = activeProf.presets.find { it.id == updatedStore.activePresetId }
                if (activePres == null) {
                    updatedStore.copy(
                        activePresetId = activeProf.presets.firstOrNull()?.id
                    )
                } else {
                    updatedStore
                }
            }
        } else {
            createDefaultProfileStore()
        }
    } catch (e: Exception) {
        android.util.Log.e("DiceRoller", "Chyba při načítání profilů, použití výchozích", e)
        createDefaultProfileStore()
    }
}

suspend fun saveProfileStore(context: android.content.Context, store: ProfileStore) {
    try {
        context.dataStore.edit { prefs ->
            prefs[PROFILE_STORE_KEY] = jsonConfig.encodeToString(store)
        }
    } catch (e: Exception) {
        android.util.Log.e("DiceRoller", "Chyba při ukládání profilů", e)
    }
}

// ==========================================
// CHEMISTRY THEME PALETTE FOR DICE
// ==========================================
@Serializable
data class ChemistryDieTheme(
    val id: String,
    val name: String,
    val labelCzech: String,
    val translationSub: String,
    val baseHex: String,
    val pipHex: String
) {
    val baseColor: Color get() = when (id) {
        "porcelain" -> Color(0xFFF8F9FA)
        "copper_sulfate" -> Color(0xFF007FFF)
        "sodium_flame" -> Color(0xFFFF9900)
        "malachite" -> Color(0xFF0BDA51)
        "cinnabar" -> Color(0xFFE34234)
        "chrome_yellow" -> Color(0xFFFFE600)
        "graphite" -> Color(0xFF333333)
        else -> Color(0xFFF8F9FA)
    }

    val pipColor: Color get() = when (id) {
        "graphite" -> Color(0xFFF5F5F5)
        else -> Color(0xFF1A1A1A)
    }
}

val CHEMISTRY_THEMES = listOf(
    ChemistryDieTheme(
        id = "porcelain",
        name = "Porcelán",
        labelCzech = "Porcelán",
        translationSub = "Porcelain",
        baseHex = "#F8F9FA",
        pipHex = "#1A1A1A"
    ),
    ChemistryDieTheme(
        id = "copper_sulfate",
        name = "Modrá skalice",
        labelCzech = "Modrá skalice",
        translationSub = "Copper sulfate",
        baseHex = "#007FFF",
        pipHex = "#1A1A1A"
    ),
    ChemistryDieTheme(
        id = "sodium_flame",
        name = "Sodíkový plamen",
        labelCzech = "Sodíkový plamen",
        translationSub = "Sodium flame",
        baseHex = "#FF9900",
        pipHex = "#1A1A1A"
    ),
    ChemistryDieTheme(
        id = "malachite",
        name = "Malachit",
        labelCzech = "Malachit",
        translationSub = "Malachite",
        baseHex = "#0BDA51",
        pipHex = "#1A1A1A"
    ),
    ChemistryDieTheme(
        id = "cinnabar",
        name = "Rumělka",
        labelCzech = "Rumělka",
        translationSub = "Cinnabar",
        baseHex = "#E34234",
        pipHex = "#1A1A1A"
    ),
    ChemistryDieTheme(
        id = "chrome_yellow",
        name = "Chromová žluť",
        labelCzech = "Chromová žluť",
        translationSub = "Chrome yellow",
        baseHex = "#FFE600",
        pipHex = "#1A1A1A"
    ),
    ChemistryDieTheme(
        id = "graphite",
        name = "Grafit",
        labelCzech = "Grafit",
        translationSub = "Graphite",
        baseHex = "#333333",
        pipHex = "#F5F5F5"
    )
)

fun getChemistryThemeByName(name: String): ChemistryDieTheme {
    return CHEMISTRY_THEMES.find {
        it.name.equals(name, ignoreCase = true) || it.id.equals(name, ignoreCase = true)
    } ?: CHEMISTRY_THEMES.first()
}

val DICE_COLORS_KEY = stringPreferencesKey("dice_colors")

suspend fun loadDiceColors(context: android.content.Context): List<String> {
    return try {
        val prefs = context.dataStore.data.first()
        val jsonStr = prefs[DICE_COLORS_KEY]
        if (!jsonStr.isNullOrBlank()) {
            jsonConfig.decodeFromString<List<String>>(jsonStr)
        } else {
            List(4) { "Porcelán" }
        }
    } catch (e: Exception) {
        List(4) { "Porcelán" }
    }
}

suspend fun saveDiceColors(context: android.content.Context, colors: List<String>) {
    try {
        context.dataStore.edit { prefs ->
            prefs[DICE_COLORS_KEY] = jsonConfig.encodeToString(colors)
        }
    } catch (e: Exception) {
        android.util.Log.e("DiceRoller", "Chyba při ukládání barev kostek", e)
    }
}

object DiceIcons {
    val D4: ImageVector = ImageVector.Builder(
        name = "d4",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        stroke = SolidColor(Color.Black),
        strokeLineWidth = 1.5f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        moveTo(12f, 2f)
        lineTo(22f, 20f)
        lineTo(2f, 20f)
        close()
        moveTo(12f, 2f)
        lineTo(12f, 14f)
        moveTo(22f, 20f)
        lineTo(12f, 14f)
        moveTo(2f, 20f)
        lineTo(12f, 14f)
    }.build()

    val D6: ImageVector = ImageVector.Builder(
        name = "d6",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        stroke = SolidColor(Color.Black),
        strokeLineWidth = 1.5f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        moveTo(12f, 2f)
        lineTo(21f, 7f)
        lineTo(21f, 17f)
        lineTo(12f, 22f)
        lineTo(3f, 17f)
        lineTo(3f, 7f)
        close()
        moveTo(12f, 12f)
        lineTo(12f, 2f)
        moveTo(12f, 12f)
        lineTo(21f, 17f)
        moveTo(12f, 12f)
        lineTo(3f, 17f)
    }.build()

    val D8: ImageVector = ImageVector.Builder(
        name = "d8",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        stroke = SolidColor(Color.Black),
        strokeLineWidth = 1.5f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        moveTo(12f, 2f)
        lineTo(21f, 12f)
        lineTo(12f, 22f)
        lineTo(3f, 12f)
        close()
        moveTo(12f, 2f)
        lineTo(12f, 22f)
        moveTo(3f, 12f)
        lineTo(21f, 12f)
        moveTo(12f, 12f)
        lineTo(12f, 2f)
        moveTo(12f, 12f)
        lineTo(12f, 22f)
        moveTo(12f, 12f)
        lineTo(3f, 12f)
        moveTo(12f, 12f)
        lineTo(21f, 12f)
    }.build()

    val D10: ImageVector = ImageVector.Builder(
        name = "d10",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        stroke = SolidColor(Color.Black),
        strokeLineWidth = 1.5f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        moveTo(12f, 2f)
        lineTo(21f, 9f)
        lineTo(17f, 20f)
        lineTo(12f, 23f)
        lineTo(7f, 20f)
        lineTo(3f, 9f)
        close()
        val cx = 12f
        val cy = 13f
        moveTo(cx, cy)
        lineTo(12f, 2f)
        moveTo(cx, cy)
        lineTo(21f, 9f)
        moveTo(cx, cy)
        lineTo(17f, 20f)
        moveTo(cx, cy)
        lineTo(12f, 23f)
        moveTo(cx, cy)
        lineTo(7f, 20f)
        moveTo(cx, cy)
        lineTo(3f, 9f)
    }.build()

    val D12: ImageVector = ImageVector.Builder(
        name = "d12",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        stroke = SolidColor(Color.Black),
        strokeLineWidth = 1.5f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        moveTo(12f, 2f)
        lineTo(21f, 7f)
        lineTo(21f, 17f)
        lineTo(12f, 22f)
        lineTo(3f, 17f)
        lineTo(3f, 7f)
        close()
        moveTo(12f, 8f)
        lineTo(18f, 12f)
        lineTo(15f, 18f)
        lineTo(9f, 18f)
        lineTo(6f, 12f)
        close()
        moveTo(12f, 8f)
        lineTo(12f, 2f)
        moveTo(18f, 12f)
        lineTo(21f, 7f)
        moveTo(15f, 18f)
        lineTo(21f, 17f)
        moveTo(9f, 18f)
        lineTo(3f, 17f)
        moveTo(6f, 12f)
        lineTo(3f, 7f)
        moveTo(15f, 18f)
        lineTo(12f, 22f)
        moveTo(9f, 18f)
        lineTo(12f, 22f)
    }.build()

    val D20: ImageVector = ImageVector.Builder(
        name = "d20",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(
        stroke = SolidColor(Color.Black),
        strokeLineWidth = 1.5f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round
    ) {
        moveTo(12f, 2f)
        lineTo(21f, 7f)
        lineTo(21f, 17f)
        lineTo(12f, 22f)
        lineTo(3f, 17f)
        lineTo(3f, 7f)
        close()
        moveTo(12f, 7f)
        lineTo(18f, 15f)
        lineTo(6f, 15f)
        close()
        moveTo(12f, 7f)
        lineTo(12f, 2f)
        moveTo(12f, 7f)
        lineTo(21f, 7f)
        moveTo(12f, 7f)
        lineTo(3f, 7f)
        moveTo(18f, 15f)
        lineTo(21f, 7f)
        moveTo(18f, 15f)
        lineTo(21f, 17f)
        moveTo(18f, 15f)
        lineTo(12f, 22f)
        moveTo(6f, 15f)
        lineTo(3f, 7f)
        moveTo(6f, 15f)
        lineTo(3f, 17f)
        moveTo(6f, 15f)
        lineTo(12f, 22f)
    }.build()
}

data class DieType(val id: String, val sides: Int, val contentFor: (Int) -> FaceContent)

object DiceConfig {
    var d10ShowsZeroAsTen: Boolean by mutableStateOf(false)
}

object DiceTypes {
    val D4  = DieType("d4",  4)  { FaceContent.Numeral(it) }
    val D6  = DieType("d6",  6)  { FaceContent.Pips(it) }
    val D8  = DieType("d8",  8)  { FaceContent.Numeral(it) }
    val D10 = DieType("d10", 10) { FaceContent.Numeral(it) }
    val D12 = DieType("d12", 12) { FaceContent.Numeral(it) }
    val D20 = DieType("d20", 20) { FaceContent.Numeral(it) }
}

enum class ColorMode {
    TWO_COLOR, THREE_COLOR, SIX_COLOR
}

object DicePalettes {
    val palettes: Map<ColorMode, List<Pair<Color, Symbol>>> = mapOf(
        ColorMode.TWO_COLOR to listOf(
            Color(0xFFEF4444) to Symbol.CROSS,
            Color(0xFFEF4444) to Symbol.CROSS,
            Color(0xFFEF4444) to Symbol.CROSS,
            Color(0xFF6EE7B7) to Symbol.CIRCLE,
            Color(0xFF6EE7B7) to Symbol.CIRCLE,
            Color(0xFF6EE7B7) to Symbol.CIRCLE
        ),
        ColorMode.THREE_COLOR to listOf(
            Color(0xFFEF4444) to Symbol.CROSS,
            Color(0xFFEF4444) to Symbol.CROSS,
            Color(0xFFFBBF24) to Symbol.SQUARE,
            Color(0xFFFBBF24) to Symbol.SQUARE,
            Color(0xFF6EE7B7) to Symbol.CIRCLE,
            Color(0xFF6EE7B7) to Symbol.CIRCLE
        ),
        ColorMode.SIX_COLOR to listOf(
            Color(0xFFD55E00) to Symbol.CROSS,
            Color(0xFFE69F00) to Symbol.TRIANGLE,
            Color(0xFFF0E442) to Symbol.SQUARE,
            Color(0xFF009E73) to Symbol.CIRCLE,
            Color(0xFF56B4E9) to Symbol.DIAMOND,
            Color(0xFFCC79A7) to Symbol.STAR
        )
    )
}

object DefaultDieTypes {
    val pips = DieType("pips", 6) { FaceContent.Pips(it) }
    val numeral = DieType("numeral", 6) { FaceContent.Numeral(it) }
    fun swatch(colorMode: ColorMode, showSymbols: Boolean) = DieType("swatch_${colorMode.name.lowercase()}", 6) { value ->
        val palette = DicePalettes.palettes[colorMode] ?: emptyList()
        val (color, symbol) = if (value - 1 in palette.indices) {
            palette[value - 1]
        } else {
            Color.Gray to null
        }
        val actualSymbol = if (showSymbols) symbol else null
        FaceContent.Swatch(color, actualSymbol)
    }
}

// ==========================================
// SHAKE SENSOR LISTENER
// ==========================================
class ShakeDetector(private val onShake: () -> Unit) : SensorEventListener {
    private var lastUpdate: Long = 0
    private var lastX = 0f
    private var lastY = 0f
    private var lastZ = 0f
    private val shakeThreshold = 750 // Sensitivity index

    override fun onSensorChanged(event: SensorEvent) {
        val curTime = System.currentTimeMillis()
        if (curTime - lastUpdate > 100) {
            val diffTime = curTime - lastUpdate
            lastUpdate = curTime

            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val speed = sqrt((x - lastX) * (x - lastX) + (y - lastY) * (y - lastY) + (z - lastZ) * (z - lastZ)) / diffTime * 10000

            if (speed > shakeThreshold) {
                onShake()
            }
            lastX = x
            lastY = y
            lastZ = z
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}

// ==========================================
// 2D ANIMATION STATE HOLDER
// ==========================================
class DieAnimState(
    val rotationZ: Animatable<Float, AnimationVector1D>,
    val timeProgress: Animatable<Float, AnimationVector1D>,
    var driftX: Float,
    var targetRotation: Float
)

val EaseOutCubic = androidx.compose.animation.core.Easing { t -> 1f - (1f - t) * (1f - t) * (1f - t) }

// ==========================================
// MAIN ACTIVITY
// ==========================================
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                DiceRollerApp()
            }
        }
    }
}

// ==========================================
// APP COMPSABLE
// ==========================================
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DiceRollerApp() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Configuration states
    var numberOfDice by remember { mutableStateOf(1) }
    var isDarkTheme by remember { mutableStateOf(true) }
    var isMuted by remember { mutableStateOf(false) }
    var isShakeEnabled by remember { mutableStateOf(false) }
    var isSettingsOpen by remember { mutableStateOf(false) }
    var isStatsHistoryOpen by remember { mutableStateOf(false) }
    var selectedDieTypeId by remember { mutableStateOf("pips") }
    var selectedColorMode by remember { mutableStateOf(ColorMode.SIX_COLOR) }
    var showSymbols by remember { mutableStateOf(true) }
    var diceColors by remember { mutableStateOf(listOf("Porcelán", "Porcelán", "Porcelán", "Porcelán")) }
    var isColorPickerOpen by remember { mutableStateOf(false) }

    var isLoaded by remember { mutableStateOf(false) }
    var profileStore by remember { mutableStateOf(ProfileStore()) }
    var isProfileManagerOpen by remember { mutableStateOf(false) }

    val activeProfile = remember(profileStore) {
        profileStore.profiles.find { it.id == profileStore.activeProfileId }
    }
    val activePreset = remember(profileStore, activeProfile) {
        activeProfile?.presets?.find { it.id == profileStore.activePresetId }
    }
    val resolvedShowSymbols = remember(showSymbols, activePreset) {
        if (activePreset != null && activePreset.dieTypeId.startsWith("swatch_")) {
            activePreset.showSymbols ?: showSymbols
        } else {
            showSymbols
        }
    }

    val currentDieType = remember(selectedDieTypeId, selectedColorMode, resolvedShowSymbols) {
        when (selectedDieTypeId) {
            "pips" -> DefaultDieTypes.pips
            "numeral" -> DefaultDieTypes.numeral
            "d4" -> DiceTypes.D4
            "d8" -> DiceTypes.D8
            "d10" -> DiceTypes.D10
            "d12" -> DiceTypes.D12
            "d20" -> DiceTypes.D20
            else -> DefaultDieTypes.swatch(selectedColorMode, resolvedShowSymbols)
        }
    }

    LaunchedEffect(Unit) {
        val store = loadProfileStore(context)
        profileStore = store
        
        // Apply active preset
        val activeProf = store.profiles.find { it.id == store.activeProfileId }
        val activePres = activeProf?.presets?.find { it.id == store.activePresetId }
        if (activePres != null) {
            numberOfDice = activePres.count
            when {
                activePres.dieTypeId == "pips" -> {
                    selectedDieTypeId = "pips"
                }
                activePres.dieTypeId == "numeral" -> {
                    selectedDieTypeId = "numeral"
                }
                activePres.dieTypeId == "d4" -> {
                    selectedDieTypeId = "d4"
                }
                activePres.dieTypeId == "d8" -> {
                    selectedDieTypeId = "d8"
                }
                activePres.dieTypeId == "d10" -> {
                    selectedDieTypeId = "d10"
                }
                activePres.dieTypeId == "d12" -> {
                    selectedDieTypeId = "d12"
                }
                activePres.dieTypeId == "d20" -> {
                    selectedDieTypeId = "d20"
                }
                activePres.dieTypeId.startsWith("swatch_") -> {
                    selectedDieTypeId = "swatch"
                    selectedColorMode = when (activePres.dieTypeId) {
                        "swatch_two_color" -> ColorMode.TWO_COLOR
                        "swatch_three_color" -> ColorMode.THREE_COLOR
                        else -> ColorMode.SIX_COLOR
                    }
                }
            }
        }
        val storedColors = loadDiceColors(context)
        diceColors = storedColors
        isLoaded = true
    }

    val handleDiceColorChange: (Int, String) -> Unit = { index, themeName ->
        val updated = diceColors.toMutableList()
        while (updated.size <= index) {
            updated.add("Porcelán")
        }
        updated[index] = themeName
        diceColors = updated
        scope.launch {
            saveDiceColors(context, updated)
        }
    }

    val handleApplyColorToAll: (String) -> Unit = { themeName ->
        val updated = List(4) { themeName }
        diceColors = updated
        scope.launch {
            saveDiceColors(context, updated)
        }
    }

    // Approved color tokens with contrast ratios in comments as requested:
    // Light mode:
    // - Background (bg): #FFFFFF
    // - Surface (surface): #F8FAFC
    // - Text (text): #0F172A (17.9:1 vs bg)
    // - Secondary text (text-2): #475569 (7.6:1 vs bg)
    // - Muted text (text-muted): #64748B (4.8:1 vs bg, FLOOR)
    // - Icon (icon): #475569 (7.6:1 vs bg)
    // - Border (border): #64748B (4.8:1 vs bg)
    // - Decorative border (border-decor): #CBD5E1 (1.5:1, decor only)
    // - Link/Accent (link): #1D4ED8 (6.7:1 vs bg)
    // - Button bg (btn): #1D4ED8 (6.7:1 vs #FFFFFF text)
    //
    // Dark mode:
    // - Background (bg): #0F172A
    // - Surface (surface): #1E293B
    // - Text (text): #F1F5F9 (16.3:1 vs bg)
    // - Secondary text (text-2): #CBD5E1 (12.0:1 vs bg)
    // - Muted text (text-muted): #94A3B8 (7.0:1 on bg, 5.7:1 on surface, FLOOR)
    // - Icon (icon): #CBD5E1 (16.3:1 vs bg)
    // - Border (border): #64748B (3.8:1 on bg, 3.1:1 on surface)
    // - Decorative border (border-decor): #334155
    // - Link/Accent (link): #60A5FA (7.0:1 vs bg)
    // - Button bg (btn): #2563EB (5.2:1 vs #FFFFFF text)
    val bgColor = if (isDarkTheme) Color(0xFF0F172A) else Color(0xFFFFFFFF)
    val surfaceColor = if (isDarkTheme) Color(0xFF1E293B) else Color(0xFFF8FAFC)
    val textColor = if (isDarkTheme) Color(0xFFF1F5F9) else Color(0xFF0F172A)
    val text2Color = if (isDarkTheme) Color(0xFFCBD5E1) else Color(0xFF475569)
    val textMutedColor = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)
    val iconColor = if (isDarkTheme) Color(0xFFCBD5E1) else Color(0xFF475569)
    val borderColor = if (isDarkTheme) Color(0xFF64748B) else Color(0xFF64748B)
    val borderDecorColor = if (isDarkTheme) Color(0xFF334155) else Color(0xFFCBD5E1)
    val linkColor = if (isDarkTheme) Color(0xFF60A5FA) else Color(0xFF1D4ED8)
    val btnBgColor = if (isDarkTheme) Color(0xFF2563EB) else Color(0xFF1D4ED8)
    val btnTextColor = Color(0xFFFFFFFF)

    // Core rolling states
    var isRolling by remember { mutableStateOf(false) }
    val displayValues = remember { mutableStateListOf(4) } // Initial value for dice 1

    // History and Statistics tracking
    val history = remember { mutableStateListOf<RollRecord>() }
    
    val statsStorage = remember {
        mutableStateMapOf<String, LongArray>().apply {
            put("d4", LongArray(4))
            put("d6", LongArray(6))
            put("d8", LongArray(8))
            put("d10", LongArray(10))
            put("d12", LongArray(12))
            put("d20", LongArray(20))
        }
    }

    fun getStatsKey(dieType: DieType): String {
        return when (dieType.id) {
            "pips", "numeral" -> "d6"
            else -> if (dieType.id.startsWith("swatch")) "d6" else dieType.id
        }
    }

    // 2D Dice Animation states (one for each of up to 4 dice)
    val dieStates = remember {
        List(4) {
            DieAnimState(
                rotationZ = Animatable(0f),
                timeProgress = Animatable(1f),
                driftX = 0f,
                targetRotation = 0f
            )
        }
    }

    val applyPreset: (DicePreset) -> Unit = { preset ->
        numberOfDice = preset.count
        when {
            preset.dieTypeId == "pips" -> {
                selectedDieTypeId = "pips"
            }
            preset.dieTypeId == "numeral" -> {
                selectedDieTypeId = "numeral"
            }
            preset.dieTypeId == "d4" -> {
                selectedDieTypeId = "d4"
            }
            preset.dieTypeId == "d8" -> {
                selectedDieTypeId = "d8"
            }
            preset.dieTypeId == "d10" -> {
                selectedDieTypeId = "d10"
            }
            preset.dieTypeId == "d12" -> {
                selectedDieTypeId = "d12"
            }
            preset.dieTypeId == "d20" -> {
                selectedDieTypeId = "d20"
            }
            preset.dieTypeId.startsWith("swatch_") -> {
                selectedDieTypeId = "swatch"
                selectedColorMode = when (preset.dieTypeId) {
                    "swatch_two_color" -> ColorMode.TWO_COLOR
                    "swatch_three_color" -> ColorMode.THREE_COLOR
                    else -> ColorMode.SIX_COLOR
                }
            }
        }
    }

    // Synchronize Display List size when changing Number of Dice
    LaunchedEffect(numberOfDice) {
        if (displayValues.size < numberOfDice) {
            while (displayValues.size < numberOfDice) {
                displayValues.add((1..currentDieType.sides).random())
            }
        } else if (displayValues.size > numberOfDice) {
            while (displayValues.size > numberOfDice) {
                displayValues.removeAt(displayValues.size - 1)
            }
        }
    }

    LaunchedEffect(selectedDieTypeId) {
        for (i in displayValues.indices) {
            if (displayValues[i] > currentDieType.sides) {
                displayValues[i] = (1..currentDieType.sides).random()
            }
        }
    }

    // Rolling executor
    val triggerRoll = {
        if (!isRolling) {
            scope.launch {
                isRolling = true

                // Staggered roll animations for each die
                val finalValues = List(numberOfDice) { (1..currentDieType.sides).random() }
                
                val jobs = (0 until numberOfDice).map { i ->
                    launch {
                        delay(i * 70L) // Stagger the second die's start by 70ms
                        
                        val rolledVal = finalValues[i]
                        val faceContent = currentDieType.contentFor(rolledVal)
                        val snapDegrees = faceContent.settleSnapDegrees

                        val baseTravel = (900..1260).random().toFloat()
                        val nearestSnap = (baseTravel / snapDegrees).let { Math.round(it) * snapDegrees }
                        val jitter = (Random.nextFloat() * (DiceAnimConfig.settleJitterDeg * 2f)) - DiceAnimConfig.settleJitterDeg
                        val targetRot = nearestSnap + jitter
                        
                        val driftXVal = (Random.nextFloat() * 20f) - 10f // ±10dp drift
                        
                        dieStates[i].driftX = driftXVal
                        dieStates[i].targetRotation = targetRot
                        
                        val rJob = launch {
                            dieStates[i].rotationZ.snapTo(0f)
                            dieStates[i].rotationZ.animateTo(
                                targetValue = targetRot,
                                animationSpec = tween(durationMillis = DiceAnimConfig.totalDurationMs, easing = EaseOutCubic)
                            )
                        }
                        val tJob = launch {
                            dieStates[i].timeProgress.snapTo(0f)
                            dieStates[i].timeProgress.animateTo(
                                targetValue = 1f,
                                animationSpec = tween(durationMillis = DiceAnimConfig.totalDurationMs, easing = LinearEasing)
                            )
                        }
                        
                        // Swap value at apex (280ms)
                        val swapJob = launch {
                            delay(280L)
                            displayValues[i] = finalValues[i]
                            if (!isMuted) {
                                AudioSynth.playRollClick(context)
                            }
                        }
                        
                        rJob.join()
                        tJob.join()
                        swapJob.join()
                        
                        // Guarantee the final value is set on final frame
                        displayValues[i] = finalValues[i]
                    }
                }

                // Wait for all dice to complete their animations
                jobs.forEach { it.join() }

                isRolling = false

                // Record history (keep up to 15 records)
                val formatTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                history.add(0, RollRecord(formatTime, finalValues))
                if (history.size > 15) {
                    history.removeAt(history.size - 1)
                }

                // Increment counts for statistics
                finalValues.forEach { v ->
                    val key = getStatsKey(currentDieType)
                    val array = statsStorage[key] ?: LongArray(currentDieType.sides).also { statsStorage[key] = it }
                    if (v - 1 in array.indices) {
                        array[v - 1] = array[v - 1] + 1
                    }
                }

                // Play custom harmonic land chords based on the values
                if (!isMuted) {
                    for (v in finalValues) {
                        AudioSynth.playResultSound(context, v)
                        delay(70) // spacing out landing chime notes beautifully
                    }
                }
            }
        }
    }

    // Accelerometer listener lifecycle management
    DisposableEffect(isShakeEnabled) {
        if (isShakeEnabled) {
            val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            val detector = ShakeDetector {
                if (!isRolling) {
                    triggerRoll()
                }
            }

            if (accelerometer != null) {
                sensorManager.registerListener(detector, accelerometer, SensorManager.SENSOR_DELAY_UI)
            }

            onDispose {
                sensorManager.unregisterListener(detector)
            }
        } else {
            onDispose {}
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = bgColor,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Casino,
                            contentDescription = null,
                            tint = linkColor,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = "Dice Roller",
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { isMuted = !isMuted },
                        modifier = Modifier.testTag("mute_button")
                    ) {
                        Icon(
                            imageVector = if (isMuted) Icons.AutoMirrored.Filled.VolumeMute else Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Toggle Mute",
                            tint = if (isMuted) textMutedColor else linkColor
                        )
                    }
                    IconButton(
                        onClick = { isStatsHistoryOpen = true },
                        modifier = Modifier.testTag("stats_history_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "Statistiky a historie",
                            tint = linkColor
                        )
                    }
                    IconButton(
                        onClick = { isColorPickerOpen = true },
                        modifier = Modifier.testTag("dice_colors_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = "Barvy kostek",
                            tint = linkColor
                        )
                    }
                    IconButton(
                        onClick = { isSettingsOpen = true },
                        modifier = Modifier.testTag("settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Nastavení",
                            tint = linkColor
                        )
                    }
                    IconButton(
                        onClick = { (context as? android.app.Activity)?.finish() },
                        modifier = Modifier.testTag("exit_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Ukončit aplikaci",
                            tint = linkColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = surfaceColor
                )
            )
        }
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor)
                .padding(innerPadding)
        ) {
            val layoutPaddingVal = 16.dp

            // Centered main content area containing ONLY the active playground
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(layoutPaddingVal)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isLoaded) {
                    // Profile quick switcher header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 500.dp)
                            .padding(bottom = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Folder,
                                contentDescription = null,
                                tint = linkColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Profil: ${activeProfile?.name ?: "Základní"}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                        }

                        TextButton(
                            onClick = { isProfileManagerOpen = true },
                            modifier = Modifier.testTag("manage_profiles_button"),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = linkColor
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Spravovat",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = linkColor
                            )
                        }
                    }

                    // LazyRow of presets (the quick bar)
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 500.dp)
                            .padding(bottom = 12.dp)
                            .testTag("quick_presets_row"),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        activeProfile?.presets?.forEach { preset ->
                            item(key = preset.id) {
                                val isSelected = profileStore.activePresetId == preset.id
                                val countCzechStr = when (preset.count) {
                                    1 -> "1 kostka"
                                    2, 3, 4 -> "${preset.count} kostky"
                                    else -> "${preset.count} kostek"
                                }
                                val dieNameCzech = when (preset.dieTypeId) {
                                    "pips" -> "s body (d6)"
                                    "numeral" -> "s čísly (d6)"
                                    "swatch_two_color" -> "se 2 barvami (d6)"
                                    "swatch_three_color" -> "s 3 barvami (d6)"
                                    "swatch_six_color" -> "se 6 barvami (d6)"
                                    else -> preset.dieTypeId
                                }
                                val contentDesc = "$countCzechStr $dieNameCzech"

                                DieIdentityChip(
                                    dieTypeId = preset.dieTypeId,
                                    labelOverride = preset.label,
                                    isDarkTheme = isDarkTheme,
                                    isSelected = isSelected,
                                    contentDescription = contentDesc,
                                    modifier = Modifier.testTag("preset_chip_${preset.id}"),
                                    onClick = {
                                        if (!isRolling) {
                                            val updatedStore = profileStore.copy(activePresetId = preset.id)
                                            profileStore = updatedStore
                                            scope.launch {
                                                saveProfileStore(context, updatedStore)
                                            }
                                            applyPreset(preset)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                PlaygroundSection(
                    numberOfDice = numberOfDice,
                    displayValues = displayValues,
                    isDarkTheme = isDarkTheme,
                    isRolling = isRolling,
                    dieStates = dieStates,
                    dieType = currentDieType,
                    diceColors = diceColors,
                    onDiceClick = { triggerRoll() }
                )
            }

            // Profile Management overlay
            if (isProfileManagerOpen) {
                ProfileManagementDialog(
                    profileStore = profileStore,
                    onStoreChange = { updatedStore ->
                        profileStore = updatedStore
                        scope.launch {
                            saveProfileStore(context, updatedStore)
                        }
                    },
                    onCloseRequest = { isProfileManagerOpen = false },
                    isDarkTheme = isDarkTheme,
                    onApplyPreset = applyPreset
                )
            }

            // Chemistry Colors Customizer modal overlay
            if (isColorPickerOpen) {
                ChemistryColorCustomizerDialog(
                    numberOfDice = numberOfDice,
                    diceColors = diceColors,
                    onDiceColorChange = handleDiceColorChange,
                    onApplyColorToAll = handleApplyColorToAll,
                    onCloseClick = { isColorPickerOpen = false },
                    isDarkTheme = isDarkTheme
                )
            }

            // Settings modal overlay
            if (isSettingsOpen) {
                Dialog(onDismissRequest = { isSettingsOpen = false }) {
                    SettingsPanel(
                        numberOfDice = numberOfDice,
                        onNumberChange = { numberOfDice = it },
                        isDarkTheme = isDarkTheme,
                        onThemeToggle = { isDarkTheme = it },
                        isShakeEnabled = isShakeEnabled,
                        onShakeToggle = { isShakeEnabled = it },
                        selectedDieTypeId = selectedDieTypeId,
                        onDieTypeChange = { selectedDieTypeId = it },
                        selectedColorMode = selectedColorMode,
                        onColorModeChange = { selectedColorMode = it },
                        showSymbols = showSymbols,
                        onShowSymbolsToggle = { showSymbols = it },
                        d10ShowsZeroAsTen = DiceConfig.d10ShowsZeroAsTen,
                        onD10ShowsZeroAsTenToggle = { DiceConfig.d10ShowsZeroAsTen = it },
                        diceColors = diceColors,
                        onDiceColorChange = handleDiceColorChange,
                        onApplyColorToAll = handleApplyColorToAll,
                        onCloseClick = { isSettingsOpen = false }
                    )
                }
            }

            // Statistics & History modal overlay
            if (isStatsHistoryOpen) {
                Dialog(onDismissRequest = { isStatsHistoryOpen = false }) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.85f)
                            .testTag("stats_history_dialog"),
                        colors = CardDefaults.cardColors(containerColor = surfaceColor),
                        border = BorderStroke(1.dp, borderDecorColor),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp)
                        ) {
                            // Header with Close Button
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.History,
                                        contentDescription = null,
                                        tint = linkColor,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "STATISTIKY A HISTORIE",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = textColor
                                    )
                                }
                                IconButton(
                                    onClick = { isStatsHistoryOpen = false },
                                    modifier = Modifier.testTag("close_stats_history_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Zavřít",
                                        tint = iconColor
                                    )
                                }
                            }

                            // Scrollable list/content containing StatsPanel and HistoryPanel
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                StatsPanel(
                                    statsStorage = statsStorage,
                                    isDarkTheme = isDarkTheme,
                                    onResetClick = { type ->
                                        val sidesVal = when (type) {
                                            "d4" -> 4
                                            "d6" -> 6
                                            "d8" -> 8
                                            "d10" -> 10
                                            "d12" -> 12
                                            "d20" -> 20
                                            else -> 6
                                        }
                                        statsStorage[type] = LongArray(sidesVal)
                                    }
                                )
                                HistoryPanel(history = history, isDarkTheme = isDarkTheme)
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Close Button at bottom
                            Button(
                                onClick = { isStatsHistoryOpen = false },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = btnBgColor,
                                    contentColor = btnTextColor
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("save_stats_history_button"),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "HOTOVO",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// PLAYGROUND CONTAINER
// ==========================================
@Composable
fun PlaygroundSection(
    numberOfDice: Int,
    displayValues: List<Int>,
    isDarkTheme: Boolean,
    isRolling: Boolean,
    dieStates: List<DieAnimState>,
    dieType: DieType,
    diceColors: List<String> = List(4) { "Porcelán" },
    onDiceClick: () -> Unit
) {
    // Approved color tokens with contrast ratios in comments as requested:
    val bgColor = if (isDarkTheme) Color(0xFF0F172A) else Color(0xFFFFFFFF)
    val surfaceColor = if (isDarkTheme) Color(0xFF1E293B) else Color(0xFFF8FAFC)
    val textColor = if (isDarkTheme) Color(0xFFF1F5F9) else Color(0xFF0F172A) // Dark: 16.3:1 vs bg, Light: 17.9:1 vs bg
    val text2Color = if (isDarkTheme) Color(0xFFCBD5E1) else Color(0xFF475569) // Dark: 12.0:1 vs bg, Light: 7.6:1 vs bg
    val textMutedColor = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B) // Dark: 7.0:1 vs bg, Light: 4.8:1 vs bg
    val iconColor = if (isDarkTheme) Color(0xFFCBD5E1) else Color(0xFF475569) // Dark: 16.3:1 vs bg, Light: 7.6:1 vs bg
    val borderColor = if (isDarkTheme) Color(0xFF64748B) else Color(0xFF64748B) // Dark: 3.8:1 on bg, Light: 4.8:1 on bg
    val borderDecorColor = if (isDarkTheme) Color(0xFF334155) else Color(0xFFCBD5E1)
    val linkColor = if (isDarkTheme) Color(0xFF60A5FA) else Color(0xFF1D4ED8) // Dark: 7.0:1 vs bg, Light: 6.7:1 vs bg
    val btnBgColor = if (isDarkTheme) Color(0xFF2563EB) else Color(0xFF1D4ED8) // Dark: 5.2:1 vs white text, Light: 6.7:1 vs white text
    val btnTextColor = Color(0xFFFFFFFF)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 500.dp)
            .testTag("playground_card"),
        colors = CardDefaults.cardColors(containerColor = surfaceColor),
        border = BorderStroke(1.dp, borderDecorColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Latest Roll Pulse-like Badge
            Box(
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .background(btnBgColor, RoundedCornerShape(16.dp))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = if (isRolling) "HÁZENÍ..." else "POSLEDNÍ HOD",
                    style = MaterialTheme.typography.labelSmall,
                    color = btnTextColor,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
            }

            // Header sum helper
            val sum = displayValues.take(numberOfDice).sum()
            Text(
                text = if (isRolling) "Házení..." else if (numberOfDice > 1) "Celkový součet: $sum" else "Hodnota: $sum",
                style = MaterialTheme.typography.headlineSmall,
                color = if (isRolling) linkColor else textColor,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Active Dice viewport with subtle glow
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                // Glow Effect behind the dice
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .background(linkColor.copy(alpha = 0.1f), CircleShape)
                )

                // Active Dice Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clickable { onDiceClick() }
                        .testTag("dice_throw_area"),
                    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 0 until numberOfDice) {
                        if (i < displayValues.size) {
                            val state = dieStates[i]
                            val progress = state.timeProgress.value
                            val rotationZ = state.rotationZ.value
                            val velocity = state.rotationZ.velocity
                            val driftX = state.driftX

                            val themeName = diceColors.getOrElse(i) { "Porcelán" }
                            val chemistryTheme = getChemistryThemeByName(themeName)

                            val faceSize = if (numberOfDice == 1) 130.dp else if (numberOfDice == 2) 105.dp else 82.dp
                            
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.wrapContentSize()
                            ) {
                                ContactShadow(
                                    progress = progress,
                                    isDarkTheme = isDarkTheme,
                                    modifier = Modifier.offset(y = faceSize / 2)
                                )
                                DiceFaceSmearContainer(
                                    content = dieType.contentFor(displayValues[i]),
                                    progress = progress,
                                    rotationZ = rotationZ,
                                    velocity = velocity,
                                    driftX = driftX,
                                    isDarkTheme = isDarkTheme,
                                    modifier = Modifier.size(faceSize),
                                    dieTypeId = dieType.id,
                                    theme = chemistryTheme
                                )

                                // Static Identity Chip (Sibling, stays outside graphicsLayer, anchors below)
                                Row(
                                    modifier = Modifier
                                        .offset(y = (faceSize / 2) + 22.dp)
                                        .background(
                                            color = if (isDarkTheme) Color(0xFF334155) else Color(0xFFE2E8F0),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = if (isDarkTheme) Color(0xFF475569) else Color(0xFFCBD5E1),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .padding(horizontal = 6.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    // Mini color theme indicator pip
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(chemistryTheme.baseColor, CircleShape)
                                            .border(
                                                width = 0.5.dp,
                                                color = if (chemistryTheme.name == "Porcelán") Color.Gray else chemistryTheme.pipColor,
                                                shape = CircleShape
                                            )
                                    )

                                    val icon = when (dieType.id) {
                                        "d4" -> DiceIcons.D4
                                        "d8" -> DiceIcons.D8
                                        "d10" -> DiceIcons.D10
                                        "d12" -> DiceIcons.D12
                                        "d20" -> DiceIcons.D20
                                        else -> DiceIcons.D6
                                    }
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        tint = if (isDarkTheme) Color(0xFFCBD5E1) else Color(0xFF475569),
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(
                                        text = when (dieType.id) {
                                            "pips", "numeral" -> {
                                                if (numberOfDice > 1) "d6 (${chemistryTheme.labelCzech})" else "d6"
                                            }
                                            else -> {
                                                if (dieType.id.startsWith("swatch")) {
                                                    val parts = dieType.id.split("_")
                                                    val modeName = parts.getOrNull(1) ?: "six_color"
                                                    val label = when (modeName) {
                                                        "two_color" -> "2 barvy"
                                                        "three_color" -> "3 barvy"
                                                        else -> "6 barev"
                                                    }
                                                    "d6 · $label"
                                                } else {
                                                    dieType.id
                                                }
                                            }
                                        },
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (isDarkTheme) Color(0xFFF1F5F9) else Color(0xFF0F172A),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "KLEPNI NA KOSTKY PRO HOD",
                style = MaterialTheme.typography.labelMedium,
                color = text2Color,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.5.sp
            )
        }
    }
}

// ==========================================
// CONFIGURATION OBJECT
// ==========================================
object DiceAnimConfig {
    const val totalDurationMs = 850
    val jumpHeightDp = 70.dp
    val bounceHeightDp = 18.dp
    const val rotationTurnsMin = 2.5f
    const val rotationTurnsMax = 3.5f
    val faceSizeDp = 90.dp
    const val smearCopies = 5
    const val smearMaxAlpha = 0.75f
    const val settleJitterDeg = 5f
    const val swatchDiameterFraction = 0.62f
    const val symbolSizeFraction = 0.50f
    val symbolInk = Color(0xFF0F172A)
}

// ==========================================
// CONTACT SHADOW COMPOSABLE
// ==========================================
@Composable
fun ContactShadow(
    progress: Float,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier
) {
    val ms = progress * DiceAnimConfig.totalDurationMs.toFloat()
    
    val alphaVal: Float
    val scaleVal: Float
    
    when {
        ms < 60f -> {
            val t = ms / 60f
            alphaVal = 0.45f + t * (0.55f - 0.45f)
            scaleVal = 0.85f + t * (0.75f - 0.85f)
        }
        ms < 240f -> {
            val t = (ms - 60f) / 180f
            alphaVal = 0.55f + t * (0.15f - 0.55f)
            scaleVal = 0.75f + t * (1.25f - 0.75f)
        }
        ms < 320f -> {
            alphaVal = 0.15f
            scaleVal = 1.25f
        }
        ms < 540f -> {
            val t = (ms - 320f) / 220f
            alphaVal = 0.15f + t * (0.45f - 0.15f)
            scaleVal = 1.25f + t * (0.85f - 1.25f)
        }
        ms < 610f -> {
            val t = (ms - 540f) / 70f
            alphaVal = 0.45f + t * (0.55f - 0.45f)
            scaleVal = 0.85f + t * (0.75f - 0.85f)
        }
        ms < 740f -> {
            val t = (ms - 610f) / 130f
            val bounceHeight = 4f * t * (1f - t)
            alphaVal = 0.55f + bounceHeight * (0.3f - 0.55f)
            scaleVal = 0.75f + bounceHeight * (1.05f - 0.75f)
        }
        else -> {
            alphaVal = 0.45f
            scaleVal = 0.85f
        }
    }
    
    val shadowColor = if (isDarkTheme) Color(0xFF020617) else Color(0xFF0F172A)
    
    Canvas(
        modifier = modifier
            .size(width = 80.dp, height = 24.dp)
            .graphicsLayer {
                scaleX = scaleVal
                scaleY = scaleVal * 0.35f
                alpha = alphaVal
            }
    ) {
        drawOval(color = shadowColor)
    }
}

// ==========================================
// DICE FACE COMPOSABLE
// ==========================================
@Composable
fun DiceFace(
    content: FaceContent,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier,
    dieTypeId: String = "pips",
    theme: ChemistryDieTheme = getChemistryThemeByName("Porcelán")
) {
    val textMeasurer = rememberTextMeasurer()

    AnimatedContent(
        targetState = content,
        transitionSpec = {
            val isSwatch = targetState is FaceContent.Swatch || initialState is FaceContent.Swatch
            if (isSwatch) {
                fadeIn(animationSpec = tween(0)).togetherWith(fadeOut(animationSpec = tween(0)))
                    .using(SizeTransform(clip = false))
            } else {
                (fadeIn(animationSpec = tween(220, easing = FastOutSlowInEasing)) + 
                 scaleIn(initialScale = 0.55f, animationSpec = tween(220, easing = FastOutSlowInEasing)))
                .togetherWith(
                    fadeOut(animationSpec = tween(220, easing = FastOutSlowInEasing)) + 
                    scaleOut(targetScale = 0.55f, animationSpec = tween(220, easing = FastOutSlowInEasing))
                ).using(SizeTransform(clip = false))
            }
        },
        label = "DiceFaceSwirl",
        modifier = modifier
    ) { targetContent ->
        val rotationOffset by transition.animateFloat(
            transitionSpec = {
                val isSwatch = targetContent is FaceContent.Swatch
                if (isSwatch) tween(0) else tween(220, easing = FastOutSlowInEasing)
            },
            label = "SwirlRotation"
        ) { enterExitState ->
            when (enterExitState) {
                EnterExitState.PreEnter -> if (targetContent is FaceContent.Swatch) 0f else 90f
                EnterExitState.Visible -> 0f
                EnterExitState.PostExit -> if (targetContent is FaceContent.Swatch) 0f else -90f
            }
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    rotationZ = rotationOffset
                }
        ) {
            val diceBg = theme.baseColor
            val pipColor = theme.pipColor

            // High-contrast border definition ensuring Porcelán on light and Grafit on dark backgrounds remain crisp
            val diceBorder = when (theme.name) {
                "Porcelán" -> if (isDarkTheme) Color(0xFF64748B) else Color(0xFF94A3B8)
                "Grafit" -> if (isDarkTheme) Color(0xFF64748B) else Color(0xFF1E1E1E)
                "Chromová žluť" -> Color(0xFFCA9E00)
                "Sodíkový plamen" -> Color(0xFFD97706)
                "Malachit" -> Color(0xFF059669)
                "Rumělka" -> Color(0xFFDC2626)
                "Modrá skalice" -> Color(0xFF0284C7)
                else -> if (isDarkTheme) Color(0xFF64748B) else Color(0xFFCBD5E1)
            }

            val sizeVal = size.width
            val cornerRadius = 22.dp.toPx()

            // Subtle ambient shadow underneath the face for depth and visibility
            drawRoundRect(
                color = if (isDarkTheme) Color(0x66000000) else Color(0x22000000),
                cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                topLeft = Offset(0f, 2.dp.toPx()),
                size = androidx.compose.ui.geometry.Size(sizeVal, sizeVal)
            )

            // 1. Draw rounded-rect body
            drawRoundRect(
                color = diceBg,
                cornerRadius = CornerRadius(cornerRadius, cornerRadius)
            )

            // 2. Draw 1.5dp border
            drawRoundRect(
                color = diceBorder,
                cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                style = Stroke(width = 1.5.dp.toPx())
            )

            when (targetContent) {
                is FaceContent.Pips -> {
                    // Draw pips on 3x3 grid
                    val pipRadius = sizeVal * 0.09f
                    val left = sizeVal * 0.25f
                    val center = sizeVal * 0.5f
                    val right = sizeVal * 0.75f
                    val top = sizeVal * 0.25f
                    val middle = sizeVal * 0.5f
                    val bottom = sizeVal * 0.75f

                    val points = when (targetContent.value) {
                        1 -> listOf(center to middle)
                        2 -> listOf(left to top, right to bottom)
                        3 -> listOf(left to top, center to middle, right to bottom)
                        4 -> listOf(left to top, right to top, left to bottom, right to bottom)
                        5 -> listOf(left to top, right to top, center to middle, left to bottom, right to bottom)
                        6 -> listOf(left to top, right to top, left to middle, right to middle, left to bottom, right to bottom)
                        else -> emptyList()
                    }

                    points.forEach { (x, y) ->
                        drawCircle(
                            color = pipColor,
                            radius = pipRadius,
                            center = Offset(x, y)
                        )
                    }
                }
                is FaceContent.Numeral -> {
                    val displayValue = if (dieTypeId == "d10" && DiceConfig.d10ShowsZeroAsTen && targetContent.value == 10) {
                        "0"
                    } else {
                        targetContent.value.toString()
                    }
                    val digitCount = displayValue.length
                    val sizeFraction = if (digitCount == 1) 0.52f else 0.38f
                    val fontSize = sizeVal * sizeFraction

                    val textStyle = TextStyle(
                        color = pipColor,
                        fontSize = fontSize.toSp(),
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )

                    val textLayoutResult = textMeasurer.measure(
                        text = displayValue,
                        style = textStyle
                    )

                    val textWidth = textLayoutResult.size.width.toFloat()
                    val textHeight = textLayoutResult.size.height.toFloat()

                    val leftOffset = (sizeVal - textWidth) / 2f
                    val topOffset = (sizeVal - textHeight) / 2f

                    drawText(
                        textLayoutResult = textLayoutResult,
                        topLeft = Offset(leftOffset, topOffset)
                    )

                    val showUnderlineFor69 = dieTypeId == "d10" || dieTypeId == "d12" || dieTypeId == "d20"
                    if (showUnderlineFor69 && (displayValue == "6" || displayValue == "9")) {
                        val barWidth = textWidth * 0.6f
                        val barThickness = fontSize * 0.06f
                        val baseline = textLayoutResult.getLineBaseline(0)
                        val barY = topOffset + baseline + (fontSize * 0.06f)

                        drawRect(
                            color = pipColor,
                            topLeft = Offset(x = (sizeVal - barWidth) / 2f, y = barY),
                            size = androidx.compose.ui.geometry.Size(width = barWidth, height = barThickness)
                        )
                    }
                }
                is FaceContent.Swatch -> {
                    // Draw Plate (fixed dark mount, colour #0F172A, diameter 0.70 * sizeVal)
                    val plateDiameter = sizeVal * 0.70f
                    val plateRadius = plateDiameter / 2f
                    val centerOffset = Offset(sizeVal / 2f, sizeVal / 2f)

                    drawCircle(
                        color = Color(0xFF0F172A),
                        radius = plateRadius,
                        center = centerOffset
                    )

                    // Draw Swatch (filled circle centred on the face, diameter ~62% of face size)
                    val swatchDiameter = sizeVal * DiceAnimConfig.swatchDiameterFraction
                    val swatchRadius = swatchDiameter / 2f

                    drawCircle(
                        color = targetContent.color,
                        radius = swatchRadius,
                        center = centerOffset
                    )

                    // Draw symbol inside the swatch
                    val symbol = targetContent.symbol
                    if (symbol != null) {
                        val symbolSize = swatchDiameter * DiceAnimConfig.symbolSizeFraction
                        val symbolRadius = symbolSize / 2f
                        val inkColor = DiceAnimConfig.symbolInk
                        val cx = sizeVal / 2f
                        val cy = sizeVal / 2f

                        val strokeWidthPx = sizeVal * 0.045f

                        when (symbol) {
                            Symbol.CIRCLE -> {
                                drawCircle(
                                    color = inkColor,
                                    radius = symbolRadius,
                                    center = centerOffset,
                                    style = Stroke(width = strokeWidthPx)
                                )
                            }
                            Symbol.TRIANGLE -> {
                                val path = Path().apply {
                                    moveTo(cx, cy - symbolRadius)
                                    lineTo(cx - symbolRadius * 0.866f, cy + symbolRadius * 0.5f)
                                    lineTo(cx + symbolRadius * 0.866f, cy + symbolRadius * 0.5f)
                                    close()
                                }
                                drawPath(
                                    path = path,
                                    color = inkColor,
                                    style = Stroke(width = strokeWidthPx)
                                )
                            }
                            Symbol.SQUARE -> {
                                val halfS = symbolSize / 2f
                                drawRect(
                                    color = inkColor,
                                    topLeft = Offset(cx - halfS, cy - halfS),
                                    size = androidx.compose.ui.geometry.Size(symbolSize, symbolSize),
                                    style = Stroke(width = strokeWidthPx)
                                )
                            }
                            Symbol.DIAMOND -> {
                                val path = Path().apply {
                                    moveTo(cx, cy - symbolRadius)
                                    lineTo(cx + symbolRadius, cy)
                                    lineTo(cx, cy + symbolRadius)
                                    lineTo(cx - symbolRadius, cy)
                                    close()
                                }
                                drawPath(
                                    path = path,
                                    color = inkColor,
                                    style = Stroke(width = strokeWidthPx)
                                )
                            }
                            Symbol.STAR -> {
                                val path = Path().apply {
                                    val numPoints = 5
                                    val outerRadius = symbolRadius
                                    val innerRadius = symbolRadius * 0.4f
                                    for (step in 0 until 2 * numPoints) {
                                        val r = if (step % 2 == 0) outerRadius else innerRadius
                                        val angle = Math.toRadians(-90.0 + step * (360.0 / (2 * numPoints)))
                                        val px = (cx + r * cos(angle)).toFloat()
                                        val py = (cy + r * sin(angle)).toFloat()
                                        if (step == 0) {
                                            moveTo(px, py)
                                        } else {
                                            lineTo(px, py)
                                        }
                                    }
                                    close()
                                }
                                drawPath(
                                    path = path,
                                    color = inkColor,
                                    style = Fill
                                )
                            }
                            Symbol.HEX -> {
                                val path = Path().apply {
                                    for (step in 0 until 6) {
                                        val angle = Math.toRadians(step * 60.0)
                                        val px = (cx + symbolRadius * cos(angle)).toFloat()
                                        val py = (cy + symbolRadius * sin(angle)).toFloat()
                                        if (step == 0) {
                                            moveTo(px, py)
                                        } else {
                                            lineTo(px, py)
                                        }
                                    }
                                    close()
                                }
                                drawPath(
                                    path = path,
                                    color = inkColor,
                                    style = Stroke(width = strokeWidthPx)
                                )
                            }
                            Symbol.CROSS -> {
                                val thickness = sizeVal * 0.05f
                                drawLine(
                                    color = inkColor,
                                    start = Offset(cx - symbolRadius, cy),
                                    end = Offset(cx + symbolRadius, cy),
                                    strokeWidth = thickness
                                )
                                drawLine(
                                    color = inkColor,
                                    start = Offset(cx, cy - symbolRadius),
                                    end = Offset(cx, cy + symbolRadius),
                                    strokeWidth = thickness
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// DICE FACE SMEAR CONTAINER (MOTION BLUR + POSITIONING)
// ==========================================
@Composable
fun DiceFaceSmearContainer(
    content: FaceContent,
    progress: Float,
    rotationZ: Float,
    velocity: Float,
    driftX: Float,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier,
    dieTypeId: String = "pips",
    theme: ChemistryDieTheme = getChemistryThemeByName("Porcelán")
) {
    val ms = progress * DiceAnimConfig.totalDurationMs.toFloat()
    
    val transY: Dp
    val scX: Float
    val scY: Float
    
    when {
        ms < 60f -> {
            val t = ms / 60f
            transY = 0.dp
            scY = 1.0f + t * (0.92f - 1.0f)
            scX = 1.0f + t * (1.06f - 1.0f)
        }
        ms < 240f -> {
            val t = (ms - 60f) / 180f
            val easeOut = 1f - (1f - t) * (1f - t)
            transY = -DiceAnimConfig.jumpHeightDp * easeOut
            scY = 0.92f + t * (1.0f - 0.92f)
            scX = 1.06f + t * (1.0f - 1.06f)
        }
        ms < 320f -> {
            transY = -DiceAnimConfig.jumpHeightDp
            scY = 1.0f
            scX = 1.0f
        }
        ms < 540f -> {
            val t = (ms - 320f) / 220f
            val easeIn = t * t
            transY = -DiceAnimConfig.jumpHeightDp * (1f - easeIn)
            scY = 1.0f
            scX = 1.0f
        }
        ms < 610f -> {
            val t = (ms - 540f) / 70f
            transY = 0.dp
            scY = 1.0f + t * (0.88f - 1.0f)
            scX = 1.0f + t * (1.10f - 1.0f)
        }
        ms < 740f -> {
            val t = (ms - 610f) / 130f
            val height = 4f * t * (1f - t) * -DiceAnimConfig.bounceHeightDp.value
            transY = height.dp
            scY = 0.88f + t * (1.0f - 0.88f)
            scX = 1.10f + t * (1.0f - 1.10f)
        }
        else -> {
            transY = 0.dp
            scY = 1.0f
            scX = 1.0f
        }
    }
    
    val transX = driftX.dp * progress.coerceIn(0f, 1f)
    
    val smearStep = (velocity / 360f).coerceIn(0f, 6f)
    val smearIntensity = (abs(velocity) / 2400f).coerceIn(0f, DiceAnimConfig.smearMaxAlpha)
    
    Box(
        modifier = modifier
            .graphicsLayer {
                translationX = transX.toPx()
                translationY = transY.toPx()
                scaleX = scX
                scaleY = scY
                this.rotationZ = rotationZ
            }
    ) {
        for (k in 0 until DiceAnimConfig.smearCopies) {
            val key = k
            val relRot = -key * smearStep
            val alphaVal = if (key == 0) 1.0f else (1.0f - key * 0.2f) * smearIntensity
            
            if (alphaVal > 0.01f) {
                val ghostModifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        this.rotationZ = relRot
                        alpha = alphaVal
                    }
                    .let { baseMod ->
                        if (key > 0 && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                            baseMod.blur(2.dp)
                        } else {
                            baseMod
                        }
                    }
                
                DiceFace(
                    content = content,
                    isDarkTheme = isDarkTheme,
                    modifier = ghostModifier,
                    dieTypeId = dieTypeId,
                    theme = theme
                )
            }
        }
    }
}

// ==========================================
// CHEMISTRY DICE COLOR CUSTOMIZER
// ==========================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChemistryDiceColorSection(
    numberOfDice: Int,
    diceColors: List<String>,
    onDiceColorChange: (index: Int, themeName: String) -> Unit,
    onApplyColorToAll: (themeName: String) -> Unit,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier
) {
    var selectedDieIndex by remember { mutableStateOf(0) }
    val safeDieIndex = selectedDieIndex.coerceIn(0, (numberOfDice - 1).coerceAtLeast(0))
    val currentThemeName = diceColors.getOrElse(safeDieIndex) { "Porcelán" }
    val currentTheme = getChemistryThemeByName(currentThemeName)

    val bgColor = if (isDarkTheme) Color(0xFF0F172A) else Color(0xFFFFFFFF)
    val textColor = if (isDarkTheme) Color(0xFFF1F5F9) else Color(0xFF0F172A)
    val text2Color = if (isDarkTheme) Color(0xFFCBD5E1) else Color(0xFF475569)
    val textMutedColor = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)
    val borderDecorColor = if (isDarkTheme) Color(0xFF334155) else Color(0xFFCBD5E1)
    val linkColor = if (isDarkTheme) Color(0xFF60A5FA) else Color(0xFF1D4ED8)
    val btnBgColor = if (isDarkTheme) Color(0xFF2563EB) else Color(0xFF1D4ED8)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("color_customizer_section")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Palette,
                    contentDescription = null,
                    tint = linkColor,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "CHEMICKÉ BARVY KOSTEK",
                    style = MaterialTheme.typography.labelSmall,
                    color = text2Color,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "7 minerálních témat",
                style = MaterialTheme.typography.labelSmall,
                color = textMutedColor
            )
        }

        Text(
            text = "Přizpůsobte barvy kostek podle chemických prvků a minerálů.",
            style = MaterialTheme.typography.bodySmall,
            color = textMutedColor,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        // Multi-die selector tabs if N > 1
        if (numberOfDice > 1) {
            Text(
                text = "VÝBĚR KOSTKY PRO ZMĚNU BARVY:",
                style = MaterialTheme.typography.labelSmall,
                color = text2Color,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (dieIdx in 0 until numberOfDice) {
                    val isTabSelected = safeDieIndex == dieIdx
                    val assignedThemeName = diceColors.getOrElse(dieIdx) { "Porcelán" }
                    val assignedTheme = getChemistryThemeByName(assignedThemeName)

                    OutlinedButton(
                        onClick = { selectedDieIndex = dieIdx },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("die_color_tab_$dieIdx"),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isTabSelected) btnBgColor.copy(alpha = 0.15f) else Color.Transparent,
                            contentColor = if (isTabSelected) linkColor else textColor
                        ),
                        border = BorderStroke(
                            width = if (isTabSelected) 2.dp else 1.dp,
                            color = if (isTabSelected) linkColor else borderDecorColor
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(assignedTheme.baseColor, CircleShape)
                                    .border(
                                        width = 1.dp,
                                        color = if (assignedTheme.name == "Porcelán") Color.Gray else assignedTheme.pipColor,
                                        shape = CircleShape
                                    )
                            )
                            Text(
                                text = "Kostka ${dieIdx + 1}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = if (isTabSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // Palette Theme Options List
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(bgColor, RoundedCornerShape(12.dp))
                .border(1.dp, borderDecorColor, RoundedCornerShape(12.dp))
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CHEMISTRY_THEMES.forEach { theme ->
                val isSelected = currentThemeName.equals(theme.name, ignoreCase = true)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = if (isSelected) linkColor.copy(alpha = 0.12f) else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            width = if (isSelected) 1.5.dp else 0.5.dp,
                            color = if (isSelected) linkColor else borderDecorColor.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            onDiceColorChange(safeDieIndex, theme.name)
                        }
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                        .testTag("theme_option_${theme.name.lowercase().replace(" ", "_")}"),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Die preview tile
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .background(theme.baseColor, RoundedCornerShape(8.dp))
                                .border(
                                    width = 1.dp,
                                    color = when (theme.name) {
                                        "Porcelán" -> Color(0xFF94A3B8)
                                        "Grafit" -> Color(0xFF64748B)
                                        else -> theme.baseColor.copy(alpha = 0.7f)
                                    },
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(theme.pipColor, CircleShape)
                            )
                        }

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = theme.labelCzech,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = textColor
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "(${theme.translationSub})",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = textMutedColor
                                )
                            }
                            Text(
                                text = "${theme.baseHex} · body: ${if (theme.name == "Grafit") "světlé (#F5F5F5)" else "černé (#1A1A1A)"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = text2Color,
                                fontSize = 11.sp
                            )
                        }
                    }

                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Vybráno",
                            tint = linkColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // Quick Apply to all button when multiple dice are active
        if (numberOfDice > 1) {
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                onClick = { onApplyColorToAll(currentTheme.name) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("apply_to_all_colors_button"),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = linkColor
                ),
                border = BorderStroke(1.dp, linkColor),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DoneAll,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = linkColor
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Použít \"${currentTheme.labelCzech}\" pro všechny kostky",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ==========================================
// CHEMISTRY COLOR CUSTOMIZER DIALOG
// ==========================================
@Composable
fun ChemistryColorCustomizerDialog(
    numberOfDice: Int,
    diceColors: List<String>,
    onDiceColorChange: (index: Int, themeName: String) -> Unit,
    onApplyColorToAll: (themeName: String) -> Unit,
    onCloseClick: () -> Unit,
    isDarkTheme: Boolean
) {
    val surfaceColor = if (isDarkTheme) Color(0xFF1E293B) else Color(0xFFF8FAFC)
    val textColor = if (isDarkTheme) Color(0xFFF1F5F9) else Color(0xFF0F172A)
    val iconColor = if (isDarkTheme) Color(0xFFCBD5E1) else Color(0xFF475569)
    val borderDecorColor = if (isDarkTheme) Color(0xFF334155) else Color(0xFFCBD5E1)
    val linkColor = if (isDarkTheme) Color(0xFF60A5FA) else Color(0xFF1D4ED8)
    val btnBgColor = if (isDarkTheme) Color(0xFF2563EB) else Color(0xFF1D4ED8)
    val btnTextColor = Color(0xFFFFFFFF)

    androidx.compose.ui.window.Dialog(onDismissRequest = onCloseClick) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 680.dp)
                .testTag("dice_color_dialog"),
            colors = CardDefaults.cardColors(containerColor = surfaceColor),
            border = BorderStroke(1.dp, borderDecorColor),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = null,
                            tint = linkColor,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "BARVY KOSTEK",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    }
                    IconButton(
                        onClick = onCloseClick,
                        modifier = Modifier.testTag("close_color_dialog_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Zavřít",
                            tint = iconColor
                        )
                    }
                }

                ChemistryDiceColorSection(
                    numberOfDice = numberOfDice,
                    diceColors = diceColors,
                    onDiceColorChange = onDiceColorChange,
                    onApplyColorToAll = onApplyColorToAll,
                    isDarkTheme = isDarkTheme
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onCloseClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = btnBgColor,
                        contentColor = btnTextColor
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("done_color_dialog_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "HOTOVO",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// ==========================================
// SETTINGS PANEL
// ==========================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingsPanel(
    numberOfDice: Int,
    onNumberChange: (Int) -> Unit,
    isDarkTheme: Boolean,
    onThemeToggle: (Boolean) -> Unit,
    isShakeEnabled: Boolean,
    onShakeToggle: (Boolean) -> Unit,
    selectedDieTypeId: String,
    onDieTypeChange: (String) -> Unit,
    selectedColorMode: ColorMode,
    onColorModeChange: (ColorMode) -> Unit,
    showSymbols: Boolean,
    onShowSymbolsToggle: (Boolean) -> Unit,
    d10ShowsZeroAsTen: Boolean = false,
    onD10ShowsZeroAsTenToggle: (Boolean) -> Unit = {},
    diceColors: List<String> = emptyList(),
    onDiceColorChange: (Int, String) -> Unit = { _, _ -> },
    onApplyColorToAll: (String) -> Unit = {},
    onCloseClick: (() -> Unit)? = null
) {
    // Approved color tokens with contrast ratios in comments as requested:
    val bgColor = if (isDarkTheme) Color(0xFF0F172A) else Color(0xFFFFFFFF)
    val surfaceColor = if (isDarkTheme) Color(0xFF1E293B) else Color(0xFFF8FAFC)
    val textColor = if (isDarkTheme) Color(0xFFF1F5F9) else Color(0xFF0F172A) // Dark: 16.3:1 vs bg, Light: 17.9:1 vs bg
    val text2Color = if (isDarkTheme) Color(0xFFCBD5E1) else Color(0xFF475569) // Dark: 12.0:1 vs bg, Light: 7.6:1 vs bg
    val textMutedColor = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B) // Dark: 7.0:1 vs bg, Light: 4.8:1 vs bg
    val iconColor = if (isDarkTheme) Color(0xFFCBD5E1) else Color(0xFF475569) // Dark: 16.3:1 vs bg, Light: 7.6:1 vs bg
    val borderColor = if (isDarkTheme) Color(0xFF64748B) else Color(0xFF64748B) // Dark: 3.8:1 on bg, Light: 4.8:1 on bg
    val borderDecorColor = if (isDarkTheme) Color(0xFF334155) else Color(0xFFCBD5E1)
    val linkColor = if (isDarkTheme) Color(0xFF60A5FA) else Color(0xFF1D4ED8) // Dark: 7.0:1 vs bg, Light: 6.7:1 vs bg
    val btnBgColor = if (isDarkTheme) Color(0xFF2563EB) else Color(0xFF1D4ED8) // Dark: 5.2:1 vs white text, Light: 6.7:1 vs white text
    val btnTextColor = Color(0xFFFFFFFF)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 680.dp)
            .padding(top = 12.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceColor),
        border = BorderStroke(1.dp, borderDecorColor)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (onCloseClick != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                            tint = linkColor,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "NASTAVENÍ",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    }
                    IconButton(
                        onClick = onCloseClick,
                        modifier = Modifier.testTag("close_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Zavřít",
                            tint = iconColor
                        )
                    }
                }
            }

            // Theme Switch
            Text(
                text = "MOTIV VZHLEDU",
                style = MaterialTheme.typography.labelSmall,
                color = text2Color,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(bgColor, RoundedCornerShape(10.dp))
                    .border(1.dp, borderDecorColor, RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Casino,
                        contentDescription = null,
                        tint = linkColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "TMAVÝ MOTIV",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                        Text(
                            text = if (isDarkTheme) "Aktivní tmavý motiv" else "Aktivní světlý motiv",
                            style = MaterialTheme.typography.bodySmall,
                            color = textMutedColor
                        )
                    }
                }
                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = onThemeToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = btnBgColor,
                        uncheckedThumbColor = Color(0xFF64748B),
                        uncheckedTrackColor = Color(0xFFCBD5E1)
                    ),
                    modifier = Modifier.testTag("theme_switch")
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Number of Dice selector
            Text(
                text = "POČET KOSTEK",
                style = MaterialTheme.typography.labelSmall,
                color = text2Color,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                (1..3).forEach { count ->
                    val isSelected = count == numberOfDice
                    Button(
                        onClick = { onNumberChange(count) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) btnBgColor else borderDecorColor,
                            contentColor = if (isSelected) btnTextColor else textColor
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("dice_count_$count"),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = if (count == 1) "1 kostka" else if (count == 2) "2 kostky" else "3 kostky",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Die Type Selection
            Text(
                text = "TYP KOSTKY",
                style = MaterialTheme.typography.labelSmall,
                color = text2Color,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    "pips" to "Body (D6)",
                    "numeral" to "Číselná (D6)",
                    "d4" to "D4",
                    "d8" to "D8",
                    "d10" to "D10",
                    "d12" to "D12",
                    "d20" to "D20",
                    "swatch" to "Barevná"
                ).forEach { (id, label) ->
                    val isSelected = id == selectedDieTypeId
                    Button(
                        onClick = { onDieTypeChange(id) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) btnBgColor else borderDecorColor,
                            contentColor = if (isSelected) btnTextColor else textColor
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .testTag("die_type_$id"),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            if (selectedDieTypeId == "d10") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(bgColor, RoundedCornerShape(10.dp))
                        .border(1.dp, borderDecorColor, RoundedCornerShape(10.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Casino,
                            contentDescription = null,
                            tint = linkColor,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "D10 ZOBRAZUJE 0",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                            Text(
                                text = "Zobrazit hodnotu 10 jako 0",
                                style = MaterialTheme.typography.bodySmall,
                                color = textMutedColor
                            )
                        }
                    }
                    Switch(
                        checked = d10ShowsZeroAsTen,
                        onCheckedChange = onD10ShowsZeroAsTenToggle,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = btnBgColor,
                            uncheckedThumbColor = Color(0xFF64748B),
                            uncheckedTrackColor = Color(0xFFCBD5E1)
                        ),
                        modifier = Modifier.testTag("d10_zero_switch")
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (selectedDieTypeId == "swatch") {
                // Color Mode Selection
                Text(
                    text = "BAREVNÉ SCHÉMA",
                    style = MaterialTheme.typography.labelSmall,
                    color = text2Color,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    listOf(
                        ColorMode.TWO_COLOR to "2 barvy",
                        ColorMode.THREE_COLOR to "3 barvy",
                        ColorMode.SIX_COLOR to "6 barev"
                    ).forEach { (mode, label) ->
                        val isSelected = mode == selectedColorMode
                        Button(
                            onClick = { onColorModeChange(mode) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) btnBgColor else borderDecorColor,
                                contentColor = if (isSelected) btnTextColor else textColor
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("color_mode_${mode.name.lowercase()}"),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Show Symbols Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(bgColor, RoundedCornerShape(10.dp))
                        .border(1.dp, borderDecorColor, RoundedCornerShape(10.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = linkColor,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "ZOBRAZIT SYMBOLY",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                            Text(
                                text = "Usnadňuje čtení pro barvoslepost",
                                style = MaterialTheme.typography.bodySmall,
                                color = textMutedColor
                            )
                        }
                    }
                    Switch(
                        checked = showSymbols,
                        onCheckedChange = onShowSymbolsToggle,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = btnBgColor,
                            uncheckedThumbColor = Color(0xFF64748B),
                            uncheckedTrackColor = Color(0xFFCBD5E1)
                        ),
                        modifier = Modifier.testTag("symbols_switch")
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Chemistry Dice Color Section
            ChemistryDiceColorSection(
                numberOfDice = numberOfDice,
                diceColors = diceColors,
                onDiceColorChange = onDiceColorChange,
                onApplyColorToAll = onApplyColorToAll,
                isDarkTheme = isDarkTheme
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Shake to Roll Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(bgColor, RoundedCornerShape(10.dp))
                    .border(1.dp, borderDecorColor, RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Vibration,
                        contentDescription = null,
                        tint = linkColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "HODIT ZATŘESENÍM",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                        Text(
                            text = "Zatřeste telefonem pro hod",
                            style = MaterialTheme.typography.bodySmall,
                            color = textMutedColor
                        )
                    }
                }
                Switch(
                    checked = isShakeEnabled,
                    onCheckedChange = onShakeToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = btnBgColor,
                        uncheckedThumbColor = Color(0xFF64748B),
                        uncheckedTrackColor = Color(0xFFCBD5E1)
                    ),
                    modifier = Modifier.testTag("shake_switch")
                )
            }

            if (onCloseClick != null) {
                Spacer(modifier = Modifier.height(16.dp))

                // Licence / CC BY Attribution
                Spacer(modifier = Modifier.height(1.dp).fillMaxWidth().background(borderDecorColor))
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Icons made by Delapouite. Available on https://game-icons.net",
                    style = MaterialTheme.typography.bodySmall,
                    color = textMutedColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                )

                Button(
                    onClick = onCloseClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = btnBgColor,
                        contentColor = btnTextColor
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("save_settings_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "HOTOVO",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// ==========================================
// STATISTICS PANEL
// ==========================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StatsPanel(
    statsStorage: Map<String, LongArray>,
    isDarkTheme: Boolean,
    onResetClick: (String) -> Unit
) {
    var activeType by remember { mutableStateOf("d6") }

    val counts = statsStorage[activeType] ?: LongArray(6)
    val sides = counts.size
    val totalRollsForActive = counts.sum()

    // Approved color tokens with contrast ratios in comments as requested:
    val bgColor = if (isDarkTheme) Color(0xFF0F172A) else Color(0xFFFFFFFF)
    val surfaceColor = if (isDarkTheme) Color(0xFF1E293B) else Color(0xFFF8FAFC)
    val textColor = if (isDarkTheme) Color(0xFFF1F5F9) else Color(0xFF0F172A) // Dark: 16.3:1 vs bg, Light: 17.9:1 vs bg
    val text2Color = if (isDarkTheme) Color(0xFFCBD5E1) else Color(0xFF475569) // Dark: 12.0:1 vs bg, Light: 7.6:1 vs bg
    val textMutedColor = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B) // Dark: 7.0:1 vs bg, Light: 4.8:1 vs bg
    val borderColor = if (isDarkTheme) Color(0xFF64748B) else Color(0xFF64748B) // Dark: 3.8:1 on bg, Light: 4.8:1 on bg
    val borderDecorColor = if (isDarkTheme) Color(0xFF334155) else Color(0xFFCBD5E1)
    val linkColor = if (isDarkTheme) Color(0xFF60A5FA) else Color(0xFF1D4ED8) // Dark: 7.0:1 vs bg, Light: 6.7:1 vs bg
    val btnBgColor = if (isDarkTheme) Color(0xFF2563EB) else Color(0xFF1D4ED8) // Dark: 5.2:1 vs white text, Light: 6.7:1 vs white text
    val btnTextColor = Color(0xFFFFFFFF)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("stats_card"),
        colors = CardDefaults.cardColors(containerColor = surfaceColor),
        border = BorderStroke(1.dp, borderDecorColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "ZVOLTE TYP KOSTKY PRO STATISTIKY",
                style = MaterialTheme.typography.labelSmall,
                color = text2Color,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("d4", "d6", "d8", "d10", "d12", "d20").forEach { type ->
                    val isSelected = type == activeType
                    Button(
                        onClick = { activeType = type },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) btnBgColor else borderDecorColor,
                            contentColor = if (isSelected) btnTextColor else textColor
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("stats_select_$type")
                    ) {
                        Text(
                            text = type.uppercase(),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "STATISTIKY KOSTEK (${activeType.uppercase()})",
                    style = MaterialTheme.typography.titleSmall,
                    color = textColor,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Celkem hodů: $totalRollsForActive",
                    style = MaterialTheme.typography.bodySmall,
                    color = linkColor,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { onResetClick(activeType) },
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = if (isDarkTheme) Color(0xFFEF4444) else Color(0xFFDC2626)
                ),
                border = BorderStroke(1.dp, if (isDarkTheme) Color(0xFFEF4444) else Color(0xFFDC2626)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .testTag("reset_stats_$activeType")
            ) {
                Text(
                    text = "Vynulovat statistiky ${activeType.uppercase()}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }

            counts.forEachIndexed { idx, count ->
                val num = idx + 1
                val ratio = if (totalRollsForActive > 0) count.toFloat() / totalRollsForActive else 0f

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(surfaceColor, RoundedCornerShape(4.dp))
                            .border(1.dp, borderColor, RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = num.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = linkColor
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(10.dp)
                            .background(bgColor, RoundedCornerShape(5.dp))
                    ) {
                        val animatedRatio by animateFloatAsState(targetValue = ratio, label = "ratio_${activeType}_$num")
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(animatedRatio)
                                .background(linkColor, RoundedCornerShape(5.dp))
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "$count (${(ratio * 100).toInt()}%)",
                        style = MaterialTheme.typography.bodySmall,
                        color = text2Color,
                        modifier = Modifier.width(60.dp),
                        textAlign = TextAlign.End,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "ANALÝZA SPRAVEDLIVOSTI (Chi-kvadrát test)",
                style = MaterialTheme.typography.labelSmall,
                color = text2Color,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            if (totalRollsForActive == 0L) {
                Text(
                    text = "Nahoďte kostky pro analýzu spravedlivosti",
                    style = MaterialTheme.typography.bodyMedium,
                    color = textMutedColor,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            } else {
                val expected = totalRollsForActive.toDouble() / sides.toDouble()
                var chi2 = 0.0
                for (c in counts) {
                    val diff = c.toDouble() - expected
                    chi2 += (diff * diff) / expected
                }

                val df = sides - 1

                val pValue = if (chi2 == 0.0) {
                    1.0
                } else {
                    val part1 = 9.0 * df / 2.0
                    val term1 = Math.pow(chi2 / df.toDouble(), 1.0 / 3.0)
                    val term2 = 1.0 - (2.0 / (9.0 * df))
                    val z = Math.sqrt(part1) * (term1 - term2)

                    val absZ = Math.abs(z)
                    val phi = 1.0 / (1.0 + Math.exp(-1.5976 * absZ - 0.070566 * absZ * absZ * absZ))
                    val pValApprox = if (z >= 0) 1.0 - phi else phi
                    pValApprox.coerceIn(0.0, 1.0)
                }

                val looksFair = pValue >= 0.01

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    val badgeBg = if (looksFair) Color(0xFF22C55E) else Color(0xFFEF4444)
                    val badgeText = if (looksFair) "Looks Fair" else "Looks Biased"
                    
                    Box(
                        modifier = Modifier
                            .background(badgeBg, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = badgeText,
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = String.format(Locale.US, "χ² = %.3f, df = %d", chi2, df),
                            style = MaterialTheme.typography.bodySmall,
                            color = textColor,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = String.format(Locale.US, "p-value = %.4f", pValue),
                            style = MaterialTheme.typography.bodySmall,
                            color = text2Color
                        )
                    }
                }

                val conversationalSummary = if (looksFair) {
                    "Rozložení hodů se zdá být statisticky spravedlivé a odpovídá náhodě."
                } else {
                    "POZOR: Rozložení hodů vykazuje statistické odchylky. Kostka se může zdát vychýlená."
                }

                Text(
                    text = conversationalSummary,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (looksFair) text2Color else Color(0xFFEF4444),
                    fontWeight = if (looksFair) FontWeight.Normal else FontWeight.Bold
                )
            }
        }
    }
}

// ==========================================
// HISTORY LOGS PANEL
// ==========================================
@Composable
fun HistoryPanel(history: List<RollRecord>, isDarkTheme: Boolean) {
    // Approved color tokens with contrast ratios in comments as requested:
    val bgColor = if (isDarkTheme) Color(0xFF0F172A) else Color(0xFFFFFFFF)
    val surfaceColor = if (isDarkTheme) Color(0xFF1E293B) else Color(0xFFF8FAFC)
    val textColor = if (isDarkTheme) Color(0xFFF1F5F9) else Color(0xFF0F172A) // Dark: 16.3:1 vs bg, Light: 17.9:1 vs bg
    val text2Color = if (isDarkTheme) Color(0xFFCBD5E1) else Color(0xFF475569) // Dark: 12.0:1 vs bg, Light: 7.6:1 vs bg
    val textMutedColor = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B) // Dark: 7.0:1 vs bg, Light: 4.8:1 vs bg
    val iconColor = if (isDarkTheme) Color(0xFFCBD5E1) else Color(0xFF475569) // Dark: 16.3:1 vs bg, Light: 7.6:1 vs bg
    val borderColor = if (isDarkTheme) Color(0xFF64748B) else Color(0xFF64748B) // Dark: 3.8:1 on bg, Light: 4.8:1 on bg
    val borderDecorColor = if (isDarkTheme) Color(0xFF334155) else Color(0xFFCBD5E1)
    val linkColor = if (isDarkTheme) Color(0xFF60A5FA) else Color(0xFF1D4ED8) // Dark: 7.0:1 vs bg, Light: 6.7:1 vs bg
    val btnBgColor = if (isDarkTheme) Color(0xFF2563EB) else Color(0xFF1D4ED8) // Dark: 5.2:1 vs white text, Light: 6.7:1 vs white text
    val btnTextColor = Color(0xFFFFFFFF)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("history_card"),
        colors = CardDefaults.cardColors(containerColor = surfaceColor),
        border = BorderStroke(1.dp, borderDecorColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "HISTORIE HODŮ",
                style = MaterialTheme.typography.labelSmall,
                color = text2Color,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            if (history.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            tint = textMutedColor,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Zatím žádné záznamy. Hoďte kostkou!",
                            style = MaterialTheme.typography.bodySmall,
                            color = textMutedColor
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.height(180.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(history) { record ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(bgColor, RoundedCornerShape(8.dp))
                                .border(1.dp, borderDecorColor, RoundedCornerShape(8.dp))
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = record.time,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = textMutedColor,
                                    modifier = Modifier.padding(end = 12.dp)
                                )
                                
                                // Render small color-matching cubes representing landed rolls
                                record.values.forEach { v ->
                                    Box(
                                        modifier = Modifier
                                            .padding(end = 6.dp)
                                            .size(20.dp)
                                            .background(surfaceColor, RoundedCornerShape(4.dp))
                                            .border(1.dp, borderColor, RoundedCornerShape(4.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = v.toString(),
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = linkColor
                                        )
                                    }
                                }
                            }
                            
                            // Displays total summation
                            Text(
                                text = "Součet: ${record.values.sum()}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = linkColor
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DieIdentityChip(
    dieTypeId: String,
    labelOverride: String? = null,
    isDarkTheme: Boolean,
    isSelected: Boolean = false,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val bgColor = if (isSelected) {
        if (isDarkTheme) Color(0xFF1E3A8A) else Color(0xFFDBEAFE)
    } else {
        if (isDarkTheme) Color(0xFF334155) else Color(0xFFE2E8F0)
    }
    
    val borderColor = if (isSelected) {
        if (isDarkTheme) Color(0xFF3B82F6) else Color(0xFF3B82F6)
    } else {
        if (isDarkTheme) Color(0xFF475569) else Color(0xFFCBD5E1)
    }

    val icon = when (dieTypeId) {
        "d4" -> DiceIcons.D4
        "d8" -> DiceIcons.D8
        "d10" -> DiceIcons.D10
        "d12" -> DiceIcons.D12
        "d20" -> DiceIcons.D20
        else -> DiceIcons.D6
    }

    val text = labelOverride ?: when (dieTypeId) {
        "pips", "numeral" -> "d6"
        else -> {
            if (dieTypeId.startsWith("swatch")) {
                val parts = dieTypeId.split("_")
                val modeName = parts.getOrNull(1) ?: "six_color"
                val label = when (modeName) {
                    "two_color" -> "2 barvy"
                    "three_color" -> "3 barvy"
                    else -> "6 barev"
                }
                "d6 · $label"
            } else {
                dieTypeId
            }
        }
    }

    Row(
        modifier = modifier
            .background(color = bgColor, shape = RoundedCornerShape(8.dp))
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .run {
                if (onClick != null) clickable(onClick = onClick) else this
            }
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = if (isDarkTheme) Color(0xFF60A5FA) else Color(0xFF1D4ED8),
                modifier = Modifier.size(12.dp)
            )
        }
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isDarkTheme) Color(0xFFCBD5E1) else Color(0xFF475569),
            modifier = Modifier.size(12.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = if (isDarkTheme) Color(0xFFF1F5F9) else Color(0xFF0F172A),
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ProfileManagementDialog(
    profileStore: ProfileStore,
    onStoreChange: (ProfileStore) -> Unit,
    onCloseRequest: () -> Unit,
    isDarkTheme: Boolean,
    onApplyPreset: (DicePreset) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Colors
    val bgColor = if (isDarkTheme) Color(0xFF0F172A) else Color(0xFFFFFFFF)
    val surfaceColor = if (isDarkTheme) Color(0xFF1E293B) else Color(0xFFF8FAFC)
    val textColor = if (isDarkTheme) Color(0xFFF1F5F9) else Color(0xFF0F172A)
    val text2Color = if (isDarkTheme) Color(0xFFCBD5E1) else Color(0xFF475569)
    val textMutedColor = if (isDarkTheme) Color(0xFF94A3B8) else Color(0xFF64748B)
    val borderDecorColor = if (isDarkTheme) Color(0xFF334155) else Color(0xFFCBD5E1)
    val linkColor = if (isDarkTheme) Color(0xFF60A5FA) else Color(0xFF1D4ED8)
    val btnBgColor = if (isDarkTheme) Color(0xFF2563EB) else Color(0xFF1D4ED8)
    val btnTextColor = Color.White

    // States
    var selectedProfileId by remember { mutableStateOf(profileStore.activeProfileId ?: profileStore.profiles.firstOrNull()?.id ?: "") }
    
    val selectedProfile = remember(profileStore, selectedProfileId) {
        profileStore.profiles.find { it.id == selectedProfileId } ?: profileStore.profiles.firstOrNull()
    }

    var showCreateProfileDialog by remember { mutableStateOf(false) }
    var showRenameProfileDialog by remember { mutableStateOf(false) }
    var showPresetEditorDialog by remember { mutableStateOf(false) }
    var presetToEdit by remember { mutableStateOf<DicePreset?>(null) } // null = creating new preset

    Dialog(onDismissRequest = onCloseRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 600.dp)
                .testTag("profile_manager_card"),
            colors = CardDefaults.cardColors(containerColor = surfaceColor),
            border = BorderStroke(1.dp, borderDecorColor),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "SPRAVA PROFILŮ A PŘEDVOLEB",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    IconButton(onClick = onCloseRequest) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Zavřít", tint = text2Color)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Profile Selector Dropdown / Row
                Text(
                    text = "AKTIVNÍ PROFIL",
                    style = MaterialTheme.typography.labelSmall,
                    color = text2Color,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Profile Selection spinner/row
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(bgColor, RoundedCornerShape(8.dp))
                            .border(1.dp, borderDecorColor, RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = selectedProfile?.name ?: "Vyberte profil",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                        }
                    }

                    // Rename button
                    IconButton(
                        onClick = { showRenameProfileDialog = true },
                        modifier = Modifier
                            .background(bgColor, RoundedCornerShape(8.dp))
                            .border(1.dp, borderDecorColor, RoundedCornerShape(8.dp))
                            .testTag("rename_profile_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Přejmenovat profil", tint = linkColor)
                    }

                    // Delete button
                    val canDeleteProfile = profileStore.profiles.size > 1
                    IconButton(
                        onClick = {
                            selectedProfile?.let { prof ->
                                if (canDeleteProfile) {
                                    val newList = profileStore.profiles.filter { it.id != prof.id }
                                    val nextActiveProfileId = if (profileStore.activeProfileId == prof.id) {
                                        newList.first().id
                                    } else {
                                        profileStore.activeProfileId
                                    }
                                    val nextActivePresetId = if (profileStore.activeProfileId == prof.id) {
                                        newList.first().presets.firstOrNull()?.id
                                    } else {
                                        profileStore.activePresetId
                                    }
                                    val updatedStore = profileStore.copy(
                                        profiles = newList,
                                        activeProfileId = nextActiveProfileId,
                                        activePresetId = nextActivePresetId
                                    )
                                    onStoreChange(updatedStore)
                                    selectedProfileId = nextActiveProfileId ?: ""
                                    // Apply fallback preset
                                    if (profileStore.activeProfileId == prof.id) {
                                        val fallbackPreset = newList.first().presets.firstOrNull()
                                        if (fallbackPreset != null) {
                                            onApplyPreset(fallbackPreset)
                                        }
                                    }
                                }
                            }
                        },
                        enabled = canDeleteProfile,
                        modifier = Modifier
                            .background(if (canDeleteProfile) bgColor else bgColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .border(1.dp, if (canDeleteProfile) borderDecorColor else borderDecorColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .testTag("delete_profile_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete, 
                            contentDescription = "Smazat profil", 
                            tint = if (canDeleteProfile) Color.Red else textMutedColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Profile selection list or tabs
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    profileStore.profiles.forEach { prof ->
                        item(key = prof.id) {
                            val isCurrentActive = prof.id == profileStore.activeProfileId
                            val isSelected = prof.id == selectedProfileId
                            
                            val chipBg = if (isSelected) btnBgColor else bgColor
                            val chipBorder = if (isCurrentActive) linkColor else borderDecorColor
                            val chipText = if (isSelected) btnTextColor else textColor
                            
                            Box(
                                modifier = Modifier
                                    .background(chipBg, RoundedCornerShape(16.dp))
                                    .border(if (isSelected || isCurrentActive) 2.dp else 1.dp, chipBorder, RoundedCornerShape(16.dp))
                                    .clickable {
                                        selectedProfileId = prof.id
                                        // Also set as active profile!
                                        val updatedStore = profileStore.copy(
                                            activeProfileId = prof.id,
                                            activePresetId = prof.presets.firstOrNull()?.id
                                        )
                                        onStoreChange(updatedStore)
                                        prof.presets.firstOrNull()?.let { onApplyPreset(it) }
                                    }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                                    .testTag("profile_tab_${prof.id}")
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    if (isCurrentActive) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Aktivní",
                                            tint = if (isSelected) Color.White else linkColor,
                                            modifier = Modifier.size(12.dp)
                                        )
                                    }
                                    Text(
                                        text = prof.name,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        color = chipText
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Create profile button with limit checker
                val profilesCapReached = profileStore.profiles.size >= 16
                Button(
                    onClick = { showCreateProfileDialog = true },
                    enabled = !profilesCapReached,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = btnBgColor,
                        contentColor = btnTextColor,
                        disabledContainerColor = borderDecorColor,
                        disabledContentColor = textMutedColor
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("create_profile_btn")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Vytvořit nový profil", fontWeight = FontWeight.Bold)
                }

                if (profilesCapReached) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Dosažen maximální limit 16 profilů.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Red,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Divider
                Spacer(modifier = Modifier.height(1.dp).fillMaxWidth().background(borderDecorColor))
                Spacer(modifier = Modifier.height(12.dp))

                // Presets list header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "PŘEDVOLBY PROFILU",
                        style = MaterialTheme.typography.labelSmall,
                        color = text2Color,
                        fontWeight = FontWeight.Bold
                    )

                    val presetsCapReached = (selectedProfile?.presets?.size ?: 0) >= 8
                    TextButton(
                        onClick = {
                            presetToEdit = null
                            showPresetEditorDialog = true
                        },
                        enabled = !presetsCapReached && selectedProfile != null,
                        modifier = Modifier.testTag("add_preset_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Přidat předvolbu", fontWeight = FontWeight.Bold)
                    }
                }

                val presetsCapReached = (selectedProfile?.presets?.size ?: 0) >= 8
                if (presetsCapReached) {
                    Text(
                        text = "Dosažen maximální limit 8 předvoleb pro tento profil.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Red,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Presets List
                selectedProfile?.let { prof ->
                    if (prof.presets.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Žádné předvolby v tomto profilu.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = textMutedColor
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .testTag("presets_list"),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(prof.presets.size) { index ->
                                val preset = prof.presets[index]
                                val isActivePreset = profileStore.activePresetId == preset.id && profileStore.activeProfileId == prof.id

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(bgColor, RoundedCornerShape(10.dp))
                                        .border(if (isActivePreset) 2.dp else 1.dp, if (isActivePreset) linkColor else borderDecorColor, RoundedCornerShape(10.dp))
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = preset.label.ifEmpty { "${preset.count}× ${preset.dieTypeId}" },
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = textColor
                                        )
                                        Text(
                                            text = "${preset.count} kostky · ${preset.dieTypeId}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = textMutedColor
                                        )
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        // Edit
                                        IconButton(
                                            onClick = {
                                                presetToEdit = preset
                                                showPresetEditorDialog = true
                                            },
                                            modifier = Modifier.testTag("edit_preset_${preset.id}")
                                        ) {
                                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Upravit", tint = linkColor)
                                        }

                                        // Delete
                                        IconButton(
                                            onClick = {
                                                val remainingPresets = prof.presets.filter { it.id != preset.id }
                                                val nextActivePresetId = if (profileStore.activePresetId == preset.id) {
                                                    remainingPresets.firstOrNull()?.id
                                                } else {
                                                    profileStore.activePresetId
                                                }
                                                val updatedProfiles = profileStore.profiles.map { p ->
                                                    if (p.id == prof.id) {
                                                        p.copy(presets = remainingPresets)
                                                    } else {
                                                        p
                                                    }
                                                }
                                                val updatedStore = profileStore.copy(
                                                    profiles = updatedProfiles,
                                                    activePresetId = nextActivePresetId
                                                )
                                                onStoreChange(updatedStore)
                                                // Apply remaining active preset if current active got deleted
                                                if (profileStore.activePresetId == preset.id) {
                                                    val nextPreset = remainingPresets.firstOrNull()
                                                    if (nextPreset != null) {
                                                        onApplyPreset(nextPreset)
                                                    }
                                                }
                                            },
                                            modifier = Modifier.testTag("delete_preset_${preset.id}")
                                        ) {
                                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Smazat", tint = Color.Red)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Sub-dialog: Create Profile
    if (showCreateProfileDialog) {
        var profileName by remember { mutableStateOf("") }
        Dialog(onDismissRequest = { showCreateProfileDialog = false }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = surfaceColor),
                border = BorderStroke(1.dp, borderDecorColor),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(16.dp).testTag("create_profile_dialog")
            ) {
                Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                    Text(
                        text = "Vytvořit nový profil",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = profileName,
                        onValueChange = { profileName = it },
                        label = { Text("Název profilu") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = linkColor,
                            focusedLabelColor = linkColor,
                            unfocusedBorderColor = borderDecorColor,
                            unfocusedLabelColor = textMutedColor
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("profile_name_input")
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = { showCreateProfileDialog = false }) {
                            Text("Zrušit", color = text2Color)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (profileName.isNotBlank() && profileStore.profiles.size < 16) {
                                    val newId = "profile_${System.currentTimeMillis()}"
                                    // Create with at least one default preset so activePresetId never dangles!
                                    val defaultPreset = DicePreset(
                                        id = "preset_${System.currentTimeMillis()}",
                                        label = "Body (D6)",
                                        dieTypeId = "pips",
                                        count = 1
                                    )
                                    val newProfile = DiceProfile(
                                        id = newId,
                                        name = profileName.trim(),
                                        presets = listOf(defaultPreset)
                                    )
                                    val updatedStore = profileStore.copy(
                                        profiles = profileStore.profiles + newProfile,
                                        activeProfileId = newId,
                                        activePresetId = defaultPreset.id
                                    )
                                    onStoreChange(updatedStore)
                                    selectedProfileId = newId
                                    onApplyPreset(defaultPreset)
                                    showCreateProfileDialog = false
                                }
                            },
                            enabled = profileName.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(containerColor = btnBgColor, contentColor = btnTextColor)
                        ) {
                            Text("Vytvořit", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // Sub-dialog: Rename Profile
    if (showRenameProfileDialog && selectedProfile != null) {
        var profileName by remember { mutableStateOf(selectedProfile.name) }
        Dialog(onDismissRequest = { showRenameProfileDialog = false }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = surfaceColor),
                border = BorderStroke(1.dp, borderDecorColor),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(16.dp).testTag("rename_profile_dialog")
            ) {
                Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                    Text(
                        text = "Přejmenovat profil",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = profileName,
                        onValueChange = { profileName = it },
                        label = { Text("Název profilu") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = linkColor,
                            focusedLabelColor = linkColor,
                            unfocusedBorderColor = borderDecorColor,
                            unfocusedLabelColor = textMutedColor
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("rename_profile_input")
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = { showRenameProfileDialog = false }) {
                            Text("Zrušit", color = text2Color)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (profileName.isNotBlank()) {
                                    val updatedProfiles = profileStore.profiles.map { p ->
                                        if (p.id == selectedProfile.id) {
                                            p.copy(name = profileName.trim())
                                        } else {
                                            p
                                        }
                                    }
                                    val updatedStore = profileStore.copy(profiles = updatedProfiles)
                                    onStoreChange(updatedStore)
                                    showRenameProfileDialog = false
                                }
                            },
                            enabled = profileName.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(containerColor = btnBgColor, contentColor = btnTextColor)
                        ) {
                            Text("Uložit", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // Sub-dialog: Preset Editor
    if (showPresetEditorDialog && selectedProfile != null) {
        val editing = presetToEdit
        var label by remember { mutableStateOf(editing?.label ?: "") }
        var dieTypeId by remember { mutableStateOf(editing?.dieTypeId ?: "pips") }
        var count by remember { mutableStateOf(editing?.count ?: 1) }
        var showSymbolsTri by remember { 
            mutableStateOf(
                when (editing?.showSymbols) {
                    true -> "zapnuto"
                    false -> "vypnuto"
                    else -> "výchozí"
                }
            ) 
        }

        Dialog(onDismissRequest = { showPresetEditorDialog = false }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = surfaceColor),
                border = BorderStroke(1.dp, borderDecorColor),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .padding(16.dp)
                    .heightIn(max = 500.dp)
                    .testTag("preset_editor_dialog")
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = if (editing != null) "Upravit předvolbu" else "Přidat předvolbu",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Label input
                    OutlinedTextField(
                        value = label,
                        onValueChange = { label = it },
                        label = { Text("Štítek (např. 2d10, Semafor)") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = linkColor,
                            focusedLabelColor = linkColor,
                            unfocusedBorderColor = borderDecorColor,
                            unfocusedLabelColor = textMutedColor
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("preset_label_input")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Die type selector
                    Text(
                        text = "TYP KOSTKY",
                        style = MaterialTheme.typography.labelSmall,
                        color = text2Color,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(
                            "pips" to "Body (D6)",
                            "numeral" to "Čísla (D6)",
                            "d4" to "D4",
                            "d8" to "D8",
                            "d10" to "D10",
                            "d12" to "D12",
                            "d20" to "D20",
                            "swatch_two_color" to "2 barvy",
                            "swatch_three_color" to "3 barvy",
                            "swatch_six_color" to "6 barev"
                        ).forEach { (id, dLabel) ->
                            val isSelected = id == dieTypeId
                            Button(
                                onClick = { dieTypeId = id },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSelected) btnBgColor else borderDecorColor,
                                    contentColor = if (isSelected) btnTextColor else textColor
                                ),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.testTag("preset_editor_dietype_$id")
                            ) {
                                Text(text = dLabel, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Count selector (1..4 stepper)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "POČET KOSTEK",
                            style = MaterialTheme.typography.labelSmall,
                            color = text2Color,
                            fontWeight = FontWeight.Bold
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            IconButton(
                                onClick = { if (count > 1) count-- },
                                enabled = count > 1,
                                modifier = Modifier
                                    .background(bgColor, RoundedCornerShape(8.dp))
                                    .border(1.dp, borderDecorColor, RoundedCornerShape(8.dp))
                                    .size(36.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Remove, contentDescription = "Méně", tint = linkColor)
                            }

                            Text(
                                text = count.toString(),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = textColor,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )

                            IconButton(
                                onClick = { if (count < 4) count++ },
                                enabled = count < 4,
                                modifier = Modifier
                                    .background(bgColor, RoundedCornerShape(8.dp))
                                    .border(1.dp, borderDecorColor, RoundedCornerShape(8.dp))
                                    .size(36.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = "Více", tint = linkColor)
                            }
                        }
                    }

                    // showSymbols tri-state (only when dieTypeId is a color d6, i.e. starts with swatch_)
                    if (dieTypeId.startsWith("swatch_")) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "ZOBRAZIT SYMBOLY",
                            style = MaterialTheme.typography.labelSmall,
                            color = text2Color,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("výchozí", "zapnuto", "vypnuto").forEach { option ->
                                val isSelected = option == showSymbolsTri
                                Button(
                                    onClick = { showSymbolsTri = option },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isSelected) btnBgColor else borderDecorColor,
                                        contentColor = if (isSelected) btnTextColor else textColor
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("tri_state_$option")
                                ) {
                                    Text(
                                        text = option.replaceFirstChar { it.uppercase() },
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = { showPresetEditorDialog = false }) {
                            Text("Zrušit", color = text2Color)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                val symValue = when (showSymbolsTri) {
                                    "zapnuto" -> true
                                    "vypnuto" -> false
                                    else -> null
                                }
                                val newPreset = DicePreset(
                                    id = editing?.id ?: "preset_${System.currentTimeMillis()}",
                                    label = label.trim().ifEmpty { "${count}× ${dieTypeId}" },
                                    dieTypeId = dieTypeId,
                                    count = count,
                                    showSymbols = symValue
                                )

                                val updatedPresets = if (editing != null) {
                                    selectedProfile.presets.map { if (it.id == editing.id) newPreset else it }
                                } else {
                                    selectedProfile.presets + newPreset
                                }

                                val updatedProfiles = profileStore.profiles.map { p ->
                                    if (p.id == selectedProfile.id) {
                                        p.copy(presets = updatedPresets)
                                    } else {
                                        p
                                    }
                                }

                                val updatedStore = profileStore.copy(
                                    profiles = updatedProfiles,
                                    activePresetId = if (editing != null && profileStore.activePresetId == editing.id) {
                                        newPreset.id
                                    } else {
                                        profileStore.activePresetId ?: newPreset.id
                                    }
                                )
                                onStoreChange(updatedStore)
                                if (profileStore.activePresetId == newPreset.id || editing == null) {
                                    onApplyPreset(newPreset)
                                }
                                showPresetEditorDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = btnBgColor, contentColor = btnTextColor)
                        ) {
                            Text("Uložit", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
