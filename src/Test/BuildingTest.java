package Test;

import java.util.*;
import org.junit.jupiter.api.Test;
import org.junit.Assert;
import Classes.*;

public class BuildingTest{

    final Room a = new Room("a");
    final Room b = new Room("b");
    final Room c = new Room("c");
    final Room d = new Room("d");
    final Room e = new Room("e");
    final Room f = new Room("f");
    // @Test
    // void testNumberOfCorridors() {
        
    // }

    // @Test
    // void testNumberOfRooms() {

    // }

    // @Test
    // void testRooms() {

    // }

    @Test
    void testShortestPath() {

        //nominal case, randomly generated
        Building building = new Building(a);
        building.addRoom(c, a, 2);
        building.addRoom(d, c, 2);
        building.addRoom(b, c, 4);
        building.addRoom(e, d, 3);
        building.addRoom(f, e, 4);
        
        List<Room> expectedPath = Arrays.asList(e, f, e, d, c, a, c, b);
        Assert.assertEquals(building.shortestPath(e), expectedPath);

        //1: error case: building does not contain the start room
        initializeTest(Arrays.asList(a, b, c, d));
        final Building building1 = new Building(a);
        building1.addRoom(c, a, 2);
        building1.addRoom(d, c, 2);
        building1.addRoom(b, c, 4);

        Assert.assertThrows(IllegalArgumentException.class, () -> {building1.shortestPath(e);}); 

        //2: rooms list only contains start room
        initializeTest(Arrays.asList(a));
        building = new Building(a);
        expectedPath = Arrays.asList(a);
        Assert.assertEquals(building.shortestPath(a), expectedPath);

        //3: building has two rooms
        initializeTest(Arrays.asList(a, c));
        building = new Building(a);
        building.addRoom(c, a, 2);
        expectedPath = Arrays.asList(a, c);
        Assert.assertEquals(building.shortestPath(a), expectedPath);
        
        //3: error case: start room is null
        final Building building2 = new Building(a);
        Assert.assertThrows(NullPointerException.class, () -> {building2.shortestPath(null);}); 

        //4: error case: building is above the max supported size
        initializeTest(Arrays.asList(e));
        int unsupportedSize = 100;
        final Building building3 = new Building(e);
        for(int i = 0; i < unsupportedSize; i++) { //add 100 rooms to this building
            building3.addRoom(new Room("" + i), e, 5);
        }
        Assert.assertThrows(IllegalArgumentException.class, () -> {building3.shortestPath(e);});

    }

    //initializes a shortest path test
    void initializeTest(List<Room> roomsList) {
        roomsList.forEach( (room) -> {
            room.emptyAdjList(); //empty the adjacency list of this room
        });
    }

    @Test
    void testTraversal() {

    }
}
