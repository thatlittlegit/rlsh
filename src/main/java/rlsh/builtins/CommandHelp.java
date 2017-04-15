package rlsh.builtins;

import rlsh.Command;
import rlsh.CommandAction;
import rlsh.CommandParser;

import java.util.ArrayList;

public class CommandHelp extends Command implements CommandAction {
    public CommandHelp(ArrayList<String> arguments) {
        super("cd", arguments);
        this.action = this;
    }

    protected String underline(String toUnderline) {
        return (char) 27 + "[4m" + toUnderline + (char) 27 + "[24m";
    }

    protected String italic(String toItalicize) {
        return (char) 27 + "[3m" + toItalicize + (char) 27 + "[23m";
    }

    public void run() {
        if(arguments.size() < 1 || arguments.size() > 1) {
            System.out.println("rlsh by wapidstyle, version 0.1.0");
            System.out.println("an attempt at a cross-platform shell");
            System.out.println("");
            System.out.println("\trlsh is under the Modified BSD License (BSD-3). You should have");
            System.out.println("\trecieved a copy of the license - if not, see <http://spdx.org/licenses/BSD-3-Clause>.");
            System.out.println("");
            System.out.println("Some builtin commands:");
            System.out.println(underline("pwd") + "   - Prints current directory.");
            System.out.println(underline("cd") + "    - Changes current directory.");
            System.out.println(underline("alias") + " - Makes one command run another.");
            System.out.println(underline("exit") + "  - Exits the shell, since you asked nicely.");
            System.out.println("Some ones that you might like: " + italic("(if not installed, then a basic one's provided!)"));
            System.out.println(underline("rm") + " - Deletes files. NO WASTEBASKET!   " + underline("mv") + " - Moves a file");
            System.out.println(underline("ls") + " - Lists files.                     " + underline("cp") + " - Copies files");
            System.out.println("");
            System.out.println("For more information, see the " + italic("(currently nonexistant)") + " documentation.");
        } else {
            // NOTE We should probably do a better job at handling the help instead of just passing it on
            ArrayList<String> argsToPass = new ArrayList<>();
            argsToPass.add("--help");
            CommandParser.run(new Command(arguments.get(0), argsToPass));
        }
    }
}
