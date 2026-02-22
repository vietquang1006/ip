package perlica.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import perlica.parser.DateParser;
import perlica.exception.PerlicaException;

/**
 * Represents a task with a deadline in the Perlica application.
 * A deadline task contains a description and a specific time by which it needs
 * to be completed.
 */
public class Deadline extends Task {
    private LocalDateTime time;

    /**
     * Constructs a {@code Deadline} task with the given description and time.
     * Parses the time string into a {@code LocalDateTime} object.
     *
     * @param description The description of the deadline task.
     * @param timeString  The deadline time in a recognized string format.
     * @throws PerlicaException If the time string is in an invalid format.
     */
    public Deadline(String description, String timeString) throws PerlicaException {
        super(description);
        try {
            this.time = DateParser.parse(timeString);
        } catch (DateTimeParseException e) {
            throw new PerlicaException("Invalid date format. Try d/M/yyyy HHmm (e.g., 2/12/2019 1800)");
        }
    }

    /**
     * Returns a string representation of the deadline task.
     * The format includes the task type ("D"), its completion status, its
     * description, and its formatted time.
     *
     * @return A formatted string representing the deadline task.
     */
    @Override
    public String toString() {
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + DateParser.format(time);
    }
}
