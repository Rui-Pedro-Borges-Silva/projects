#include "graph.h"
#include "station.h"
#include <utility>
#include <vector>
#include <queue>
#include <iostream>
#include <limits>
#include <unordered_map>
#include <algorithm>


/**
 * @brief Constructs a new Graph.
 *
 * This constructor initializes the number of nodes in the graph to zero
 * and creates empty lists for the graph's stations and lines.
 */
Graph::Graph(){
    // Initialize the number of nodes in the graph to zero
    n = 0;

    // Create empty lists for the graph's stations and lines
    stations = {};
    lines = {};
}


/**
 * @brief Adds a new node to the graph with the specified station.
 *
 * This function creates a new node in the graph with the specified station
 * and initializes outgoing lines. The node is added to the list of stations
 *
 * @param s Pointer to the station to add to the graph.
 */
void Graph::addNode(Station* s) {
    // Create an empty vector of outgoing lines
    vector<OutLine*> outlines = {};

    // Create a new StationNode and set its fields
    StationNode toAdd;
    toAdd.station = s;
    toAdd.color = "white";
    toAdd.outLines = outlines;

    // Add the new node to the graph's list of stations
    stations.push_back(toAdd);
    n += 1;
}

void Graph::deleteNode(int id) {
    for(auto & node : stations){
        for(auto it = node.outLines.begin(); it != node.outLines.end(); it++){
            if((*it)->dest == stations[id].station->getName()){
                node.outLines.erase(it);
                break;
            }
        }
    }
    stations.erase(stations.begin() + id);
    n -= 1;
}

void Graph::deleteEdge(int id1, int id2) {
    for(auto it = stations[id1].outLines.begin(); it != stations[id1].outLines.end(); it++){
        if((*it)->dest == stations[id2].station->getName()){
            stations[id1].outLines.erase(it);
            break;
        }
    }
}


/**
 * @brief Performs a BFS starting from the specified node.
 *
 * During the search, calculates its distance from the starting node, and keeps track
 * of its predecessor in the search tree.
 *
 * @param nod The index of the starting node in the graph's list of stations.
 */

void Graph::bfs(int nod) {
    // Initialize all nodes as unvisited (white)
    for(auto & node: stations){
        node.color = "white";
        node.dist = -1;
        node.pred = -1;
    }

    // Mark the starting node as visiting (gray) and set its distance to 0
    stations[nod].color = "gray";
    stations[nod].dist = 0;

    // Create a queue and add the starting node to it
    queue<int> Q;
    Q.push(nod);

    while(!Q.empty()){
        // Get the next node from the queue
        int u = Q.front();

        // Check all the "sons" from the current node
        for(auto outEdge : stations[u].outLines){
            int v = findNode(outEdge->dest);
            // Visit the node if it is unvisited and the edge has available capacity
            if(stations[v].color == "white" && outEdge->flow < outEdge->capacity){
                // Mark the node as visiting (gray) and update its distance and predecessor
                stations[v].color = "gray";
                stations[v].dist = stations[u].dist + 1;
                stations[v].pred = u;
                // Add the node to the queue for further traversal
                Q.push(v);
            }
        }

        // Remove the current node from the queue and mark it as visited (black)
        Q.pop();
        stations[u].color = "black";
    }
}


/**
 * @brief Performs a DFS on the graph.
 *
 * Visiting each node in the graph exactly once. Calculates its
 * discovery and finish times, and keeps track of its predecessor in the DFS tree.
 *
 * Time Complexity: O(V + E)
 */
void Graph::dfs(){
    // Initialize all nodes as unvisited (white)
    for(auto node : stations){
        node.color = "white";
        node.visit = 0;
        node.pred = -1;
    }

    // Perform DFS
    int time = 1;
    for(int i = 0; i < n; i++){
        if(stations[i].color == "white"){
            time = dfs_visit(i, time);
        }
    }
}


