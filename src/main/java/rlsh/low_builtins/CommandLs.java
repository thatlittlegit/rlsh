package rlsh.low_builtins;

import rlsh.Command;
import rlsh.CommandAction;

import java.util.ArrayList;

import java.io.File;

import wapi.core.DataManager;

public class CommandLs extends Command implements CommandAction {
    public CommandLs(ArrayList<String> arguments) {
        super("ls", arguments);
        this.action = this;
    }

    public void run() {
        File directory = new File(DataManager.get("rlsh", "directory").string);

        if(arguments.size() < 2) {
            // just print it
            int filePrintedOf4 = 0;
            for(File file : directory.listFiles()) {
                System.out.print(file.getName() + "  ");
                filePrintedOf4 += 1;
                if(filePrintedOf4 > 4) {
                    System.out.print("\n");
                    filePrintedOf4 = 0;
                }
            }
        }
    }
}
