package exploration.src;

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
        Objects.requireNonNull(firstRoom);
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
        Objects.requireNonNull(start); //1
        Objects.requireNonNull(rooms());
        if(!rooms().contains(start)) { //2
            throw new IllegalArgumentException("Start room must be in the building");
        }

        List<Room> path = new ArrayList<>();

        //if the building only contains the start room, return only the start room
        if(rooms().size() == 1) { //3
            path.add(start);
            return path;
        }

        //update the distance of every room from the start, to figure out the direction to traverse
        updateDistances(start);
        //the leaves in this building - i.e. rooms connected to only one corridor - sorted by their distance from the start
        Queue<Room> unvisitedLeaves = new PriorityQueue<>(leaves());

        //the path through the building
        path = path(start, unvisitedLeaves); //4

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
        Objects.requireNonNull(start);
        Objects.requireNonNull(leaves);

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
        Objects.requireNonNull(start);
        Objects.requireNonNull(destination);

        traversalHousekeeping();
        //set the parent for every room on the traversal from start -> destination
        traversalUtil(start, destination);
        //build the path based on the parents
        List<Room> path = buildPath(start, destination);
        return path;
    }

    // use depth first traversal to find the path from the start node to the destination node
    private void traversalUtil(Room start, Room destination) {
        start.setVisited();

        if (start.equals(destination)) {
            return;
        }

        //add every path during traversal, until destination is found
        for(Corridor c : start.adjList()) {
            Room next = c.otherEnd(start);

            if(next.unvisited()) {
                next.setParent(start);
                traversalUtil(next, destination); //move to next room in traversal
            }
        }
   }

   /**
    * Helper for traversal method.
    * Returns a list of rooms representing the path from the start node to the destination node,
    * based on their parent fields.
    * If the parent fields are not correct for the traversal, this method will fail.
    */
    private List<Room> buildPath(Room start, Room destination) {
        Objects.requireNonNull(start);
        Objects.requireNonNull(destination);
        
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
        Objects.requireNonNull(start);
        traversalHousekeeping();
        start.setDistanceFromStart(0);
        updateDistancesUtil(start);
    }

    private void updateDistancesUtil(Room current) {
        current.setVisited();

        //use depth first traversal
        for(Corridor c : current.adjList()) {
            Room nextRoom = c.otherEnd(current);
            if(nextRoom.unvisited()) { //1
                //update the distance of this room
                nextRoom.setDistanceFromStart(current.distanceFromStart() + c.distance());
                updateDistancesUtil(nextRoom);
            }
        }
    }


        // /**
        //  * Dummy class to test the private methods in Building.
        //  * Public methods are tested in the separate JUnit file named BuildingTest.
        //  */
        // private class MockClass {
        //     private static void testUpdateDistances() {
        //         //initialize
        //         Room a = new Room("a");
        //         Room b = new Room("b");
        //         Room c = new Room("c");
        //         Room d = new Room("d");
        //         Room e = new Room("e");

        //         //1: every room in adj list has been visited
        //         Building building = new Building(a);
        //         building.addRoom(b, a, 2);
        //         building.addRoom(c, a, 3);
        //         building.addRoom(d, c, 4);
        //         building.rooms().forEach( (room) -> {room.setVisited();});
        //         building.rooms().forEach( (room) -> {room.setDistanceFromStart(-1);});
        //         building.updateDistancesUtil(e);
        //         //assert that no distances were updated if rooms were already visited
        //         building.rooms().forEach((room) -> { 
        //             if(room.distanceFromStart() != -1) {
        //                 throw new RuntimeException("updateDistancesUtil test failed.");
        //             }
        //         });
        //         System.out.println("Case 1 successful.");

        //         //1: no rooms in adj list has been visited; nominal case
        //         a.emptyAdjList(); //initialize
        //         b.emptyAdjList();
        //         building = new Building(a);
        //         building.addRoom(b, a, 2);
        //         building.rooms().forEach( (room) -> {room.setUnvisited();});

        //         building.updateDistances(a); //test
        //         if((a.distanceFromStart() == 0) && (b.distanceFromStart() == 2))
        //             System.out.println("Nominal case successful.");
                            
        //     }


        //     public static void main(String[] args) {
        //         testUpdateDistances();
        //     }

        // }


}
