package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.BroadcastTermination;
import bgu.spl.mics.application.messages.DeactivationEvent;

/**
 * R2D2Microservices is in charge of the handling {@link DeactivationEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link DeactivationEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class R2D2Microservice extends MicroService {
    private long duration;

    public R2D2Microservice(long duration) {
        super("R2D2");
        this.duration = duration;
    }

    @Override
    protected void initialize() {
        this.subscribeBroadcast(BroadcastTermination.class,(BroadcastTermination tBroad) -> {
            diary.setTimeStampToTerminate(this);
            terminate();
        });
        this.subscribeEvent(DeactivationEvent.class,(DeactivationEvent dEvent) -> {
            try{
                Thread.sleep(this.duration);//deactivating shield
               } catch (InterruptedException ex){ex.printStackTrace();}
            this.complete(dEvent,true);
            diary.setTimeStampToFinish(this);
        });
        DeactivationEvent.latch.countDown();
    }

}















