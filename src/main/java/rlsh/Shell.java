package rlsh;

public interface Shell {
    public String getName();
    public CommandParser getCommandParser();
    // NOTE: Someone add a "public static void main(String[])" here, I can't make it work
}
