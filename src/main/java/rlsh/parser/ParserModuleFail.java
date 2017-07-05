package rlsh.parser;

import rlsh.Command;
import rlsh.CommandAction;
import rlsh.HashtableFromReferenceFinder;
import rlsh.BuiltinType;

import java.util.ArrayList;
import java.util.Hashtable;

import java.lang.reflect.Constructor;

import wapi.core.DataManager;
import wapi.core.NullChecker;

public class ParserModuleFail implements ParserModule {
    public static final String name = "fail";

    private static Hashtable<String, Class<Command>> cache = HashtableFromReferenceFinder.getHashtableFromBuiltinType(BuiltinType.LOW_BUILTIN);

    public String getName() {
	return name;
    }

    public boolean getIfNeedToRun(Command c) {
	return true;
    }

    public void run(Command c) {
	try {
	    if(NullChecker.isNull(cache.get(c.name))) {
		System.err.println("rlsh: Command not found.");
	    } else {
		Constructor<Command> toRun = cache.get(c.name).getDeclaredConstructor(ArrayList.class);
		CommandAction action = toRun.newInstance(c.arguments).action;
		action.run();
	    }
	} catch(NullPointerException e) {
	    System.err.println("Command not found.");
	}
    }
}
