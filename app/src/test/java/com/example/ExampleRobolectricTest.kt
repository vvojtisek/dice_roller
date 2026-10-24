package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import kotlin.math.abs
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.ui.theme.MyApplicationTheme
import org.junit.Rule

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Dice Roller", appName)
  }

  @Test
  fun `test ChemistryDiceColorSection tab selection`() {
    var colors by mutableStateOf(listOf("Porcelán", "Porcelán", "Porcelán"))
    // The app always shows this section inside a scrolling container; without one the lower
    // theme rows can fall outside Robolectric's default 320x470dp screen and miss the click.
    composeTestRule.setContent {
      Column(Modifier.verticalScroll(rememberScrollState())) {
        ChemistryDiceColorSection(
          numberOfDice = 3,
          diceColors = colors,
          onDiceColorChange = { idx, name ->
            val updated = colors.toMutableList()
            updated[idx] = name
            colors = updated
          },
          onApplyColorToAll = { name -> colors = List(3) { name } },
          isDarkTheme = true
        )
      }
    }

    composeTestRule.onNodeWithTag("die_color_tab_0").performScrollTo().performClick()
    composeTestRule.onNodeWithTag("theme_option_modrá_skalice").performScrollTo().performClick()
    println("DEBUG_AFTER_TAB_0: $colors")

    composeTestRule.onNodeWithTag("die_color_tab_1").performScrollTo().performClick()
    composeTestRule.onNodeWithTag("theme_option_rumělka").performScrollTo().performClick()
    println("DEBUG_AFTER_TAB_1: $colors")

    composeTestRule.onNodeWithTag("die_color_tab_2").performScrollTo().performClick()
    composeTestRule.onNodeWithTag("theme_option_malachit").performScrollTo().performClick()
    println("DEBUG_AFTER_TAB_2: $colors")

    assertEquals(listOf("Modrá skalice", "Rumělka", "Malachit"), colors)
  }

  @Test
  fun `ChemistryDiceColorSection offers pink, purple, grey and random swatches without labels`() {
    var colors by mutableStateOf(List(6) { "Porcelán" })
    composeTestRule.setContent {
      Column(Modifier.verticalScroll(rememberScrollState())) {
        ChemistryDiceColorSection(
          numberOfDice = 6,
          diceColors = colors,
          onDiceColorChange = { idx, name ->
            colors = colors.toMutableList().also { it[idx] = name }
          },
          onApplyColorToAll = { name -> colors = List(6) { name } },
          isDarkTheme = false
        )
      }
    }

    // Swatches carry no visible names; they are announced through content descriptions.
    composeTestRule.onAllNodesWithText("Malachit", substring = true).assertCountEquals(0)

    composeTestRule.onNodeWithTag("die_color_tab_3").performScrollTo().performClick()
    composeTestRule.onNodeWithTag("theme_option_rodochrozit").performScrollTo().performClick()
    composeTestRule.onNodeWithTag("die_color_tab_4").performScrollTo().performClick()
    composeTestRule.onNodeWithTag("theme_option_ametyst").performScrollTo().performClick()
    composeTestRule.onNodeWithTag("die_color_tab_5").performScrollTo().performClick()
    composeTestRule.onNodeWithTag("theme_option_olovo").performScrollTo().performClick()
    assertEquals(listOf("Rodochrozit", "Ametyst", "Olovo"), colors.takeLast(3))

    composeTestRule.onNodeWithTag("die_color_tab_0").performScrollTo().performClick()
    composeTestRule.onNodeWithTag("theme_option_náhodná").performScrollTo().performClick()
    assertEquals(RANDOM_DIE_COLOR, colors[0])

    composeTestRule.onNodeWithTag("apply_to_all_colors_button").performScrollTo().performClick()
    assertEquals(List(6) { RANDOM_DIE_COLOR }, colors)
  }

  @Test
  @Config(qualifiers = "w360dp-h640dp")
  fun `ChemistryColorCustomizerDialog shows every swatch and the done button without scrolling`() {
    composeTestRule.setContent {
      ChemistryColorCustomizerDialog(
        numberOfDice = 6,
        diceColors = List(6) { "Porcelán" },
        onDiceColorChange = { _, _ -> },
        onApplyColorToAll = {},
        onCloseClick = {},
        isDarkTheme = true
      )
    }

    (CHEMISTRY_THEME_NAMES + RANDOM_DIE_COLOR).forEach { name ->
      composeTestRule.onNodeWithTag("theme_option_${name.lowercase().replace(" ", "_")}").assertIsDisplayed()
    }
    composeTestRule.onNodeWithTag("done_color_dialog_button").assertIsDisplayed()
  }

  @Test
  fun `PlaygroundSection keeps three dice square with equal gaps`() {
    assertDiceAreSquareAndEvenlySpaced(3, listOf("Porcelán", "Modrá skalice", "Chromová žluť"))
  }

  @Test
  fun `PlaygroundSection lays out six dice in two rows of square dice`() {
    assertDiceAreSquareAndEvenlySpaced(
      6,
      listOf("Porcelán", "Modrá skalice", "Chromová žluť", "Rodochrozit", "Ametyst", "Olovo")
    )
    val top = (0..2).map { composeTestRule.onNodeWithTag("die_face_$it", useUnmergedTree = true).getBoundsInRoot().top }
    val bottom = (3..5).map { composeTestRule.onNodeWithTag("die_face_$it", useUnmergedTree = true).getBoundsInRoot().top }
    assertTrue("rows: $top / $bottom", top.toSet().size == 1 && bottom.toSet().size == 1 && bottom[0] > top[0])
  }

  private fun assertDiceAreSquareAndEvenlySpaced(count: Int, colors: List<String>) {
    // Built outside composition: Animatable must not be created during composition without remember.
    val dieStates = List(count) {
      DieAnimState(Animatable(0f), Animatable(1f), driftX = 0f, targetRotation = 0f)
    }
    composeTestRule.setContent {
      PlaygroundSection(
        numberOfDice = count,
        displayValues = List(count) { it % 6 + 1 },
        isDarkTheme = true,
        isRolling = false,
        dieStates = dieStates,
        dieType = DefaultDieTypes.pips,
        diceColors = colors,
        onDiceClick = {}
      )
    }

    val bounds = (0 until count).map { composeTestRule.onNodeWithTag("die_face_$it", useUnmergedTree = true).getBoundsInRoot() }
    val tolerance = 0.5.dp
    val edge = bounds[0].right - bounds[0].left
    bounds.forEachIndexed { i, b ->
      val width = b.right - b.left
      val height = b.bottom - b.top
      assertTrue("die $i is ${width}x$height", abs((width - height).value) <= tolerance.value)
      assertTrue("die $i edge $width vs $edge", abs((width - edge).value) <= tolerance.value)
    }
    // Horizontal gaps between neighbours in the same row are all equal.
    val gaps = bounds.zipWithNext()
      .filter { (a, b) -> abs((a.top - b.top).value) <= tolerance.value }
      .map { (a, b) -> (b.left - a.right).value }
    assertTrue("gaps $gaps", gaps.isNotEmpty() && gaps.all { abs(it - gaps[0]) <= tolerance.value && it > 0f })
  }
}
