#include <chrono>
#include "menu.h"
#include "graph.h"
#include "networkline.h"
#include "station.h"
#include <iostream>
#include <fstream>
#include <string>
#include <sstream>

void Menu::main_menu() {
    string file1;
    string file2;
    while (true) { //por exemplo
        cout << "======================================================" << endl;
        cout << "                         Menu                         " << endl;
        cout << "======================================================" << endl;
        cout << "  0-Close                                             " << endl;
        cout << "  1-Parse data set file                               " << endl;
        cout << "  2-Most used/busy stations                           " << endl; //T2.3
        cout << "  3-Max number of trains between stations             " << endl; //T2.1
        cout << "  4-Max number of trains that can arrive at station   " << endl; //T2.4
        cout << "  5-Max number of trains between stations (greedy)    " << endl; //T3.1
        cout << "  6-Max train capacity    " << endl; //T2.2
        cout << "  7-Reduced Connectivity " << endl;
        cout << "======================================================" << endl;
        cout << ">?";
        // ...
        string input;
        cin >> input;
        int option = check_input_menu(input, 7);
        switch (option) {
            case 1:
                cout << "Stations File Name: ";
                cin >> file1;
                cout << "Network File Name: ";
                cin >> file2;
                parse_file(file1, file2);
                build_subgraph(graph.kruskal_mst());
                break;
            case 2:
                Municipalidades();
                break;
            case 3:
                //graph.print();
                maxTrainBetweenStations(false);
                break;
            case 4:
                maxTrainsAtStation();
                break;
            case 5:
                break;
            case 6:
                PairOfStationsWithMostTrains();
                break;
            case 7:
                // mst_graph.print();
                maxTrainBetweenStations(true);
                break;
            case 0:
                clear_graph();
                return;
        }
    }
}

/**
 * @brief Parses the station and network line data files and adds them to the graph.
 *
 * This function reads the station and network line data files and creates Station
 * and NetworkLine objects for each entry in the files. It then adds the stations
 * and lines to the graph using the `addNode` and `addLine` functions, respectively.
 *
 * @param fileS The filename for the station data file.
 * @param fileN The filename for the network line data file.
 */
void Menu::parse_file(string fileS, string fileN){
    // Construct the file paths
    string path = "../Project1Data/";
    fileS = path + fileS;
    fileN = path + fileN;

    // Open the station and network line data files
    ifstream line_stream_S(fileS);
    ifstream line_stream_N(fileN);

    // Check if the files could be opened
    if(!line_stream_S.good() || !line_stream_N.good()){
        cout << "error: file does not exist" << endl;
        return;
    }

    // Check if the files are valid
    if(path == fileS || path == fileN){
        cout << "error: no file chosen" << endl;
        return;
    }

    // Parse the station data file
    string lineToRead;
    getline(line_stream_S, lineToRead); // discard the first line
    while (getline(line_stream_S, lineToRead)) {
        stringstream Siss(lineToRead);
        string name, district, municipality, township, line;

        getline(Siss, name, ',');
        getline(Siss, district, ',');
        getline(Siss, municipality, ',');
        getline(Siss, township, ',');
        getline(Siss, line, '\n');

        Station* s = new Station(name, district, municipality, township, line);
        graph.addNode(s);
    }

    // Parse the network line data file
    getline(line_stream_N, lineToRead); // discard the first line
    while (getline(line_stream_N, lineToRead)) {
        stringstream Niss(lineToRead);
        string station_a, station_b, capacity_string, service;

        getline(Niss, station_a, ',');
        getline(Niss, station_b, ',');
        getline(Niss, capacity_string, ',');
        getline(Niss, service, ',');

        int capacity = stoi(capacity_string);
        NetworkLine* l = new NetworkLine(station_a, station_b, capacity, service);
        graph.addLine(l);
    }
}


/**
 * @brief Gets the maximum number of trains that can travel between two stations.
 *
 * This function receives a bool which indicates if it execute the edmondsKarp on the graph
 * or on the minimum spanning tree of the graph
 *
 * @param mode The boolean indicating which of the graph is to be used.
 *
 */

