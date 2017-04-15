package rlsh.low_builtins;

import rlsh.Command;
import rlsh.CommandAction;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;

public class CommandTouch extends Command implements CommandAction {
    public CommandTouch(ArrayList<String> arguments) {
        super("touch", arguments);
        this.action = this;
    }

    public void run() {
        if(arguments.size() == 0 || (arguments.get(0) == "--help" || arguments.get(0) == "-h")) {
            if(arguments.size() == 0) {
                System.err.println("rlsh: touch: Missing operand");
            }
            System.out.println("rlsh: touch: Usage: touch <files> [files...]");
        } else {
            ArrayList<String> files = new ArrayList<>();
            if(arguments.size() > 1) {
                for(String name : arguments) {
                    files.add(name);
                }
            } else {
                files.add(arguments.get(0));
            }

            for(String name : arguments) {
                try {
                    if(name.startsWith("/")) {
                        if(!new File(name).createNewFile()) {
                            System.err.println("rlsh: touch: Couldn't make file " + name + ", does it exist?");
                        }
                    } else {
                        if(!new File(wapi.core.DataManager.get("rlsh", "directory").string
                                     + "/" + name).createNewFile()) {
                            System.err.println("rlsh: touch: Couldn't make file " + name + ", does it exist?");
                        }
                    }
                } catch(IOException e) {
                    System.err.println("rlsh: touch: Something went wrong: " + e.getMessage());
                }
            }
        }
    }
}
