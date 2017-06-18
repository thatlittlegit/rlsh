package rlsh.parser;

import rlsh.Command;

public class ParserModuleFail implements ParserModule {
    public static final String name = "fail";

    public String getName() {
	return name;
    }

    public boolean getIfNeedsToRun(Command c) {
	return true;
    }

    public void run(Command c) {
	System.err.println("rlsh: Command not found.");
    }
}
