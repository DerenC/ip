package wessy.exceptions.int_exceptions;

import wessy.exceptions.WessyException;

public class EmptyListException extends WessyException {
    public EmptyListException(String cmd) {
        super("You do not have any task on the list for you to " + cmd + ".");
    }
}
