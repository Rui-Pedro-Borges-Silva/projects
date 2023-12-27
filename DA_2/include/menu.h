#include "graph.h"
#include <string>

#ifndef PROJETO_DA_MENU
#define PROJETO_DA_MENU

using namespace std;

class Menu{
private:
    Graph graph;
public :
    void main_menu();
    static int check_input_menu(const string& input, int max);
    void parse_file_menu();
    void parse_ToyGraphs(string fileS, bool check);
    void parse_RealWorldGraphs(string file);
    void clear_graph();

    void backtracktsp();
};

#endif