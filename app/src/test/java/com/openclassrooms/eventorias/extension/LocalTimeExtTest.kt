package com.openclassrooms.eventorias.extension

import com.openclassrooms.eventorias.extension.LocalTimeExt.Companion.toHumanTime
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.LocalTime
import java.util.Locale

class LocalTimeExtTest {

    @Test
    fun test() {
        // Given
        val time = LocalTime.of(15, 6)

        // When
        val timeHuman = time.toHumanTime(Locale.ENGLISH)

        // Then
        assertEquals("3:06 PM", timeHuman)
    }

    @Test
    fun test2() {
        // Given
        val time = LocalTime.of(22, 6)

        // When
        val timeHuman = time.toHumanTime(Locale.ENGLISH)

        // Then
        assertEquals("10:06 PM", timeHuman)
    }

    @Test
    fun test3() {
        // Given
        val time = LocalTime.of(10, 0) // Assuming this is a time

        // When
        val timeHuman = time.toHumanTime(Locale.ENGLISH)

        // Then
        assertEquals("10:00 AM", timeHuman)
    }

    @Test
    fun test4() {
         val time = LocalTime.of(22, 6)

        // When
        val timeHuman = time.toHumanTime(Locale.FRENCH)

        // Then
        assertEquals("22:06", timeHuman)
    }

    @Test
    fun test5() {
         val time = LocalTime.of(6, 6)

        // When
        val timeHuman = time.toHumanTime(Locale.FRENCH)

        // Then
        assertEquals("06:06", timeHuman)
    }
}