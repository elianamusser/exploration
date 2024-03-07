/**
 * A building, ie a set of n rooms which are connected by n-1 corridors.
 * Ie, a graph, in which the rooms are nodes, and the corridors are undirected weighted edges.
 */

import java.util.*;

public class Building {
    private List<Room> building;

    public List<Room> building() {
        return building;
    }

    //todo add objects.requirenonnull to everything

    /**
     * Adds a room, with the given ID, to the building.
     * The room is connected to the given room in the building, along a corridor with the given distance.
     * @param toAdd
     * @param connection
     * @param distance
     * @throws Exception
     */
    public void addRoom(Room toAdd, Room connection, int distance) throws IllegalArgumentException {
        //housekeeping
        Objects.requireNonNull(toAdd);
        Objects.requireNonNull(connection);
        if(distance < 0) {
            throw new IllegalArgumentException("The distance must be positive");
        }

        building().add(toAdd);
        toAdd.addConnection(connection, distance);
        connection.addConnection(toAdd, distance);
    }


    /**
     * @return the number of rooms in this building
     */
    public int numberOfRooms() {
        return building().size();
    }

    /**
     * @return the number of corridors in this building
     */
    public int numberOfCorridors() {
        int numberOfCorridors = 0;

        for(Room room : building()) {
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
        List<Path> path = new ArrayList<>();

        //update the distance of every room from the start room
        updateDistances(start);
        //find the leaves in this building - ie rooms connected to only one corridor
        List<Room> leaves = leaves();
        //sort this list by the distance of each leaf node from the start
        leaves.sort((room1, room2) -> Integer.compare(room1.distanceFromStart, room2.distanceFromStart));

        //create the path
        Iterator<Room> leavesIterator = leaves.listIterator();
        while(leavesIterator.hasNext()) {
            Room room = leavesIterator.next();
            path.add(new Path(start, room, room.distanceFromStart()));
            //if this is not the last room in the iteration, backtrack to start node
            if(leavesIterator.hasNext()) {
                path.add(new Path(room, start, room.distanceFromStart()));
            }
        }

        //housekeeping - reset all visited flags to false, and all room distances to 0

        return path;
    }

    /**
     * Returns a list of the leaf nodes in the building
     * A leaf node is defined as a room with only one corridor connection, ie. a dead end in traversal.
     * @return a list of the leaf nodes in the building
     */
    private List<Room> leaves() {
        List<Room> leaves = new ArrayList<Room>();

        //create list of leaves
        for(Room r : building()) {
            if(r.isLeaf()) {
                leaves.add(r);
            }
        }

        return leaves;
    }

    //updates the distances of each room in this building from the given start room
    public void updateDistances(Room start) {
        start.setVisited();
        start.distanceFromStart = 0;

        //use depth first traversal
        for(Corridor c : start.corridors()) {
            Room nextRoom = c.otherEnd(start);
            if(!nextRoom.visited()) {
                //update the distance of each room
                nextRoom.distanceFromStart = start.distanceFromStart + c.distance();
                updateDistances(nextRoom);
            }
        }
    }

    /**
     * A directed path between rooms in the building.
     */
    public static class Path {
        private final Room source;
        private final Room destination;
        private final int distance;

        //private constructor to abstract the use of this class
        private Path(Room source, Room destination, int distance) {
            this.source = source;
            this.destination = destination;
            this.distance = distance;
        }

        public Room source() {
            return source;
        }

        public Room destination() {
            return destination;
        }

        public int distance() {
            return distance;
        }

        /**
         * The total distance of a list of paths
         * @param paths the list of paths to calculate the distance of
         * @return the total distance of the given list of paths
         */
        public static int totalDistance(List<Path> paths) {
            int totalDistance = 0;

            for(Path p : paths) {
                totalDistance += p.distance();
            }

            return totalDistance;
        }

    }

}
