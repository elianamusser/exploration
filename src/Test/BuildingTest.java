package Test;

import Classes.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BuildingTest {
    static Building building;

    static Room a = new Room("a");
    static Room b = new Room("b");
    static Room c = new Room("c");
    static Room d = new Room("d");
    static Room e = new Room("e");
    static Room f = new Room("f");
    static Room g = new Room("g");

    // static Corridor c1;
    // static Corridor c2;
    // static Corridor c3;
    // static Corridor c4;
    // static Corridor c5;
    // static Corridor c6;

    @BeforeAll
    static void initialize() {
        building = new Building(a);
        // c1 = new Corridor(a, b, 2);
        // c2 = new Corridor(d, b, 4);
        // c3 = new Corridor(e, b, 9);
        // c4 = new Corridor(b, f, 1);
        // c5 = new Corridor(c, a, 5);

        building.addRoom(b, a, 2);
        building.addRoom(d, b, 4);
        building.addRoom(e, b, 9);
        building.addRoom(f, b, 1);
        building.addRoom(c, a, 5);
        building.addRoom(g, e, 2);
    }

    void addRoom() {
        //todo check that rooms() list is updated
        assertEquals(building.rooms().get(0), a);

        //todo check adjacency lists of rooms
        List<Corridor> adjList = new ArrayList<>();
        Collections.addAll(adjList, new Corridor(a, b, 2), new Corridor(a, c, 5));
        assertEquals(a.adjList(), adjList);
    }

    @Test
    void shortestPath() {
        List<Room> shortestPath = building.shortestPath(a);
        System.out.println(shortestPath);
        List<Room> desiredPath = List.of(a, b, f, b, a, c, a, b, d, b, e, g);
        assertEquals(desiredPath, shortestPath);
    }

    @Test
    void traversal() {
        List<Room> expectedPath = new ArrayList<>();
        Collections.addAll(expectedPath, a, b, e);
        assertEquals(expectedPath, building.traversal(a, e));

        expectedPath = new ArrayList<>();
        Collections.addAll(expectedPath, a, b, e, g);
        assertEquals(expectedPath, building.traversal(a, g));
    }
}