package rlsh.parser;

import rlsh.Command;

public class ParserModuleEmpty implements ParserModule {
    public static final String name = "empty";

    public String getName() {
        return name;
    }

    public boolean getIfNeedToRun(Command command) {
        return command.equals("");
    }

    public void run(Command command) {}
}
