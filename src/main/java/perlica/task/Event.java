package perlica.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import perlica.parser.DateParser;
import perlica.exception.PerlicaException;

/**
 * Represents an event task in the Perlica application.
 * An event task contains a description, a start time, and an end time.
 */
public class Event extends Task {
    private LocalDateTime start;
    private LocalDateTime end;

    /**
     * Constructs an {@code Event} task with the specified description, start time,
     * and end time.
     * Parses the time strings into {@code LocalDateTime} objects.
     *
     * @param description The description of the event task.
     * @param startString The start time of the event in a recognized string format.
     * @param endString   The end time of the event in a recognized string format.
     * @throws PerlicaException If any of the time strings are in an invalid format.
     */
    public Event(String description, String startString, String endString) throws PerlicaException {
        super(description);
        try {
            this.start = DateParser.parse(startString);
            this.end = DateParser.parse(endString);
        } catch (DateTimeParseException e) {
            throw new PerlicaException("Invalid date format. Try d/M/yyyy HHmm (e.g., 2/12/2019 1800)");
        }
    }

    /**
     * Returns a string representation of the event task.
     * The format includes the task type ("E"), its completion status, its
     * description, and its formatted start and end times.
     *
     * @return A formatted string representing the event task.
     */
    @Override
    public String toString() {
        return "E | " + (isDone ? "1" : "0") + " | " + description + " | " + DateParser.format(start) + " | "
                + DateParser.format(end);
    }
}
