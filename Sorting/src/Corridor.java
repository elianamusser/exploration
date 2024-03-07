/**
 * A corridor, connecting two rooms.
 */
public record Corridor(Room room1, Room room2, int distance) {

    /**
     * If the given room is at one end of the corridor, returns the room at the other end of the corridor.
     *
     * @param room the room to reference
     * @return the room at the other end of the corridor
     * @throws IllegalArgumentException if the given room is not at one end of the corridor
     */
    public Room otherEnd(Room room) {
        if (room1().equals(room)) {
            return room2();
        } else if (room2().equals(room)) {
            return room1();
        } else {
            throw new IllegalArgumentException("The given room must be at one end of the corridor");
        }
    }
}

