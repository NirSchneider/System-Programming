package bgu.spl.net.impl.BGRS;

public class CommandClientToServer implements Message{

    private short opcode;
    private short courseNumber;
    private String userName;
    private String password;

    public CommandClientToServer(short opcode,short courseNumber,String userName,String password)
    {
        this.opcode = opcode;
        this.courseNumber = courseNumber;
        this.userName = userName;
        this.password = password;
    }

    //Getters
    public short getOpcode(){return this.opcode;}
    public short getCourseNumber() {return courseNumber;}
    public String getUserName() {return userName;}
    public String getPassword() {return password;}
}
