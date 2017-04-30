package rlsh.parser;

import rlsh.Command;
import rlsh.CommandParser;

import wapi.core.DataManager;

import java.io.File;

public class ParserModuleCurrentDir implements ParserModule {
    public static final String name = "current";

    public String getName() {
        return name;
    }

    public boolean getIfNeedToRun(Command command) {
        return command.name.startsWith("./");
    }

    public void run(Command command) {
        StringBuilder programNameBuilder = new StringBuilder(command.name);
        programNameBuilder.deleteCharAt(0);
        programNameBuilder.insert(0, DataManager.get("rlsh", "directory").string);
        String programName = programNameBuilder.toString();

        if(!new File(programName).exists()) {
            System.err.println("rlsh: Executable not found");
        } else if(new File(programName).isDirectory()) {
            System.err.println("rlsh: Can't execute a directory!");
        } else {
            // Get RootDir to do our dirty work
            CommandParser.run(new Command(programName, command.arguments));
        }
    }
}
