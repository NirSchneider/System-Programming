package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.BroadcastTermination;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import java.util.Collections;


/**
 * C3POMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class C3POMicroservice extends MicroService {
	
    public C3POMicroservice() {
        super("C3PO");
    }

    @Override
    protected void initialize() {
        this.subscribeBroadcast(BroadcastTermination.class,(BroadcastTermination tBroad) -> {
            diary.setTimeStampToTerminate(this);
            terminate();
        });

        this.subscribeEvent(AttackEvent.class, (AttackEvent aEvent) -> {
            Ewoks ewoks = Ewoks.getInstance();
            Collections.sort(aEvent.getAttackInfo().getSerials());//sort list of ewoks serials to prevent possible deadlock
            ewoks.acquire(aEvent.getAttackInfo().getSerials());
            ewoks.makeResource(aEvent.getAttackInfo().getSerials().size());
            try{
                Thread.sleep(aEvent.getAttackInfo().getDuration());// attacking
            }catch (InterruptedException ex){ex.printStackTrace();}
            ewoks.releaseResources(aEvent.getAttackInfo().getSerials());
            this.diary.increaseNumberAttack();
            this.complete(aEvent,true);
            this.diary.setTimeStampToFinish(this);
        });
        AttackEvent.latch.countDown();
    }

}
