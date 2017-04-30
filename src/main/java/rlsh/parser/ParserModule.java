package rlsh.parser;

import rlsh.Command;

public interface ParserModule {
    public String getName();
    public boolean getIfNeedToRun(Command command);
    public void run(Command command);
}
