/**
 * A building, ie a set of n rooms which are connected by n-1 corridors.
 * Ie, a graph, in which the rooms are nodes, and the corridors are undirected weighted edges.
 */
import java.util.*;

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
        Objects.requireNonNull(toAdd);
        Objects.requireNonNull(connection);
        if(distance < 0) {
            throw new IllegalArgumentException("The distance must be positive");
        }
        if(!rooms().contains(connection)) {
            throw new IllegalArgumentException("The connection room must be in the graph");
        }

        rooms().add(toAdd);

        //ensure this building still has n rooms and n-1 corridors
        if(numberOfCorridors() != numberOfRooms() - 1) {
            rooms().remove(toAdd);
            throw new IllegalArgumentException("Building must maintain n rooms and n-1 corridors");
        }

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
        Objects.requireNonNull(start);

        List<Path> path = new ArrayList<>();

        //update the distance of every room from the start room
        updateDistances(start);
        //find the leaves in this building - ie rooms connected to only one corridor
        List<Room> leaves = leaves();
        //sort this list by the distance of each leaf node from the start
        leaves.sort((room1, room2) -> Integer.compare(room1.distanceFromStart, room2.distanceFromStart));

        /*
         * create the path:
         * traverse from the start node to the closest leaf node
         * begin traversal at that leaf node
         * repeat until all leaf nodes have been visited
         */
        for (Room room : leaves) {
            path.add(new Path(start, room, room.distanceFromStart()));
            start = room;
        }

        postTraversalHousekeeping();

        return path;
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
            //error checking - ensure every room was visited
            if(room.unvisited()) {
                throw new RuntimeException("Shortest path algorithm failed: not every room was visited");
            }
            //reset all visited flags to false, and all room distances to 0
            room.setUnvisited();
            room.setDistanceFromStart(0);
        }
    }

    /**
     * Returns a list of the leaf nodes in the building
     * A leaf node is defined as a room with only one corridor connection, ie. a dead end in traversal.
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

        return leaves;
    }

    /**
     * Sets the distanceFromStart for each of the rooms in the building, to be its distance from the given starting room.
     */
    private void updateDistances(Room start) {
        start.setVisited();
        start.setDistanceFromStart(0);

        //use depth first traversal
        for(Corridor c : start.adjList()) {
            Room nextRoom = c.otherEnd(start);
            if(nextRoom.unvisited()) {
                //update the distance of this room
                nextRoom.setDistanceFromStart(start.distanceFromStart + c.distance());
                updateDistances(nextRoom);
            }
        }
    }

    /**
     * A directed path between rooms in the building.
     */
    public record Path(Room source, Room destination, int distance) { }

}
