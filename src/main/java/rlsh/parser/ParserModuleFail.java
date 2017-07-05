package rlsh.parser;

import rlsh.Command;
import rlsh.CommandAction;
import rlsh.HashtableFromReferenceFinder;
import rlsh.BuiltinType;

import java.util.ArrayList;
import java.util.Hashtable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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
		Exception failed = null;
		CommandAction action = null;

		try {
		    Constructor<Command> toRun = cache.get(c.name).getDeclaredConstructor(ArrayList.class);

		    action = toRun.newInstance(c.arguments).action;
		} catch(NoSuchMethodException e) {
		    failed = e;
		} catch(InstantiationException e) {
		    failed = e;
		} catch(IllegalAccessException e) {
		    failed = e;
		} catch(InvocationTargetException e) {
		    failed = e;
		} finally {
		    if(failed != null) {
			System.err.println("rlsh: error: Failed to run builtin command. This is a problem with your");
			System.err.println("rlsh: error: installation or a plugin. Try removing some plugins.");
			failed.printStackTrace();
			System.exit(-10);
		    }
		    action.run();
		}
	    }
	} catch(NullPointerException e) {
	    System.err.println("Command not found.");
	}
    }
}
