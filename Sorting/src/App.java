public class App {

    public static void main(String[] args) {
        Room a = new Room("A"), b = new Room("B"), c = new Room("C"), d = new Room("D"),
                e = new Room("E"), f = new Room("F");
        Building building = new Building(a);
        building.addRoom(b, a, 2);
        building.addRoom(d, b, 4);
        building.addRoom(e, b, 9);
        building.addRoom(f, b, 1);
        building.addRoom(c, a, 5);

        System.out.println(building.shortestPath(a));

    }
}
