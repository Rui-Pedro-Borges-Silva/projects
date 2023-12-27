#include "../include/menu.h"
#include <iostream>
#include <fstream>
#include <string>
#include <sstream>
#include <algorithm>

/**
 * @brief This function displays the main menu, takes in user input, and performs the desired action.
 */
void Menu::main_menu() {
    string file1;
    string file2;
    while (true) {
        cout << "======================================================" << endl;
        cout << "                         Menu                         " << endl;
        cout << "======================================================" << endl;
        cout << "  0-Close                                             " << endl;
        cout << "  1-Parse data set file                               " << endl;
        cout << "  2-Backtracking Algorithm                          " << endl; //4.1
        cout << "  3-Triangular Approximation Heuristic            " << endl;   //4.2
        cout << "  4-Other Heuristics   " << endl;                              //4.3
        cout << "======================================================" << endl;
        cout << ">?";

        string input;
        cin >> input;
        int option = check_input_menu(input, 4);
        switch (option) {
            case 1:
                parse_file_menu();
                break;
            case 2:
                backtracktsp();
                break;
            case 3:
                graph.triangularApproximation();
                break;
            case 4:
                break;
            case 0:
                clear_graph();
                return;
        }
    }
}

/**
 * @brief This function displays the parse file menu, takes in user input, and performs the desired action.
 */
void Menu::parse_file_menu(){
    string input;
    cout << "======================================================" << endl;
    cout << "                     Parse data set file               " << endl;
    cout << "======================================================" << endl;
    cout << "  0-Back                                              " << endl;
    cout << "  1-Toy Graphs                                        " << endl;
    cout << "  2-Real World Graphs                                 " << endl;
    cout << "  3-Extra Fully Connected Graphs                       " << endl;
    cout << "======================================================" << endl;
    cout << ">?";
    cin >> input;
    int option2 = check_input_menu(input, 3);
    switch(option2) {
        case 1:
            cout << "======================================================" << endl;
            cout << "                     Toy Graphs                       " << endl;
            cout << "======================================================" << endl;
            cout << "What is the name of the file you want to parse? ";
            cin >> input;
            parse_ToyGraphs(input, true);
            //graph.print();
            break;
        case 2:
            cout << "======================================================" << endl;
            cout << "                   Real World Graphs                  " << endl;
            cout << "======================================================" << endl;
            cout << "What is the name of the graph you want to parse? ";
            cin >> input;
            parse_RealWorldGraphs(input);

            break;
        case 3:
            cout << "======================================================" << endl;
            cout << "               Extra Fully Connected Graphs           " << endl;
            cout << "======================================================" << endl;
            cout << "What is the name of the file you want to parse? ";
            cin >> input;
            parse_ToyGraphs(input, false);
            //graph.print();
            break;
    }
}

/**
 * @brief This function parses the Real World Graphs from the provided file.
 *
 * @param file The file name from which the graph is to be parsed.
 */
void Menu::parse_RealWorldGraphs(string file){
    // Construct the file paths
    string common_path = "../data/Real-world Graphs/";
    file = common_path + file;
    string path_nodes = file + "/nodes.csv";
    string path_edges = file + "/edges.csv";

    ifstream node_stream(path_nodes);
    ifstream edge_stream(path_edges);

    // Check if the files could be opened
    if(!node_stream.good() || !edge_stream.good()){
        cout << "error: file(s) does/do not exist" << endl;
        return;
    }

    // Check if the files are valid
    if(common_path == file){
        cout << "error: no file chosen" << endl;
        return;
    }

    // Parse the station data file
    string nodeToRead;
    getline(node_stream, nodeToRead); // discard the first line (can be used to validate input)

    while (getline(node_stream, nodeToRead)) { //read nodes
        stringstream iss(nodeToRead);
        string id, longitude, latitude;

        getline(iss, id, ',');
        getline(iss, longitude, ',');
        getline(iss, latitude, ',');

        auto* station1 = new Station(stoi(id), stod(longitude), stod(latitude));

        graph.addNode(station1);
    }

    graph.sort_stations();

    string edgeToRead;
    getline(edge_stream, nodeToRead);
    while (getline(edge_stream, nodeToRead)){
        stringstream iss(nodeToRead);
        string origin, destination, distance;

        getline(iss, origin, ',');
        getline(iss, destination, ',');
        getline(iss, distance, ',');

        auto* route1_2 = new Route(stoi(origin), stoi(destination), stof(distance));
        auto* route2_1 = new Route(stoi(destination), stoi(origin), stof(distance));

        graph.addRouteReal(route1_2);
        graph.addRouteReal(route2_1);
    }
}

/**
 * @brief This function parses the Toy Graphs or the Extra Fully Connected Graphs from the provided file.
 *
 * @param fileS The file name from which the graph is to be parsed.
 * @param check Boolean to check if it's a Toy Graph. If false, it's an Extra Fully Connected Graph.
 */
void Menu::parse_ToyGraphs(string fileS, bool check){
    // Construct the file paths
    string path = "../data/";
    if(check){
        path += "Toy-Graphs/";
    }
    else{
        path += "Extra_Fully_Connected_Graphs";
    }
    fileS = path + fileS;

    // Open the data file
    ifstream route_streamS(fileS);

    // Check if the files could be opened
    if(!route_streamS.good()){
        cout << "error: file does not exist" << endl;
        return;
    }

    // Check if the files are valid
    if(path == fileS){
        cout << "error: no file chosen" << endl;
        return;
    }

    // Parse the station data file
    string routeToRead;
    if(check){
        getline(route_streamS, routeToRead);
    } // discard the first line

    while (getline(route_streamS, routeToRead)) {
        stringstream Siss(routeToRead);
        string origin, destination, distance, label_o, label_d;

        getline(Siss, origin, ',');
        getline(Siss, destination, ',');
        getline(Siss, distance, ',');

        int origin_id = stoi(origin);
        int destination_id = stoi(destination);

        if(!graph.isStationInGraph(origin_id)){
            auto* station1 = new Station(stoi(origin));
            graph.addNode(station1);
        }

        if(!graph.isStationInGraph(destination_id)) {
            auto *station2 = new Station(stoi(destination));
            graph.addNode(station2);
        }

        auto* route1_2 = new Route(stoi(origin), stoi(destination), stof(distance));
        auto* route2_1 = new Route(stoi(destination), stoi(origin), stof(distance));

        graph.addRouteToy(route1_2);
        graph.addRouteToy(route2_1);
    }

    graph.sort_stations();

    // prints "succeeded" if the file was parsed correctly
    cout << "succeeded" << endl;
}

/**
 * @brief This function starts the execution of the Backtracking algorithm for the Travelling Salesman Problem (TSP).
 */
void Menu::backtracktsp(){
    graph.solveTSP_Backtracking();
}

/**
 * @brief This function checks if the user's input matches the options available in the menu.
 *
 * @param input The user's input to be checked.
 * @param max The maximum option available in the menu.
 * @return The valid menu option as an integer. If the input is invalid, it returns -1.
 */
int Menu::check_input_menu(const string& input, int max) {
    // Check if the input consists of only digits
    for(auto number : input){
        if(!isdigit(number)) return -1;
    }

    // Convert the input to an integer and check if it is within the range of valid menu options
    int ret_value = stoi(input);
    if(!(ret_value >= 0 && ret_value <= max)) return -1;

    // Return the valid menu option as an integer
    return ret_value;
}

/**
 * @brief This function clears the graph data structure.
 */
void Menu::clear_graph() {
    graph.clear();
}
