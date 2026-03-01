package perlica.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import perlica.exception.PerlicaException;

public class DeadlineTest {
    @Test
    public void toString_validFormat_success() throws PerlicaException {
        Deadline deadline = new Deadline("return book", "2/12/2019 1800");
        assertEquals("D | 0 | return book | Dec 02 2019 18:00", deadline.toString());
    }

    @Test
    public void constructor_invalidDateFormat_exceptionThrown() {
        assertThrows(PerlicaException.class, () -> new Deadline("return book", "wrong date"));
    }
}
