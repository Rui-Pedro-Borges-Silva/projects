#include "../include/graph.h"
#include <vector>
#include <iostream>
#include <algorithm>
#include <cmath>
#include <queue>

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
    vector<Route*> outlines = {};

    // Create a new StationNode and set its fields
    StationNode toAdd;
    toAdd.station = s;
    toAdd.color = "white";
    toAdd.outLines = outlines;

    // Add the new node to the graph's list of stations
    stations.push_back(toAdd);
    n += 1;
}

/**
 * @brief Adds a new route to the graph, connecting two stations.
 *
 * Used in Toy graphs.
 * The route is added to the
 * appropriate station's list of outgoing edges.
 *
 * @param l Pointer to the route to add to the graph.
 * Time Complexity: O(V)
 */
void Graph::addRouteToy(Route* r) {
    // Initialize the outgoing edges from each station on the route
    for(int i = 0; i < n; i++) {
        // Add edge to station
        if (stations[i].station->getId() == r->getOrigin()) {
            stations[i].outLines.push_back(r);
        }
    }
}

/**
 * @brief Adds a new route to the graph, connecting two stations.
 *
 *
 * Used in Real World graphs, taking advantage of the fact that
 * all nodes are already added and sorted.
 * The route is added to the
 * appropriate station's list of outgoing edges.
 *
 * @param l Pointer to the route to add to the graph.
 * Time Complexity: O(1)
 */
void Graph::addRouteReal(Route* r) {
    stations[r->getOrigin()].outLines.push_back(r);
}

/**
 * @brief Clears the graph, deleting all nodes and lines.
 *
 * Time Complexity: O(V + E)
 */
void Graph::clear() {
    // Delete all nodes and their outgoing lines
    for (const auto &node: stations) {
        for (auto line: node.outLines) {
            delete (line);
        }
        delete (node.station);
    }
}

// print graph (debugging)
void Graph::print() {
    for(const auto& node: stations){
        cout << node.station->getId() << ":";
        for(auto route: node.outLines){
            cout << route->getDestination() << " ,";
        }
    cout << endl;
    }
}

/**
 * @brief Checks if a station is in the graph.
 *
 * @param id The ID of the station to check.
 * @return true If the station is in the graph.
 * @return false If the station is not in the graph.
 */
bool Graph::isStationInGraph(int id){
    for(const auto& node: stations){
        if(node.station->getId() == id){
            return true;
        }
    }   
    return false;
}

/**
 * @brief Sorts stations based on their IDs.
 */
void Graph::sort_stations(){
    sort(stations.begin(), stations.end(), [](const StationNode& a, const StationNode& b){return a.station->getId() < b.station->getId();});
}

/**
 * @brief Get the distance between two nodes.
 *
 * @param id1 The ID of the first node.
 * @param id2 The ID of the second node.
 * @return The distance between the two nodes, or -1 if no route exists between them.
 */
double Graph::getDistance(int id1, int id2) {
    // Find the StationNode with ID id1
    StationNode* node1 = nullptr;
    for (StationNode& node : stations) {
        if (node.station->getId() == id1) {
            node1 = &node;
            break;
        }
    }
    // If no node with ID id1 exists, return -1
    if (node1 == nullptr) {
        return -1;
    }
    // Search for the Route that connects id1 and id2
    for (Route* route : node1->outLines) {
        if (route->getDestination() == id2) {
            return route->getDistance();
        }
    }

    // If no route exists between id1 and id2, return -1
    return -1;
}



/**
 * @brief Calculate the total distance of the current tour.
 *
 * @param tour Vector representing the tour.
 * @return The total distance of the tour.
 */
// Function to calculate the total distance of the current tour
double Graph::calculateDistance(vector<int>& tour) {
    double totalDistance = 0;
    for (int i = 0; i < tour.size() -1; i++) {
        totalDistance += getDistance(tour[i], tour[i + 1]);
    }
    return totalDistance;
}

/**
 * @brief Recursive function for exploring all paths in the graph.
 *
 * @param currentNode The current node being explored.
 * @param currentTour The current tour.
 * @param visited A vector indicating whether each node has been visited.
 * @param minDistance The current minimum distance.
 * @param bestTour The best tour found so far.
 */
