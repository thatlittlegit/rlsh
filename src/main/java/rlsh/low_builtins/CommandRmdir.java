package rlsh.low_builtins;

import rlsh.Command;
import rlsh.CommandAction;

import java.io.File;
import java.util.ArrayList;

import wapi.core.DataManager;

public class CommandRmdir extends Command implements CommandAction {
    public CommandRmdir(ArrayList<String> arguments) {
        super("rmdir", arguments);
        this.action = this;
    }

    public void run() {
        if(arguments.size() == 0 || (arguments.get(0) == "--help" || arguments.get(0) == "-h")) {
            if(arguments.size() == 0) {
                System.err.println("rlsh: rmdir: Missing operand");
            }
            System.out.println("rlsh: rmdir: Usage: rmdir <directory> [directory....]");
        } else {
            for(String name : arguments) {
                File folder;
                if(name.startsWith("/")) {
                    folder = new File(name);
                } else {
                    folder = new File(DataManager.get("rlsh", "directory").string + "/" + name);
                }

                if(!folder.exists()) {
                    System.err.println("rlsh: rmdir: Can't remove nonexistant directory " + folder.getName());
                } else if(!folder.isDirectory()){
                    System.err.println("rlsh: rmdir: Can't delete a file with rmdir, did you want rm?");
                } else {
                    boolean isEmpty;
                    try {
                        isEmpty = folder.list()[0] == null;
                    } catch(ArrayIndexOutOfBoundsException e) {
                        isEmpty = true;
                    }

                    if(!isEmpty) {
                        System.err.println("rlsh: rmdir: Can't delete " + folder.getName() + ": directory not empty");
                    } else {
                        if(!folder.delete()) {
                            System.err.println("rlsh: rmdir: Can't delete " + folder.getName() + ", do you have permission?");
                        }
                    }
                }
            }
        }
    }
}
