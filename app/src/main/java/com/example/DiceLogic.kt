package com.example

import kotlinx.serialization.Serializable
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlin.random.Random

// ==========================================
// PURE DICE LOGIC (no Android or Compose dependencies, covered by unit tests)
// ==========================================

enum class ColorMode {
    TWO_COLOR, THREE_COLOR, SIX_COLOR
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

const val MIN_DICE = 1
const val MAX_DICE = 4
const val MAX_HISTORY_SIZE = 15

// Fairness verdict threshold used by the statistics panel.
const val FAIRNESS_P_VALUE_THRESHOLD = 0.01

val VALID_DIE_TYPE_IDS = listOf(
    "pips", "numeral", "d4", "d8", "d10", "d12", "d20",
    "swatch_two_color", "swatch_three_color", "swatch_six_color"
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

/**
 * Drops presets whose dieTypeId is unknown and repoints the active profile and preset
 * when they no longer exist. [onInvalidDieType] is called once per dropped preset.
 */
fun sanitizeProfileStore(
    store: ProfileStore,
    onInvalidDieType: (String) -> Unit = {}
): ProfileStore {
    val validatedProfiles = store.profiles.map { profile ->
        profile.copy(
            presets = profile.presets.filter { preset ->
                val isValid = preset.dieTypeId in VALID_DIE_TYPE_IDS
                if (!isValid) {
                    onInvalidDieType(preset.dieTypeId)
                }
                isValid
            }
        )
    }
    val updatedStore = store.copy(profiles = validatedProfiles)
    val activeProf = updatedStore.profiles.find { it.id == updatedStore.activeProfileId }
    return if (activeProf == null) {
        val firstProf = updatedStore.profiles.firstOrNull()
        updatedStore.copy(
            activeProfileId = firstProf?.id,
            activePresetId = firstProf?.presets?.firstOrNull()?.id
        )
    } else if (activeProf.presets.none { it.id == updatedStore.activePresetId }) {
        updatedStore.copy(activePresetId = activeProf.presets.firstOrNull()?.id)
    } else {
        updatedStore
    }
}

/** UI selection derived from a preset: the settings die type id plus the swatch color mode, if any. */
data class DieSelection(val selectedDieTypeId: String, val colorMode: ColorMode? = null)

/** Maps a stored preset dieTypeId to the UI selection, or null when the id is unknown. */
fun dieSelectionFor(dieTypeId: String): DieSelection? = when (dieTypeId) {
    "pips", "numeral", "d4", "d8", "d10", "d12", "d20" -> DieSelection(dieTypeId)
    "swatch_two_color" -> DieSelection("swatch", ColorMode.TWO_COLOR)
    "swatch_three_color" -> DieSelection("swatch", ColorMode.THREE_COLOR)
    else -> if (dieTypeId.startsWith("swatch_")) DieSelection("swatch", ColorMode.SIX_COLOR) else null
}

/** All six-sided variants (pips, numerals, color swatches) share the "d6" statistics bucket. */
fun statsKeyFor(dieTypeId: String): String = when {
    dieTypeId == "pips" || dieTypeId == "numeral" -> "d6"
    dieTypeId.startsWith("swatch") -> "d6"
    else -> dieTypeId
}

fun rollDice(count: Int, sides: Int, random: Random = Random.Default): List<Int> {
    require(sides >= 1) { "sides must be >= 1, was $sides" }
    require(count >= 0) { "count must be >= 0, was $count" }
    return List(count) { random.nextInt(1, sides + 1) }
}

/**
 * Final rotation for a rolling die: [baseTravel] rounded to the nearest multiple of
 * [snapDegrees] so the face lands upright, plus a small [jitter] for a natural look.
 */
fun settleRotation(baseTravel: Float, snapDegrees: Float, jitter: Float): Float {
    require(snapDegrees > 0f) { "snapDegrees must be > 0, was $snapDegrees" }
    return (baseTravel / snapDegrees).roundToInt() * snapDegrees + jitter
}

/** Inserts [item] at the front and drops the oldest entries beyond [maxSize]. */
fun <T> MutableList<T>.addNewestBounded(item: T, maxSize: Int = MAX_HISTORY_SIZE) {
    add(0, item)
    while (size > maxSize) {
        removeAt(size - 1)
    }
}

data class FairnessResult(val chiSquare: Double, val degreesOfFreedom: Int, val pValue: Double) {
    val looksFair: Boolean get() = pValue >= FAIRNESS_P_VALUE_THRESHOLD
}

/**
 * Pearson chi-square goodness-of-fit test against a uniform die. The p-value uses the
 * Wilson-Hilferty normal approximation and a logistic approximation of the normal CDF,
 * which is accurate to roughly 1e-4 and adequate for a fair/biased hint.
 * Returns null when there are no rolls or fewer than two faces.
 */
fun chiSquareFairness(counts: LongArray): FairnessResult? {
    val total = counts.sum()
    val sides = counts.size
    if (total == 0L || sides < 2) return null

    val expected = total.toDouble() / sides.toDouble()
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
        val term1 = (chi2 / df.toDouble()).pow(1.0 / 3.0)
        val term2 = 1.0 - (2.0 / (9.0 * df))
        val z = sqrt(part1) * (term1 - term2)

        val absZ = abs(z)
        val phi = 1.0 / (1.0 + exp(-1.5976 * absZ - 0.070566 * absZ * absZ * absZ))
        val pValApprox = if (z >= 0) 1.0 - phi else phi
        pValApprox.coerceIn(0.0, 1.0)
    }

    return FairnessResult(chi2, df, pValue)
}
