package bgu.spl.mics.application.services;

import java.util.LinkedList;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.BroadcastTermination;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.passiveObjects.Attack;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
	private Attack[] attacks;
    private LinkedList<Future> futureList;


    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
		this.attacks = attacks;
		this.futureList=new LinkedList<>();
    }

    @Override
    protected void initialize() {
        this.subscribeBroadcast(BroadcastTermination.class,(BroadcastTermination tBroad) -> {
            diary.setTimeStampToTerminate(this);
            terminate();
        });

        try{
    	    AttackEvent.latch.await();// wait until HanSolo and C3PO initialize
        }catch (InterruptedException ex){ex.printStackTrace();}

        Future future;
        for(Attack a : attacks) // send all attack events
        {
            AttackEvent aEvent = new AttackEvent(a);
            future = this.sendEvent(aEvent);
            futureList.addLast(future);
        }
        for(Future f : futureList) // make sure all attacks are completed
        {
            f.get();
        }

        try{
            DeactivationEvent.latch.await();// wait until R2D2 initialize
        }catch (InterruptedException ex){ex.printStackTrace();}

        DeactivationEvent dEvent = new DeactivationEvent();
        future = this.sendEvent(dEvent);
        //futureList.add(future);
        future.get();//wait until shield deactivate

        try{
            BombDestroyerEvent.latch.await();// wait until Lando initialize
        }catch (InterruptedException ex){ex.printStackTrace();}

        BombDestroyerEvent bEvent = new BombDestroyerEvent();
        future = this.sendEvent(bEvent);
        //futureList.add(future);
        future.get();//wait until lando bomb death star

        BroadcastTermination tEvent = new BroadcastTermination();
        this.sendBroadcast(tEvent);
    }
}















