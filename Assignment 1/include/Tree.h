//
// Created by spl211 on 10/11/2020.
//

#ifndef ASSIGNMENT1_TREE_H
#define ASSIGNMENT1_TREE_H
class Session;

class Tree{
public:
    //rule of 5
    Tree(int rootLabel); //CTR
    Tree(const Tree& other); //COPY CTR
    const Tree& operator=(const Tree &other); //COPY ASSIGNMENT OPERATOR
    virtual ~Tree(); //DESTRUCTOR
    Tree (Tree && other);//MOVE CTR
    const Tree& operator= (Tree &&other);//MOVE ASSIGNMENT OPERATOR

    void clean();

    //getters
    int getroot() const;
    std::vector<Tree*> getChildren() const;

    //setter
    void setroot(const int root);

    //sub function
    void addChild(const Tree& child);

    //virtuals
    virtual Tree* clone() const =0;
    virtual int traceTree()=0;

    static Tree* createTree(const Session& session, int rootLabel);

private:
    int node;
    std::vector<Tree*> children;
};

class CycleTree: public Tree{
public:
    CycleTree(int rootLabel, int currCycle);

    virtual CycleTree* clone() const;

    virtual int traceTree();
private:
    int currCycle;
};

class MaxRankTree: public Tree{
public:
    MaxRankTree(int rootLabel);
    virtual MaxRankTree* clone() const;

    virtual int traceTree();
};

class RootTree: public Tree{
public:
    RootTree(int rootLabel);
    virtual Tree* clone() const;

    virtual int traceTree();
};

#endif //ASSIGNMENT1_TREE_H
