package perlica.parser;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

/**
 * A utility class for parsing and formatting dates and times in the Perlica
 * application.
 * It supports various date and date-time formats for input and standardizes the
 * output format.
 */
public class DateParser {
    private static final List<DateTimeFormatter> FORMATS = Arrays.asList(
            DateTimeFormatter.ofPattern("d/M/yyyy HHmm"),
            DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm"), // Format used for toString/Storage
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"),
            DateTimeFormatter.ofPattern("d/M/yyyy HH:mm"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

    // Formats that are date-only (we will set time to 23:59)
    private static final List<DateTimeFormatter> DATE_ONLY_FORMATS = Arrays.asList(
            DateTimeFormatter.ofPattern("d/M/yyyy"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("MMM dd yyyy"));

    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");

    /**
     * Parses a string representation of a date and time into a
     * {@code LocalDateTime} object.
     * Attempts to parse using a list of predefined date-time formats. If those
     * fail,
     * attempts to parse using predefined date-only formats, defaulting the time to
     * 23:59.
     *
     * @param dateString The string representation of the date (and time) to parse.
     * @return A {@code LocalDateTime} object representing the parsed date and time.
     * @throws DateTimeParseException If the provided string cannot be parsed by any
     *                                supported format.
     */
    public static LocalDateTime parse(String dateString) throws DateTimeParseException {
        // Try date-time formats
        for (DateTimeFormatter formatter : FORMATS) {
            try {
                return LocalDateTime.parse(dateString, formatter);
            } catch (DateTimeParseException ignored) {
                // Try next format
            }
        }

        // Try date-only formats
        for (DateTimeFormatter formatter : DATE_ONLY_FORMATS) {
            try {
                return LocalDate.parse(dateString, formatter).atTime(23, 59);
            } catch (DateTimeParseException ignored) {
                // Try next format
            }
        }

        throw new DateTimeParseException("Unable to parse date: " + dateString, dateString, 0);
    }

    /**
     * Formats a {@code LocalDateTime} object into a standardized string
     * representation.
     * The standard format used is "MMM dd yyyy HH:mm".
     *
     * @param date The {@code LocalDateTime} object to format.
     * @return A formatted string representation of the given date and time.
     */
    public static String format(LocalDateTime date) {
        return date.format(OUTPUT_FORMAT);
    }
}
