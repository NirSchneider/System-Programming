package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.impl.BGRS.BGRSEncoderDecoder;
import bgu.spl.net.impl.BGRS.BGRSProtocol;
import bgu.spl.net.impl.BGRS.Database;
import bgu.spl.net.srv.Server;

public class TPCMain {

    public static void main(String[] args)
    {
        int inPort;
        if(args.length < 1){throw new IllegalArgumentException();}
        try{
            inPort = Integer.decode(args[0]);
        }catch(NumberFormatException ex){throw new IllegalArgumentException();}

        Database database = Database.getInstance();
        database.initialize("Courses.txt");//path
        Server tpc = Server.threadPerClient(inPort, BGRSProtocol::new, BGRSEncoderDecoder::new);
        tpc.serve();
    }
}
