
//Created by spl211 on 11/11/2020.


#include <iostream>
#include "../include/Session.h"
#include "ostream"
#define JSON_PATH "output.json"
using namespace std;

int main(int argc, char** argv){
    if(argc != 2){
        cout << "usage cTrace <config_path>" << endl;
        return 0;
    }
    Session sess(argv[1]);
    sess.simulate();
    return 0;
}




