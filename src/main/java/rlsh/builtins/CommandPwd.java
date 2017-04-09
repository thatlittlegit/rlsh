package rlsh.builtins;

import rlsh.Command;
import rlsh.CommandAction;

import wapi.core.DataManager;

import java.util.ArrayList;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

public class CommandPwd extends Command implements CommandAction {
    public CommandPwd(ArrayList<String> arguments) {
        super("pwd", arguments);
        this.action = this;
    }

    public void run() {
        if(arguments.isEmpty()) {
            System.out.println(DataManager.get("rlsh", "directory").string);
        } else {
            // true=logical, false=physical
            boolean outputType = false;
            // true=help/version, false=neither
            boolean helpOrVersion = false;
            // true=version, false=help
            boolean version = false;

            boolean worked = true;

            try {
                for(int argument = 0; arguments.get(argument) != null && worked == true; argument++) {
                    if(arguments.get(argument).startsWith("-") && !arguments.get(argument).startsWith("--")) {
                        // Is a short argument, parse it
                        if(!StringUtils.containsOnly(arguments.get(argument), "-LPvh")) {
                            System.err.println("rlsh: Unknown argument set " + arguments.get(argument));
                            worked = false;
                            break;
                        }
                        if(arguments.get(argument).contains("L")) {
                            // don't check
                            outputType = true;
                        }
                        if(arguments.get(argument).contains("P")) {
                            // do check
                            if(outputType == true) {
                                System.err.println("rlsh: Can't print logical and physical at the same time!");
                                worked = false;
                                break;
                            } else {
                                outputType = false;
                            }
                        }
                        if(arguments.get(argument).contains("v")) {
                            helpOrVersion = true;
                            version = true;
                        }
                        if(arguments.get(argument).contains("h")) {
                            if(version == true) {
                                System.err.println("rlsh: Can't display both version and help at the same time!");
                                worked = false;
                                break;
                            } else {
                                helpOrVersion = true;
                                version = false;
                            }
                        }
                    } else if(arguments.get(argument).startsWith("--")) {
                        if(arguments.get(argument) == "--logical") {
                            outputType = true;
                        }
                        if(arguments.get(argument) == "--physical") {
                            if(outputType == true) {
                                System.err.println("rlsh: Can't print logical and physical at the same time!");
                                worked = false;
                                break;
                            } else {
                                outputType = false;
                            }
                        }
                        if(arguments.get(argument) == "--version") {
                            helpOrVersion = true;
                            version = true;
                        }
                        if(arguments.get(argument) == "--help") {
                            if(version == true) {
                                System.err.println("rlsh: Can't display both version and help at the same time!");
                                worked = false;
                                break;
                            } else {
                                helpOrVersion = true;
                                version = false;
                            }
                        }
                    }
                }
            } catch(NullPointerException e) {
            } catch(IndexOutOfBoundsException e) {}
            // output
            if(worked) {
                if(helpOrVersion == true) {
                    if(version == true) {
                        System.out.println("rlsh: pwd: rlsh version 0.1.0");
                    } else {
                        System.out.println("rlsh: pwd: Usage: pwd [options]");
                    }
                } else {
                    if(outputType == true) {
                    // logical
                        System.out.println(DataManager.get("rlsh", "directory").string);
                    } else {
                        // physical
                        System.out.println(new File(DataManager.get("rlsh", "directory").string).getAbsolutePath());
                    }
                }
            }
        }
    }
}
