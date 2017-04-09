package rlsh;

import wapi.core.DataManager;
import wapi.core.NullChecker;

import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Arrays;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;

import java.io.File;
import java.io.IOException;

public class CommandParser {
    static Hashtable getFluidBuiltins(BuiltinType type) throws IllegalArgumentException {
        String packageTypeToLookFor;
        switch(type) {
        case BUILTIN:
            packageTypeToLookFor = "builtin-class";
            break;
        case LOW_BUILTIN:
            packageTypeToLookFor = "low-builtin-class";
            break;
        default:
            throw new IllegalArgumentException("Failed to determine builtin type.");
        }

        try {
            // get the builtin class
            Class<?> rlshShellBuiltins = Class.forName(DataManager.get("rlsh", "shell-" +
                                                                       packageTypeToLookFor).string);
            // check if it's processed the builtins
            Boolean hasChecked = (Boolean) rlshShellBuiltins.getDeclaredMethod("getIsFilled").invoke(null);
            if(!hasChecked.booleanValue()) {
                // if not, process them
                rlshShellBuiltins.getDeclaredMethod("fillBuiltins").invoke(null);
            }
            // now, get them as a Hashtable
            Hashtable<String, Class<Command>> builtins = (Hashtable<String, Class<Command>>) rlshShellBuiltins.
                getField("builtins").get(null);
            return builtins;
        } catch(NoSuchFieldException e) {
            System.err.println("rlsh: error: Shell builtin list does not contain Hashtable map, this is a problem");
            System.err.println("rlsh: error: with your installation or a plugin. Try removing some plugins.");
            e.printStackTrace();
            System.exit(-2);
        } catch(IllegalAccessException e) {
            System.err.println("rlsh: error: Failed to access necessary fields in shell builtin list. This is");
            System.err.println("rlsh: error: a problem with your installation or a plugin. Try removing some");
            System.err.println("rlsh: error: plugins.");
            e.printStackTrace();
            System.exit(-3);
        } catch(ClassNotFoundException e) {
            System.err.println("rlsh: error: Builtin class not found, this is a problem with your installation");
            System.err.println("rlsh: error: or a plugin. Try removing some plugins");
            e.printStackTrace();
            System.exit(-4);
        } catch(InvocationTargetException e) {
            System.err.println("rlsh: error: An error occurred while processing builtins, this is a problem with");
            System.err.println("rlsh: error: a plugin.");
            e.printStackTrace();
            System.exit(-5);
        } catch(NoSuchMethodException e) {
            System.err.println("rlsh: error: A required function for builtin management is missing, this is a");
            System.err.println("rlsh: error: problem with a plugin.");
            e.printStackTrace();
            System.exit(-6);
        }
        return null;
    }

    public static void run(Command c) {
        Hashtable<String, Class<Command>> builtins = getFluidBuiltins(BuiltinType.BUILTIN);

        boolean alias;
        try {
            alias = !NullChecker.isNull(DataManager.get("rlsh", "alias-" + c.name).string);
        } catch(NullPointerException e) {
            alias = false;
        }

        if(alias) {
            ArrayList<String> arguments = new ArrayList<>(c.arguments);
            try {
                ArrayList<String> tempArgs = new ArrayList<>();
                tempArgs.addAll(Arrays.asList(DataManager.get("rlsh", "alias-" + c.name + "-arguments")
                                               .string.split(" ")));
                tempArgs.remove(0);
                arguments.addAll(tempArgs);
            } catch(NullPointerException e) {}
            CommandParser.run(new Command(DataManager.get("rlsh", "alias-" + c.name).string,
                                              arguments));
        } else if(!NullChecker.isNull(builtins.get(c.name))) {
            try {
                Constructor<Command> toRun = builtins.get(c.name).getDeclaredConstructor(ArrayList.class);
                CommandAction action = toRun.newInstance(c.arguments).action;

                action.run();
            } catch(Exception e) {
                System.err.println("rlsh: error: Failed to run builtin command. This is a problem with your");
                System.err.println("rlsh: error: installation or a plugin. Try removing some plugins.");
                e.printStackTrace();
                System.exit(-10);
            }
        } else {
            // Scan the PATH
            String[] folderPaths = DataManager.get("rlsh", "path").string.split(":");
            boolean success = false;
            for(String folderPath : folderPaths) {
                File folder = new File(folderPath);
                try {
                    for(File file : folder.listFiles()) {
                        if(file.exists() && !file.isDirectory()) { 
                            if(file.getName().replace("\\s+", "").equals(c.name.replace("\\s+", ""))) {
                                // found a match!
                                success = true;
                                ArrayList<String> arguments = c.arguments;
                                arguments.add(0, file.getAbsolutePath());
                                ProcessBuilder p = new ProcessBuilder(arguments);
                                p.directory(new File(DataManager.get("rlsh", "directory").string));
                                p.inheritIO();
                                p.start().waitFor();
                                break;
                            }
                        }
                    }
                } catch(NullPointerException e) {/* do nothing */
                } catch(InterruptedException e) {/* still do nothing*/
                } catch(IllegalArgumentException e) {
                    System.err.println("rlsh: error: Not enough arguments provided to programmic command");
                    e.printStackTrace();
                } catch(IOException e) {
                    System.err.println("rlsh: error: An I/O error occurred. Please file a bug report.");
                    e.printStackTrace();
                }
            }
            if(success != true) {
                if(!c.name.equals("")) {
                    System.err.println("rlsh: Command " + c.name + " not found.");
                }
                // don't do anything if command blank
            }
        }
    }
}

enum BuiltinType {
    BUILTIN, LOW_BUILTIN
}
