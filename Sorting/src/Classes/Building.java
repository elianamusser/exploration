package Classes;

import java.util.*;
import java.util.List.*;

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

        //ensure this building still has n rooms and n-1 corridors
//        if(numberOfCorridors() != numberOfRooms() - 1) {
//            rooms().remove(toAdd);
//            throw new IllegalArgumentException("Classes.Building must maintain n rooms and n-1 corridors");
//        }

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
    public List<Path> shortestPath(Room start) {
        //error checking
        Objects.requireNonNull(start);
        if(!rooms().contains(start)) {
            throw new IllegalArgumentException("Start room must be in the building");
        }

        //the path
        List<Path> path = new ArrayList<>();
        //create a queue of the leaves in this building - ie rooms connected to only one corridor - sorted by their distance from the start node
        Queue<Room> leaves = new PriorityQueue<Room>(Room::compareTo);

        //update the distance of every room from the closest leaf
        updateDistances(start);
        //add every leaf to the queue, sorted by distance from start
        leaves.addAll(leaves());

        path = path(start, leaves, path);

        postTraversalHousekeeping();

        return path;
    }

    private List<Path> path(Room start, Queue<Room> leaves, List<Path> path) {

        //if every leaf has been visited, return the path
        if(leaves.isEmpty()) {
            return path;
        }

        //update the distances between the starting room and the next room to visit
        updateDistances(start, leaves.peek());

        /*
         * create the path:
         * traverse from the start node to the closest leaf node
         * begin traversal at that leaf node
         * repeat until all leaf nodes have been visited
         */
        Room destination = leaves.poll(); //the current destination of the path
        path.add(new Path(start, destination, destination.distanceFromStart()));
        return path(destination, leaves, path);
    }


    /**
     * Housekeeping after a traversal of the entire building.
     * Ensures every room has been visited.
     * Then, sets every room's visited flag to false, and every room's distanceFromStart to 0.
     * @throws RuntimeException if not every room has been visited
     */
    private void postTraversalHousekeeping() {
        //housekeeping and error checking
        for(Room room : rooms()) {
            //reset all visited flags to false, and all room distances to 0
            room.setUnvisited();
            room.setDistanceFromStart(0);
        }
    }

    /**
     * Returns a todo update queue of the leaf nodes in the building
     * A leaf node is defined as a room with only one corridor connection, i.e. a dead end in traversal.
     * @return a list of the leaf nodes in the building
     */
    private Queue<Room> leaves() {
        Queue<Room> leaves = new PriorityQueue<>();

        //create list of leaves
        for(Room r : rooms()) {
            if(r.isLeaf()) {
                leaves.add(r);
            }
        }

        return leaves;
    }

    public Queue<Path> traversalUtil(Room start, Room destination, Queue<Path> path) {
        start.setVisited();

        

        //add every path during traversal, until destination is found
        for(Corridor c : start.adjList()) {
            Room next = c.otherEnd(start);
            path.add(new Path(start, next));

            if (next.equals(destination)) {
                return path;
            }
            else if(next.isLeaf()) { //dead end in traversal
                continue;
            }
            else if (next.unvisited()) {
                traversalUtil(next, destination, path); //move to next room in traversal
            }
//            else { //backtrack
//                traversalUtil(start, destination, path);
//            }
        }

        return path;

        //throw new RuntimeException("Traversal failed: destination never reached");
    }

//    private List<Path> traversal(Room destination) {
//        return traversalUtil(rooms().get(0), destination, new ArrayList<Path>());
//    }

    public Queue<Path> traversal(Room start, Room destination) {
        //add every path over depth first traversal from start to destination
        Queue<Path> path = traversalUtil(start, destination, new ArrayDeque<>());

        //remove any unnecessary paths
        Queue<Path> pathElements = new ArrayDeque<>(path);

        while(!pathElements.isEmpty()) {
            Path p1 = pathElements.poll();
            Path p2 = pathElements.poll();
            assert p1 != null;
            assert p2 != null;

            if(p1.source().equals(p2.destination()) ) {
                path.remove(p2);
            }

            pathElements.
        }

        postTraversalHousekeeping();

        return path;
    }

    /**
     * Sets the distanceFromStart for each of the rooms in the building, to be its distance from the given starting room.
     */
    private void updateDistances(Room start, Room end) {
        start.setDistanceFromStart(0);
        updateDistancesUtil(start, end);
    }

    private void updateDistances(Room start) {
        start.setDistanceFromStart(0);
        updateDistancesUtil(start, null);
    }

    private void updateDistancesUtil(Room start, Room end) {
        start.setVisited();

        if(start.equals(end)) {
            return;
        }

        //use depth first traversal
        for(Corridor c : start.adjList()) {
            Room nextRoom = c.otherEnd(start);
            if(nextRoom.unvisited()) {
                //update the distance of this room
                nextRoom.setDistanceFromStart(start.distanceFromStart() + c.distance());
                updateDistancesUtil(nextRoom, end);
            }
        }
    }

    /**
     * A directed path between rooms in the building.
     */
    public record Path(Room source, Room destination, int distance) {

        public Path(Room source, Room destination) {
            this(source, destination, 0);
        }

        /**
         * Whether this path is connected to the given path - i.e. if the destination of this path is the same as the source of the given path
         * @param path
         * @return
         */
        public boolean connected(Path path) {
            return destination().equals(path.source());
        }

        @Override
        public String toString() {
            return source.toString() + " -> " + destination.toString() + ", distance: " + distance + '\n';
        }

        /**
         * Paths are equal if their sources and destinations are equal
         * @param o   the reference object with which to compare.
         * @return if the paths are equal
         */
        @Override
        public boolean equals(Object o) {
            if(!(o instanceof Path p)) {
                return false;
            }
            return source().equals(p.source()) && destination().equals(p.destination());
        }
    }

}
