#ifndef DA_NETWORKLINE_H
#define DA_NETWORKLINE_H

#include <string>

using namespace std;

class NetworkLine{
    private:
        string station1;
        string station2;
            int capacity;
        string service;
    public:
        NetworkLine(string s1, string s2, int c, string s);
        string getStation1() {return station1;};
        string getStation2() {return station2;};
        int getCapacity() const {
            return this->capacity;
        }
};

#endif //DA_NETWORKLINE_H
