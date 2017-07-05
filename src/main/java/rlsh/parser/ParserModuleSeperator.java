package rlsh.parser;

import rlsh.Command;
import rlsh.CommandParser;

import java.util.Arrays;
import java.util.ArrayList;

public class ParserModuleSeperator implements ParserModule {
    public static final String name = "seperator";

    public String getName() {
	return name;
    }

    public boolean getIfNeedToRun(Command command) {
	try {
	    return command.completeInput.split(";").length > 0;
	} catch(NullPointerException e) {
	    return false;
	}
    }

    public void run(Command rawCommand) {
	for(String command : rawCommand.completeInput.split(";")) {
	    ArrayList<String> arguments = new ArrayList<String>(Arrays.asList(command.split(" ")));
	    arguments.remove(0);
	    Command newCommand = new Command(command.split(" ")[0], arguments, null);
	    rawCommand.completeInput = command;
	    CommandParser.run(newCommand);
	}
    }
}
