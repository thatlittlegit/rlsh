package rlsh;

import java.util.Hashtable;

public class RlshShellBuiltinList {
    public static Hashtable<String, Class<?>> builtins = new Hashtable<>();
    private static boolean hasFilled = false;

    static void fillBuiltins() {
        try {
            builtins.put("cd", Class.forName("rlsh.builtins.CommandCd"));
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
