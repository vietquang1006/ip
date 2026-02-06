import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class Deadline extends Task {
    private LocalDateTime time;

    public Deadline(String description, String timeString) throws PerlicaException {
        super(description);
        try {
            this.time = DateParser.parse(timeString);
        } catch (DateTimeParseException e) {
            throw new PerlicaException("Invalid date format. Try d/M/yyyy HHmm (e.g., 2/12/2019 1800)");
        }
    }

    @Override
    public String toString() {
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + DateParser.format(time);
    }
}
