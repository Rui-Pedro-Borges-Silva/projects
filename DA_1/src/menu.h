#include "graph.h"
#include <string>

#ifndef PROJETO_DA_MENU
#define PROJETO_DA_MENU

using namespace std;

class Menu{
private:
    Graph graph;
    Graph mst_graph;
public :
    void main_menu();
    int check_input_menu(string input, int max);
    void clear_graph();
    void build_subgraph(vector<NetworkLine*> lines);

    void maxTrainBetweenStations(bool mode);

    void parse_file(string fileS, string fileN);

    void PairOfStationsWithMostTrains();

    void maxTrainsAtStation();

    void topKMunDistr(unsigned k);

    void Municipalidades();
};

#endif