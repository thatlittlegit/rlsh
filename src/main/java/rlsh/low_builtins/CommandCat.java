package rlsh.low_builtins;

import rlsh.Command;
import rlsh.CommandAction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;

import wapi.core.DataManager;

public class CommandCat extends Command implements CommandAction {
    public CommandCat(ArrayList<String> arguments) {
        super("cat", arguments);
        this.action = this;
    }

    public void run() {
        if(arguments.size() == 0 || (arguments.get(0).equals("--help") || arguments.get(0).equals("-h"))) {
            if(arguments.size() == 0) {
                System.err.println("rlsh: cat: Missing operand");
            }
            System.out.println("rlsh: cat: Usage: cat <file> [file....]");
        } else {
            BufferedReader reader = null;
            FileReader fileReader = null;
            String line = null;

            for(String filename : arguments) {
                try {
                    if(filename.startsWith("/")) {
                        fileReader = new FileReader(filename);
                    } else {
                        fileReader = new FileReader(DataManager.get("rlsh", "directory").string + "/" + filename);
                    }
                    reader = new BufferedReader(fileReader);

                    while((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }

                    reader.close();
                        fileReader.close();
                        line = null;
                } catch(IOException e) {
                    System.err.print("rlsh: cat: Something bad happened: " + e.getMessage());
                    try {
                        reader.close();
                        fileReader.close();
                    } catch(IOException ioe) {
                        System.err.print(" (additionally, something bad happened while this was happening: )"
                                         + ioe.getMessage());
                    } catch(NullPointerException npe) {
                        System.err.print(" (additionally, we didn't seem to get anything done, did that "
                                         + "file exist?)");
                    } finally {
                        System.err.println("");
                    }
                }
            }
        }
    }
}
