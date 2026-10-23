package com.example

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class DiceLogicTest {

  // ---------- rollDice ----------

  @Test
  fun `rollDice returns the requested number of values`() {
    for (count in MIN_DICE..MAX_DICE) {
      assertEquals(count, rollDice(count, 6, Random(42)).size)
    }
  }

  @Test
  fun `rollDice values stay within 1 to sides for every supported die`() {
    val random = Random(1234)
    for (sides in listOf(4, 6, 8, 10, 12, 20)) {
      repeat(2_000) {
        rollDice(MAX_DICE, sides, random).forEach { value ->
          assertTrue("d$sides produced $value", value in 1..sides)
        }
      }
    }
  }

  @Test
  fun `rollDice hits every face of a d20`() {
    val seen = rollDice(5_000, 20, Random(7)).toSet()
    assertEquals((1..20).toSet(), seen)
  }

  @Test
  fun `rollDice is deterministic for a seeded random`() {
    assertEquals(rollDice(4, 20, Random(99)), rollDice(4, 20, Random(99)))
  }

  @Test(expected = IllegalArgumentException::class)
  fun `rollDice rejects a die without sides`() {
    rollDice(1, 0)
  }

  // ---------- settleRotation ----------

  @Test
  fun `settleRotation snaps to the nearest multiple without jitter`() {
    assertEquals(900f, settleRotation(930f, 90f, 0f), 0.0001f)
    assertEquals(990f, settleRotation(950f, 90f, 0f), 0.0001f)
    assertEquals(1080f, settleRotation(1200f, 360f, 0f), 0.0001f)
  }

  @Test
  fun `settleRotation adds jitter after snapping`() {
    assertEquals(903.5f, settleRotation(930f, 90f, 3.5f), 0.0001f)
    assertEquals(896f, settleRotation(930f, 90f, -4f), 0.0001f)
  }

  // ---------- statsKeyFor ----------

  @Test
  fun `six sided variants share the d6 statistics bucket`() {
    listOf("pips", "numeral", "swatch_two_color", "swatch_three_color", "swatch_six_color")
      .forEach { assertEquals("d6", statsKeyFor(it)) }
  }

  @Test
  fun `polyhedral dice keep their own statistics bucket`() {
    listOf("d4", "d8", "d10", "d12", "d20").forEach { assertEquals(it, statsKeyFor(it)) }
  }

  // ---------- dieSelectionFor ----------

  @Test
  fun `dieSelectionFor maps plain die types to themselves`() {
    listOf("pips", "numeral", "d4", "d8", "d10", "d12", "d20").forEach {
      assertEquals(DieSelection(it, null), dieSelectionFor(it))
    }
  }

  @Test
  fun `dieSelectionFor maps swatch die types to the swatch selection and color mode`() {
    assertEquals(DieSelection("swatch", ColorMode.TWO_COLOR), dieSelectionFor("swatch_two_color"))
    assertEquals(DieSelection("swatch", ColorMode.THREE_COLOR), dieSelectionFor("swatch_three_color"))
    assertEquals(DieSelection("swatch", ColorMode.SIX_COLOR), dieSelectionFor("swatch_six_color"))
  }

  @Test
  fun `dieSelectionFor returns null for unknown die types`() {
    assertNull(dieSelectionFor("d100"))
    assertNull(dieSelectionFor(""))
  }

  @Test
  fun `every valid die type id resolves to a selection`() {
    VALID_DIE_TYPE_IDS.forEach { assertTrue(it, dieSelectionFor(it) != null) }
  }

  // ---------- addNewestBounded ----------

  @Test
  fun `addNewestBounded keeps newest first and caps the size`() {
    val history = mutableListOf<Int>()
    for (i in 1..20) history.addNewestBounded(i)
    assertEquals(MAX_HISTORY_SIZE, history.size)
    assertEquals(20, history.first())
    assertEquals(6, history.last())
  }

  @Test
  fun `addNewestBounded respects a custom limit`() {
    val history = mutableListOf("a", "b")
    history.addNewestBounded("c", maxSize = 2)
    assertEquals(listOf("c", "a"), history)
  }

  // ---------- chiSquareFairness ----------

  @Test
  fun `chiSquareFairness returns null without rolls`() {
    assertNull(chiSquareFairness(LongArray(6)))
  }

  @Test
  fun `perfectly uniform counts look fair`() {
    val result = chiSquareFairness(longArrayOf(10, 10, 10, 10, 10, 10))!!
    assertEquals(0.0, result.chiSquare, 1e-9)
    assertEquals(5, result.degreesOfFreedom)
    assertEquals(1.0, result.pValue, 1e-9)
    assertTrue(result.looksFair)
  }

  @Test
  fun `moderate deviation matches the exact chi-square p-value closely`() {
    // chi2 = 7.142857 with df = 5; the exact upper-tail p-value is 0.2102.
    val result = chiSquareFairness(longArrayOf(10, 10, 10, 10, 10, 20))!!
    assertEquals(7.142857, result.chiSquare, 1e-5)
    assertEquals(0.2102, result.pValue, 0.01)
    assertTrue(result.looksFair)
  }

  @Test
  fun `a die that always lands on one face looks biased`() {
    val result = chiSquareFairness(longArrayOf(120, 0, 0, 0, 0, 0))!!
    assertTrue(result.pValue < FAIRNESS_P_VALUE_THRESHOLD)
    assertFalse(result.looksFair)
  }

  @Test
  fun `seeded fair rolls are not flagged as biased`() {
    val counts = LongArray(20)
    rollDice(10_000, 20, Random(2024)).forEach { counts[it - 1]++ }
    assertTrue(chiSquareFairness(counts)!!.looksFair)
  }

  // ---------- profile store ----------

  @Test
  fun `default profile store is consistent`() {
    val store = createDefaultProfileStore()
    assertEquals(store, sanitizeProfileStore(store))
    val active = store.profiles.single { it.id == store.activeProfileId }
    assertTrue(active.presets.any { it.id == store.activePresetId })
    active.presets.forEach { assertTrue(it.dieTypeId in VALID_DIE_TYPE_IDS) }
  }

  @Test
  fun `sanitizeProfileStore drops presets with unknown die types and reports them`() {
    val reported = mutableListOf<String>()
    val store = ProfileStore(
      profiles = listOf(
        DiceProfile(
          id = "p",
          presets = listOf(
            DicePreset(id = "ok", dieTypeId = "d20"),
            DicePreset(id = "bad", dieTypeId = "d100"),
          )
        )
      ),
      activeProfileId = "p",
      activePresetId = "ok",
    )
    val sanitized = sanitizeProfileStore(store) { reported += it }
    assertEquals(listOf("ok"), sanitized.profiles.single().presets.map { it.id })
    assertEquals(listOf("d100"), reported)
    assertEquals("ok", sanitized.activePresetId)
  }

  @Test
  fun `sanitizeProfileStore repoints a removed active preset to the first remaining one`() {
    val store = ProfileStore(
      profiles = listOf(
        DiceProfile(
          id = "p",
          presets = listOf(
            DicePreset(id = "first", dieTypeId = "d6_invalid"),
            DicePreset(id = "second", dieTypeId = "d8"),
          )
        )
      ),
      activeProfileId = "p",
      activePresetId = "first",
    )
    assertEquals("second", sanitizeProfileStore(store).activePresetId)
  }

  @Test
  fun `sanitizeProfileStore falls back to the first profile when the active one is missing`() {
    val store = ProfileStore(
      profiles = listOf(
        DiceProfile(id = "a", presets = listOf(DicePreset(id = "a1", dieTypeId = "d4"))),
        DiceProfile(id = "b", presets = listOf(DicePreset(id = "b1", dieTypeId = "d12"))),
      ),
      activeProfileId = "deleted",
      activePresetId = "b1",
    )
    val sanitized = sanitizeProfileStore(store)
    assertEquals("a", sanitized.activeProfileId)
    assertEquals("a1", sanitized.activePresetId)
  }

  @Test
  fun `sanitizeProfileStore handles an empty store`() {
    val sanitized = sanitizeProfileStore(ProfileStore())
    assertNull(sanitized.activeProfileId)
    assertNull(sanitized.activePresetId)
  }

  @Test
  fun `profile store survives a JSON round trip`() {
    val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }
    val store = createDefaultProfileStore()
    assertEquals(store, json.decodeFromString<ProfileStore>(json.encodeToString(store)))
  }

  @Test
  fun `profile store JSON ignores unknown fields from newer app versions`() {
    val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }
    val decoded = json.decodeFromString<ProfileStore>(
      """{"schemaVersion":2,"futureField":true,"profiles":[],"activeProfileId":null}"""
    )
    assertEquals(2, decoded.schemaVersion)
    assertTrue(decoded.profiles.isEmpty())
  }
}
