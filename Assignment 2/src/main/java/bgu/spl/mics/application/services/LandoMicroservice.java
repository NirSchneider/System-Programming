package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.BroadcastTermination;
import bgu.spl.mics.application.messages.BombDestroyerEvent;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {
    private long duration;

    public LandoMicroservice(long duration) {
        super("Lando");
        this.duration = duration;
    }

    @Override
    protected void initialize() {
        this.subscribeBroadcast(BroadcastTermination.class,(BroadcastTermination tBroad) -> {
            diary.setTimeStampToTerminate(this);
            terminate();
        });

        this.subscribeEvent(BombDestroyerEvent.class,(BombDestroyerEvent bEvent) -> {
            try{
                Thread.sleep(this.duration);//bomb death star
            } catch (InterruptedException ex) { }
            this.complete(bEvent,true);
            diary.setTimeStampToFinish(this);
        });
        BombDestroyerEvent.latch.countDown();
    }

}
