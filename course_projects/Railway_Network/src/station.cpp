#include "station.h"
#include <string>

Station::Station(string n, string d, string m, string t, string l) {
    name = n;
    district = d;
    municipality = m;
    township = t;
    line = l;

}
/**
 * @brief Checks if two stations have the same name.
 *
 * @param other The other station to compare to.
 * @return True if the stations have the same name, false otherwise.
 */
bool Station::operator==(const Station& other) const {
    return this->name == other.name;
}
