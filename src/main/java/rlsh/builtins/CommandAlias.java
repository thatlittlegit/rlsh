package rlsh.builtins;

import rlsh.Command;
import rlsh.CommandAction;

import wapi.core.DataManager;
import wapi.core.CompiledValue;
import wapi.core.NullChecker;

import java.util.ArrayList;

public class CommandAlias extends Command implements CommandAction {
    public CommandAlias(ArrayList<String> arguments) {
        super("alias", arguments);
        this.action = this;
    }

    public void run() {
        if(arguments.size() < 1) {
            System.err.println("rlsh: alias: Missing operand (it's different from Bash's syntax)");
            System.err.println("rlsh: alias: Usage: alias <command>=<alias>");
        } else {
            String command = arguments.get(0).split("=")[0];
            String alias = arguments.get(0).split("=")[1];
            String aliasArguments = "";

            boolean aliasLonger;

            try {
                if(aliasLonger = !NullChecker.isNull(arguments.get(1))) {

                    for(int part = 1; arguments.get(part) != null; part += 1) {
                        aliasArguments += " " + arguments.get(part);
                    }
                }
            } catch(IndexOutOfBoundsException e) {}

            DataManager.put("rlsh", "alias-" + command, new CompiledValue(alias));
            DataManager.put("rlsh", "alias-" + command + "-arguments", new CompiledValue(aliasArguments));
        }
    }
}
