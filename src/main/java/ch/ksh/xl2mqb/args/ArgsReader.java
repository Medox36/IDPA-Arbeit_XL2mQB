package ch.ksh.xl2mqb.args;

import java.util.List;
import java.util.Objects;

/**
 * This class evaluates the program arguments
 *
 * @author Lorenzo Giuntini
 * @version 1.0
 */
public class ArgsReader {

    private final List<String> args;

    public ArgsReader(List<String> args) {
        this.args = args;
    }

    /**
     * Checks the program arguments for the file (-f) argument and returns its value, if present.
     *
     * @return the value(path to a file) of the file argument, if argument is present, otherwise empty String
     */
    public String getFileNameAndPathFromArgs() {
        if (args.size() == 2 && Objects.equals(args.get(0), "-f")) {
            return args.get(1);
        } else {
            return "";
        }
    }
}
