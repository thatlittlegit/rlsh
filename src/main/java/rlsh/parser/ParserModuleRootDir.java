package rlsh.parser;

import rlsh.Command;

import java.io.File;
import java.io.IOException;

import wapi.core.DataManager;

public class ParserModuleRootDir implements ParserModule {
    public static final String name = "root";

    public String getName() {
        return name;
    }

    public boolean getIfNeedToRun(Command command) {
        return command.name.startsWith("/");
    }

    public void run(Command command) {
        if(!new File(command.name).exists()) {
            System.err.println("rlsh: Executable not found");
        } else if(new File(command.name).isDirectory()) {
            System.err.println("rlsh: Can't execute a directory!");
        } else {
            try {
                command.arguments.add(0, new File(command.name).getAbsolutePath());
                ProcessBuilder process = new ProcessBuilder(command.arguments);
                process.directory(new File(DataManager.get("rlsh", "directory").string));
                process.inheritIO();
                process.start().waitFor();
            } catch(IOException e) {
            } catch(InterruptedException e) {}
        }
    }
}
