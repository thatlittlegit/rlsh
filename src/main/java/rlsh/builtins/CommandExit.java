package rlsh.builtins;

import rlsh.Command;
import rlsh.CommandAction;

import wapi.core.DataManager;
import wapi.core.CompiledValue;

import java.util.ArrayList;

public class CommandExit extends Command implements CommandAction {
    public CommandExit(ArrayList<String> arguments) {
        super("exit", arguments);
        this.action = this;
    }

    public void run() {
        DataManager.put("rlsh", "continue-shell", new CompiledValue(false));
        System.out.println("logout");
    }
}
