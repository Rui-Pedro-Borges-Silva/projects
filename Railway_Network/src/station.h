#ifndef DA_STATION_H
#define DA_STATION_H

#include <string>

using namespace std;

class Station{
    private:
        string name;
        string district;
        string municipality;
        string township;
        string line;
    public:
        Station(string n, string d, string m, string t, string l);
        string getName() {return name;};
        string getMunicipality() {return municipality;};
        string getDistrict() {return district;};
        bool operator==(const Station& other) const;
        string get_municipality(){return municipality;}
        string get_district(){return district;}
};


#endif //DA_STATION_H
