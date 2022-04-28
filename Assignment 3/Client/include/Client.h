//
// Created by spl211 on 09/01/2021.
//

#ifndef BOOST_ECHO_CLIENT_BGRSCLIENT_H
#define BOOST_ECHO_CLIENT_BGRSCLIENT_H

#include <connectionHandler.h>
#include <mutex>
#include <condition_variable>
#include <thread>

class Client{
private:
    bool terminate;
    ConnectionHandler& handler;
    std::mutex &mutex;
    std::condition_variable cv;

public:
    Client(ConnectionHandler & handler, std::mutex &_mutex);

    void keyboard();//thread
    void socket();//thread
    short findOpcode(string);
    void shortToBytes(short,char*);
    void shortToBytes2(short,vector<char>*);

    void append(string,vector<char>*);
    short bytesToShort(char* bytesArr);

};





#endif //UNTITLED_CLIENT_H
