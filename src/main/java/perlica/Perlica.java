package perlica;

import java.util.Scanner;
import java.util.ArrayList;
import perlica.task.Task;
import perlica.task.Todo;
import perlica.task.Deadline;
import perlica.task.Event;
import perlica.storage.Storage;
import perlica.exception.PerlicaException;

/**
 * The main class for the Perlica task management application.
 * It handles the initialization of the application, user interaction loop, and
 * command execution.
 */
public class Perlica {
    /**
     * The entry point of the Perlica application.
     * Initializes the scanner, loads existing tasks from storage, and enters the
     * main command loop.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks = Storage.loadTasks();
        printWelcomeMessage();
        while (scanner.hasNextLine()) {
            try {
                String[] input = scanner.nextLine().trim().split(" ", 2);
                String command = input[0];
                switch (command) {
                    case "bye":
                        if (input.length == 1) {
                            printResponse(" Bye. Hope to see you again soon!");
                            return;
                        } else {
                            throw new PerlicaException("Hey, only saying bye is enough.");
                        }

                    case "list":
                        if (input.length == 1) {
                            printList(tasks);
                            break;
                        } else {
                            throw new PerlicaException("Type \"list\" to view all tasks.");
                        }

                    case "mark":
                    case "unmark":
                        updateMarking(command, input, tasks);
                        break;

                    case "todo":
                    case "deadline":
                    case "event":
                        addTask(command, input, tasks);
                        break;

                    case "delete":
                        deleteTask(input, tasks);
                        break;

                    case "find":
                        findTask(input, tasks);
                        break;

                    default:
                        throw new PerlicaException("I can't understand your command. Please try again.");
                }
                Storage.saveTasks(tasks);
            } catch (PerlicaException e) {
                printResponse(" " + e.getMessage());
            } catch (NumberFormatException e) {
                printResponse("Invalid task index.");
            }
        }
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
     * @param tasks The current list of tasks to which the new task will be added.
     * @throws PerlicaException If the input format is invalid or missing details.
     */
    static void addTask(String type, String[] input, ArrayList<Task> tasks) throws PerlicaException {
        if (input.length < 2 || input[1].trim().isEmpty()) {
            throw new PerlicaException("Invalid task.");
        }
        String details = input[1];
        Task task;
        switch (type) {
            case "todo":
                task = new Todo(details);
                break;
            case "deadline":
                if (!details.contains(" /by ")) {
                    throw new PerlicaException("Invalid deadline, please try again.\n"
                            + "deadline + task + /by + time");
                }
                String[] deadlineArguments = input[1].split(" /by ", 2);
                if (deadlineArguments.length < 2
                        || deadlineArguments[0].trim().isEmpty()
                        || deadlineArguments[1].trim().isEmpty()) {
                    throw new PerlicaException("Invalid deadline, please try again.\n"
                            + "deadline + task + /by + time");
                }
                String deadlineDescription = deadlineArguments[0].trim();
                String time = deadlineArguments[1].trim();
                task = new Deadline(deadlineDescription, time);
                break;
            case "event":
                if (!details.contains(" /from ") || !details.contains(" /to ")
                        || details.indexOf(" /from ") > details.indexOf(" /to ")) {
                    throw new PerlicaException("Invalid event, please try again.\n"
                            + "event + task + /from + startTime + /to + endTime");
                }
                String[] eventArguments = input[1].split(" /from | /to ", 3);
                if (eventArguments.length < 3
                        || eventArguments[0].trim().isEmpty()
                        || eventArguments[1].trim().isEmpty()
                        || eventArguments[2].trim().isEmpty()) {
                    throw new PerlicaException("Invalid event, please try again.\n"
                            + "deadline + task + /by + time");
                }
                String eventDescription = eventArguments[0];
                String start = eventArguments[1];
                String end = eventArguments[2];
                task = new Event(eventDescription, start, end);
                break;
            default:
                throw new PerlicaException("Unknown error");
        }
        tasks.add(task);
        printResponse("Got it. I've added this task:",
                "   " + task,
                "Now you have " + tasks.size() + " task(s) in the list.");
    }

    /**
     * Updates the completion status of a specific task.
     * Can either mark a task as done or unmark it as not done.
     *
     * @param type  The action to perform ("mark" or "unmark").
     * @param input The parsed user input containing the command and the task index.
     * @param tasks The current list of tasks containing the target task.
     * @throws PerlicaException If the index is invalid or the input is improperly
     *                          formatted.
     */
    static void updateMarking(String type, String[] input, ArrayList<Task> tasks) throws PerlicaException {
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
        printResponse(type.equals("mark")
                ? " Nice! I've marked this task as done:"
                : " OK, I've marked this task as not done yet:", "   " + task);
    }

    /**
     * Displays all current tasks in the task list.
     *
     * @param tasks The list of tasks to be displayed.
     */
    static void printList(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            printResponse(" No tasks yet.");
        } else {
            String[] listOutput = new String[tasks.size()];
            for (int i = 0; i < tasks.size(); i++) {
                listOutput[i] = " " + (i + 1) + " | " + tasks.get(i);
            }
            printResponse(listOutput);
        }
    }

    /**
     * Deletes a specific task from the task list based on its index.
     *
     * @param input The parsed user input containing the command and the task index.
     * @param tasks The current list of tasks from which the task will be removed.
     * @throws PerlicaException If the index is invalid or the input is improperly
     *                          formatted.
     */
    static void deleteTask(String[] input, ArrayList<Task> tasks) throws PerlicaException {
        if (input.length < 2) {
            throw new PerlicaException("Please specify which task to delete.");
        }
        
        if (input[1].trim().equalsIgnoreCase("all")) {
            tasks.clear();
            printResponse("Noted. I've removed all tasks.",
                    "Now you have 0 task(s) in the list.");
            return;
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
        printResponse("Noted. I've removed this task:", "   " + taskDeleted,
                "Now you have " + tasks.size() + " task(s) in the list.");
    }

    /**
     * Searches for and displays tasks whose descriptions contain the specified
     * keyword.
     *
     * @param input The parsed user input containing the command and the search
     *              keyword.
     * @param tasks The current list of tasks to search within.
     * @throws PerlicaException If no keyword is provided.
     */
    static void findTask(String[] input, ArrayList<Task> tasks) throws PerlicaException {
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
            printResponse(" No matching tasks found.");
        } else {
            printResponse(matchingOutput.toArray(new String[0]));
        }
    }
}
