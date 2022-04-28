package bgu.spl.net.impl.BGRS;

public class CommandServerToClient implements Message{

    //only for Err and Ack
    protected short currOpcode;
    protected short messageOpcode;

    public CommandServerToClient(short messageOpcode, short currOpcode)
    {
        this.currOpcode = currOpcode;
        this.messageOpcode = messageOpcode;
    }

    //Getters
    public short getCurrOpcode(){return this.currOpcode;}
    public short getMessageOpcode() {return messageOpcode;}
}
