//
// Created by spl211 on 10/11/2020.
//

#include "../include/Agent.h"

//constructor
Agent::Agent() =default;

//cunstructors
ContactTracer::ContactTracer()=default;//CT
Virus::Virus(int nodeInd) :nodeInd(nodeInd){}//Virus

//virtuals:
//CT act
void ContactTracer::act(Session(& session)) {
    if(!session.isInfectedEmpty())
    {
        int node = session.dequeueInfected();
        Tree* t = session.getGraph().createBFS(session,node);
        int toIsolate = t->traceTree();
        session.deleteNeighbors(toIsolate);//isolating the relevent node
        delete t;
    }
}

//Virus act
void Virus::act(Session(& session)) {
    if(session.getGraph().getNodeStatus(nodeInd)==1)//if the node carry the virus
    {
        session.changeStatusInGraphToInfected(nodeInd);
        session.enqueueInfected(nodeInd);
    }
    for (int i = 0; i < session.getGraph().getSize(); i++)
    {
        if (session.getGraph().isEdgeExist(nodeInd,i) && session.getGraph().getNodeStatus(i)==0) {//if is neighbors and healthy
            session.changeStatusInGraphToCarry(i);
            Virus toAdd(i);//make node i a virus carry
            session.addAgent(toAdd);
            break;
        }
    }
}

//clones
ContactTracer * ContactTracer::clone() const {return new ContactTracer(*this);}
Virus * Virus::clone() const {return new Virus(*this);}



