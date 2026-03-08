package perlica.task;

/**
 * Represents a generic task in the Perlica application.
 * A task has a description and a completion status.
 */
public abstract class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Constructs a {@code Task} with the specified description.
     * The task is initially marked as not done.
     *
     * @param description The description of the task.
     */
    public Task(String description) {
        assert description != null : "Task description cannot be null";
        this.description = description;
        this.isDone = false;
    }

    /**
     * Marks the task as completed.
     */
    public void mark() {
        isDone = true;
    }

    /**
     * Marks the task as not completed.
     */
    public void unmark() {
        isDone = false;
    }

    /**
     * Returns the completion status of the task.
     *
     * @return {@code true} if the task is completed, {@code false} otherwise.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the description of the task.
     *
     * @return The description of the task.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns a string representing the status icon of the task.
     *
     * @return " |X| " if the task is done, " | | " otherwise.
     */
    public String getStatusIcon() {
        return isDone ? " |X| " : " | | ";
    }

    /**
     * Returns a string representation of the task.
     *
     * @return A string containing the task's status icon and description.
     */
    @Override
    public abstract String toString();
}
