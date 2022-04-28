package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.concurrent.CountDownLatch;

public class DeactivationEvent implements Event<Boolean> {

    public static CountDownLatch latch = new CountDownLatch(1);
}

