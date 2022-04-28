//
// Created by spl211 on 10/11/2020.
//

#ifndef ASSIGNMENT1_GRAPH_H
#define ASSIGNMENT1_GRAPH_H
#include <vector>
#include <queue>
#include "Tree.h"

class Graph{
public:
    Graph(std::vector<std::vector<int>> matrix);
    Graph();

    //getters
    int getSize();
    std::vector<std::vector<int>> getEdges()const;
    std:: vector<int> getInfectedStatus()const;
    int getNodeStatus (int x);

    //setters
    void setNodeStatusToCarry (int node);
    void setNodeStatusToInfected (int node);

    //sub functions
    bool isEdgeExist (int x, int y);
    void deleteEdge(int x,int y);

    Tree* createBFS(Session& session,int node);
private:
    std::vector<std::vector<int>> edges;
    std:: vector<int> nodeStatus; //0-healthy , 1-carry, 2-infected
    std::vector<bool> _visited; // visited for BFS function
};


#endif //ASSIGNMENT1_GRAPH_H
