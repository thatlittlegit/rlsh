package rlsh.parser;

import rlsh.Command;

import wapi.core.DataManager;

public class ParserModuleAlias implements ParserModule {
    public static final String name = "alias";

    public void getName() {
        return name;
    }

    public boolean getIfNeedToRun(Command command) {
        boolean needToRun = false;
        try {
            needToRun = DataManager.get("rlsh", "alias-" + command.name).string != null;
        } catch(NullPointerException e) {
            needToRun = false;
        }

        return needToRun;
    }

    public void run(Command command) {
        // We'll assume that getIfNeedToRun has been done - if not, they deserve the error
        ArrayList<String> arguments = new ArrayList<>(command.arguments);

        try {
            ArrayList<String> aliasArguments = new ArrayList<>(DataManager.get("rlsh", "alias-" + command.name + "-arguments")
                                                               .string.split(" "));
            aliasArguments.remove(0);
            arguments.addAll(aliasArguments);
        } catch(NullPointerException e) {}
        CommandParser.run(new Command(DataManager.get("rlsh", "alias-" + c.name).string, arguments));
    }
}