// Recursive function for exploring all paths
void Graph::backtrackTSP(int currentNode, vector<int>& currentTour, vector<bool>& visited, double& minDistance, vector<int>& bestTour) {
    currentTour.push_back(currentNode);
    visited[currentNode] = true;

    if (currentTour.size() == stations.size()) { // all nodes have been visited
        // now we consider returning to the start node (node 0)
        for (const auto& route : stations[currentNode].outLines) {
            if (route->getDestination() == 0) { // there is a path back to the start node
                currentTour.push_back(0); // return to the start node
                double currentDistance = calculateDistance(currentTour);
                if (currentDistance < minDistance) {
                    minDistance = currentDistance;
                    bestTour = currentTour;
                }
                currentTour.pop_back(); // remove the start node from the current tour
                break; // we've returned to the start node, so no need to check other paths from the current node
            }
        }
    } else {
        // visit other nodes
        for (const auto& route : stations[currentNode].outLines) {
            if (!visited[route->getDestination()]) {
                backtrackTSP(route->getDestination(), currentTour, visited, minDistance, bestTour);
            }
        }
    }

    visited[currentNode] = false;
    currentTour.pop_back();
}

/**
 * @brief Solves the Traveling Salesman Problem (TSP) using backtracking.
 */
// Function to solve the TSP using backtracking
void Graph::solveTSP_Backtracking() {
    vector<int> currentTour, bestTour;
    vector<bool> visited(stations.size(), false);
    double minDistance = numeric_limits<double>::max();

    // Start the tour at the first station
    visited[0] = true;
    backtrackTSP(0, currentTour, visited, minDistance, bestTour);

    // Print out the best tour
    cout << "Minimum distance: " << minDistance << endl;
    cout << "Best tour: ";
    for (const auto& station : bestTour) {
        cout << station << " ";
    }
    cout << endl;
}

/**
 * @brief Calculates the real distance between two nodes.
 *
 * @param x The ID of the first node.
 * @param y The ID of the second node.
 * @return The real distance between the two nodes.
 */
double Graph::real_distance(int x, int y){
    for(auto route : stations[x].outLines){
        if(route->getDestination() == y){
            return route->getDistance();
        }
    }

    double r, latX, latY, lonX, lonY;
    r = PI/180;
    latX=stations[x].station->getLatitude()*r;
    latY=stations[y].station->getLatitude()*r;
    lonX=stations[x].station->getLongitude()*r;
    lonY=stations[y].station->getLongitude()*r;
    return EARTH_RADIUS_KM * acos((sin(latX)*sin(latY)) + (cos(latX)*cos(latY)*cos(lonX - lonY)));
}

/**
 * @brief Constructs the minimum spanning tree of the graph using Prim's algorithm.
 */
void Graph::prim_mst() {
    auto compare_real = [](StationNode* x, StationNode* y) { return x->key < y->key; };
    priority_queue<StationNode*, vector<StationNode*>, decltype(compare_real)> pQ(compare_real);

    for (auto& node : stations) {
        node.in_MST = false;
        node.key = numeric_limits<double>::max();
    }
    stations[0].key = 0;
    stations[0].pred = -1;

    pQ.push(&stations[0]);

    while (!pQ.empty()) { //build mst
        auto u = pQ.top();
        pQ.pop();
        if (u->in_MST) {
            continue;  // Skip this node if it is already in the MST
        }
        u->in_MST = true;

        for (auto v : u->outLines) {
            int index_v = v->getDestination();
            if (!stations[index_v].in_MST && v->getDistance() < stations[index_v].key) {
                stations[index_v].pred = v->getOrigin();
                stations[index_v].key = v->getDistance();
                if(!stations[index_v].in_pQ){
                    stations[index_v].in_pQ = true;
                    pQ.push(&stations[index_v]);
                }
            }
        }
    }

    for(const auto& node : stations){ //add children to correct nodes
        if(node.station->getId() != 0){
            stations[node.pred].children.push_back(node.station->getId());
        }
    }
}


/**
 * @brief Calculates the length of a tour using the triangular approximation method.
 *
 * @return The total distance of the tour.
 */
double Graph::triangularApproximation(){
    double distance = 0;
    vector<int> path;
    prim_mst();
    int last = triangularApproximation_R(&distance, 0, &path);
    distance += real_distance(last, 0);
    path.push_back(0);

    cout << "Best Tour (approximation): ";
    for(auto i : path){
        cout << i << " ";
    }
    cout << endl << "Distance: " << distance << endl;

    return distance;
}

/**
 * @brief Recursive helper function for triangular approximation.
 *
 * @param d Pointer to the total distance.
 * @param index The current node index.
 * @param path Pointer to the path of nodes.
 * @return The ID of the last node in the path.
 */
int Graph::triangularApproximation_R(double* d, int index, vector<int>* path){
    int prev_node = -1;
    path->push_back(index);
    if(stations[index].children.empty()){
        return index;
    }
    for(auto child : stations[index].children){
        if(prev_node != -1){
            *d += real_distance(prev_node, child);
        }
        else{
            *d += real_distance(index, child);
        }
        prev_node = triangularApproximation_R(d, child, path);
    }
    return path->back();
}