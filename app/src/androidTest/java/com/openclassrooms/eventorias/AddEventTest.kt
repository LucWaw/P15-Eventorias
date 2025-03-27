package com.openclassrooms.eventorias

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest

@RunWith(AndroidJUnit4::class)
class AddEventTest : KoinTest {


    @get:Rule
    val koinTestRule = KoinTestRule(listOf())

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testAddEvent() {
        // Vérifier le nombre initial d'événements affichés
        val initialEventCount = composeTestRule
            .onAllNodesWithTag("eventItemTag")
            .fetchSemanticsNodes()
            .size

        // 1️⃣ - Cliquer sur le bouton "Add an Event"
        composeTestRule
            .onNodeWithTag("addEventButton")
            .performClick()

        // 2️⃣ - Remplir le formulaire (exemple avec un champ de texte)
        composeTestRule
            .onNodeWithTag("eventTitleInput")
            .performTextInput("Nouvel Événement")

        composeTestRule
            .onNodeWithTag("eventDescriptionInput")
            .performTextInput("Ceci est un test")

        // 3️⃣ - Valider l'ajout
        composeTestRule
            .onNodeWithTag("submitEventButton")
            .performClick()

        // 4️⃣ - Vérifier que le Home Screen affiche un événement en plus
        val newEventCount = composeTestRule
            .onAllNodesWithTag("eventItemTag")
            .fetchSemanticsNodes()
            .size

        assertEquals(initialEventCount + 1, newEventCount)
    }
}
