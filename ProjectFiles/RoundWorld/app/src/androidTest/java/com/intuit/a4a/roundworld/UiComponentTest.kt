package com.intuit.a4a.roundworld

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.intuit.a4a.roundworld.ui.components.ErrorStateUI
import com.intuit.a4a.roundworld.ui.components.LOADING_SPINNER_TEST_TAG_NAME
import com.intuit.a4a.roundworld.ui.components.LoadingScreenUI

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class UiComponentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun errorUiTest() {
        composeTestRule.setContent {
            ErrorStateUI(exception = Exception("Test Exception"))
        }

        composeTestRule.onNodeWithText("Error loading country list, error is Test Exception").assertIsDisplayed()
    }

    @Test
    fun loadingUiTest() {
        composeTestRule.setContent {
            LoadingScreenUI()
        }

        composeTestRule.onNodeWithTag(LOADING_SPINNER_TEST_TAG_NAME).assertIsDisplayed()
    }
}