/**
 * @brief Helper function for DFS function.
 *
 * Performs a DFS of the graph starting from the specified node. Calculates
 * its discovery and finish times, and keeps track of its predecessor
 * in the DFS tree.
 *
 * @param nod The index of the starting node in the graph's list of stations.
 * @param time The current time in the DFS.
 * @return The updated time in the DFS traversal.
 * Time Complexity: O(V + E)
 */
int Graph::dfs_visit(int nod, int time) {
    // Mark the current node as visiting (gray) and set its discovery time
    stations[nod].color = "gray";
    stations[nod].dist = time;
    time += 1;

    // Visit each outgoing edge from the current node
    for(auto outEdge : stations[nod].outLines){
        int v = findNode(outEdge->dest);
        // Visit the node if it is unvisited
        if(stations[v].color == "white"){
            // Mark the node's predecessor and continue the DFS from the node
            stations[v].pred = nod;
            time = dfs_visit(v, time);
        }
    }

    // Mark the current node as visited (black) and set its finish time
    stations[nod].color = "black";
    stations[nod].visit = time;
    time = time + 1;

    return time;
}

/**
 * @brief Finds the index of the node with the specified station name.
 *
 * @param stationName The name of the station to find in the graph.
 * @return The index of the node with the specified station name, or -1 if the node is not found.
 * Time Complexity: O(V)
 */

int Graph::findNode(const std::string& stationName) const {
    for (int i = 0; i < n; i++) {
        if (stations[i].station->getName() == stationName) {
            return i;
        }
    }

    return -1;
}


/**
 * @brief Adds a new line to the graph, connecting two stations.
 *
 * The line is added to the graph's list of lines,
 * and the edges are added to the
 * appropriate station's list of outgoing edges.
 *
 * @param l Pointer to the line to add to the graph.
 * Time Complexity: O(V)
 */
void Graph::addLine(NetworkLine* l) {
    // Add the new line to the graph's list of lines
    lines.push_back(l);

    // Initialize the outgoing edges from each station on the line
    for(int i = 0; i < n; i++) {
        // Mark the current station as visited (black)
        stations[i].color = "black";

        // Get the names of the current station and the stations on the line
        string source, station1, station2;
        source = stations[i].station->getName();
        station1 = l->getStation1();
        station2 = l->getStation2();

        // Create a new outgoing edge and add it to the appropriate station's list of edges
        OutLine *toAdd;
        if (source == station1) {
            toAdd = new OutLine({l, source,station2, 0, l->getCapacity() / 2});
            stations[i].outLines.push_back(toAdd);
        }
        if(source == station2) {
            toAdd = new OutLine({l, source,station1, 0 ,(l->getCapacity()) / 2});
            stations[i].outLines.push_back(toAdd);
        }
    }
}

/**
 * @brief Prints the adjacency list of the graph.
 *
 * It prints the name of the node followed by a list of its outgoing edges,
 * each of which is represented by the name of its destination node.
 * Time Complexity: O(V + E)
 */
void Graph::print() {
    // Print the adjacency list representation of the graph
    for (auto i: stations) {
        cout << i.station->getName() << ": ";
        for (auto e: i.outLines) {
            cout << e->dest << ", ";
        }
        cout << endl;
    }
}

/**
 * @brief Computes the maximum flow from the source to the sink using the Edmonds-Karp algorithm.
 *
 * The algorithm repeatedly finds augmenting paths in the residual graph and updates
 * the flow along those paths. Once no more augmenting paths can be found, the
 * algorithm terminates and returns the maximum flow. If the source and sink nodes
 * are the same, the function returns -1 to indicate that no flow can be found.
 *
 * @param source The name of the source station.
 * @param destiny The name of the sink station.
 * @return The maximum flow from the source to the sink, or -1 if the source and sink are the same.
 * Time Complexity: O(V * E^2)
 */
