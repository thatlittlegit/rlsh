package rlsh.low_builtins;

import rlsh.Command;
import rlsh.CommandAction;

import java.util.ArrayList;
import java.io.File;

import wapi.core.DataManager;

public class CommandMv extends Command implements CommandAction {
    public CommandMv(ArrayList<String> arguments) {
        super("mv", arguments);
        this.action = this;
    }

    public void run() {
        if(arguments.size() < 2) {
            System.err.println("rlsh: mv: Missing arguments");
            System.err.println("rlsh: mv: Usage: mv <from> <to>");
        } else if(arguments.size() > 2) {
            System.err.println("rlsh: mv: Too many arguments!");
            System.err.println("rlsh: mv: Usage: mv <from> <to>");
        } else {
            if(arguments.get(0).startsWith("/")) {
                if(!new File(arguments.get(0)).renameTo(new File(arguments.get(1)))) {
                    System.err.println("rlsh: mv: File not moved, does it exist?");
                }
            } else {
                String directory = DataManager.get("rlsh", "directory").string;
                if(!new File(directory + "/" + arguments.get(0)).renameTo(new File(directory + "/" + arguments.get(1)))) {
                    System.err.println("rlsh: mv: File not moved, does it exist?");
                }
            }
        }
    }
}
