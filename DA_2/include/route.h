#ifndef DA_NETWORKLINE_H
#define DA_NETWORKLINE_H

#include <string>

using namespace std;

class Route{
    private:
        int origin;
        int destination;
        double distance;
    public:
        Route(int s1, int s2, double d);
        int getOrigin() const {return origin;};
        int getDestination() const {return destination;};
        double getDistance() const {return distance;};
};

#endif //DA_NETWORKLINE_H
