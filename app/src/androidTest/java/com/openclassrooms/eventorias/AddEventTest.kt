package com.openclassrooms.eventorias

import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
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

        println("testAddEvent : Start")

        composeTestRule.waitUntil(timeoutMillis = 50000) {
            composeTestRule
                .onNodeWithText("Sign in with email")
                .isDisplayed()
        }

        println("testAddEvent : Sign in with email displayed")

        composeTestRule
            .onNodeWithText("Sign in with email")
            .performClick()

        println("testAddEvent : Sign in with email clicked")

        composeTestRule.waitUntil(timeoutMillis = 50000) {
            composeTestRule
                .onNodeWithText("Already have an account? Login")
                .isDisplayed()
        }

        println("testAddEvent : Already have an account? Login displayed")

        composeTestRule
            .onNodeWithText("Already have an account? Login")
            .performClick()

        println("testAddEvent : Already have an account? Login clicked")

        composeTestRule.waitUntil(timeoutMillis = 50000) {
            composeTestRule
                .onNodeWithText("Please enter your password.")
                .isDisplayed()
        }

        println("testAddEvent : Please enter your password. displayed")

        composeTestRule
            .onNodeWithTag("emailInput")
            .performTextInput("fakehhkgugugugufugubkdt@mail.com")

        println("testAddEvent : emailInput filled")

        composeTestRule
            .onNodeWithTag("passwordInput")
            .performTextInput("Gjgjgjvkfyfuvk")

        println("testAddEvent : passwordInput filled")


        composeTestRule
            .onNodeWithTag("logInButton")
            .performClick()

        println("testAddEvent : logInButton clicked")

        // WAIT Loading
        composeTestRule.waitUntil(timeoutMillis = 50000) {
            composeTestRule
                .onNodeWithContentDescription("Add an Event")
                .isDisplayed()
        }

        println("testAddEvent : Add an Event displayed")

        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule
                .onAllNodesWithTag("eventItemTag")
                .fetchSemanticsNodes().isNotEmpty()
        }

        println("testAddEvent : eventItemTag displayed")

        val initialEventCount = composeTestRule
            .onAllNodesWithTag("eventItemTag")
            .fetchSemanticsNodes()
            .size



        composeTestRule.onAllNodesWithTag("eventItemTag").printToLog("eventItemTag")

        println("testAddEvent : eventItemTag count : $initialEventCount")


        composeTestRule
            .onNodeWithContentDescription("Add an Event")
            .performClick()

        println("testAddEvent : Add an Event clicked")

        composeTestRule
            .onNodeWithTag("eventTitleInput")
            .performTextInput("Nouvel Événement")

        println("testAddEvent : eventTitleInput filled")

        composeTestRule
            .onNodeWithTag("eventDescriptionInput")
            .performTextInput("Ceci est un test")

        println("testAddEvent : eventDescriptionInput filled")

        composeTestRule
            .onNodeWithTag("eventAddressInput")
            .performTextInput("Paris")

        println("testAddEvent : eventAddressInput filled")

        composeTestRule.onNodeWithTag("eventDateInput")
            .performClick()

        println("testAddEvent : eventDateInput clicked")

        //Click on the "OK" button
        composeTestRule
            .onNodeWithText("OK")
            .performClick()


        println("testAddEvent : OK clicked")

        composeTestRule.onNodeWithTag("eventTimeInput")
            .performClick()

        println("testAddEvent : eventTimeInput clicked")

        //Click on the "OK" button
        composeTestRule
            .onNodeWithText("OK")
            .performClick()

        println("testAddEvent : OK clicked")

        composeTestRule
            .onNodeWithText("Validate")
            .performClick()

        println("testAddEvent : Validate clicked")

        composeTestRule.waitUntil(timeoutMillis = 50000) {
            composeTestRule
                .onNodeWithContentDescription("Add an Event")
                .isDisplayed()
        }


        println("testAddEvent : Add an Event displayed")

        composeTestRule.waitUntil(timeoutMillis = 50000) {
            composeTestRule
                .onAllNodesWithTag("eventItemTag")
                .fetchSemanticsNodes().size > initialEventCount
        }

        println("testAddEvent : eventItemTag displayed")


        val newEventCount = composeTestRule
            .onAllNodesWithTag("eventItemTag")
            .fetchSemanticsNodes()
            .size

        assertEquals(initialEventCount + 1, newEventCount)
    }
}
