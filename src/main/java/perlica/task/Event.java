package perlica.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import perlica.parser.DateParser;
import perlica.exception.PerlicaException;

public class Event extends Task {
    private LocalDateTime start;
    private LocalDateTime end;

    public Event(String description, String startString, String endString) throws PerlicaException {
        super(description);
        try {
            this.start = DateParser.parse(startString);
            this.end = DateParser.parse(endString);
        } catch (DateTimeParseException e) {
            throw new PerlicaException("Invalid date format. Try d/M/yyyy HHmm (e.g., 2/12/2019 1800)");
        }
    }

    @Override
    public String toString() {
        return "E | " + (isDone ? "1" : "0") + " | " + description + " | " + DateParser.format(start) + " | "
                + DateParser.format(end);
    }
}
