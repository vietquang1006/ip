package perlica.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import perlica.task.Task;
import perlica.task.Todo;
import perlica.task.Deadline;
import perlica.task.Event;
import perlica.exception.PerlicaException;

/**
 * Handles the loading and saving of tasks to the hard disk.
 * Connects the application's task list with a persistent storage file.
 */
public class Storage {
    private static final String FILE_PATH = "./data/perlica-list.txt";

    /**
     * Loads tasks from the persistent storage file.
     * If the file or directory does not exist, it returns an empty list.
     * Corrupted lines in the file are skipped.
     *
     * @return An {@code ArrayList} of {@code Task} objects recovered from the file.
     */
    public static ArrayList<Task> loadTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Path path = Paths.get(FILE_PATH);

        if (!Files.exists(path)) {
            return tasks;
        }

        try (Scanner scanner = new Scanner(path)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }
                try {
                    Task task = parseLineToTask(line);
                    tasks.add(task);
                } catch (Exception e) {
                    System.out.println("Corrupted task skipped: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return tasks;
    }

    /**
     * Saves a list of tasks to the persistent storage file.
     * Creates the required directories and file if they do not exist.
     *
     * @param tasks The {@code ArrayList} of {@code Task} objects to save.
     */
    public static void saveTasks(ArrayList<Task> tasks) {
        try {
            Path path = Paths.get(FILE_PATH);
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }

            FileWriter writer = new FileWriter(FILE_PATH);
            for (Task task : tasks) {
                writer.write(task.toString() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    /**
     * Parses a single line from the storage file into a corresponding {@code Task}
     * object.
     *
     * @param line A single line from the storage file representing a task.
     * @return A {@code Task} object corresponding to the parsed line format.
     * @throws PerlicaException If the line is improperly formatted and cannot be
     *                          parsed.
     */
    private static Task parseLineToTask(String line) throws PerlicaException {
        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            throw new PerlicaException("Invalid format");
        }

        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        Task task;
        switch (type) {
            case "T":
                task = new Todo(description);
                break;
            case "D":
                if (parts.length < 4)
                    throw new PerlicaException("Invalid deadline format");
                task = new Deadline(description, parts[3]);
                break;
            case "E":
                if (parts.length < 5)
                    throw new PerlicaException("Invalid event format");
                task = new Event(description, parts[3], parts[4]);
                break;
            default:
                throw new PerlicaException("Unknown task type");
        }
        if (isDone)
            task.mark();
        return task;
    }
}
