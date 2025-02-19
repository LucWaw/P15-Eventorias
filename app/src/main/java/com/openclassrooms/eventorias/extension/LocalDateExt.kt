package com.openclassrooms.eventorias.extension

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class LocalDateExt {
    companion object{
        /**
         * Converts a LocalDate object to a human-readable date string.
         * If locale is in English (en), the date will be formatted as "Month Day, Year".
         * If locale is in French (fr), the date will be formatted as "Day Month Year".
         */
        fun LocalDate.toHumanDate(locale: Locale = Locale.getDefault()) : String {
            return when (locale.language) {
                "en" -> {
                    val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", locale)
                    this.format(formatter)
                }
                "fr" -> {
                    val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", locale)
                    this.format(formatter)
                }
                else -> this.toString()
            }
        }
    }
}