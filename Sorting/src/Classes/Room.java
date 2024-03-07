package Classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A room in a building.
 */
public class Room implements Comparable<Room> {

    //the ID of this room
    private String id;
    //the adjacency list of this room (as a node in the graph)
    private final List<Corridor> adjList;

    //to be used during traversal: the distance between this room and the start room in traversal
    private int distanceFromStart = 0;
    //to be used during traversal: if this room has been visited
    private boolean visited = false;

    public Room(String id) {
        this.id = id;
        adjList = new ArrayList<Corridor>();
    }

    public void setID(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }

    public int distanceFromStart() {
        return distanceFromStart;
    }

    public void setDistanceFromStart(int distance) {
        distanceFromStart = distance;
    }

    /**
     * In traversal, whether this room has not yet been visited.
     *
     * @return true if this room has not yet been visited in traversal
     */
    public boolean unvisited() {
        return !visited;
    }

    /**
     * Set this room to be visited
     */
    public void setVisited() {
        visited = true;
    }

    /**
     * Set this room to be unvisited
     */
    public void setUnvisited() {
        visited = false;
    }

    /**
     * Whether this room is a "leaf node."
     * A leaf node is defined as a room which is connected to only one corridor.
     *
     * @return true if this room is a leaf node.
     */
    public boolean isLeaf() {
        return adjList().size() == 1;
    }

    /**
     * The corridors that this room is connected to.
     *
     * @return a list of the corridors that this room is connected to
     */
    public List<Corridor> adjList() {
        return adjList;
    }

    /**
     * Rooms are compared based on their distanceFromStart.
     * @param room the object to be compared.
     * @return -1, 0, or 1 based on comparison
     */
    @Override
    public int compareTo(Room room) {
        return Integer.compare(distanceFromStart(), room.distanceFromStart());
    }

    /**
     * Whether this room is connected to the given room
     */
    public boolean connected(Room room) {
        for(Corridor c : adjList()) {
            if(c.connected(room) && !this.equals(room)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Adds a corridor between this room and the given room.
     * @param room     the room to connect this room to
     * @param distance the distance of the corridor
     */
    public void addConnection(Room room, int distance) {
        Corridor c = new Corridor(this, room, distance);
        //if the corridor is already in the list, do nothing
        if(!adjList().contains(c))
            adjList().add(c); //add corridor to the adjacency list
    }

    /**
     * Adds a corridor between rooms.
     * @param room1 the first end of the corridor
     * @param room2 the second end of the corridor
     * @param distance the distance of the corridor
     * @throws IllegalArgumentException if distance < 0
     */
    public static void addCorridor(Room room1, Room room2, int distance) {
        Objects.requireNonNull(room1);
        Objects.requireNonNull(room2);
        if(distance < 0) {
            throw new IllegalArgumentException("Distance must be greater than 0");
        }

        room1.addConnection(room2, distance);
        room2.addConnection(room1, distance);
    }

    /**
     * The number of corridors that this room is connected to.
     * @return the number of corridors that this room is connected to
     */
    public int numberOfConnections() {
        return adjList().size();
    }

    @Override
    public String toString() {
        return id;
    }

    /**
     * Tests for equality.
     * @param o room to compare this room to
     * @return true if: the given object is a room, the ids of the rooms are the same, and
     * the adjacency lists of the rooms are the same
     */
    @Override
    public boolean equals(Object o) {
        return (o instanceof Room) && this.id().equals(((Room) o).id()) && this.adjList.equals(((Room) o).adjList);
    }

    public static boolean equals(Room room1, Room room2) {
        return room1.equals(room2);
    }

}

