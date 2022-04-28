package bgu.spl.mics;

import bgu.spl.mics.application.BroadcastTermination;
import bgu.spl.mics.application.messages.AttackEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {


    private MessageBusImpl messageBus;
    private MicroService micro;
    private MicroService micro2;
    private AttackEvent event;
    private Future<?> future;
    private BroadcastTermination broadcast;

    @BeforeEach
    void setUp() {
        messageBus = MessageBusImpl.getInstance();
        event = new AttackEvent();
        future = new Future<>();
        broadcast = new BroadcastTermination();
        micro = new MicroService("C3PO") {
            @Override
            protected void initialize() {}
        };
        micro2 = new MicroService("Han-Solo") {
            @Override
            protected void initialize() {}
        };
    }


    @Test
    void subscribeEvent() {
        messageBus.register(micro);
        messageBus.subscribeEvent(event.getClass(),micro);
        messageBus.sendEvent(event);
        try {
            assertEquals(event, messageBus.awaitMessage(micro));
        }
        catch(InterruptedException ex){}
        messageBus.unregister(micro);
    }

    @Test
    void subscribeBroadcast() {
        messageBus.sendBroadcast(broadcast);
    }

    @Test
    void complete() {
        messageBus.register(micro);
        messageBus.subscribeEvent(event.getClass(),micro);
        future = messageBus.sendEvent(event);
        assertFalse(future.isDone());
        try {
            messageBus.awaitMessage(micro);
        } catch (InterruptedException e) {}
        messageBus.complete((Event)event,5);
        assertEquals(5,future.get());
        assertTrue(future.isDone());
        messageBus.unregister(micro);
    }

    @Test
    void sendBroadcast() {
        messageBus.register(micro);
        messageBus.register(micro2);
        messageBus.subscribeBroadcast(broadcast.getClass(),micro);
        messageBus.subscribeBroadcast(broadcast.getClass(),micro2);
        messageBus.sendBroadcast(broadcast);
        try {
            assertEquals(broadcast,messageBus.awaitMessage(micro));
            assertEquals(broadcast,messageBus.awaitMessage(micro2));
        }
        catch(InterruptedException ex){}
        messageBus.unregister(micro);
        messageBus.unregister(micro2);
    }

    @Test
    void sendEvent() {
        this.subscribeEvent();
    }

    @Test
    void register() {
        messageBus.register(micro);
        messageBus.subscribeEvent(event.getClass(),micro);
        messageBus.sendEvent(event);
        try {
            assertEquals(event, messageBus.awaitMessage(micro));
        }
        catch(InterruptedException ex){}
        messageBus.unregister(micro);
    }

    @Test
    void awaitMessage() {
        messageBus.register(micro);
        messageBus.subscribeEvent(event.getClass(), micro);
        messageBus.sendEvent(event);
        try {
            Message m = messageBus.awaitMessage(micro);
            //assertEquals(event, messageBus.awaitMessage(micro));
        } catch (InterruptedException ex) {}
        messageBus.unregister(micro);

    }
}




















