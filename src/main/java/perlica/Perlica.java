package perlica;

import java.util.ArrayList;
import java.util.Scanner;

import perlica.exception.PerlicaException;
import perlica.storage.Storage;
import perlica.task.Deadline;
import perlica.task.Event;
import perlica.task.Task;
import perlica.task.Todo;

/**
 * The main class for the Perlica task management application.
 * It handles the initialization of the application, user interaction loop, and
 * command execution.
 */
public class Perlica {

    private ArrayList<Task> tasks;

    /**
     * Initializes Perlica by loading tasks from storage.
     */
    public Perlica() {
        tasks = Storage.loadTasks();
    }

    /**
     * The entry point of the Perlica CLI application.
     * Initializes the scanner, loads existing tasks from storage, and enters the
     * main command loop.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        Perlica perlica = new Perlica();
        Scanner scanner = new Scanner(System.in);
        printWelcomeMessage();
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            printResponse(perlica.getResponse(input).split("\n"));
            if (input.trim().equals("bye")) {
                break;
            }
        }
        scanner.close();
    }

    /**
     * Prints the initial welcome message when the application starts.
     */
    static void printWelcomeMessage() {
        printResponse(" Hello! I'm Perlica", " What can I do for you?");
    }

    /**
     * Prints one or more custom messages framed by horizontal lines.
     *
     * @param lines An arbitrary number of string messages to be printed line by
     *              line.
     */
    static void printResponse(String... lines) {
        System.out.println("____________________________________________________________");
        for (String line : lines) {
            System.out.println(line);
        }
        System.out.println("____________________________________________________________");
    }

    /**
     * Adds a new task based on the user's input command.
     * Supports adding Todo, Deadline, and Event tasks.
     *
     * @param type  The type of task to add ("todo", "deadline", or "event").
     * @param input The parsed user input containing the command and task details.
     * @return The response string indicating success or error.
     * @throws PerlicaException If the input format is invalid or missing details.
     */
    private String addTask(String type, String[] input) throws PerlicaException {
        assert type.equals("todo") || type.equals("deadline") || type.equals("event") : "Invalid task type";
        if (input.length < 2 || input[1].trim().isEmpty()) {
            throw new PerlicaException("Invalid task.");
        }
        String details = input[1];
        Task task;
        switch (type) {
        case "todo":
            task = createTodo(details);
            break;
        case "deadline":
            task = createDeadline(details);
            break;
        case "event":
            task = createEvent(details);
            break;
        default:
            throw new PerlicaException("Unknown error");
        }
        tasks.add(task);
        return String.join("\n", "Got it. I've added this task:",
                "   " + task,
                "Now you have " + tasks.size() + " task(s) in the list.");
    }

    private Todo createTodo(String details) {
        return new Todo(details);
    }

    private Deadline createDeadline(String details) throws PerlicaException {
        if (!details.contains(" /by ")) {
            throw new PerlicaException("Invalid deadline, please try again.\n"
                    + "deadline + task + /by + time");
        }
        String[] arguments = details.split(" /by ", 2);
        if (arguments.length < 2 || arguments[0].trim().isEmpty() || arguments[1].trim().isEmpty()) {
            throw new PerlicaException("Invalid deadline, please try again.\n"
                    + "deadline + task + /by + time");
        }
        return new Deadline(arguments[0].trim(), arguments[1].trim());
    }

    private Event createEvent(String details) throws PerlicaException {
        if (!details.contains(" /from ") || !details.contains(" /to ")
                || details.indexOf(" /from ") > details.indexOf(" /to ")) {
            throw new PerlicaException("Invalid event, please try again.\n"
                    + "event + task + /from + startTime + /to + endTime");
        }
        String[] arguments = details.split(" /from | /to ", 3);
        if (arguments.length < 3 || arguments[0].trim().isEmpty()
                || arguments[1].trim().isEmpty() || arguments[2].trim().isEmpty()) {
            throw new PerlicaException("Invalid event, please try again.\n"
                    + "event + task + /from + startTime + /to + endTime");
        }
        return new Event(arguments[0].trim(), arguments[1].trim(), arguments[2].trim());
    }

