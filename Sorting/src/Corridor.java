public class Corridor {
    int distance;
    Room room1;
    Room room2;

    public Corridor(Room room1, Room room2, int distance) {
        this.room1 = room1;
        this.room2 = room2;
        this.distance = distance;
    }

    public int distance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Room room1() {
        return room1;
    }

    public Room room2() {
        return room2;
    }

    /**
     * The other end of the corridor from the given room.
     * ie. if this corridor is between rooms A and B, and the given room is room A, returns room B.
     * @param room the room to check
     * @return the other end of the corridor from the given room
     * @throws IllegalArgumentException if the given room is not on one end of the corridor
     */
    public Room otherEnd(Room room) {
        //todo reduce code rep
        if(room1().equals(room)) {
            return room2();
        }
        else if(room2().equals(room)) {
            return room1();
        }
        else {
            throw new IllegalArgumentException("Given room must be one end of this corridor.");
        }
    }


}
