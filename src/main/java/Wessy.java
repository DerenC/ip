import exceptions.WessyException;
import exceptions.CommandNotFoundException;
import exceptions.int_exceptions.NotAnIntegerException;
import exceptions.num_of_input_exceptions.MissingInputException;
import exceptions.num_of_input_exceptions.TooManyInputException;
import task.Task;

import java.io.IOException;
import java.time.format.DateTimeParseException;

public class Wessy {
    private final Storage storage;
    private TaskList tasks;
    private final Ui ui;

    public Wessy(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (IOException | SecurityException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
            tasks = new TaskList();
        }
    }

    public void run() {
        startsUp();
        while (ui.hasNextLine()) {
            try {
                String userInput = ui.readNextLine();
                CmdType cmd = Parser.getCmd(userInput);
                if (cmd == null) {
                    throw new CommandNotFoundException();
                }
                UserInputChecker.checkSpacingAftCmd(userInput, cmd);
                switch (cmd) {
                    case BYE:
                        ui.printBye();
                        return;
                    case LIST:
                        ui.printListMessage(tasks.getSize(), tasks.printAsStr());
                        break;
                    case TODO:
                    case DEADLINE:
                    case EVENT:
                        UserInputChecker.checkForMissingInput(userInput, cmd);
                        UserInputChecker.checkMissingKeyword(userInput, cmd);
                        if (cmd == CmdType.DEADLINE) {
                            UserInputChecker.checkForDeadlineMissingInput(userInput, cmd);
                        } else if (cmd == CmdType.EVENT) {
                            UserInputChecker.checkForEventMissingInput(userInput, cmd);
                        }
                        String[] taskComponents = Parser.getTaskComponents(userInput, cmd);
                        Task newTask = tasks.add(taskComponents);
                        save2Storage();
                        ui.printAdded(newTask, tasks.getSize());
                        break;
                    case MARK:
                    case UNMARK:
                        checkB4Parse(userInput, cmd);
                        boolean isMark = cmd == CmdType.MARK;
                        Task updatedTask = tasks.markOrUnmark(Parser.parseInt(userInput, cmd), isMark);
                        save2Storage();
                        ui.printMarkUnmark(updatedTask, isMark);
                        break;
                    case DELETE:
                        checkB4Parse(userInput, cmd);
                        Task deletedTask = tasks.delete(Parser.parseInt(userInput, cmd));
                        save2Storage();
                        ui.printDelete(deletedTask, tasks.getSize());
                        break;
                    case CLEAR:
                        tasks.clear();
                        save2Storage();
                        ui.printClear();
                }
            } catch (DateTimeParseException dtpe) {
                ui.handleException("Please enter the date (and time, if any) in the correct format.");
            } catch (SecurityException se) {
                ui.handleException("You do not have the permission to access the file.");
                se.printStackTrace();
            } catch (IOException ioe) {
                ui.handleException("There is some issue in the input-output operation.");
                ioe.printStackTrace();
            } catch (WessyException we) {
                ui.handleException(we.toString());
            } catch (Exception ex) {
                ui.handleException(ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new Wessy("data/savedTasks.txt").run();
    }

    // HELPER FUNCTIONS
    private void startsUp() {
        ui.showWelcome(tasks.printAsStr(), tasks.getSize());
    }

    void save2Storage() throws IOException {
        storage.save(tasks.saveAsStr());
    }

    void checkB4Parse(String userInput, CmdType cmd) throws MissingInputException, NotAnIntegerException, TooManyInputException {
        UserInputChecker.checkForMissingInput(userInput, cmd);
        UserInputChecker.checkNotNumerical(userInput, cmd);
        UserInputChecker.checkTooManyInputs(userInput, cmd);
    }
}
