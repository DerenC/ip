package wessy;

import java.util.Scanner;
import wessy.task.Task;

/**
 *
 */
public class Ui {
    private final Scanner sc;

    /**
     * Constructs an instance of Ui.
     */
    public Ui() {
        this.sc = new Scanner(System.in);
    }

    /**
     * Handles inputs.
     *
     * @return A boolean value
     */
    boolean hasNextLine() {
        return sc.hasNextLine();
    }

    /**
     * Handles inputs.
     *
     * @return
     */
    String readNextLine() {
        return sc.nextLine();
    }

    /**
     * Prints output message for "bye" command.
     */
    // MAIN OUTPUT FUNCTIONS
    void printBye() {
        printMessage("Bye. Hope to see you again soon!");
    }

    /**
     * Prints output message for "list" command.
     *
     * @param totalNumOfTasks
     * @param tasks
     */
    void printListMessage(int totalNumOfTasks, String[] tasks) {
        printOpening();
        printList(totalNumOfTasks, tasks);
        printClosing();
    }

    /**
     * A helper function used in pritnListMessage(...).
     *
     * @param totalNumOfTasks
     * @param tasks
     */
    // HELPER
    void printList(int totalNumOfTasks, String[] tasks) {
        if (totalNumOfTasks == 0) {
            println("WOOHOO! You do not have any task on the list.");
        } else {
            println("Here are the tasks in your list:");
            printMultiln(tasks);
        }
    }

    /**
     * Prints output message for "todo", "deadline" and "event" commands.
     *
     * @param task
     * @param size
     */
    void printAdded(Task task, int size) {
        String numOfTasks = " " + size + " task";
        if (size > 1) {
            numOfTasks += "s";
        }
        printMessage("Got it. I've added this task:",
                "  " + task, "Now you have" + numOfTasks + " in the list.");
    }

    /**
     * Prints output message for "mark" and "unmark" commands.
     *
     * @param chosenTask
     * @param isMark
     */
    void printMarkUnmark(Task chosenTask, boolean isMark) {
        String start = isMark ? "Nice! I've" : "OK, I've";
        if (isMark == chosenTask.checkIsDone()) {
            start = "You have already";
        }
        String end = isMark ? "done:" : "not done yet:";
        printMessage(start + " marked this task as " + end, "  " +
                chosenTask);
    }

    /**
     * Prints output message for "delete" command.
     *
     * @param chosenTask
     * @param totalNumOfTasks
     */
    void printDelete(Task chosenTask, int totalNumOfTasks) {
        printMessage("Noted. I've removed this task:", "  " +
            chosenTask, String.format(
            "Now you have %d task%s in the list.", totalNumOfTasks,
            totalNumOfTasks == 1 ? "" : "s"));
    }

    /**
     * Prints output message for "clear" command.
     */
    void printClear() {
        printMessage("You have cleared your task list. The list is empty now.");
    }

    /**
     * Prints output message when starting up Wessy.
     *
     * @param tasks
     * @param totalNumOfTasks
     */
    void showWelcome(String[] tasks, int totalNumOfTasks) {
        printOpening();
        println("Hi, I am Wessy, your personal assistant chatbot.");
        println("");
        printList(totalNumOfTasks, tasks);
        printClosing();
    }

    /**
     *
     *
     * @param message
     */
    void handleException(String message) {
        System.err.println("    -Wessy------------------------------" +
                "---------------------------------- ");
        printErr(message);
        System.err.println("    -----------------------------------" +
                "----------------------------------- ");
    }

    /**
     * A helper function
     */
    private static void printOpening() {
        System.out.println("    -Wessy------------------------------" +
                "---------------------------------- ");
    }

    /**
     * A helper function
     */
    private static void printClosing() {
        System.out.println("    -----------------------------------" +
                "----------------------------------- ");
    }

    /**
     * A helper function
     *
     * @param str
     */
    private void println(String str) {
        int length = str.length();
        if (length <= 64) {
            String message = "   |   " + str;
            message += " ".repeat(67 - length);
            message += "|";
            System.out.println(message);
        } else {
            System.out.println("   |   " + str.substring(0, 64) + "   |");
            int remainingLength = length - 64;
            int leftover = remainingLength % 62;
            int n = (int) Math.floor(remainingLength/62);
            for (int i = 0; i < n; i++) {
                System.out.println("   |     " + str.substring(62 * i, 62 * (i + 1)) + "   |");
            }
            String message = "   |     " + str.substring(length - leftover);
            message += " ".repeat(65 - leftover) + "|";
            System.out.println(message);
        }
    }

    /**
     * A helper function
     *
     * @param str
     */
    private void printErr(String str) {
        int length = str.length();
        if (length <= 64) {
            String message = "   |   " + str;
            message += " ".repeat(68 - length);
            message += "|";
            System.err.println(message);
        } else {
            System.err.println("   |   " + str.substring(0, 64) + "    |");
            int remainingLength = length - 64;
            int leftover = remainingLength % 62;
            int n = (int) Math.floor(remainingLength/62);
            for (int i = 0; i < n; i++) {
                System.err.println("   |     " + str.substring(62 * i, 62 * (i + 1)) + "    |");
            }
            String message = "   |     " + str.substring(length - leftover);
            message += " ".repeat(65 - leftover) + "|";
            System.err.println(message);
        }
    }

    /**
     * A helper function
     *
     * @param linesOfString
     */
    private void printMultiln(String... linesOfString) {
        for (String line : linesOfString) {
            println(line);
        }
    }

    /**
     * A helper function
     *
     * @param linesOfString
     */
    private void printMessage(String... linesOfString) {
        printOpening();
        printMultiln(linesOfString);
        printClosing();
    }
}
