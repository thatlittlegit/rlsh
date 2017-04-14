package rlsh;

import wapi.core.DataManager;
import wapi.core.NullChecker;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class StringFormatter {
    // toFormat is final to stop me from coding bad
    public static String format(final String toFormat) {
        String formatted = toFormat;

        // Change ${ to the friendlier %$
        formatted = formatted.replace("${", "%$");

        // Find any non-escaped }s
        // First check if there are any
        if(StringUtils.countMatches(formatted, "}") + 1 == 1) {
            // Ignore it
        } else {
            for(int index = 0; index <= StringUtils.countMatches(formatted, "}") + 1; index += 1) {
                if(Character.toString(formatted.charAt(formatted.indexOf("}") - 1)).toString() != "\"") {
                    // We can change it
                    StringBuilder tempFormatted = new StringBuilder(formatted);
                    int charAffected = formatted.indexOf("}");
                    tempFormatted.setCharAt(charAffected, "$".charAt(0));
                    tempFormatted.insert(charAffected + 1, "%");
                    formatted = tempFormatted.toString();
                } else {
                    // Remove the \ and change the character to some obscure
                    // Unicode character and change it back after.
                    // Only caveat is that you can't use a Latin Small Letter
                    // E with Stroke
                    formatted.replaceFirst("\\}", "\u0247");
                }
            }
        }

        // Okay, now we have the old *$var$* system. Use it.
        String[] outputArray = formatted.split("%");

        try {
            for(int section = 0; !NullChecker.isNull(outputArray[section]); section += 1) {
                String varName = outputArray[section].replace("$", "");
                if(varName != outputArray[section]) {
                    try {
                        if(!NullChecker.isNull(DataManager.get("rlsh", "stringvar-" + varName).string)){
                            outputArray[section] = DataManager.get("rlsh", "stringvar-" + varName).string;
                        } else if(!NullChecker.isNull(System.getenv(varName))) {
                            outputArray[section] = System.getenv(varName);
                        } else {
                        outputArray[section] = varName;
                        }
                    } catch(NullPointerException e) {}
                } else {
                    outputArray[section] = varName;
                }
            }
        } catch(ArrayIndexOutOfBoundsException e) {} // Ignore
        return StringUtils.join(outputArray, "");
    }
}
