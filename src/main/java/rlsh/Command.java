package rlsh;

import java.util.ArrayList;

public class Command {
    public String name;
    public ArrayList<String> arguments;
    public CommandAction action;
    public String completeInput;

    public Command(String newName, ArrayList<String> arguments, CommandAction newAction) {
        name = newName;
        this.arguments = arguments;
        action = newAction;
    }

    public Command(String newName, ArrayList<String> arguments) {
        this(newName, arguments, null);
    }
}
