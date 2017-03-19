package rlsh;

import wapi.core.DataManager;
import wapi.core.NullChecker;

import java.util.Arrays;

public class StringFormatter {
    // toFormat is final to stop me from coding bad
    public static String format(final String toFormat) {
        String outputArray[] = toFormat.split("%");
        try {
            for(short section = 0; !NullChecker.isNull(outputArray[section]); section += 1) {
                try {
                    System.out.println(DataManager.get("rlsh", "stringvar-" + outputArray[section].replace("$", "")).string);
                    if(DataManager.get("rlsh", "stringvar-" + outputArray[section].replace("$", "")) != null) {
                        outputArray[section] = DataManager.get("rlsh", "stringvar-" + outputArray[section].replace("$", "")).string;
                    }
                } catch(NullPointerException e) {}
            }
        } catch(ArrayIndexOutOfBoundsException e) {}
        return String.join("", outputArray);
    }
}
