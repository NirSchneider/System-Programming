//
// Created by spl211 on 10/11/2020.
//
#include "../include/Session.h"

//constructor
Tree::Tree(int rootLabel) :node(rootLabel),children(){}

//copy constructor
Tree::Tree(const Tree &other):node(other.node),children() {
    for(unsigned int i=0; i<other.children.size();i++)
    {
        Tree* toAdd = other.children[i]->clone();
        children.push_back(toAdd);
    }
}

// copy assignment operator
const Tree & Tree::operator=(const Tree &other) {
    if(this!= & other)
    {
        clean();
        this->node = other.node;
        for(unsigned int i=0; i<other.children.size();i++)
        {
            this->children.push_back(other.children[i]->clone());
        }

    }
    return *this;
}

//destructor
Tree::~Tree()  {
    clean();
}

//move constructor
Tree::Tree(Tree &&other) : node(other.node),children(move(other.children)){}

//move assignment operator
const Tree & Tree::operator=(Tree &&other) {
    if(this!=&other)
    {
        clean();
        this->node = other.node;
        for(unsigned int i=0;i<other.children.size();i++)
        {
            this->children.push_back(move(other.children[i]));
            other.children[i] = nullptr;
        }
        other.children.clear();
    }
    return *this;
}

void Tree::clean() {
    for (Tree * toDelete : this->children) {
        if (toDelete)
            delete toDelete;
    }
    this->children.clear();
}

//getters
int Tree::getroot() const {return this->node;}

std::vector<Tree *> Tree::getChildren() const {return this->children;}

//setter
void Tree::setroot(const int root) {this->node = root;}

//sub function
void Tree::addChild(const Tree &child) {this->children.push_back(child.clone());}

// constructors
CycleTree::CycleTree(int rootLabel, int currCycle) :Tree(rootLabel),currCycle(currCycle) {}
MaxRankTree::MaxRankTree(int rootLabel) : Tree(rootLabel){}
RootTree::RootTree(int rootLabel) :Tree(rootLabel){}

//virtuals:
//clones
CycleTree * CycleTree::clone() const {return new CycleTree(*this);}
MaxRankTree * MaxRankTree::clone() const {return new MaxRankTree(*this);}
Tree * RootTree::clone() const {return new RootTree(*this);}

//Tracers
int CycleTree::traceTree() {
    vector<Tree*>children = this->getChildren();
    Tree * output = this;

    for(int i=1; !output->getChildren().empty() && i<currCycle;i++) //go to the left most child "currcycle" times if possible
    {
        output = children[0];
        children = output->getChildren();
    }
    return output->getroot();
}

//goings over the nodes by level from left to right (like BFS) so in case of a tie the current node will be the relevent one
int MaxRankTree::traceTree() {
    int max = this->getroot();
    unsigned int maxChildren = this->getChildren().size();
    queue<Tree*> bfs;
    bfs.push(this);
    Tree* curr;
    while(!bfs.empty())
    {
        curr=bfs.front();
        bfs.pop();
        if(maxChildren < curr->getChildren().size()) {//compare number of children (children=degree)
            max = curr->getroot();
            maxChildren = curr->getChildren().size();
        }
        for(unsigned int i =0 ; i<curr->getChildren().size();i++)//insert all children to the queue
        {
            bfs.push(curr->getChildren()[i]);
        }
    }
    return max;
}
int RootTree::traceTree() {return this->getroot();}

Tree * Tree::createTree(const Session &session, int rootLabel) {
    TreeType type = session.getTreeType();
    if(type == Root)
        return new RootTree(rootLabel);
    else if (type == MaxRank)
        return new MaxRankTree(rootLabel);
    else
        return new CycleTree(rootLabel,session.getCurrCycle());
}