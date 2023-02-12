package wessy;

import java.util.Map;
import java.util.HashMap;

/**
 * CmdType is an enumeration that represents all the different types of commands
 * "Wessy" takes in.
 */
public enum CmdType {
    LIST("list"),
    BYE("bye"),
    MARK("mark"),
    UNMARK("unmark"),
    TODO("todo"),
    DEADLINE("deadline"),
    EVENT("event"),
    DELETE("delete"),
    CLEAR("clear");

    private final String cmd;
    private static final Map<String, CmdType> COMMANDS = new HashMap<>();
    static {
        COMMANDS.put("bye", CmdType.BYE);
        COMMANDS.put("list", CmdType.LIST);
        COMMANDS.put("todo", CmdType.TODO);
        COMMANDS.put("deadline", CmdType.DEADLINE);
        COMMANDS.put("event", CmdType.EVENT);
        COMMANDS.put("mark", CmdType.MARK);
        COMMANDS.put("unmark", CmdType.UNMARK);
        COMMANDS.put("delete", CmdType.DELETE);
        COMMANDS.put("clear", CmdType.CLEAR);
    }

    /** Constructs an instance of CmdType that corresponds to the different
     * types of commands, along with the command in its text form.
     */
    CmdType(String str) {
        this.cmd = str;
    }

    /**
     * Converts a command (if valid) from its text form to the corresponding
     * CmdType.
     *
     * @param str Specified command in its text form.
     * @return A CmdType that corresponds to str.
     */
    public static CmdType getCmdType(String str) {
        return COMMANDS.get(str);
    }

    /**
     * Converts CmdType to command in its text form.
     *
     * @return The text form of the command when it is of CmdType type.
     */
    @Override
    public String toString() {
        return cmd;
    }

    /**
     * Gets the length of the string when the command is in its text form.
     *
     * @return The length of string when the command is in its text form.
     */
    public int len() {
        return cmd.length();
    }
}
