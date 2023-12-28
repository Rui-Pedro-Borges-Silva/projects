#ifndef DA_STATION_H
#define DA_STATION_H

#include <string>

using namespace std;

class Station{
    private:
        int id;
        double latitude = 0;
        double longitude = 0;
    public:
        Station(int id_, double la, double lo);
        explicit Station(int id_);
        double getLatitude() const {return latitude;};
        double getLongitude() const {return longitude;};
        int getId() const {return id;};
};


#endif //DA_STATION_H
