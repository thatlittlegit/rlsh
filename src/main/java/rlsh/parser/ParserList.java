package rlsh.parser;

import java.util.Hashtable;

public class ParserList {
    public static Hashtable<String, Class<?>> parsers = new Hashtable<>();

    private static boolean hasFilled = false;

    static void fillParserList() {
	try {
	    parsers.put("seperator", Class.forName("rlsh.parser.ParserModuleSeperator"));
	    parsers.put("alias", Class.forName("rlsh.parser.ParserModuleAlias"));
	    parsers.put("builtins", Class.forName("rlsh.parser.ParserModuleBuiltin"));
	    parsers.put("empty", Class.forName("rlsh.parser.ParserModuleEmptry"));
	    parsers.put("root", Class.forName("rlsh.parser.ParserModuleRootDir"));
	    parsers.put("current", Class.forName("rlsh.parser.ParserModuleCurrentDir"));
	    parsers.put("path", Class.forName("rlsh.parser.ParserModulePath"));
	    parsers.put("fail", Class.forName("rlsh.parser.ParserModuleFail"));
	} catch(ClassNotFoundException e) {
	    System.err.println("rlsh: error: Required class not found: " + e.getMessage());
	    System.err.println("rlsh: error: Your installation may be corrupt.");
	    System.exit(-1);
	}
	System.exit(-1);
    }
}
