package rlsh.parser;

import java.util.Hashtable;
import java.util.ArrayList;

import java.io.File;
import java.io.IOException;

import wapi.core.DataManager;

import rlsh.Command;
import rlsh.CommandParser;

public class ParserModulePath implements ParserModule {
    public static final String name = "path";
    public static final Hashtable<String, String> cache = new Hashtable<String, String>();

    public String getName() {
	return name;
    }

    public boolean getIfNeedToRun(Command command) {
	boolean onlyLowBuiltins;

	try {
	    onlyLowBuiltins = wapi.core.Boolean.toBoolean(DataManager.get("rlsh", "only-lowbuiltins").bool);
	} catch(NullPointerException e) {
	    onlyLowBuiltins = false;
	}
	if(onlyLowBuiltins) {
	    return false;
	}

	// Search the path for if command exists. If so, then
	// add it to our cache.
	String[] folderPaths = DataManager.get("rlsh", "path").string.split(":");
	boolean success = false;
	for(String folderPath : folderPaths) {
	    File folder = new File(folderPath);
	    for(File file : folder.listFiles()) {
		if(file.exists() && !file.isDirectory() &&
		   file.getName().replace("\\s+", "").equals(command.name.replace("\\s+", ""))) {
		    // Found a match, index it.
		    success = true;
		    cache.put(command.name, file.getAbsolutePath());
		}
	    }
	}
	return success;
    }

    public void run(Command command) {
	// Is the command in our cache?
	if(cache.get(command.name) == null) {
	    System.err.println("rlsh: warning: Path command called without being in cache. This is an issue with a plugin.");
	    // Reparse the command
	    CommandParser.run(command);
	}
	// Okay, now run it.
	ArrayList<String> arguments = command.arguments;
	arguments.add(0, new File(cache.get(command.name)).getAbsolutePath());
	ProcessBuilder p = new ProcessBuilder(arguments);
	p.directory(new File(DataManager.get("rlsh", "directory").string));
	p.inheritIO();
	try {
	    p.start().waitFor();
	} catch(IOException e) {
	    System.err.println("rlsh: error: Failed to run command.");
	    e.printStackTrace();
	} catch(InterruptedException e) { /* do nothing */ }
    }
}
