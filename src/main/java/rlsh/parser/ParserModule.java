package rlsh.parser;

import rlsh.Command;

public interface ParserModule {
    public String getName();
    public boolean getIfNeedToRun(Command);
    public void run(Command);
}
