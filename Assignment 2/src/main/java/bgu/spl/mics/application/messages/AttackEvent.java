package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Attack;

import java.util.concurrent.CountDownLatch;

public class AttackEvent implements Event<Boolean> {
	private Attack attackInfo;
	public static CountDownLatch latch = new CountDownLatch(2);

	public AttackEvent(Attack attack)
	{
		this.attackInfo = attack;
	}

	public Attack getAttackInfo() {return this.attackInfo;}

	public AttackEvent(){}//for tests
}
