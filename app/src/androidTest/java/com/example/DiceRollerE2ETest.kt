package com.example

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.getBoundsInRoot
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToKey
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * End-to-end tests: launch the real MainActivity on a device or emulator and drive the UI
 * the way a user would. Run with `./gradlew connectedDebugAndroidTest`.
 */
@OptIn(ExperimentalTestApi::class)
@RunWith(AndroidJUnit4::class)
class DiceRollerE2ETest {

  @get:Rule val composeTestRule = createAndroidComposeRule<MainActivity>()

  @Before
  fun setUp() {
    // Profiles load asynchronously from DataStore; the profile header appears once loaded.
    composeTestRule.waitUntilAtLeastOneExists(hasText("Profil:", substring = true), TIMEOUT_MS)
    // Silence the synthesized sounds so tests do not depend on audio output.
    composeTestRule.onNodeWithTag("mute_button").performClick()
  }

  @Test
  fun appStartsWithPlaygroundAndPresets() {
    composeTestRule.onNodeWithTag("playground_card").assertIsDisplayed()
    composeTestRule.onNodeWithTag("dice_throw_area").assertIsDisplayed()
    composeTestRule.onNodeWithTag("quick_presets_row").assertIsDisplayed()
    composeTestRule.onNodeWithText("KLEPNI NA KOSTKY PRO HOD").assertExists()
  }

  @Test
  fun tappingTheDiceRecordsARollInHistory() {
    composeTestRule.onNodeWithTag("dice_throw_area").performClick()

    val sum = waitForLatestHistorySum()
    // Default and persisted presets use 1 to MAX_DICE dice with at most 20 sides.
    assertTrue("Unexpected sum $sum", sum in 1..(MAX_DICE * 20))
  }

  @Test
  fun d20PresetRollsWithinOneToTwenty() {
    composeTestRule.onNodeWithTag("quick_presets_row").performScrollToKey("preset_d20")
    composeTestRule.onNodeWithTag("preset_chip_preset_d20").performClick()

    composeTestRule.onNodeWithTag("dice_throw_area").performClick()

    val sum = waitForLatestHistorySum()
    assertTrue("d20 rolled $sum", sum in 1..20)
  }

  @Test
  fun settingsChangeDiceCountAndType() {
    composeTestRule.onNodeWithTag("settings_button").performClick()
    composeTestRule.onNodeWithTag("dice_count_3").performScrollTo().performClick()
    composeTestRule.onNodeWithTag("die_type_d8").performScrollTo().performClick()
    composeTestRule.onNodeWithTag("save_settings_button").performScrollTo().performClick()

    composeTestRule.onNodeWithTag("dice_throw_area").performClick()

    val sum = waitForLatestHistorySum()
    assertTrue("3d8 rolled $sum", sum in 3..24)
    composeTestRule.onNodeWithTag("close_stats_history_button").performClick()
    composeTestRule.onNodeWithText("Celkový součet:", substring = true).assertExists()
  }

  @Test
  fun sixRandomColoredDiceRollInTwoRows() {
    composeTestRule.onNodeWithTag("settings_button").performClick()
    composeTestRule.onNodeWithTag("dice_count_6").performScrollTo().performClick()
    composeTestRule.onNodeWithTag("die_type_pips").performScrollTo().performClick()
    composeTestRule.onNodeWithTag("save_settings_button").performScrollTo().performClick()

    composeTestRule.onNodeWithTag("dice_colors_button").performClick()
    composeTestRule.onNodeWithTag("theme_option_náhodná").performScrollTo().performClick()
    composeTestRule.onNodeWithTag("apply_to_all_colors_button").performScrollTo().performClick()
    composeTestRule.onNodeWithTag("done_color_dialog_button").assertIsDisplayed().performClick()

    val faces = (0 until 6).map { composeTestRule.onNodeWithTag("die_face_$it").assertIsDisplayed().getBoundsInRoot() }
    faces.forEach { assertEquals((it.right - it.left).value, (it.bottom - it.top).value, 1f) }
    assertTrue("second row below the first", faces[3].top > faces[0].bottom)

    composeTestRule.onNodeWithTag("dice_throw_area").performClick()

    val sum = waitForLatestHistorySum()
    assertTrue("6d6 rolled $sum", sum in 6..36)
  }

  @Test
  fun statisticsDialogOpensAndCloses() {
    composeTestRule.onNodeWithTag("stats_history_button").performClick()
    composeTestRule.onNodeWithTag("stats_history_dialog").assertIsDisplayed()
    composeTestRule.onNodeWithTag("close_stats_history_button").performClick()
    composeTestRule.waitUntilDoesNotExist(hasTestTag("stats_history_dialog"), TIMEOUT_MS)
  }

  @Test
  fun profileManagerOpens() {
    composeTestRule.onNodeWithTag("manage_profiles_button").performClick()
    composeTestRule.waitUntilAtLeastOneExists(hasTestTag("profile_manager_card"), TIMEOUT_MS)
  }

  /** Opens statistics and history, waits for the roll to land and returns its sum. */
  private fun waitForLatestHistorySum(): Int {
    composeTestRule.onNodeWithTag("stats_history_button").performClick()
    composeTestRule.waitUntilAtLeastOneExists(hasText("Součet:", substring = true), TIMEOUT_MS)
    // History is newest first, so the first match is the roll that just happened.
    val node = composeTestRule.onAllNodes(hasText("Součet:", substring = true))[0]
    val text = node.fetchSemanticsNode().config[SemanticsProperties.Text].joinToString("")
    return text.substringAfter("Součet:").trim().toInt()
  }

  private companion object {
    const val TIMEOUT_MS = 15_000L
  }
}
