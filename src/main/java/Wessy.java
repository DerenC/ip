import Exceptions.CommandNotFoundException;
import Exceptions.EmptyListException;
import Exceptions.MissingInputException;
import Exceptions.MissingSpacingException;
import Exceptions.UnspecifiedTimeException;
import Exceptions.WessyException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Wessy {
    static String OPENING_LINE = "    -Wessy------------------------------" +
            "---------------------------------- ";
    static String CLOSING_LINE = "    -----------------------------------" +
            "----------------------------------- ";
    static List<Task> tasks = new ArrayList<Task>();

    public static void main(String[] args) {
        System.out.println(OPENING_LINE);
        println("Hi, I am Wessy, your personal assistant chatbot.");
        println("Please type something to interact with me.");
        System.out.println(CLOSING_LINE);

        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String userInput = sc.nextLine();
            if (checkCmd(userInput, CmdType.BYE)) {
                printNormal("Bye. Hope to see you again soon!");
                break;
            } else if (checkCmd(userInput, CmdType.LIST)) {
                printList();
            } else {
                try {
                    if (checkCmd(userInput, CmdType.MARK)) {
                        markOrUnmark(userInput, true);
                    } else if (checkCmd(userInput,CmdType.UNMARK)) {
                        markOrUnmark(userInput, false);
                    } else if (checkCmd(userInput, CmdType.DELETE)) {
                        delete(userInput);
                    } else if (checkCmd(userInput, CmdType.TODO)) {
                        printAdded(add(parse(userInput, CmdType.TODO)));
                    } else if (checkCmd(userInput, CmdType.DEADLINE)) {
                        printAdded(add(parse(userInput, CmdType.DEADLINE)));
                    } else if (checkCmd(userInput, CmdType.EVENT)) {
                        printAdded(add(parse(userInput, CmdType.EVENT)));
                    } else {
                        throw new CommandNotFoundException();
                    }
                } catch (WessyException wessyEx) {
                    printNormal(String.valueOf(wessyEx));
                } catch (NumberFormatException nfe) {
                    printNormal("☹ OOPS!!! It is not a number." +
                            " Please enter a number.");
                } catch (ArrayIndexOutOfBoundsException ex) {
                    printNormal("☹ OOPS!!! Please enter a " +
                            "valid task number.");
                }
            }
        }
    }

    static String[] parse(String description, CmdType type) throws
            MissingSpacingException, MissingInputException,
            UnspecifiedTimeException {
        checkForMissingInput(description, type);
        checkForSpacingAftCmd(description, type);
        String byStr = "/by";
        String fromStr = "/from";
        String toStr = "/to";
        int firstIdx;
        int secondIdx;
        description = description.substring(type.len() + 1);
        switch (type) {
            case DEADLINE:
                firstIdx = description.indexOf(byStr);
                return new String[]{description.substring(0, firstIdx - 1),
                        description.substring(firstIdx +
                                byStr.length() + 1)};
            case EVENT:
                firstIdx = description.indexOf(fromStr);
                secondIdx = description.indexOf(toStr);
                return new String[]{description.substring(0, firstIdx - 1),
                        description.substring(firstIdx + fromStr.length() + 1, secondIdx - 1),
                        description.substring(secondIdx +
                                toStr.length() + 1)};
            case TODO:
                return new String[]{description};
        }
        return new String[] {};
    }

    static boolean checkCmd(String userInput, CmdType type) {
        int threshold = type.len();
        return userInput.length() >= threshold && userInput.substring(0,
                threshold).equalsIgnoreCase(type.toString());
    }

    static void checkForSpacingAftCmd(String userInput, CmdType type) throws
            MissingSpacingException {
        String cmd = type.toString();
        if (userInput.charAt(cmd.length()) != ' ') {
            throw new MissingSpacingException(cmd, true);
        }
    }

    static boolean checkAllSpaces(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) != ' ') {
                return false;
            }
        }
        return true;
    }

    static void checkForMissingInput(String userInput, CmdType type) throws
            MissingInputException, MissingSpacingException,
            UnspecifiedTimeException {
        String cmd = type.toString();
        if (userInput.equalsIgnoreCase(cmd) || checkAllSpaces(
                userInput.substring(cmd.length()))) {
            throw new MissingInputException(cmd);
        }
        if (type == CmdType.DEADLINE) {
            checkForTimeKeywordEx(userInput, "/by");
            int idxOfBy = userInput.indexOf("/by");
            if (idxOfBy == 8 || checkAllSpaces(userInput.substring(8,
                    idxOfBy))) {
                throw new MissingInputException(cmd);
            }
            if (idxOfBy + 3 == userInput.length() || checkAllSpaces(
                    userInput.substring(idxOfBy + 3))) {
                throw new UnspecifiedTimeException("/by");
            }
        }
        if (type == CmdType.EVENT) {
            checkForTimeKeywordEx(userInput, "/from");
            int idxOfFrom = userInput.indexOf("/from");
            if (idxOfFrom == 5 || checkAllSpaces(userInput.substring(5,
                    idxOfFrom))) {
                throw new MissingInputException(cmd);
            }
            checkForTimeKeywordEx(userInput, "/to");
            int idxOfTo = userInput.indexOf("/to");
            if (idxOfTo == idxOfFrom + 5 || checkAllSpaces(
                    userInput.substring(idxOfFrom + 5, idxOfTo))) {
                throw new UnspecifiedTimeException("/from");
            }
            if (idxOfTo + 3 == userInput.length() || checkAllSpaces(
                    userInput.substring(idxOfTo + 3))) {
                throw new UnspecifiedTimeException("/to");
            }
        }

    }

    static void checkForTimeKeywordEx(String userInput, String keyword) throws
            UnspecifiedTimeException, MissingSpacingException {
        int idx = userInput.indexOf(keyword);
        if (idx == -1) {
            throw new UnspecifiedTimeException(keyword);
        }
        if (userInput.charAt(idx - 1) != ' ') {
            throw new MissingSpacingException(keyword, false);
        }
        if (userInput.length() == idx + keyword.length() ||
                userInput.charAt(idx + keyword.length()) != ' ') {
            throw new MissingSpacingException(keyword, true);
        }
    }

    static void checkForEmptyList(CmdType type) throws EmptyListException {
        if (tasks.isEmpty()) {
            throw new EmptyListException(type.toString());
        }
    }

    static void printNormal(String... linesOfString) {
        System.out.println(OPENING_LINE);
        for (String line : linesOfString) {
            println(line);
        }
        System.out.println(CLOSING_LINE);
    }

    static void printAdded(Task task) {
        int size = tasks.size();
        String numOfTasks = " " + size + " task";
        if (size > 1) {
            numOfTasks += "s";
        }
        printNormal("Got it. I've added this task:",
                "  " + task, "Now you have" + numOfTasks + " in the list.");
    }

    static void println(String str) {
        int length = str.length();
        String message = "   |   " + str;
        for (int i = 0; i < 70 - length - 3; i++) {
            message += " ";
        }
        message += "|";
        System.out.println(message);
    }

    static Task add(String[] strings) {
        int len = strings.length;
        if (len == 1) {
            tasks.add(new ToDo(strings[0]));
        } else if (len == 2) {
            tasks.add(new Deadline(strings[0], strings[1]));
        } else if (len == 3) {
            tasks.add(new Event(strings[0], strings[1], strings[2]));
        }
        return tasks.get(tasks.size() - 1);
    }

    static void printList() {
        int size = tasks.size();
        System.out.println(OPENING_LINE);
        if (size == 0) {
            println("WOOHOO! You do not have any task on the list.");
        } else {
            println("Here are the tasks in your list:");
            for (int i = 0; i < size; i++) {
                println((i + 1) + "." + tasks.get(i));
            }
        }
        System.out.println(CLOSING_LINE);
    }

    static int parseInt(String userInput, CmdType type) throws
            EmptyListException, MissingInputException,
            MissingSpacingException, NumberFormatException,
            ArrayIndexOutOfBoundsException, UnspecifiedTimeException {
        checkForEmptyList(type);
        checkForMissingInput(userInput, type);
        checkForSpacingAftCmd(userInput, type);
        int idx = Integer.parseInt(userInput.substring(
                type.len() + 1)) - 1;
        if (idx < 0 || idx >= tasks.size()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return idx;
    }

    static void markOrUnmark(String userInput, boolean isMark) throws
            EmptyListException, MissingInputException,
            MissingSpacingException, NumberFormatException,
            ArrayIndexOutOfBoundsException, UnspecifiedTimeException {
        CmdType type = isMark ? CmdType.MARK : CmdType.UNMARK;
        int idx = parseInt(userInput, type);
        String start = isMark ? "Nice! I've" : "OK, I've";
        if (isMark == tasks.get(idx).isDone) {
            start = "You have already";
        }
        if (isMark) {
            tasks.get(idx).mark();
        } else {
            tasks.get(idx).unmark();
        }
        String end = isMark ? "done:" : "not done yet:";
        printNormal(start + " marked this task as " + end, "  " +
                tasks.get(idx));
    }

    static void delete(String userInput) throws EmptyListException,
            MissingInputException, MissingSpacingException,
            NumberFormatException, ArrayIndexOutOfBoundsException,
            UnspecifiedTimeException {
        int idx = parseInt(userInput, CmdType.DELETE);
        Task removedTask = tasks.get(idx);
        tasks.remove(idx);
        int size = tasks.size();
        printNormal("Noted. I've removed this task:", "  " +
                removedTask, String.format(
                        "Now you have %d task%s in the list.", size,
                        size == 1 ? "" : "s"));
    }
}
