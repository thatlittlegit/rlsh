package rlsh;

import wapi.core.DataManager;

import java.util.Hashtable;
import java.util.ArrayList;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;

import java.io.File;
import java.io.IOException;

public class CommandParser {
    public static void run(Command c) {
        try {
            // get the builtin class
            Class<?> rlshShellBuiltins = Class.forName(DataManager.get("rlsh", "shell-builtin-class").string);
            // Check if it's processed the builtins
            Boolean hasChecked = (Boolean) rlshShellBuiltins.getDeclaredMethod("getIsFilled").invoke(null);
            if(!hasChecked.booleanValue()) {
                // if not, process it
                rlshShellBuiltins.getDeclaredMethod("fillBuiltins").invoke(null);
            }
            // now, get the builtin list as a Hashtable
            Hashtable<String, Class<Command>> builtins = (Hashtable<String, Class<Command>>) rlshShellBuiltins.getField("builtins").get(null);

            if(builtins.get(c.name) != null) {
                Constructor<Command> toRun = (Constructor<Command>)builtins.get(c.name).getDeclaredConstructor(ArrayList.class);
                toRun.newInstance(c.arguments).action.run();
            } else {
                // We don't want to use the same catches, as this is a different component.
                //try {
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
                    //}
            }
            //
            // the OFFICIAL WALL of CATCHES!
            //
        } catch(NoSuchFieldException e) {
            System.err.println("rlsh: error: Shell builtin list does not contain Hashtable map, this is a problem");
            System.err.println("rlsh: error: with your installation, implementation, or a plugin. Try removing");
            System.err.println("rlsh: error: some plugins to see if that fixes it.");
            e.printStackTrace();
            System.exit(-2);
        } catch(IllegalAccessException e) {
            System.err.println("rlsh: error: Failed to access necessary fields in shell builtin list. This is a");
            System.err.println("rlsh: error: problem with your installation, implementation, or a plugin.");
            System.err.println("rlsh: error: Try removing some plugins to see if that fixes it.");
            e.printStackTrace();
            System.exit(-3);
        } catch(ClassNotFoundException e) {
            System.err.println("rlsh: error: Builtin class is not found, this may be a problem with a plugin.");
            e.printStackTrace();
            System.exit(-4);
        } catch(InvocationTargetException e) {
            System.err.println("rlsh: error: An error occurred while processing builtins.");
            e.printStackTrace();
            System.exit(-5);
        } catch(NoSuchMethodException e) {
            System.err.println("rlsh: error: A required method for builtin management is missing, this is a");
            System.err.println("rlsh: error: problem with your installation, implementation or a plugin. Try");
            System.err.println("rlsh: error: removing a plugin.");
            e.printStackTrace();
            System.exit(-6);
        } catch(InstantiationException e) {
            System.err.println("rlsh: error: Failed to make command instance, this may be a problem with a plugin.");
        }
    }
}
