import java.util.Scanner;
import java.util.ArrayList;

public class Perlica {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		ArrayList<Task> tasks = new ArrayList<>();
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

					default:
						throw new PerlicaException("I can't understand your command. Please try again.");
				}
			} catch (PerlicaException e) {
				printResponse(" " + e.getMessage());
			} catch (NumberFormatException e) {
				printResponse("Invalid task index.");
			}
		}
	}

	static void printWelcomeMessage() {
		printResponse(" Hello! I'm Perlica", " What can I do for you?");

	}

	static void printResponse(String... lines) {
		System.out.println("____________________________________________________________");
		for (String line : lines) {
			System.out.println(line);
		}
		System.out.println("____________________________________________________________");
	}

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
						: " OK, I've marked this task as not done yet:"
				, "   " + task);
	}

	static void printList(ArrayList<Task> tasks) {
		if (tasks.isEmpty()) {
			printResponse(" No tasks yet.");
		} else {
			String[] listOutput = new String[tasks.size()];
			for (int i = 0; i < tasks.size(); i++) {
				listOutput[i] = " " + (i + 1) + "." + tasks.get(i);
			}
			printResponse(listOutput);
		}
	}

	static void deleteTask(String[] input, ArrayList<Task> tasks) throws PerlicaException {
		if (input.length < 2) {
			throw new PerlicaException("Please specify which task to delete.");
		}
		int index = Integer.parseInt(input[1]) - 1;
		if (index >= tasks.size() || index < 0) {
			throw new PerlicaException("Invalid task index.");
		}
		String taskDeleted = tasks.get(index).toString();
		tasks.remove(index);
		printResponse("Noted. I've removed this task:"
				, "   " + taskDeleted
				, "Now you have " + tasks.size() + " task(s) in the list.");
	}
}
