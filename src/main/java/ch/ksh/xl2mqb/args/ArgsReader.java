package ch.ksh.xl2mqb.args;

import java.util.List;
import java.util.Objects;

public class ArgsReader {

    private final List<String> args;

    public ArgsReader(List<String> args) {
        this.args = args;
    }

    public String getFileNameAndPathFromArgs() {
        if (args.size() == 2 && Objects.equals(args.get(0), "-f")) {
            return args.get(1);
        } else {
            return "";
        }
    }
}
