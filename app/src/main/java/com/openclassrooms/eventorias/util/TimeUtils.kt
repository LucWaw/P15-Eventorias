package com.openclassrooms.eventorias.util

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class TimeUtils {
    companion object {
        /**
         * Converts a LocalTime object to a human-readable time string.
         * If locale is in English (en), the time will be formatted as "HH:MM AM/PM".
         * If locale is in French (fr), the time will be formatted as "HH:MM".
         */
        fun LocalTime.toHumanTime(locale: Locale = Locale.getDefault()): String {
            return when (locale.language) {
                "en" -> {
                    val formatter = DateTimeFormatter.ofPattern("h:mm a", locale)
                    this.format(formatter)
                }

                "fr" -> {
                    val formatter = DateTimeFormatter.ofPattern("HH:mm", locale)
                    this.format(formatter)
                }

                else -> this.toString()
            }
        }

        /**
         * Convert a int hours, int minutes to a hum
         */
    }
}