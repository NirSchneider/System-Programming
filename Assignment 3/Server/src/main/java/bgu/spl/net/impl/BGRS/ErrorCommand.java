package bgu.spl.net.impl.BGRS;

public class ErrorCommand extends CommandServerToClient {

    public ErrorCommand(short messageOpcode)
    {
        super(messageOpcode,(short)13);
    }

}
