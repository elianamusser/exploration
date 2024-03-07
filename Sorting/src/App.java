public class App {

    public static void main(String[] args) {
        Room a = new Room("A"), b = new Room("B"), c = new Room("C"), d = new Room("D"),
                e = new Room("E"), f = new Room("F");
        Building building = new Building(a);
        building.addRoom(b, a, 2);
        building.addRoom(b, d, 4);
        building.addRoom(b, e, 9);
        building.addRoom(b, f, 1);
        building.addRoom(a, c, 5);



    }
}
