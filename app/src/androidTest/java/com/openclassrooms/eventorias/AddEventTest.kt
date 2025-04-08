package com.openclassrooms.eventorias

import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest

@RunWith(AndroidJUnit4::class)
class AddEventTest : KoinTest {


    /*@get:Rule
    val koinTestRule = KoinTestRule(listOf())*/

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testAddEvent() {


        composeTestRule.waitUntil(timeoutMillis = 50000) {
            composeTestRule
                .onNodeWithText("Sign in with email")
                .isDisplayed()
        }


        composeTestRule
            .onNodeWithText("Sign in with email")
            .performClick()


        composeTestRule.waitUntil(timeoutMillis = 50000) {
            composeTestRule
                .onNodeWithText("Already have an account? Login")
                .isDisplayed()
        }


        composeTestRule
            .onNodeWithText("Already have an account? Login")
            .performClick()


        composeTestRule.waitUntil(timeoutMillis = 50000) {
            composeTestRule
                .onNodeWithText("Please enter your password.")
                .isDisplayed()
        }


        composeTestRule
            .onNodeWithTag("emailInput")
            .performTextInput("fakehhkgugugugufugubkdt@mail.com")


        composeTestRule
            .onNodeWithTag("passwordInput")
            .performTextInput("Gjgjgjvkfyfuvk")



        composeTestRule
            .onNodeWithTag("logInButton")
            .performClick()


        // WAIT Loading
        composeTestRule.waitUntil(timeoutMillis = 50000) {
            composeTestRule
                .onNodeWithContentDescription("Add an Event")
                .isDisplayed()
        }


        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule
                .onAllNodesWithTag("eventItemTag")
                .fetchSemanticsNodes().isNotEmpty()
        }



        composeTestRule
            .onNodeWithContentDescription("Add an Event")
            .performClick()


        composeTestRule
            .onNodeWithTag("eventTitleInput")
            .performTextInput("Nouvel Événement")


        composeTestRule
            .onNodeWithTag("eventDescriptionInput")
            .performTextInput("Ceci est un test")


        composeTestRule
            .onNodeWithTag("eventAddressInput")
            .performTextInput("Paris")


        composeTestRule.onNodeWithTag("eventDateInput")
            .performClick()


        //Click on the "OK" button
        composeTestRule
            .onNodeWithText("OK")
            .performClick()



        composeTestRule.onNodeWithTag("eventTimeInput")
            .performClick()


        //Click on the "OK" button
        composeTestRule
            .onNodeWithText("OK")
            .performClick()


        composeTestRule
            .onNodeWithText("Validate")
            .performClick()


        composeTestRule.waitUntil(timeoutMillis = 50000) {
            composeTestRule
                .onNodeWithContentDescription("Add an Event")
                .isDisplayed()
        }



        composeTestRule.waitUntil(timeoutMillis = 50000) {
            try {
                composeTestRule.onNodeWithTag("LazyEvent").performScrollToNode(hasText("Nouvel Événement"))
            } catch (_: Exception) {
                println("testAddEvent : LazyEvent not found")
            }

            composeTestRule
                .onNodeWithText("Nouvel Événement")
                .isDisplayed()
        }


    }
}
