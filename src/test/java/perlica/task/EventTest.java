package perlica.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import perlica.exception.PerlicaException;

public class EventTest {
    @Test
    public void toString_validFormat_success() throws PerlicaException {
        Event event = new Event("project meeting", "2/12/2019 1800", "2/12/2019 2000");
        assertEquals("E | 0 | project meeting | Dec 02 2019 18:00 | Dec 02 2019 20:00", event.toString());
    }

    @Test
    public void constructor_invalidDateFormat_exceptionThrown() {
        assertThrows(PerlicaException.class, () -> new Event("project meeting", "wrong date", "2/12/2019 2000"));
    }
}
