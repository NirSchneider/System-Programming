package bgu.spl.mics.application.passiveObjects;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class Ewoks {
    private static class EwoksSingletonHolder{
        private static Ewoks instance = new Ewoks();
    }

    //Fields
    private ConcurrentHashMap<Integer,Ewok> ewoksStash;// ewoks map by serial number
    private List<Integer> ewoks;

    //CTR
    public Ewoks()
    {
        ewoksStash = new ConcurrentHashMap<>();
        ewoks = new LinkedList<>();
    }

    public static Ewoks getInstance() {
        return EwoksSingletonHolder.instance;
    }

    public List<Integer> makeResource(int numOfResources)//create ewoks from input
    {
            for (int i=1; i <= numOfResources; i++)
            {
                Ewok e= ewoksStash.get(i);
                if(e==null)
                {
                    e = new Ewok(i);
                    this.ewoksStash.put(i,e);
                    this.ewoks.add(i);
                }
            }
            return ewoks;
    }

    public void releaseResources(List<Integer> ewoks)
    {
            for(int i: ewoks)
            {
                this.ewoksStash.get(i).release();
            }
    }

    public void acquire(List<Integer> ewoksPerAttack)
    {
        for (int i=0; i<ewoksPerAttack.size();i++)
        {
            this.ewoksStash.get(ewoksPerAttack.get(i)).acquire();
        }
    }
}









