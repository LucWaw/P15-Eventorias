package com.openclassrooms.eventorias.util

import com.openclassrooms.eventorias.util.TimeUtils.Companion.toHumanTime
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.LocalTime
import java.util.Locale

class TimeUtilsTest {

    @Test
    fun testToHumanTime_english_pm() {
        // Given
        val time = LocalTime.of(15, 6)

        // When
        val timeHuman = time.toHumanTime(Locale.ENGLISH)

        // Then
        assertEquals("3:06 PM", timeHuman)
    }

    @Test
    fun testToHumanTime_english_pm2() {
        // Given
        val time = LocalTime.of(22, 6)

        // When
        val timeHuman = time.toHumanTime(Locale.ENGLISH)

        // Then
        assertEquals("10:06 PM", timeHuman)
    }

    @Test
    fun testToHumanTime_english_am() {
        // Given
        val time = LocalTime.of(10, 0) // Assuming this is a time

        // When
        val timeHuman = time.toHumanTime(Locale.ENGLISH)

        // Then
        assertEquals("10:00 AM", timeHuman)
    }

    @Test
    fun testToHumanTime_french_pm() {
         val time = LocalTime.of(22, 6)

        // When
        val timeHuman = time.toHumanTime(Locale.FRENCH)

        // Then
        assertEquals("22:06", timeHuman)
    }

    @Test
    fun testToHumanTime_french_am() {
         val time = LocalTime.of(6, 6)

        // When
        val timeHuman = time.toHumanTime(Locale.FRENCH)

        // Then
        assertEquals("06:06", timeHuman)
    }
}