package perlica.task;

/**
 * Represents a to-do task in the Perlica application.
 * A to-do task is a basic task with a description but no specific deadline or
 * time constraints.
 */
public class Todo extends Task {

    /**
     * Constructs a {@code Todo} task with the specified description.
     *
     * @param description The definition of the to-do task.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns a string representation of the to-do task.
     * The format includes the task type ("T"), its completion status, and its
     * description.
     *
     * @return A formatted string representing the to-do task.
     */
    @Override
    public String toString() {
        return "T | " + (isDone ? "1" : "0") + " | " + description;
    }
}
