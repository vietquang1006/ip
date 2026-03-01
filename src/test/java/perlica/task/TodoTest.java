package perlica.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TodoTest {
    @Test
    public void toString_validDescription_success() {
        Todo todo = new Todo("read book");
        assertEquals("T | 0 | read book", todo.toString());
    }

    @Test
    public void mark_markAsDone_success() {
        Todo todo = new Todo("read book");
        todo.mark();
        assertTrue(todo.isDone());
        assertEquals("T | 1 | read book", todo.toString());
    }

    @Test
    public void unmark_unmarkAsDone_success() {
        Todo todo = new Todo("read book");
        todo.mark();
        todo.unmark();
        assertFalse(todo.isDone());
        assertEquals("T | 0 | read book", todo.toString());
    }
}
