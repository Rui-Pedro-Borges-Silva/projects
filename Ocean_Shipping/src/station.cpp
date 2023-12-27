#include "../include/station.h"

Station::Station(int id_, double la, double lo) {
    id = id_;
    latitude = la;
    longitude = lo;
}

Station::Station(int id_) {
    id = id_;
}

