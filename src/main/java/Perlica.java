import java.util.Scanner;

public class Perlica {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[100];
        int taskCount = 0;
        System.out.println("____________________________________________________________");
        System.out.println(" Hello! I'm Perlica");
        System.out.println(" What can I do for you?");
        System.out.println("____________________________________________________________");
        while (true) {
            String[] input = scanner.nextLine().trim().split(" ", 2);
            String command = input[0];
            switch (command) {
                case "bye":
                    if (input.length == 1) {
                        System.out.println("____________________________________________________________");
                        System.out.println(" Bye. Hope to see you again soon!");
                        System.out.println("____________________________________________________________");
                        return;
                    }

                case "list":
                    if (input.length == 1) {
                        System.out.println("____________________________________________________________");
                        if (taskCount == 0) {
                            System.out.println(" No tasks yet.");
                        } else {
                            for (int i = 0; i < taskCount; i++) {
                                System.out.println(" " + (i + 1) + "." + tasks[i]);
                            }
                        }
                        System.out.println("____________________________________________________________");
                    }
                    break;

                case "mark":
                    int markIndex = Integer.parseInt(input[1]) - 1;
                    tasks[markIndex].mark();
                    System.out.println("____________________________________________________________");
                    System.out.println(" Nice! I've marked this task as done:");
                    System.out.println("   " + tasks[markIndex]);
                    System.out.println("____________________________________________________________");
                    break;

                case "unmark":
                    int unmarkIndex = Integer.parseInt(input[1]) - 1;
                    tasks[unmarkIndex].unmark();
                    System.out.println("____________________________________________________________");
                    System.out.println(" OK, I've marked this task as not done yet:");
                    System.out.println("   " + tasks[unmarkIndex]);
                    System.out.println("____________________________________________________________");
                    break;

                case "todo":
                    Task newTodo = new Todo(input[1]);
                    tasks[taskCount] = newTodo;
                    taskCount++;
                    System.out.println("____________________________________________________________");
                    System.out.println("Got it. I've added this task:");
                    System.out.println("    " + newTodo);
                    System.out.println("Now you have " + taskCount + " tasks in the list.");
                    System.out.println("____________________________________________________________");
                    break;

                case "deadline":
                    String[] deadlineArguments = input[1].split(" /by ", 2);
                    String deadlineDescription = deadlineArguments[0];
                    String time = deadlineArguments[1];
                    Task newDeadline = new Deadline(deadlineDescription, time);
                    tasks[taskCount] = newDeadline;
                    taskCount++;
                    System.out.println("____________________________________________________________");
                    System.out.println("Got it. I've added this task:");
                    System.out.println("    " + newDeadline);
                    System.out.println("Now you have " + taskCount + " tasks in the list.");
                    System.out.println("____________________________________________________________");
                    break;

                case "event":
                    String[] eventArguments = input[1].split(" /from | /to ", 3);
                    String eventDescription = eventArguments[0];
                    String start = eventArguments[1];
                    String end = eventArguments[2];
                    Task newEvent = new Event(eventDescription, start, end);
                    tasks[taskCount] = newEvent;
                    taskCount++;
                    System.out.println("____________________________________________________________");
                    System.out.println("Got it. I've added this task:");
                    System.out.println("    " + newEvent);
                    System.out.println("Now you have " + taskCount + " tasks in the list.");
                    System.out.println("____________________________________________________________");
                    break;

                default:
                    System.out.println("No idea what u talkin bout");
                    break;
            }
        }
    }
}
