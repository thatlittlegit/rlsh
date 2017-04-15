package rlsh.builtins;

import rlsh.Command;
import rlsh.CommandAction;

import wapi.core.DataManager;
import wapi.core.CompiledValue;

import java.util.Arrays;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;

public class CommandCd extends Command implements CommandAction {
    public CommandCd(ArrayList<String> arguments) {
        super("cd", arguments);
        this.action = this;
    }
    private static void changeDirectory(String directory, boolean isCdCommand) {
        if(!new File(directory).exists() || !new File(directory).isDirectory()) {
            if(isCdCommand) {
                System.err.println("rlsh: cd: Attempt to switch to file instead of directory.");
            } else {
                System.err.println("rlsh: Attempt to switch to file instead of directory.");
            }
        } else {
            DataManager.put("rlsh", "stringvar-directory", new CompiledValue(directory));
            DataManager.put("rlsh", "directory", new CompiledValue(directory));
            DataManager.put("rlsh", "ps1-calculated", new CompiledValue(false));
        }
    }

    public static void changeDirectory(String directory) {
        changeDirectory(directory, false);
    }

    public void run() {
        if(arguments.isEmpty()) {
            changeDirectory("/home/" + DataManager.get("rlsh", "stringvar-user").string, true);
        } else if(arguments.get(0) == "--help" || arguments.get(0) == "-h") {
            System.out.println("rlsh: cd: Usage: cd <directory/-h>");
        } else {
            File toSwitchTo = null;
            if(arguments.get(0).startsWith("/")) {
                toSwitchTo = new File(arguments.get(0));
            } else {
                toSwitchTo = new File(DataManager.get("rlsh", "directory").string + "/" + arguments.get(0));
            }
            if(!toSwitchTo.exists()) {
                System.err.println("rlsh: cd: Attempt to switch to nonexistant directory.");
            } else if(!toSwitchTo.isDirectory()) {
                System.err.println("rlsh: cd: Attempt to switch to file");
            } else {
                // it's OK to switch to
                try {
                    changeDirectory(toSwitchTo.getCanonicalPath());
                } catch(IOException e) {
                    System.err.println("rlsh: cd: error: Error getting canonical path of object requested.");
                    System.err.println("rlsh: cd: error: This is (most likely) a problem with your computer.");
                    e.printStackTrace();
                    System.exit(-60);
                }
            }
        }
    }
}
