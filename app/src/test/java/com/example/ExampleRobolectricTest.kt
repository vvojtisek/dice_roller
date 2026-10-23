package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.*
import androidx.compose.ui.Modifier
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
}
