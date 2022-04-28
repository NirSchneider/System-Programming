package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.impl.BGRS.BGRSEncoderDecoder;
import bgu.spl.net.impl.BGRS.BGRSProtocol;
import bgu.spl.net.impl.BGRS.Database;
import bgu.spl.net.srv.Server;

public class ReactorMain {

    public static void main(String[] args)
    {
        int inPort;
        int threadNum;
        if(args.length < 2){throw new IllegalArgumentException();}
        try{
            inPort = Integer.decode(args[0]);
            threadNum = Integer.decode(args[1]);
        }catch (NumberFormatException ez){throw new IllegalArgumentException();}

        Database data = Database.getInstance();
        data.initialize("Courses.txt");//path
        Server.reactor(threadNum,inPort, BGRSProtocol::new, BGRSEncoderDecoder::new).serve();
    }
}
