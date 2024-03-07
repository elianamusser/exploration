package Classes;

import java.util.*;

/**
 * A building, ie a set of n rooms which are connected by n-1 corridors.
 * Ie, a graph, in which the rooms are nodes, and the corridors are undirected weighted edges.
 */
public class Building {
    private final List<Room> rooms;

    /**
     * @param firstRoom the initial room in the building
     */
    public Building(Room firstRoom) {
        rooms = new ArrayList<>();
        rooms.add(firstRoom);
    }

    public List<Room> rooms() {
        return rooms;
    }

    /**
     * Adds a room to the building.
     * The room is connected to the given room in the building, along a corridor with the given distance.
     * @param toAdd the room to add to the building
     * @param connection the room already in the building, to connect the new room to
     * @param distance the distance of the corridor to add
     * @throws IllegalArgumentException if: the distance < 0, the connection room is not in the building, or adding this
     * room would not keep the ratio of n rooms and n-1 corridors
     */
    public void addRoom(Room toAdd, Room connection, int distance) throws IllegalArgumentException {
        //error checking
        //other error checking occurs in Classes.Room.addCorridor method
        if(!rooms().contains(connection)) {
            throw new IllegalArgumentException("The connection room must be in the graph");
        }

        rooms().add(toAdd);

        Room.addCorridor(toAdd, connection, distance);
    }

    /**
     * @return the number of rooms in this building
     */
    public int numberOfRooms() {
        return rooms().size();
    }

    /**
     * @return the number of corridors in this building
     */
    public int numberOfCorridors() {
        int numberOfCorridors = 0;

        for(Room room : rooms()) {
            numberOfCorridors += room.numberOfConnections();
        }

        return numberOfCorridors;
    }

    /**
     * Returns the shortest path through this building, visiting every node. Begins at the given starting room.
     * @param start the room to start at
     * @return a list of paths between rooms, in order of traversal for the shortest possible distance
     */
    public List<Room> shortestPath(Room start) {
        //error checking
        Objects.requireNonNull(start);
        if(!rooms().contains(start)) {
            throw new IllegalArgumentException("Start room must be in the building");
        }

        traversalHousekeeping();

        //update the distance of every room from the start, to figure out the direction to traverse
        updateDistances(start);
        //the leaves in this building - i.e. rooms connected to only one corridor - sorted by their distance from the start
        Queue<Room> unvisitedLeaves = new PriorityQueue<>(leaves());

        //the path through the building
        List<Room> path = path(start, unvisitedLeaves);

        traversalHousekeeping();

        return path;
    }

    /**
     * Helper for shortestPath algorithm.
     * @param start room to start traversal at
     * @param leaves the leaf nodes in the building
     * @return a path from the start room to every other room in the building, based on the order of the leaves queue.
     */
    private List<Room> path(Room start, Queue<Room> leaves) {
        List<Room> path = new ArrayList<>();

        /*
         * for each leaf in the building:
         * traverse from the start node to the closest leaf node
         * add this traversal to the total path
         * repeat this with the leaf node as the new start node
         */
        Room current = start;
        while(!leaves.isEmpty()) {
            Room leaf = leaves.poll();
            path.addAll(traversal(current, leaf));
            //todo - code repetition
            if(!leaves.isEmpty()) { //unless this is the last element, remove the leaf, so that the leaf doesn't repeat in final path
                path.remove(path.size() - 1);
            }
            current = leaf;
        }

        return path;
    }


    /**
     * Housekeeping before or after a traversal of the entire building.
     * Sets every visited flag to false
     */
    private void traversalHousekeeping() {
        //housekeeping and error checking
        for(Room room : rooms()) {
            //reset all visited flags to false, and all room distances to 0
            room.setUnvisited();
        }
    }

    /**
     * Returns a queue of the leaf nodes in the building.
     * A leaf node is defined as a room with only one corridor connection, i.e. a dead end in traversal.
     * The queue is sorted by the distanceFromStart for each room.
     * @return a list of the leaf nodes in the building
     */
    private List<Room> leaves() {
        List<Room> leaves = new ArrayList<>();

        //create list of leaves
        for(Room r : rooms()) {
            if(r.isLeaf()) {
                leaves.add(r);
            }
        }

        leaves.sort(Room::compareTo);

        return leaves;
    }

    /**
     * Returns a path representing the shortest path from a starting room to a destination room.
     * @param start room to start traversal at
     * @param destination room to end traversal at
     * @return list of paths, representing the shortest path from start -> destination
     */
    public List<Room> traversal(Room start, Room destination) {
        traversalHousekeeping();
        //set the parent for every room on the traversal from start -> destination
        traversalUtil(start, destination);
        //build the path based on the parents
        List<Room> path = buildPath(start, destination);
        traversalHousekeeping();

        return path;
    }

    //use depth first traversal to find the path from the start node to the destination node
    private void traversalUtil(Room start, Room destination) {
        start.setVisited();

        if (start.equals(destination)) {
            return;
        }

        //todo - reduce code rep with other DFT?
        //add every path during traversal, until destination is found
        for(Corridor c : start.adjList()) {
            Room next = c.otherEnd(start);

            if(next.isLeaf() && !next.equals(destination)) { //dead end in traversal
                continue;
            }
            if(next.unvisited()) {
                next.setParent(start);
                //path.add(new Path(start, next, c.distance()));
                traversalUtil(next, destination); //move to next room in traversal
            }
        }
    }

    private List<Room> buildPath(Room start, Room destination) {
        /*
         * start at the destination node.
         * traverse backwards, to the parent
         * repeat with the parent
         * repeat until reaching the start node
         * finally, reverse this list
         */

        List<Room> path = new ArrayList<>();
        path.add(destination);

        Room current = destination;
        while(!current.equals(start)) {
            current = current.parent();
            path.add(current);
        }

        Collections.reverse(path);

        return path;
    }

    /**
     * Updates the distance of every room in the building to be its distance from the given start room.
     * @param start the room to calculate the distances to
     */
    private void updateDistances(Room start) {
        traversalHousekeeping();
        start.setDistanceFromStart(0);
        updateDistancesUtil(start);
        traversalHousekeeping();
    }

    private void updateDistancesUtil(Room start) {
        start.setVisited();

        //use depth first traversal
        for(Corridor c : start.adjList()) {
            Room nextRoom = c.otherEnd(start);
            if(nextRoom.unvisited()) {
                //update the distance of this room
                nextRoom.setDistanceFromStart(start.distanceFromStart() + c.distance());
                updateDistancesUtil(nextRoom);
            }
        }
    }

}
