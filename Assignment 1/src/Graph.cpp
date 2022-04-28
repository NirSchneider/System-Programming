//
// Created by spl211 on 10/11/2020.
//

#include "../include/Graph.h"
using namespace std;

//constructor
Graph::Graph(std::vector <std::vector<int>> matrix):edges(matrix),nodeStatus(),_visited(){
    for(unsigned int i=0; i<this->edges.size();i++)
    {
        this->nodeStatus.push_back(0);
        this->_visited.push_back(false);
    }
}

//defult connstructor
Graph::Graph():edges(),nodeStatus(),_visited() {};


//getters
int Graph::getSize() {return this->edges.size();}

std::vector<std::vector<int>> Graph::getEdges() const {return this->edges;}

std::vector<int> Graph::getInfectedStatus() const {
    std::vector<int> output;
    for(unsigned int i=0;i<this->nodeStatus.size();i++)
    {
        if(this->nodeStatus[i]==2)
            output.push_back(i);
    }
        return output;
}

int Graph::getNodeStatus(int x) {return this->nodeStatus[x];}

//setters
void Graph::setNodeStatusToCarry(int node) {this->nodeStatus[node]=1;}
void Graph::setNodeStatusToInfected(int node) {this->nodeStatus[node]=2;}

//sub functions
bool Graph::isEdgeExist(int x, int y) {return this->edges[x][y] == 1;}

void Graph::deleteEdge(int x, int y) {
    this->edges[x][y]=0;
    this->edges[y][x]=0;
}


Tree * Graph::createBFS(Session &session,int node) {
    queue<Tree*> q;
    Tree* output = Tree::createTree(session,node);
    q.push(output);
    this->_visited[node] = true;
    while (!q.empty())//as long as there are nodes to add to the tree
    {
        Tree* currTree =q.front();
        q.pop();
        int currNode = currTree->getroot();
        for(unsigned int i=0;i<this->edges.size();i++)//go over currnode neighbors
        {
            if(this->edges[currNode][i] == 1 && !this->_visited[i])//check if i is a neighbor and if hasnt been visited
            {
                this->_visited[i] = true;
                Tree* toAdd =Tree::createTree(session,i);
                currTree->addChild(*toAdd);
                q.push(currTree->getChildren()[currTree->getChildren().size()-1]);
                delete toAdd;
            }
        }
    }
    return output;
}