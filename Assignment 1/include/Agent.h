#ifndef AGENT_H_
#define AGENT_H_

#include "Session.h"

class Agent{
public:
    Agent();//CTR
    virtual ~Agent()=default;

   //virtuals
    virtual void act(Session& session)=0;
    virtual Agent* clone()const=0;
};

class ContactTracer: public Agent{
public:
    ContactTracer();//CTR

    //virtuals
    virtual void act(Session& session);
    virtual ContactTracer* clone()const;
};


class Virus: public Agent{
public:
    Virus(int nodeInd);//CTR

    //virtuals
    virtual void act(Session& session);
    virtual Virus* clone()const;
private:
    const int nodeInd;
};

#endif