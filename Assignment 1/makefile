# All Targets
all: bin/cTrace

# Tool invocations
# Executable "cTrace" depends on the files main.o Agent.o Graph.o Tree.o Session.o
bin/cTrace: bin/main.o bin/Agent.o bin/Graph.o bin/Tree.o bin/Session.o
	@echo 'Building target: cTrace'
	@echo 'Invoking: C++ Linker'
	g++ -o bin/cTrace bin/main.o bin/Agent.o bin/Graph.o bin/Tree.o bin/Session.o
	@echo 'Finished building target: cTrace'
	@echo ' '

bin/main.o: src/main.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/main.o src/main.cpp 

bin/Session.o: src/Session.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/Session.o src/Session.cpp

bin/Agent.o: src/Agent.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/Agent.o src/Agent.cpp

bin/Graph.o: src/Graph.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/Graph.o src/Graph.cpp
	
bin/Tree.o: src/Tree.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/Tree.o src/Tree.cpp
	

#Clean the build directory
clean: 
	rm -f bin/*
