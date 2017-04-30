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
    public static void run(Command c) {
        Hashtable<String, Class<Command>> builtins = HashtableFromReferenceFinder.
            getHashtableFromBuiltinType(HashtableFromReferenceFinder.BuiltinType.BUILTIN);

        boolean alias;
        try {
            alias = !NullChecker.isNull(DataManager.get("rlsh", "alias-" + c.name).string);
        } catch(NullPointerException e) {
            alias = false;
        }

        boolean needToSplit;
        try {
            needToSplit = c.completeInput.split(";").length > 0;
        } catch(NullPointerException e) {
            needToSplit = false;
        }
        // Split it by ; before parsing it
        if(needToSplit) {
            for(String command : c.completeInput.split(";")) {
                ArrayList<String> arguments = new ArrayList<String>(Arrays.asList(command.split(" ")));
                arguments.remove(0);
                Command newCommand = new Command(command.split(" ")[0], arguments, null);
                c.completeInput = command;
                CommandParser.run(newCommand);
            }
        } else if(alias) {
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
        } else if(c.name.equals("")) {
            // No-op
        } else if(c.name.startsWith("/")) {
            // global program
            if(!new File(c.name).exists()) {
                System.err.println("rlsh: Executable not found");
            } else if(new File(c.name).isDirectory()) {
                System.err.println("rlsh: Can't execute a directory!");
            } else {
                try {
                    c.arguments.add(0, new File(c.name).getAbsolutePath());
                    ProcessBuilder p = new ProcessBuilder(c.arguments);
                    p.directory(new File(DataManager.get("rlsh", "directory").string));
                    p.inheritIO();
                    p.start().waitFor();
                } catch(IOException e) {
                } catch(InterruptedException e) {}
            }
        } else if(c.name.startsWith(".")) {
            // it's a local program
            StringBuilder programNameBuilder = new StringBuilder(c.name);
            programNameBuilder.deleteCharAt(0);
            programNameBuilder.insert(0, c.name);
            String programName = programNameBuilder.toString();
            if(!new File(programName).exists()) {
                System.err.println("rlsh: Executable not found");
            } else if(new File(programName).isDirectory()) {
                System.err.println("rlsh: Can't execute a directory!");
            } else {
                try {
                    c.arguments.add(0, new File(programName).getAbsolutePath());
                    ProcessBuilder p = new ProcessBuilder(c.arguments);
                    p.directory(new File(DataManager.get("rlsh", "directory").string));
                    p.inheritIO();
                    p.start().waitFor();
                } catch(IOException e) {
                } catch(InterruptedException e) {}
            }
        } else {
            // Scan the PATH
            String[] folderPaths = DataManager.get("rlsh", "path").string.split(":");
            boolean success = false;
            boolean onlyLowBuiltins;
            try {
                onlyLowBuiltins = !wapi.core.Boolean.toBoolean(DataManager.get("rlsh", "only-lowbuiltins").bool);
            } catch(NullPointerException e) {
                onlyLowBuiltins = false;
            }
            if(!onlyLowBuiltins) {
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
            }
            if(success != true) {
                if(!c.name.equals("")) {
                    Hashtable<String, Class<Command>> lowBuiltins = HashtableFromReferenceFinder.
                        getHashtableFromBuiltinType(HashtableFromReferenceFinder.BuiltinType.LOW_BUILTIN);

                    boolean isLowBuiltin = false;
                    try {
                        isLowBuiltin = lowBuiltins.get(c.name) != null;
                    } catch(NullPointerException e) {}

                    if(isLowBuiltin) {
                        try {
                            Constructor<Command> toRun = lowBuiltins.get(c.name).getDeclaredConstructor(ArrayList.class);
                            CommandAction action = toRun.newInstance(c.arguments).action;

                            action.run();
                        } catch(Exception e) {
                            System.err.println("rlsh: error: Failed to run low-builtin command. This is a problem with your");
                            System.err.println("rlsh: error: installation or a plugin. Try removing some plugins.");
                            e.printStackTrace();
                            System.exit(-10);
                        }
                    } else {
                        System.err.println("rlsh: Command " + c.name + " not found.");
                    }
                }
                // don't do anything if command blank
            }
        }
    }
}