    /**
     * Updates the completion status of a specific task.
     * Can either mark a task as done or unmark it as not done.
     *
     * @param type  The action to perform ("mark" or "unmark").
     * @param input The parsed user input containing the command and the task index.
     * @return The response string indicating success or error.
     * @throws PerlicaException If the index is invalid or the input is improperly
     *                          formatted.
     */
    private String updateMarking(String type, String[] input) throws PerlicaException {
        assert type.equals("mark") || type.equals("unmark") : "Type must be 'mark' or 'unmark'";
        if (input.length < 2) {
            throw new PerlicaException("Please specify which task to mark/unmark.");
        }
        int index = Integer.parseInt(input[1]) - 1;
        if (index >= tasks.size() || index < 0) {
            throw new PerlicaException("Invalid task index.");
        }
        Task task = tasks.get(index);
        if (type.equals("mark")) {
            task.mark();
        } else {
            task.unmark();
        }
        return String.join("\n", type.equals("mark")
                ? " Nice! I've marked this task as done:"
                : " OK, I've marked this task as not done yet:", "   " + task);
    }

    /**
     * Displays all current tasks in the task list.
     *
     * @return The constructed string representation of all tasks.
     */
    private String getList() {
        if (tasks.isEmpty()) {
            return " No tasks yet.";
        } else {
            String[] listOutput = new String[tasks.size()];
            for (int i = 0; i < tasks.size(); i++) {
                listOutput[i] = " " + (i + 1) + " | " + tasks.get(i);
            }
            return String.join("\n", listOutput);
        }
    }

    /**
     * Deletes a specific task from the task list based on its index.
     *
     * @param input The parsed user input containing the command and the task index.
     * @return The response string indicating success or error.
     * @throws PerlicaException If the index is invalid or the input is improperly
     *                          formatted.
     */
    private String deleteTask(String[] input) throws PerlicaException {
        if (input.length < 2) {
            throw new PerlicaException("Please specify which task to delete.");
        }

        if (input[1].trim().equalsIgnoreCase("all")) {
            tasks.clear();
            return String.join("\n", "Noted. I've removed all tasks.",
                    "Now you have 0 task(s) in the list.");
        }

        int index;
        try {
            index = Integer.parseInt(input[1]) - 1;
        } catch (NumberFormatException e) {
            throw new PerlicaException("Invalid task index. Please provide a number or 'all'.");
        }

        if (index >= tasks.size() || index < 0) {
            throw new PerlicaException("Invalid task index.");
        }
        String taskDeleted = tasks.get(index).toString();
        tasks.remove(index);
        return String.join("\n", "Noted. I've removed this task:", "   " + taskDeleted,
                "Now you have " + tasks.size() + " task(s) in the list.");
    }

    /**
     * Searches for and displays tasks whose descriptions contain the specified
     * keyword.
     *
     * @param input The parsed user input containing the command and the search
     *              keyword.
     * @return The formatted string of all matching tasks.
     * @throws PerlicaException If no keyword is provided.
     */
    private String findTask(String[] input) throws PerlicaException {
        if (input.length < 2 || input[1].trim().isEmpty()) {
            throw new PerlicaException("Please enter a keyword to find.");
        }
        String keyword = input[1].trim();
        ArrayList<String> matchingOutput = new ArrayList<>();
        matchingOutput.add(" Here are the matching tasks in your list:");
        int count = 1;
        for (Task task : tasks) {
            if (task.getDescription().contains(keyword)) {
                matchingOutput.add(" " + count + ". " + task);
                count++;
            }
        }
        if (count == 1) {
            return " No matching tasks found.";
        } else {
            return String.join("\n", matchingOutput);
        }
    }

    /**
     * Generates a response for the user's chat message by parsing the command.
     * 
     * @param inputString The raw user input command.
     * @return A formatted response showing the bot's reaction to the action.
     */
    public String getResponse(String inputString) {
        assert inputString != null : "Input string cannot be null";
        try {
            String[] input = inputString.trim().split(" ", 2);
            String command = input[0];
            String response = "";
            switch (command) {
            case "bye":
                if (input.length == 1) {
                    response = " Bye. Hope to see you again soon!";
                } else {
                    throw new PerlicaException("Hey, only saying bye is enough.");
                }
                break;
            case "list":
                if (input.length == 1) {
                    response = getList();
                } else {
                    throw new PerlicaException("Type \"list\" to view all tasks.");
                }
                break;
            case "mark":
            case "unmark":
                response = updateMarking(command, input);
                break;
            case "todo":
            case "deadline":
            case "event":
                response = addTask(command, input);
                break;
            case "delete":
                response = deleteTask(input);
                break;
            case "find":
                response = findTask(input);
                break;
            default:
                throw new PerlicaException("I can't understand your command. Please try again.");
            }
            Storage.saveTasks(tasks);
            return response;
        } catch (PerlicaException e) {
            return " " + e.getMessage();
        } catch (NumberFormatException e) {
            return "Invalid task index.";
        }
    }
}
