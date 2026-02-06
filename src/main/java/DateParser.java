import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

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

    public static String format(LocalDateTime date) {
        return date.format(OUTPUT_FORMAT);
    }
}
