import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Storage {
	private static final String FILE_PATH = "./data/perlica-list.txt";

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