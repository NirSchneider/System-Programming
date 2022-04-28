//
// Created by spl211 on 09/01/2021.
//
#include "Client.h"
#include <iostream>
#include <boost/algorithm/string/split.hpp>
#include <boost/algorithm/string/classification.hpp>
#include <boost/lexical_cast.hpp>


int main(int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }

    std::string host = argv[1];
    short port = atoi(argv[2]);

    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }
    std::mutex m;
    Client client(connectionHandler, m);

        std::thread keyboardThread(&Client::keyboard, &client);
        std::thread socketThread(&Client::socket, &client);
        keyboardThread.detach();
        socketThread.join();

    return 0;
}

Client::Client(ConnectionHandler &_handler, std::mutex &_mutex):terminate(false), handler(_handler), mutex(_mutex), cv() {}

void Client::keyboard() {
    while(!terminate)
    {
        string input;
        getline(cin,input);
        vector<string> splitWords;
        boost::split(splitWords ,input, boost::is_any_of(" "));
        string command = splitWords[0];
        string username;
        string password;
        int courseNum;
        short opcode = findOpcode(command);

        char opcodeBytes[2];
        shortToBytes(opcode,opcodeBytes);

        vector<char> frame;
        shortToBytes2(opcode,&frame);

        if(command == "LOGIN" || command == "ADMINREG" || command == "STUDENTREG")
        {
            string frameUsername = splitWords[1].c_str();
            append(frameUsername,&frame);

            string framePassword = splitWords[2].c_str();
            append(framePassword,&frame);

            handler.sendBytes(&frame[0],frame.size());
        }
        else if(command == "LOGOUT")
        {
            std::unique_lock<std::mutex> lock(mutex);
            handler.sendBytes(opcodeBytes,2);
            cv.wait(lock);
        }
        else if(command == "MYCOURSES")
        {
            handler.sendBytes(opcodeBytes,2);
        }
        else if(command == "COURSEREG" || command == "KDAMCHECK" || command == "COURSESTAT" || command == "ISREGISTERED" || command == "UNREGISTER")
        {
            courseNum = (short)(std::stoi(splitWords[1]));
            shortToBytes2(courseNum,&frame);
            handler.sendBytes(&frame[0],frame.size());
        }
        else//STUDENTSTAT
        {
            string frameUsername = splitWords[1].c_str();
            append(frameUsername,&frame);

            handler.sendBytes(&frame[0],frame.size());
        }
    }
}

void Client::socket() {
    while(!terminate)
    {
        char* typeBytes = new char[2];
        handler.getBytes(typeBytes,2);

        char* opcodeMsgBytes = new char[2];
        handler.getBytes(opcodeMsgBytes,2);

        short typeCommand = bytesToShort(typeBytes);
        short opcodeMsg = bytesToShort(opcodeMsgBytes);

        string additionalData;
        if (typeCommand == 12)//Ack command
        {
            if (!handler.getFrameAscii(additionalData,'\0'))
            {
                std::cout << "Disconnected. Exiting..." << std::endl;
                terminate=true;
            }
            else
            {
                std::cout << "ACK " << opcodeMsg << std::endl;
                if(!additionalData.empty())
                    std::cout << additionalData << std::endl;
            }
            if(opcodeMsg == 4)
            {
                cv.notify_one();
                terminate=true;
                handler.close();
            }
        }
        else//Error command
        {
            std::cout << "ERROR " << opcodeMsg << std::endl;
            if(opcodeMsg == 4)
            {
                terminate = false;
                cv.notify_one();
            }
        }

        delete [] typeBytes;
        delete [] opcodeMsgBytes;
    }
}

short Client::bytesToShort(char *bytesArr) {
    short result = (short)((bytesArr[0] & 0xff) << 8);
    result += (short)(bytesArr[1] & 0xff);
    return result;
}

short Client::findOpcode(string command) {
    if(command == "ADMINREG")
        return 1;
    else if(command == "STUDENTREG")
        return 2;
    else if(command == "LOGIN")
        return 3;
    else if(command == "LOGOUT")
        return 4;
    else if(command == "COURSEREG")
        return 5;
    else if(command == "KDAMCHECK")
        return 6;
    else if(command == "COURSESTAT")
        return 7;
    else if(command == "STUDENTSTAT")
        return 8;
    else if(command == "ISREGISTERED")
        return 9;
    else if(command == "UNREGISTER")
        return 10;
    else if(command == "MYCOURSES")
        return 11;
    else
        return -1;
}

void Client::shortToBytes(short opcode, char* arr) {
    arr[0] = ((opcode >> 8) & 0xFF);
    arr[1] = (opcode & 0xFF);
}

void Client::shortToBytes2(short opcode,vector<char>* c) {
    c->push_back(((opcode >> 8) & 0xFF));
    c->push_back((opcode & 0xFF));
}

void Client::append(string toAdd, vector<char> * frame) {
    for(int i=0;toAdd[i] != '\0';i++)
        frame->push_back(toAdd[i]);
    frame->push_back('\0');
}




