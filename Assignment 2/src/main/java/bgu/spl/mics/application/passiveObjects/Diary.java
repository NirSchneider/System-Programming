package bgu.spl.mics.application.passiveObjects;


import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.services.C3POMicroservice;
import bgu.spl.mics.application.services.HanSoloMicroservice;
import bgu.spl.mics.application.services.LeiaMicroservice;
import bgu.spl.mics.application.services.R2D2Microservice;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a Diary - in which the flow of the battle is recorded.
 * We are going to compare your recordings with the expected recordings, and make sure that your output makes sense.
 * <p>
 * Do not add to this class nothing but a single constructor, getters and setters.
 */
public class Diary {

    private static class DiarySingletonHolder{
        private static Diary instance = new Diary();
    }
    private AtomicInteger totalAttacks = new AtomicInteger(0);
    private long HanSoloFinish;
    private long C3POFinish;
    private long R2D2Deactivate;
    private long LeiaTerminate;
    private long HanSoloTerminate;
    private long C3POTerminate;
    private long R2D2Terminate;
    private long LandoTerminate;

    private Diary()
    {
        HanSoloFinish =0;
        C3POFinish =0;
        R2D2Deactivate =0;
        LeiaTerminate =0;
        HanSoloTerminate =0;
        C3POTerminate =0;
        R2D2Terminate =0;
        LandoTerminate = 0;
    }

    public static Diary getInstance()
    {
        return DiarySingletonHolder.instance;
    }


    public void increaseNumberAttack()
    {
        int val;
        do{
            val = totalAttacks.get();
        }while(!totalAttacks.compareAndSet(val,val+1));
    }

    public void setTimeStampToFinish(MicroService m)
    {
        if(m instanceof C3POMicroservice)
            this.C3POFinish = System.currentTimeMillis();
        else if(m instanceof HanSoloMicroservice)
            this.HanSoloFinish = System.currentTimeMillis();
        else
            this.R2D2Deactivate = System.currentTimeMillis();
    }

    public void setTimeStampToTerminate(MicroService m)
    {
        if(m instanceof C3POMicroservice)
            this.C3POTerminate = System.currentTimeMillis();
        else if(m instanceof HanSoloMicroservice)
            this.HanSoloTerminate = System.currentTimeMillis();
        else if (m instanceof R2D2Microservice)
            this.R2D2Terminate = System.currentTimeMillis();
        else if(m instanceof LeiaMicroservice)
            this.LeiaTerminate = System.currentTimeMillis();
        else
            this.LandoTerminate = System.currentTimeMillis();
    }

    public AtomicInteger getTotalAttacks(){return this.totalAttacks;}
    public long getHanSoloFinish(){return this.HanSoloFinish;}
    public long getC3POFinish(){return this.C3POFinish;}
    public long getR2D2Deactivate(){return this.R2D2Deactivate;}
    public long getLeiaTerminate(){return this.LeiaTerminate;}
    public long getHanSoloTerminate(){return this.HanSoloTerminate;}
    public long getC3POTerminate(){return this.C3POTerminate;}
    public long getR2D2Terminate(){return this.R2D2Terminate;}
    public long getLandoTerminate(){return this.LandoTerminate;}

    public void resetNumberAttacks(){
        this.totalAttacks.getAndSet(0);
    }

}
