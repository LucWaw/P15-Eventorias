package com.openclassrooms.eventorias.extension

import com.openclassrooms.eventorias.extension.LocalDateExt.Companion.toHumanDate
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.util.Locale

class LocalDateExtTest{
    @Test
    fun test(){
        // Given
        val date = LocalDate.of(2024, 6, 1)
        // When
        val result = date.toHumanDate(Locale.ENGLISH)
        // Then
        assertEquals("June 1, 2024", result)
    }

    @Test
    fun test2(){
        // Given
        val date = LocalDate.of(2024, 1, 1)
        // When
        val result = date.toHumanDate(Locale.ENGLISH)
        // Then
        assertEquals("January 1, 2024", result)
    }

    @Test
    fun test3(){
        // Given
        val date = LocalDate.of(2024, 1, 1)
        // When
        val result = date.toHumanDate(Locale.FRENCH)
        // Then
        assertEquals("1 janvier 2024", result)
    }

    @Test
    fun test4(){
        // Given
        val date = LocalDate.of(2003,12,31)
        // When
        val result = date.toHumanDate(Locale.FRENCH)
        // Then
        assertEquals("31 décembre 2003", result)
    }
}