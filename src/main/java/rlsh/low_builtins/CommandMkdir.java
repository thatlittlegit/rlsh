package rlsh.low_builtins;

import rlsh.Command;
import rlsh.CommandAction;

import java.io.File;
import java.util.ArrayList;

import wapi.core.DataManager;

public class CommandMkdir extends Command implements CommandAction {
    public CommandMkdir(ArrayList<String> arguments) {
        super("mkdir", arguments);
        this.action = this;
    }

    private void showError(String name) {
        System.err.println("rlsh: mkdir: Failed to make directory " + name + ", do you have permission?");
    }

    private void mkdir(File file, boolean parent) {
        if(parent) {
            if(!file.mkdirs()) {
                showError(file.getName());
            }
        } else {
            if(!file.mkdir()) {
                showError(file.getName());
            }
        }
    }

    public void run() {
        if(arguments.size() == 0 || (arguments.get(0).equals("--help") || arguments.get(0).equals("-h"))) {
            if(arguments.size() < 1) {
                System.err.println("rlsh: mkdir: Missing operand");
            }
            System.out.println("rlsh: mkdir: Usage: mkdir [-hp] <directory>");
        } else {
            boolean help = false;
            boolean parent = false;
            ArrayList<String> toCreate = new ArrayList<String>();
            if(arguments.size() > 1) {
                // Parse arguments
                for(String argument : arguments) {
                    if(argument.equals("-h") || argument.equals("--help")) {
                        help = true;
                        break;
                    } else if(argument.equals("-p") || argument.equals("--parent")) {
                        parent = true;
                    } else {
                        toCreate.add(argument);
                    }
                }
            } else {
                toCreate.add(arguments.get(0));
            }

            // Okay, now run
            if(help) {
                System.out.println("rlsh: mkdir: Usage: mkdir [-hp] <directory>");
            } else {
                for(String filename : toCreate) {
                    if(filename.startsWith("/")) {
                        mkdir(new File(filename), parent);
                    } else {
                        mkdir(new File(DataManager.get("rlsh", "directory").string + "/" + filename), parent);
                    }
                }
            }
        }
    }
}
