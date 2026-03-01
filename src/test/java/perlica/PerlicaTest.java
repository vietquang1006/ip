package perlica;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PerlicaTest {

    private Perlica perlica;

    @BeforeEach
    public void setUp() {
        perlica = new Perlica();
        // Clear all tasks for a clean slate
        perlica.getResponse("delete all");
    }

    @Test
    public void printList_emptyList_showsNoTasks() {
        String response = perlica.getResponse("list");
        assertTrue(response.contains("No tasks yet."));
    }

    @Test
    public void printList_nonEmptyList_showsTasks() {
        perlica.getResponse("todo read book");
        String response = perlica.getResponse("list");
        assertTrue(response.contains("1 | T | 0 | read book"));
    }

    @Test
    public void deleteTask_validIndex_success() {
        perlica.getResponse("todo read book");
        perlica.getResponse("todo write code");
        
        String response = perlica.getResponse("delete 1");

        assertTrue(response.contains("Noted. I've removed this task:"));
        assertTrue(response.contains("T | 0 | read book"));
        assertTrue(response.contains("Now you have 1 task(s) in the list."));
        
        String listResponse = perlica.getResponse("list");
        assertTrue(listResponse.contains("1 | T | 0 | write code"));
    }

    @Test
    public void deleteTask_deleteAll_success() {
        perlica.getResponse("todo read book");
        perlica.getResponse("todo write code");

        String response = perlica.getResponse("delete all");

        assertTrue(response.contains("Noted. I've removed all tasks."));
        assertTrue(response.contains("Now you have 0 task(s) in the list."));
    }

    @Test
    public void deleteTask_invalidIndex_returnsErrorMessage() {
        perlica.getResponse("todo read book");

        String response1 = perlica.getResponse("delete 2");
        assertTrue(response1.contains("Invalid task index."));

        String response2 = perlica.getResponse("delete 0");
        assertTrue(response2.contains("Invalid task index."));
    }

    @Test
    public void deleteTask_invalidStringIndex_returnsErrorMessage() {
        perlica.getResponse("todo read book");

        String response = perlica.getResponse("delete abc");
        assertTrue(response.contains("Invalid task index. Please provide a number or 'all'."));
    }
}
