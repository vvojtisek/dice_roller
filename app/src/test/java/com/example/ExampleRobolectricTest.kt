package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.*
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
    composeTestRule.setContent {
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

    composeTestRule.onNodeWithTag("die_color_tab_0").performClick()
    composeTestRule.onNodeWithTag("theme_option_modrá_skalice").performClick()
    println("DEBUG_AFTER_TAB_0: $colors")

    composeTestRule.onNodeWithTag("die_color_tab_1").performClick()
    composeTestRule.onNodeWithTag("theme_option_rumělka").performClick()
    println("DEBUG_AFTER_TAB_1: $colors")

    composeTestRule.onNodeWithTag("die_color_tab_2").performClick()
    composeTestRule.onNodeWithTag("theme_option_malachit").performClick()
    println("DEBUG_AFTER_TAB_2: $colors")

    assertEquals(listOf("Modrá skalice", "Rumělka", "Malachit"), colors)
  }
}
