//
// Created by spl211 on 10/11/2020.
//

#ifndef ASSIGNMENT1_SESSION_H
#define ASSIGNMENT1_SESSION_H
#define JSON_PATH "output.json"
#include "Graph.h"
#include "json.hpp"
#include "Agent.h"
#include <fstream>
#include <iostream>
#include "ostream"
using  namespace std;
using json=nlohmann::json ;

class Agent;

enum TreeType{
    Cycle,
    MaxRank,
    Root
};

class Session{
public:
    //rule of 5
    Session(const std::string& path);//CTR
    Session(const Session& other);//COPY CTR
    const Session& operator=(const Session& other);//MOVE ASSIGNMENT OPERATOR
    virtual ~Session();//DESTRUCTOR
    Session(Session&& other);//MOVE CONSTRUCTOR
    const Session& operator=(Session&& other);//MOVE ASSIGNMENT OPERATOR

    void clean();

    //getters
    Graph getGraph();
    TreeType getTreeType() const;
    std::queue<int> getInfected()const;
    int getCurrCycle() const;

    //setters
    void changeStatusInGraphToCarry(int node);
    void changeStatusInGraphToInfected(int node);
    void deleteNeighbors(int node);

    void simulate();
    void addAgent(const Agent& agent);
    void enqueueInfected(int);
    int dequeueInfected();

    //sub functions
    bool shouldCycle(unsigned int startSize);
    bool isInfectedEmpty();

private:
    Graph g;
    TreeType treeType;
    std::vector<Agent*> agents;
    std::queue<int> infected;
    int numberOfCycles;
};

#endif //ASSIGNMENT1_SESSION_H
