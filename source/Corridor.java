package exploration.source;

/**
 * A corridor, connecting two rooms.
 */
public record Corridor(Room room1, Room room2, int distance) {

    /**
     * Returns whether the corridors are connected to each other - i.e. if they share a room
     * @param corridor1 first corridor to check connection with
     * @param corridor2 second corridor to check connection with
     * @return true if the corridors are connected
     */
    public static boolean connected(Corridor corridor1, Corridor corridor2) {
        return Room.equals(corridor1.room1(), corridor2.room1()) || Room.equals(corridor1.room1(), corridor2.room2()) ||
                Room.equals(corridor1.room2(), corridor2.room1()) || Room.equals(corridor1.room2(), corridor2.room2());
    }

    /**
     * If this corridor is connected to the given room
     * @param room room to check connection with
     * @return whether the rooms are connected
     */
    public boolean connected(Room room) {
        return room1().equals(room) || room2().equals(room);
    }

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

    /**
     * Corridors are equal if the rooms are equal. The order of the rooms does not matter.
     * @param o   the reference object with which to compare.
     * @return whether the corridors are equal
     */
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Corridor c)) {
            return false;
        }
        return (room1().equals(c.room1()) && room2().equals(c.room2())) || (room2().equals(c.room1()) && room1().equals(c.room2()));
    }
}

