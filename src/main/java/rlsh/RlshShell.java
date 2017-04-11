package rlsh;

import wapi.core.DataManager;
import wapi.core.CompiledValue;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.lang3.SystemUtils;

public class RlshShell implements Shell {
    private CommandParser parser = new CommandParser();

    public String getName() {
        return "rlsh";
    }
    public CommandParser getCommandParser() {
        return parser;
    }
    public static void main(String args[]) {
        DataManager.put("rlsh", "continue-shell", new CompiledValue(true));
        DataManager.put("rlsh", "ps1-calculated", new CompiledValue(false));

        // Data variables
        DataManager.put("rlsh", "stringvar-user", new CompiledValue(System.getProperty("user.name")));
        try {
            DataManager.put("rlsh", "stringvar-computer", new CompiledValue(InetAddress.getLocalHost().getHostName()));
        } catch(UnknownHostException e) {
            System.out.println("rlsh: java.net.UnknownHostException: " + e.getMessage());
            DataManager.put("rlsh", "stringvar-computer", new CompiledValue("my-computer"));
        }
        DataManager.put("rlsh", "stringvar-directory", new CompiledValue("/"));
        DataManager.put("rlsh", "directory", DataManager.get("rlsh", "stringvar-directory"));

        DataManager.put("rlsh", "shell-low-builtin-class", new CompiledValue("rlsh.RlshShellLowBuiltinList"));
        DataManager.put("rlsh", "shell-builtin-class", new CompiledValue("rlsh.RlshShellBuiltinList"));

        // TODO let user change the default PS1
        DataManager.put("rlsh", "ps1-uncalculated", new CompiledValue("${user}@${computer}:${directory}$ "));

        // Check if Win32, Mac or *nix
        if(SystemUtils.IS_OS_WINDOWS) {
            // as we don't know what's installed, just use windows' variable
            // TODO do this without environment variables
            DataManager.put("rlsh", "path", new CompiledValue(System.getenv("PATH")));
        } else if(SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_LINUX) {
            // turns out Mac and Linux are VERY similar in environment vars
            DataManager.put("rlsh", "path", new CompiledValue("/usr/bin:/bin:/usr/sbin:/sbin:/usr/local/bin:/usr/" +
                                                              "X11/bin"));
        } else {
            System.out.println("rlsh: You seem to be using an uncommon or old OS, you may have to reset the path");
        }

        // TODO Put extension stuff here

        InputLoop.run();
    }
}