void Menu::maxTrainBetweenStations(bool mode) {

    // Prompt the user for the names of two stations
    string station_a, station_b;
    cin.clear();
    cin.ignore(1000,'\n');
    cout << "Station A: " << endl;
    getline(cin, station_a);
    cout << "Station B: " << endl;
    getline(cin, station_b);

    int max_trains;

    if(mode) {
        max_trains = mst_graph.edmondsKarp(station_a, station_b);
    }
    else{
        max_trains = graph.edmondsKarp(station_a, station_b);
    }

    // Output the result to the console
    if (max_trains == -1) {
        std::cout << "Invalid stations!\n";
        return;
    }

    cout << "Max number of trains between " << station_a << " and " << station_b << ": " << max_trains << "\n";

}


/**
 * @brief Prints the pair of stations with the most trains passing through them.
 *
 * This function finds the pair of stations with the most trains passing through them
 * using the `maxFlow` function of the `Graph` class.
 */
void Menu:: PairOfStationsWithMostTrains(){
    vector<std::pair<string, string>> maxFlow = graph.maxFlow();

    cout << maxFlow.capacity();
}

/**
 * @brief Checks if the user input is a valid menu option.
 *
 * @param input The user input to check.
 * @param max The maximum valid menu option.
 * @return The valid menu option as an integer, -1 if the input is invalid.
 */
int Menu::check_input_menu(string input, int max) {
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
 * @brief Finds the maximum number of trains arriving at a station.
 *
 */
void Menu:: maxTrainsAtStation() {
    // Prompt the user for the name of a station
    string station_name;
    cout << "\nEnter the name of the desired station:";
    cin.ignore();
    getline(cin,station_name);

    // Find the corresponding node in the graph using the `findNode` function
    auto v=graph.findNode(station_name);

    // If the node does not exist, print an error message
    if(v == 0){
        cout << "ERROR: You need to enter a valid station name!" << endl;
    }
        // Otherwise, call the `maxTrainArrivingStation` function and print the result
    else{
        int flow = graph.maxTrainArrivingStation(station_name);
        cout <<"\nThe max number of trains that arrive at " << station_name << " is " << flow << " trains." << endl;
    }
}


/**
 * @brief Clears the graph data structure.
 *
 */
void Menu::clear_graph() {
    graph.clear();
}

void Menu::build_subgraph(vector<NetworkLine*> lines){
    for(auto line : lines){
        string name1 = line->getStation1();
        string name2 = line->getStation2();
        if(mst_graph.findNode(name1) == -1){
            Station* station1 = graph.findStation(name1);
            mst_graph.addNode(station1);
        }
        if(mst_graph.findNode(name2) == -1) {
            Station *station2 = graph.findStation(name2);
            mst_graph.addNode(station2);
        }
        mst_graph.addLine(line);
    }
}

/**
 * @brief Function used to call topKMunDistr.
 *
 */
void Menu::Municipalidades(){
    unsigned k;
    cout << "Enter the k value: " << endl;
    cin >> k;
    topKMunDistr(k);
}

/**
 * @brief Finds the top-k municipalities and districts, regarding their transportation needs;
 *
 * @param k Integer which determines for how many top-k will be searching for.
 *
 */
void Menu::topKMunDistr(unsigned k) {
    vector<pair<string, int>> district_res;
    vector<pair<string, int>> municip_res;

    vector<string> districts = graph.getDistrict();
    vector<string> municipalities = graph.getMunicipality();
    cout << "chega aqui" << endl;

    for (const string &district: districts) {
        district_res.emplace_back(district, graph.transportationNeed(district));
    }

    sort(district_res.begin(), district_res.end(), [](const pair<string, int> &a, const pair<string, int> &b) {
        return a.second > b.second;
    });

    cout << "The top " << k << " districts are:" << endl;
    for (auto it = district_res.begin(); it != district_res.begin() + k && it != district_res.end(); ++it) {
        cout << it->first << " -> " << it->second << endl;
    }
}
