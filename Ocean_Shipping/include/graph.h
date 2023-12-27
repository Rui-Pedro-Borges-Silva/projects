#ifndef DA_GRAPH_H
#define DA_GRAPH_H

#include <list>
#include <vector>
#include <stack>
#include <string>
#include <limits>
#include <map>
#include "station.h"
#include "route.h"

using namespace std;

const double EARTH_RADIUS_KM = 6371.01;
const double PI = 3.14159265358979323846;

class Graph{
private:
        struct StationNode{
            Station* station;
            string color; //white -> not visited, gray -> being visited, black -> visited
            vector<Route*> outLines; //routes that go out of station
            vector<int> children;
            double key;
            bool in_MST = false;
            bool in_pQ = false;
            int pred; //predecessor
        };
        vector<Route*> mst;
        int n; //number of nodes
        vector<StationNode> stations;
        //vector<Route*> routes;
        std::map<std::pair<int, int>, double> dist;
    public:
        Graph();
        void clear();
        bool isStationInGraph(int id);
        void addNode(Station* s);
        void addRouteToy(Route* r);
        void addRouteReal(Route* r);
        void print(); //used for debugging
        void sort_stations();

        void solveTSP_Backtracking();
        double calculateDistance(vector<int>& tour);
        void backtrackTSP(int currentNode, vector<int>& currentTour, vector<bool>& visited, double& minDistance, vector<int>& bestTour);

        double real_distance(int x, int y);
        double triangularApproximation();
        int triangularApproximation_R(double* d, int index, vector<int>* path);
        void prim_mst();

        double getDistance(int id1, int id2);
};

#endif //DA_GRAPH_H