int Graph::edmondsKarp(const std::string& source, const std::string& destiny)  {
    auto src = findNode(source);
    auto sink = findNode(destiny);

    // check if source and sink are in the same station
    if (source == destiny) {
        return -1;
    }

    // Reset the flow in all outgoing lines from the nodes
    for (auto i: stations) {
        for (auto e: i.outLines) {
            e->flow = 0;
        }
    }

    int max_flow = 0;

    while (findAugmentingPath(src, sink)) {

        int pathFlow = std::numeric_limits<int>::max();

        // Find the minimum flow in the path
        for (auto v = sink; stations[v].station->getName() != stations[src].station->getName();) {
            for (auto e: stations[stations[v].pred].outLines) {
                if (e->dest == stations[v].station->getName()) {
                    pathFlow = min(pathFlow, e->capacity - e->flow);
                    v = findNode(e->source);
                    break;
                }
            }
            if (v == -1)
                break;
        }

        // Update the flow in the path
        if (pathFlow != 0){
            for (auto v = sink; stations[v].station->getName() != stations[src].station->getName();) {
                for (auto &e : stations[stations[v].pred].outLines) {
                    if (e->dest == stations[v].station->getName()) {
                        e->flow += pathFlow;
                        v = findNode(e->source);
                        break;
                    }
                }
            }
        }

        max_flow += pathFlow;
    }

    return (max_flow ? max_flow : -1);
}


/**
 * @brief Finds an augmenting path in the graph using BFS.
 *
 * Starting from the specified source node and ending at the specified destiny node.
 * If an augmenting path is found, the function returns true; otherwise, it
 * returns false. the distance of each node from the source node, is used to
 * determine the existence of an augmenting path.
 *
 * @param source The index of the source node in the graph's list of stations.
 * @param destiny The index of the destiny node in the graph's list of stations.
 * @return true if an augmenting path is found, false otherwise.
 * Time Complexity: O(V + E)
 */
bool Graph::findAugmentingPath(int source, int destiny) {
    // use a call to bfs to find the augmenting path

    bfs(source);

    if (stations[destiny].dist == -1) {
        return false;
    }

    return true;
}

bool sortByCap(NetworkLine* line1, NetworkLine* line2){
    return line1->getCapacity() < line2->getCapacity();
}

int Graph::findIndexCluster(string station, vector<vector<StationNode>> clusters){
    for(int i = 0; i < clusters.size(); i++){
        for(auto node : clusters[i]){
            if(node.station->getName() == station){
                return i;
            }
        }
    }
    return -1;
}

vector<NetworkLine*> Graph::kruskal_mst() {
    vector<vector<StationNode>> clusters;
    vector<NetworkLine*> temp = lines;
    vector<NetworkLine*> reduced;
    sort(temp.begin(), temp.end(), sortByCap);
    for(auto node : stations){
        clusters.push_back({node});
    }
    for(auto line : temp){
        int index1 = findIndexCluster(line->getStation1(), clusters);
        int index2 = findIndexCluster(line->getStation2(), clusters);
        if(!(index1 == index2)){
            reduced.push_back(line);
            clusters[index1].insert(clusters[index1].end(), clusters[index2].begin(), clusters[index2].end());
            clusters.erase(clusters.begin() + index2);
        }
    }
    return reduced;
}

/**
 * @brief Clears the graph, deleting all nodes and lines.
 *
 * Time Complexity: O(V + E)
 */
void Graph::clear() {
    // Delete all nodes and their outgoing lines
    for (auto node: stations) {
        for (auto line: node.outLines) {
            delete (line);
        }
        delete (node.station);
    }

    // Delete all lines in the graph
    for (auto line: lines) {
        delete (line);
    }
}

/**
 * @brief Finds the maximum flow between all pairs of nodes in the graph.
 *
 * Using the Edmonds-Karp algorithm, and prints the maximum flow to the console.
 *
 * @return A vector of pairs representing the source and destination nodes with the maximum flow.
 * Time Complexity: O(V^3 * T), T is the time complexity of the Edmonds-Karp algorithm.
 */

