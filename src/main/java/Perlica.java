import java.util.Scanner;
public class Perlica {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("____________________________________________________________");
        System.out.println(" Hello! I'm Perlica");
        System.out.println(" What can I do for you?");
        System.out.println("____________________________________________________________");
        boolean isExit = false;
        while (!isExit) {
            String input = scanner.nextLine(); // read user input
            if (input.equalsIgnoreCase("bye")) {
                isExit = true;
                System.out.println("____________________________________________________________");
                System.out.println(" Bye. Hope to see you again soon!");
                System.out.println("____________________________________________________________");
            } else {
                System.out.println("____________________________________________________________");
                System.out.println(" " + input);
                System.out.println("____________________________________________________________");
            }
        }
        scanner.close();
    }
}
