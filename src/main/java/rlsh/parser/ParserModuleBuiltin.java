package rlsh.parser;

import rlsh.BuiltinType;
import rlsh.Command;
import rlsh.CommandAction;
import rlsh.CommandParser;
import rlsh.HashtableFromReferenceFinder;

import java.util.ArrayList;
import java.util.Hashtable;

import wapi.core.NullChecker;

import java.lang.reflect.Constructor;

public class ParserModuleBuiltin implements ParserModule {
    public static final String name = "builtin";

    public String getName() {
        return name;
    }

    public boolean getIfNeedToRun(Command command) {
        return NullChecker.isNull(HashtableFromReferenceFinder.getHashtableFromBuiltinType(BuiltinType.BUILTIN)
                                  .get(command.name));
    }

    public void run(Command command) {
        Hashtable<String, Class<Command>> builtins = HashtableFromReferenceFinder.
            getHashtableFromBuiltinType(BuiltinType.BUILTIN);
        try {
            Constructor<Command> toRun = builtins.get(command.name).getDeclaredConstructor(ArrayList.class);
            CommandAction action = toRun.newInstance(command.arguments).action;

            action.run();
        } catch(Exception e) {
            System.err.println("rlsh: error: Failed to run builtin command. This is a problem with your");
            System.err.println("rlsh: error: installation or a plugin. Try removing some plugins.");
            e.printStackTrace();
            System.exit(-3);
        }
    }
}
