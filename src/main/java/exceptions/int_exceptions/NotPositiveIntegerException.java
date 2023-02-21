package exceptions.int_exceptions;

import exceptions.WessyException;

public class NotPositiveIntegerException extends WessyException {
    public NotPositiveIntegerException() {
        super("The number you just input is not a positive integer. Please input a positive integer.");
    }
}
