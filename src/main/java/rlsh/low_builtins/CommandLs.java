package rlsh.low_builtins;

import rlsh.Command;
import rlsh.CommandAction;

import java.util.ArrayList;

import java.io.File;

import wapi.core.DataManager;
import wapi.core.NullChecker;

public class CommandLs extends Command implements CommandAction {
    public CommandLs(ArrayList<String> arguments) {
        super("ls", arguments);
        this.action = this;
    }

    public void run() {
        File directory = new File(DataManager.get("rlsh", "directory").string);

        if(arguments.size() > 0) {
            if(arguments.get(0).equals("--help") || arguments.get(0).equals("-h")) {
                System.out.println("rlsh: ls: Usage: ls [directory]");
                return;
            }
            if(arguments.get(0).startsWith("/")) {
                directory = new File(arguments.get(0));
            } else if(!NullChecker.isNull(arguments.get(0))) {
                directory = new File(DataManager.get("rlsh", "directory").string + "/" + arguments.get(0));
            } else {
                directory = new File(DataManager.get("rlsh", "directory").string);
            }
        }
        int filePrintedOf4 = 0;
        for(File file : directory.listFiles()) {
            System.out.print(file.getName() + "  ");
            filePrintedOf4 += 1;
            if(filePrintedOf4 > 4) {
                System.out.print("\n");
                    filePrintedOf4 = 0;
            }
        }
        System.out.println("");
    }
}
