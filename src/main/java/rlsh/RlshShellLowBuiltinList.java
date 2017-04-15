package rlsh;

import java.util.Hashtable;

public class RlshShellLowBuiltinList {
    public static Hashtable<String, Class<?>> builtins = new Hashtable<>();
    private static boolean hasFilled = false;

    static void fillBuiltins() {
        try {
            builtins.put("ls", Class.forName("rlsh.low_builtins.CommandLs"));
            builtins.put("rm", Class.forName("rlsh.low_builtins.CommandRm"));
            builtins.put("mv", Class.forName("rlsh.low_builtins.CommandMv"));
            builtins.put("cp", Class.forName("rlsh.low_builtins.CommandCp"));
            builtins.put("mkdir", Class.forName("rlsh.low_builtins.CommandMkdir"));
            builtins.put("rmdir", Class.forName("rlsh.low_builtins.CommandRmdir"));
            builtins.put("touch", Class.forName("rlsh.low_builtins.CommandTouch"));
        } catch(ClassNotFoundException e) {
            System.err.println("rlsh: error: Required class not found: " + e.getMessage());
            System.err.println("rlsh: error: Your installation may be corrupt.");
            System.err.println("rlsh: error: Please reinstall rlsh, and if that fails file a bug report.");
            System.exit(-1);
        }
        hasFilled = true;
    }

    static boolean getIsFilled() {
        return hasFilled;
    }
}
