package bgu.spl.mics;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private static class MessageBusSingletonHolder{
		private static MessageBusImpl instance = new MessageBusImpl();
	}

	//Fields
	private ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>> registers;
	private ConcurrentHashMap<Class<? extends bgu.spl.mics.Message>,LinkedBlockingQueue <MicroService>> RoundRobin;
	private ConcurrentHashMap<Event,Future> Events;


	//CTR
	private MessageBusImpl(){
		registers = new ConcurrentHashMap<>();
		RoundRobin = new ConcurrentHashMap<>();
		Events = new ConcurrentHashMap<>();
	}



	public static MessageBusImpl getInstance() {
		return MessageBusSingletonHolder.instance;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		synchronized (type) {
			if (RoundRobin.get(type) != null) {
				RoundRobin.get(type).add(m);
			} else {
				RoundRobin.putIfAbsent(type, new LinkedBlockingQueue());
				RoundRobin.get(type).add(m);
			}
		}
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		synchronized (type) {
			if (RoundRobin.get(type) != null) {
				RoundRobin.get(type).add(m);
			} else {
				RoundRobin.putIfAbsent(type, new LinkedBlockingQueue<>());
				RoundRobin.get(type).add(m);
			}
		}

	}

	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		if(Events.get(e)!=null)
			Events.get(e).resolve(result);
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		if (RoundRobin.get(b.getClass()) != null && !RoundRobin.get(b.getClass()).isEmpty())
		{
			for (MicroService m : RoundRobin.get(b.getClass()))
			{
				Queue<Message> microserviceQueue = registers.get(m);
				synchronized (microserviceQueue) {
					microserviceQueue.add(b);
					microserviceQueue.notifyAll();
				}
			}
		}
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		MicroService chosen;
		synchronized (e.getClass())
		{
			if (RoundRobin.get(e.getClass()) == null || RoundRobin.get(e.getClass()).isEmpty()){
				try {
					e.getClass().wait(30);
				}catch (InterruptedException ex){}}
			if (RoundRobin.get(e.getClass()) == null || RoundRobin.get(e.getClass()).isEmpty())
				return null;
			chosen = RoundRobin.get(e.getClass()).poll();
			RoundRobin.get(e.getClass()).add(chosen); //pushed to the end of the queue
		}

		Future<T> output = new Future<>();
		Events.put(e, output);

		LinkedBlockingQueue queueOfChosen = registers.get(chosen);
		synchronized (queueOfChosen)//synchronized to notify the await message wait function
		{
			queueOfChosen.add(e);
			queueOfChosen.notifyAll();
			return output;
		}
	}

	@Override
	public void register(MicroService m){registers.putIfAbsent(m,new LinkedBlockingQueue<>());}

	@Override
	public void unregister(MicroService m){
		if (registers.get(m)!=null){
			Set<Class<? extends Message>> message = RoundRobin.keySet();
			for (Class<? extends bgu.spl.mics.Message> it: message){
				if (RoundRobin.get(it).contains(m)){
					RoundRobin.get(it).remove(m);
				}
			}
			for(int i=0; i<registers.get(m).size();i++)
			{
				registers.get(m).remove();
			}
		}
		registers.remove(m);
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {

		Queue<Message> queueOfCurrMicro = registers.get(m);
		if (queueOfCurrMicro == null) //m did no make registration
			throw new IllegalStateException();
		synchronized (queueOfCurrMicro)
		{
			while (queueOfCurrMicro.isEmpty())
			{
				try{
					queueOfCurrMicro.wait();
				} catch (InterruptedException ex){ex.printStackTrace();}
			}
			return queueOfCurrMicro.remove();//take the top message
		}
	}


}
