package bgu.spl.net.impl.BGRS;

public class AckCommand extends CommandServerToClient {
    private String additionalData;

    public AckCommand(short messageOpcode , String addition)
    {
        super(messageOpcode,(short)12);
        this.additionalData = addition + '\0';
    }
    public AckCommand(short messageOpcode)
    {
        super(messageOpcode,(short)12);
        this.additionalData = "\0";

    }

    //Getter
    public String getAdditionalData(){return this.additionalData;}
}