vector<std::pair<std::string, std::string>> Graph::maxFlow() {
    // Initialize variables for maximum flow and pairs with maximum flow
    std::vector<std::pair<std::string, std::string>> maxFlow;
    int max = 0;

    // Find the maximum flow between all pairs of nodes in the graph
    for(int i = 0; i < n; i++){
        for(int j = 0; j < n; j++){
            if(i != j){
                // Compute the flow between the current pair of nodes
                int flow = edmondsKarp(stations[i].station->getName(), stations[j].station->getName());

                // Update the maximum flow and pairs with maximum flow
                if(flow > max){
                    max = flow;
                    maxFlow.clear();
                    maxFlow.push_back({stations[i].station->getName(), stations[j].station->getName()});
                } else if (flow == max) {
                    maxFlow.push_back({stations[i].station->getName(), stations[j].station->getName()});
                }
                else {
                    continue;
                }
            }
        }
    }

    // Print the maximum flow to the console and return the pairs with maximum flow
    cout << "Max flow: " << max << endl;
    return maxFlow;
}

Station* Graph::findStation(string name){
    int index = findNode(name);
    return stations[index].station;
}

vector<string> Graph::getDistrict(){
    vector<string> districts;
    for (auto v : stations){
        districts.push_back(v.station->get_district());
    }
    return districts;
}

vector<string> Graph::getMunicipality(){
    vector<string> municipally;
    for (auto v : stations){
        municipally.push_back(v.station->get_municipality());
    }
    return municipally;
}

unsigned Graph::transportationNeed(string const &district) {
    int superSourceId = 0;
    int superSinkId = n;

    vector<int> in;
    vector<int> out;

    addNode(findStation(stations[superSourceId].station->getName()));
    addNode(findStation(stations[superSinkId].station->getName()));

    for (auto v: stations) {
        if (findNode(v.station->getName()) == superSourceId || findNode(v.station->getName()) == superSinkId) continue;

        if (v.station->get_district() == district) {
            NetworkLine* l = new NetworkLine(v.station->getName(), stations[superSinkId].station->getName(), 0, "INF");
            addLine(l);
            out.push_back(findNode(v.station->getName()));
        } else if (v.outLines.size() == 1) {
            NetworkLine* m = new NetworkLine( stations[superSinkId].station->getName(),v.station->getName(), 0, "INF");
            addLine(m);
            in.push_back(findNode(v.station->getName()));
        }
    }

    int maxFlow = edmondsKarp(stations[superSourceId].station->getName(), stations[superSinkId].station->getName());

    for (int id: in) {
        deleteEdge(superSourceId,id);
    }

    for (int id: out) {
        deleteEdge(id,superSinkId);
    }

    deleteNode(superSourceId);
    deleteNode(superSinkId);

    return maxFlow;
}


/**
 * @brief Finds the maximum number of trains that can arrive at a given station in the current graph.
 *
 * @param station_name The name of the station.
 * @return The maximum number of trains that can arrive at the given station.
 *
 * Time Complexity: O(E * V^2 + n + m), where O(E * V^2) is the time complexity of the Edmonds-Karp algorithm,
 * O(n) is the time complexity of loop all nodes and O(m) because the additional edges from super
 */
int Graph::maxTrainArrivingStation(string station_name){
    int target = findNode(station_name);
    // temporary station
    super = new Station("super", "", "", "", "");
    addNode(super);
    int flow = 0;
    for(auto v : stations) {
        // take all the nodes of beginning without station_name
        if (v.station->getName() != station_name && v.outLines.size() == 1) {
            // connect temporary station (super) to all of the beginning stations
            nt = new NetworkLine(super->getName(), v.station->getName(), std::numeric_limits<int>::max(), "");
            addLine(nt);
        }
    }
    // compute the maximum flow from the "super" node to the target station
    flow = edmondsKarp(super->getName(), station_name);
    return flow;
}