package rlsh.low_builtins;

import rlsh.Command;
import rlsh.CommandAction;

import wapi.core.DataManager;

import java.util.ArrayList;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class CommandCp extends Command implements CommandAction {
    public CommandCp(ArrayList<String> arguments) {
        super("cp", arguments);
        this.action = this;
    }

    protected void copy(File from, File to) {
        try {
            if(!from.isDirectory() && to.isDirectory()) {
                FileUtils.copyFileToDirectory(from, to);
            } else if(from.isDirectory() && !to.isDirectory()) {
                throw new IOException("Cannot copy folder to file!");
            } else if(from.isDirectory() && to.isDirectory()) {
                FileUtils.copyDirectoryToDirectory(from, to);
            } else {
                FileUtils.copyFile(from, to);
            }
        } catch(IOException e) {
            System.err.println("rlsh: cp: error: Couldn't copy file " + to.getName() + ": " + e.getMessage());
        }
    }

    private File getToFile() {
        int counter = 0;
        if(arguments.get(arguments.size() - 1).startsWith("/")) {
            return new File(arguments.get(arguments.size() - 1));
        } else {
            return new File(DataManager.get("rlsh", "directory").string + "/" + arguments.get(arguments.size() - 1));
        }
    }

    public void run() {
        if(arguments.size() < 2) {
            System.err.println("rlsh: cp: Not enough arguments");
            System.err.println("rlsh: cp: Usage: cp <from> <to>");
        } else if(arguments.size() > 2) {
            System.out.println("rlsh: cp: Note: since you supplied more than the expected amount of ");
            System.out.println("rlsh: cp: arguments, we're assuming you want to copy the following files:");

            // Print out all the files
            int counter = 0;
            for(String file : arguments) {
                if(counter == arguments.size() - 1) {
                    System.out.println(" => " + file + "/");
                } else {
                    System.out.print("'" + file + "'");
                    if(counter + 1 != arguments.size() - 1) {
                        System.out.print(", ");
                    }
                }
                counter += 1;
            }

            // Get where we are copying them to
            File to = getToFile();

            // Copy them
            for(String file : arguments) {
                if(counter != arguments.size() - 1) {
                    if(file.startsWith("/")) {
                        copy(new File(file), to);
                    } else {
                        copy(new File(DataManager.get("rlsh", "directory").string + file), to);
                    }
                }
            }
        } else {
            File to = getToFile();
            if(arguments.get(0).startsWith("/")) {
                copy(new File(arguments.get(0)), to);
            } else {
                copy(new File(DataManager.get("rlsh", "directory").string + "/" + arguments.get(0)), to);
            }
        }
    }
}
