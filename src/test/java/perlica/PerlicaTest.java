package perlica;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import perlica.exception.PerlicaException;
import perlica.task.Task;
import perlica.task.Todo;

public class PerlicaTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    private String getOutputLines(int startLineFromBottom) {
        String[] lines = outContent.toString().split(System.lineSeparator());
        if (lines.length >= startLineFromBottom) {
            return lines[lines.length - startLineFromBottom];
        }
        return "";
    }

    @Test
    public void printList_emptyList_showsNoTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Perlica.printList(tasks);
        assertTrue(outContent.toString().contains("No tasks yet."));
    }

    @Test
    public void printList_nonEmptyList_showsTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Todo("read book"));
        Perlica.printList(tasks);
        assertTrue(outContent.toString().contains("1 | T | 0 | read book"));
    }

    @Test
    public void deleteTask_validIndex_success() throws PerlicaException {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Todo("read book"));
        tasks.add(new Todo("write code"));

        String[] input = {"delete", "1"};
        Perlica.deleteTask(input, tasks);

        assertEquals(1, tasks.size());
        assertEquals("write code", tasks.get(0).getDescription());
        assertTrue(outContent.toString().contains("Noted. I've removed this task:"));
        assertTrue(outContent.toString().contains("T | 0 | read book"));
        assertTrue(outContent.toString().contains("Now you have 1 task(s) in the list."));
    }

    @Test
    public void deleteTask_deleteAll_success() throws PerlicaException {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Todo("read book"));
        tasks.add(new Todo("write code"));

        String[] input = {"delete", "all"};
        Perlica.deleteTask(input, tasks);

        assertEquals(0, tasks.size());
        assertTrue(outContent.toString().contains("Noted. I've removed all tasks."));
        assertTrue(outContent.toString().contains("Now you have 0 task(s) in the list."));
    }

    @Test
    public void deleteTask_invalidIndex_throwsException() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Todo("read book"));

        String[] input1 = {"delete", "2"};
        assertThrows(PerlicaException.class, () -> Perlica.deleteTask(input1, tasks));

        String[] input2 = {"delete", "0"};
        assertThrows(PerlicaException.class, () -> Perlica.deleteTask(input2, tasks));
    }

    @Test
    public void deleteTask_invalidStringIndex_throwsException() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Todo("read book"));

        String[] input = {"delete", "abc"};
        Exception exception = assertThrows(PerlicaException.class, () -> Perlica.deleteTask(input, tasks));
        assertEquals("Invalid task index. Please provide a number or 'all'.", exception.getMessage());
    }
}
