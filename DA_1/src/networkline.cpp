#include "networkline.h"
#include <string>

NetworkLine::NetworkLine(string s1, string s2, int c, string s) {
    station1 = s1;
    station2 = s2;
    capacity = c;
    service = s;
}
