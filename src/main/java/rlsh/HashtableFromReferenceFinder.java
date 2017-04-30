//
// WARNING: This file is NOT a part of rlsh and may be put in wapi soon.
//
package rlsh;

import wapi.core.DataManager;

import java.util.Hashtable;
import java.lang.reflect.InvocationTargetException;

public class HashtableFromReferenceFinder {
    public static Hashtable<String, Class<Command>> getHashtableFromReference(String className) {
        try {
            Class<?> hashtableClass = Class.forName(className);

            Boolean hasChecked = (Boolean) hashtableClass.getDeclaredMethod("getIsFilled").invoke(null);
            if(!hasChecked.booleanValue()) {
                hashtableClass.getDeclaredMethod("fillBuiltins").invoke(null);
            }

            @SuppressWarnings("unchecked")
            Hashtable<String, Class<Command>> builtins = (Hashtable<String, Class<Command>>)
                hashtableClass.getField("builtins").get(null);
            return builtins;
        } catch(ClassNotFoundException e) {
            System.err.println("rlsh: error: Hashclass not found, this is a problem with your");
            System.err.println("rlsh: error: installation or a plugin. Try removing some plugins.");
            e.printStackTrace();
            System.exit(-2);
        } catch(NoSuchFieldException e) {
            System.err.println("rlsh: error: Field not found in hashclass" + className + ". This is");
            System.err.println("rlsh: error: a problem with your installation or a plugin. Try removing");
            System.err.println("rlsh: error: some plugins.");
            e.printStackTrace();
            System.exit(-2);
        } catch(IllegalAccessException e) {
            System.err.println("rlsh: error: Failed to access fields in hashclass " + className + ".");
            System.err.println("rlsh: error: This is a problem with your installation or a plugin. Try");
            System.err.println("rlsh: error: removing some plugins.");
            e.printStackTrace();
            System.exit(-2);
        } catch(NoSuchMethodException e) {
            System.err.println("rlsh: error: Failed to get if hashclass is filled. This is a problem with");
            System.err.println("rlsh: error: a plugin.");
            e.printStackTrace();
            System.exit(-2);
        } catch(InvocationTargetException e) {
            System.err.println("rlsh: error: A problem occurred while reading a hashclass. This is a");
            System.err.println("rlsh: error: problem with a plugin.");
            e.printStackTrace();
            System.exit(-2);
        }
        return null;
    }

    public static Hashtable<String, Class<Command>> getHashtableFromBuiltinType(BuiltinType type)
        throws IllegalArgumentException {
        switch(type) {
        case BUILTIN:
            return getHashtableFromReference(DataManager.get("rlsh", "shell-builtin-class").string);
        case LOW_BUILTIN:
            return getHashtableFromReference(DataManager.get("rlsh", "shell-low-builtin-class").string);
        default:
            throw new IllegalArgumentException("Failed to determine builtin type.");
        }
    }

    enum BuiltinType {
        BUILTIN, LOW_BUILTIN
    }
}
