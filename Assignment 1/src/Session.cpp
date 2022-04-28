//
// Created by spl211 on 10/11/2020.
//
#include "../include/Session.h"

//CTR
Session::Session(const std::string & path): g(Graph()),treeType(),agents(),infected(),numberOfCycles(0) {
    //call from JSON
    ifstream a(path);
    json j;
    a>>j;

    this->g =Graph(j["graph"]);
    for(auto& tmp: j["agents"])
    {
        string agentType = tmp[0];
        if(agentType == "V")
        {
            Virus* virus = new Virus(tmp[1]);//intialize as virus carry
            this->agents.push_back(virus);
            this->g.setNodeStatusToCarry(tmp[1]);

        }
        else
        {
            ContactTracer* ct = new ContactTracer();
            this->agents.push_back(ct);
        }
    }
    string jsonTreeType = j["tree"];
    if(jsonTreeType == "M")
        this->treeType = MaxRank;
    else if (jsonTreeType == "C")
        this->treeType = Cycle;
    else
        this->treeType = Root;
  }

//copy constructor
Session::Session(const Session &other): g(other.g),treeType(other.treeType),agents(),infected(),numberOfCycles(other.numberOfCycles){
    for(unsigned int i=0 ; i<other.agents.size();i++)
    {
        Agent* toAdd = other.agents[i]->clone();
        this->agents.push_back(toAdd);
    }
}

//copy assignment operator
const Session & Session::operator=(const Session &other) {
    if(this != &other)
    {
        clean();
        this->g = other.g;
        this->treeType = other.treeType;
        for(int i=0;this->agents.size();i++)
            this->agents.push_back(other.agents[i]->clone());
        this->infected=other.infected;
        this->numberOfCycles=other.numberOfCycles;

    }
    return *this;
}

//destructor
Session::~Session() {clean();}

//move constructor
Session::Session(Session &&other):g(other.g),treeType(other.treeType),agents(move(other.agents)),infected(other.infected),numberOfCycles(other.numberOfCycles){}

//move assignment operator
const Session & Session::operator=(Session &&other) {
    if(this!=&other)
    {
        clean();
        this->g=other.g;
        this->infected=move(other.infected);
        this->agents=move(other.agents);
        this->numberOfCycles=other.numberOfCycles;
    }
    return *this;
}

void Session::clean() {
        for(unsigned int i=0; i<this->agents.size();i++)
    {
        if(this->agents[i])
            delete this->agents[i];
    }
    this->agents.clear();
}

//getters
Graph Session::getGraph(){return this->g;}
TreeType Session::getTreeType() const {return treeType;}
std::queue<int> Session::getInfected() const {return this->infected;}
int Session::getCurrCycle() const{return this->numberOfCycles;}

//setters
void Session::changeStatusInGraphToCarry(int node) {this->g.setNodeStatusToCarry(node);}
void Session::changeStatusInGraphToInfected(int node) {this->g.setNodeStatusToInfected(node);}
void Session::deleteNeighbors(int node) {
    for(int i=0; i < this->g.getSize();i++)
    {
        if(this->g.isEdgeExist(node,i))
            this->g.deleteEdge(node,i);
    }
}


void Session::simulate() {
    int startSize = 0;
    int agentsSize = this->agents.size();
    while(shouldCycle(startSize))
    {
        startSize = agentsSize;
        for(int i=0; i < agentsSize;i++)
            this->agents[i]->act(*this);
        agentsSize=this->agents.size();
    }

    //write to output file
    nlohmann::json output;
    output["graph"] = g.getEdges();
    output["infected"] = this->g.getInfectedStatus();
    std::ofstream o(JSON_PATH);
    o << output << endl;

}

void Session::addAgent(const Agent& agent) { //clone
    agents.push_back(agent.clone());
}

void Session::enqueueInfected(int x){this->infected.push(x);}

int Session::dequeueInfected() {
    int x = infected.front();
    infected.pop();
    return x;
}

//sub functions
bool Session::shouldCycle(unsigned int startSize) {
    if(startSize!=this->agents.size())//if other agents were added then the size at the start of the iteration will be diffrent from the size at the end of the iteration
    {
        this->numberOfCycles++;
        return true;
    }
    return false;
}

bool Session::isInfectedEmpty() {return this->infected.empty();}