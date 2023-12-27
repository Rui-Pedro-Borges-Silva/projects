#ifndef DA_GRAPH_H
#define DA_GRAPH_H

#include <list>
#include <vector>
#include <stack>
#include <string>
#include "station.h"
#include "networkline.h"

using namespace std;

class Graph{
    private:
        struct OutLine{
            NetworkLine* line;
            string source; // source
            string dest; //destination
            int flow;
            int capacity;
        };
        struct StationNode{
            Station* station;
            string color; //white -> not visited, gray -> being visited, black -> visited
            vector<OutLine*> outLines; //lines that go out of station
            int dist;
            int visit;
            int pred; //predecessor

        };
        int n; //number of nodes
        vector<StationNode> stations;
        vector<NetworkLine*> lines; //all lines (use when you need to sort all nodes for algorithm)
        NetworkLine* nt;
        Station* super;
    public:
        Graph();
        void clear();

        void addNode(Station* s);
        void addLine(NetworkLine* l);
        void bfs(int nod) ;
        void dfs();
        int dfs_visit(int nod, int time);
        vector<NetworkLine*> kruskal_mst();
        int findIndexCluster(string station, vector<vector<StationNode>> toSearch);
        Station* findStation(string name);
        vector<string> getDistrict();


    void print();

    int findNode(const string &stationName) const;

    int edmondsKarp( const string &source,  const string &destiny) ;

    bool findAugmentingPath(int source, int destiny);

    vector<pair<std::string, std::string>> maxFlow();

    vector<string> getMunicipality();

    void topKMunDistr(unsigned k);

    void deleteNode(int id);

    int maxTrainArrivingStation(string statio_name);

    void deleteEdge(int id1, int id2);

    unsigned int transportationNeed(const string &district);
};



#endif //DA_GRAPH_H
