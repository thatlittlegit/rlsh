package rlsh.low_builtins;

import rlsh.Command;
import rlsh.CommandAction;

import java.io.File;

import java.util.ArrayList;

import wapi.core.DataManager;

public class CommandRm extends Command implements CommandAction {
    public CommandRm(ArrayList<String> arguments) {
        super("rm", arguments);
        this.action = this;
    }

    public void run() {
        if(arguments.size() == 0 || (arguments.get(0).equals("--help") || arguments.get(0).equals("-h"))) {
            if(arguments.size() == 0) {
                System.err.println("rlsh: rm: Missing argument");
            }
            System.err.println("rlsh: rm: Usage: rm <file>");
        } else {
            if(arguments.get(0).startsWith("/")) {
                if(!new File(arguments.get(0)).delete()) {
                    System.err.println("rlsh: rm: File not deleted, does it exist?");
                }
            } else {
                if(!new File(DataManager.get("rlsh", "directory").string + "/" + arguments.get(0)).delete()) {
                    System.err.println("rlsh: rm: File not deleted, does it exist?");
                }
            }
        }
    }
}
