package rlsh.low_builtins;

import rlsh.Command;
import rlsh.CommandAction;

import java.util.ArrayList;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;

import wapi.core.DataManager;

public class CommandMv extends Command implements CommandAction {
    public CommandMv(ArrayList<String> arguments) {
        super("mv", arguments);
        this.action = this;
    }

    protected void move(File from, File to) {
        try {
            if(from.isDirectory() && to.isDirectory()) {
                FileUtils.moveDirectoryToDirectory(from, to, true);
            } else if(from.isDirectory() && !to.isDirectory()) {
                throw new IOException("Cannot move a directory to a file");
            } else {
                FileUtils.moveFile(from, to);
            }
        } catch(FileExistsException e) {
            try {
                FileUtils.forceDelete(to);
            } catch(IOException ioe) {
                System.err.println("rlsh: mv: Error deleting old " + to.getName() + ": " + ioe.getMessage());
            }
            move(from, to);
        } catch(IOException e) {
            System.err.println("rlsh: mv: Failed to move file, does it exist? (" + e.getMessage() + ")");
        }
    }

    public void run() {
        if(arguments.size() != 2) {
            if(arguments.size() != 1) {
                if(arguments.size() < 2) {
                    System.err.println("rlsh: mv: Missing arguments");
                } else if(arguments.size() > 2) {
                    System.err.println("rlsh: mv: Too many arguments!");
                }
            }

            System.err.println("rlsh: mv: Usage: mv <from> <to>");
        } else {
            if(arguments.get(0).startsWith("/")) {
                move(new File(arguments.get(0)), new File(arguments.get(1)));
            } else {
                String directory = DataManager.get("rlsh", "directory").string;
                move(new File(directory + "/" + arguments.get(0)), new File(directory + "/" + arguments.get(1)));
            }
        }
    }
}
