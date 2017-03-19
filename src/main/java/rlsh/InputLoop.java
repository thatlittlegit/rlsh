package rlsh;

import wapi.core.DataManager;
import wapi.core.Boolean;
import wapi.core.CompiledValue;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

import static java.lang.System.out;

public class InputLoop /*implements Runnable*/ {
    public static void run() {
        // TODO Use BufferedReader for System.in for better functionality
        Scanner scanner = new Scanner(System.in);
        String input = null;
        while(Boolean.toBoolean(DataManager.get("rlsh", "continue-shell").bool)) {
            if(Boolean.toBoolean(DataManager.get("rlsh", "ps1-calculated").bool) == true) {
                out.print(DataManager.get("rlsh", "ps1").string);
                try {
                    input = scanner.nextLine();
                } catch(NoSuchElementException e) {
                    out.println("");
                    input = "exit";
                }
                // TODO Add onCommand for JS plugins
                String name = input.split(" ")[0].replace("\\s+", "");
                ArrayList<String> arguments = new ArrayList<>(Arrays.asList(input.split(" ")));
                arguments.remove(0); // Remove the name from the arguments
                Command c = new Command(name, arguments, null);
                CommandParser.run(c);
            } else {
                DataManager.put("rlsh", "ps1", new CompiledValue(StringFormatter.format(DataManager.get("rlsh", "ps1-uncalculated").string)));
                DataManager.put("rlsh", "ps1-calculated", new CompiledValue(true));
            }
        }
        scanner.close();
    }
}
