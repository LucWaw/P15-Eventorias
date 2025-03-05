package com.openclassrooms.eventorias.util

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

class DateUtils {


    companion object {
        /**
         * Converts a LocalDate object to a human-readable date string.
         * If locale is in English (en), the date will be formatted as "Month Day, Year".
         * If locale is in French (fr), the date will be formatted as "Day Month Year".
         */
        fun LocalDate.toHumanDate(locale: Locale = Locale.getDefault()): String {
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

        /**
         * Converts a millisecond timestamp to a ZonedDateTime object in the local time zone.
         */
        fun convertMillisToLocalDate(millis: Long): LocalDate {

            val utcDateAtStartOfDay = Instant
                .ofEpochMilli(millis)
                .atZone(ZoneOffset.UTC)
                .toLocalDate()
            println("UTC Date at Start of Day: $utcDateAtStartOfDay") // Debugging UTC date

            // Convert to the same instant in Local time zone
            val localDate = utcDateAtStartOfDay.atStartOfDay(ZoneId.systemDefault())
            println("Local Date: $localDate") // Debugging local date

            return localDate.toLocalDate()

        }
    }
}