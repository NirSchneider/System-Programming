package bgu.spl.net.impl.BGRS;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.impl.rci.Command;

public class BGRSEncoderDecoder implements MessageEncoderDecoder<Message> {

    //change names!!!!!!

    private byte[] opcodeBytes = new byte[2];
    private byte[] courseNum = new byte[2];
    private byte[] userName = new byte[1 << 10];
    private byte[] password = new byte[1 << 10];

    private int len = 0;
    private short opcode = -1;
    private int numOfZeroByte = 0;


    public short bytesToShort (byte [] byteArr)
    {
        short result = (short) ((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr [1] & 0xff);
        return result;
    }
    public byte[] shortToBytes (short num)
    {
        byte[] byteArr = new byte[2];
        byteArr[0] = (byte)((num >> 8 ) & 0xFF);
        byteArr[1] = (byte)(num & 0xFF);
        return byteArr;
    }

    public Message decodeNextByte(byte b)
    {
        CommandClientToServer output = null;
        if(opcode == -1)
        {
            opcodeBytes [len++] = b;
            if(len == 2) {
                opcode = bytesToShort(opcodeBytes);
                len = 0;
            }
            if(opcode ==11 || opcode ==4)
            {
                output = new CommandClientToServer(opcode,(short)-1,null,null);
                reset();
                return output;
            }
            return null;
        }

        switch(opcode){
            case 1: case 2: case 3:
            {
                String name = "";
                if(b != '\0' && numOfZeroByte == 0)
                {
                    this.userName[len++] = b;
                }
                else if(numOfZeroByte == 0)
                {
                    numOfZeroByte++;
                    len = 0;
                }
                else if(b != '\0' && numOfZeroByte == 1)
                {
                    password[len++] = b;
                }
                else
                {
                    name = new String(this.userName);
                    name = adjustLenght(name);
                    String password = new String(this.password);
                    password = adjustLenght(password);
                    numOfZeroByte = 0;
                    output = new CommandClientToServer(opcode,(short)-1,name,password);
                    reset();
                    return output;
                }
                break;
            }
            case 4: case 11:
            {
                output = new CommandClientToServer(opcode,(short)-1,null,null);
                reset();
                return output;
            }
            case 5: case 6: case 7: case 9: case 10:
            {
                courseNum[len++] = b;
                if(len == 2)
                {
                    output = new CommandClientToServer(opcode,bytesToShort(courseNum),null,null);
                    reset();
                    return output;
                }
                break;
            }
            case 8:
            {
                if(b != '\0')
                {
                    userName[len++] = b;
                }
                else
                {
                    String userName = new String(this.userName);
                    userName = adjustLenght(userName);
                    output = new CommandClientToServer(opcode,(short)-1,userName,null);
                    reset();
                    return output;
                }
                break;
            }
        }
        return null;
    }

    public byte[] encode(Message message){
        CommandServerToClient command = (CommandServerToClient)message;
        byte[] currOpcode = shortToBytes(command.getCurrOpcode());
        byte[] msgOpcode = shortToBytes(command.getMessageOpcode());
        byte[] output = append(currOpcode,msgOpcode);
        if(message instanceof AckCommand)
        {
            byte[] additional = (((AckCommand)command).getAdditionalData()).getBytes();//uses utf8
            return append(output, additional); //return Ack
        }
        return output; //return Error
    }

    public byte[] append (byte[] arr1, byte[] arr2)
    {
        byte[] output = new byte[arr1.length+arr2.length];
        int i;
        for (i=0; i<arr1.length; i++)
        {
            output[i] = arr1[i];
        }
        for (int j=0; j<arr2.length; j++)
        {
            output[i+j] = arr2[j];
        }
        return output;
    }

public String adjustLenght(String s)
{
    String output = "";
    for(int i=0; s.charAt(i)!='\0';i++)
        output+=s.charAt(i);
    return output;
}

public void reset()
{
    this.userName = new byte[1 << 10];
    this.password = new byte[1 << 10];
    this.opcodeBytes = new byte[2];
    this.opcode=-1;
    this.len = 0;
    this.courseNum = new byte[2];
}

}
