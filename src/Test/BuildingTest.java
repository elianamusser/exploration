package Test;

import java.util.*;
import org.junit.*;
import org.junit.Assert;
import Classes.*;

public class BuildingTest{

    //todo - change to junit 4

    final Room a = new Room("a");
    final Room b = new Room("b");
    final Room c = new Room("c");
    final Room d = new Room("d");
    final Room e = new Room("e");
    final Room f = new Room("f");


    @Test
    public void testShortestPath() {
        Building building;
        List<Room> expectedPath;

        //1: error case: start room is null
        final Building building2 = new Building(a);
        Assert.assertThrows(NullPointerException.class, () -> {building2.shortestPath(null);}); 

        //2: error case: building does not contain the start room
        initializeTest(Arrays.asList(a, b, c, d));
        final Building building1 = new Building(a);
        building1.addRoom(c, a, 2);
        building1.addRoom(d, c, 2);
        building1.addRoom(b, c, 4);
        Assert.assertThrows(IllegalArgumentException.class, () -> {building1.shortestPath(e);}); 

        //3: rooms list only contains start room
        initializeTest(Arrays.asList(a));
        building = new Building(a);
        expectedPath = Arrays.asList(a); //result should be a list of only one room
        Assert.assertEquals(building.shortestPath(a), expectedPath);

        //4: test that path is correct if building has two rooms, which is the minimum building size for this branch 
        initializeTest(Arrays.asList(a, c));
        building = new Building(a);
        building.addRoom(c, a, 2);
        expectedPath = Arrays.asList(a, c); //result should be a list of two rooms
        Assert.assertEquals(building.shortestPath(a), expectedPath);

        //4: test that path is correct for nominal case
        initializeTest(Arrays.asList(a, b, c, d, e, f));
        building = new Building(a);
        building.addRoom(c, a, 2);
        building.addRoom(d, c, 2);
        building.addRoom(b, c, 4);
        building.addRoom(e, d, 3);
        building.addRoom(f, e, 4);
        
        expectedPath = Arrays.asList(e, f, e, d, c, a, c, b);
        Assert.assertEquals(building.shortestPath(e), expectedPath);
        
    }

    //initializes a shortest path test
    void initializeTest(List<Room> roomsList) {
        roomsList.forEach( (room) -> {
            room.emptyAdjList(); //empty the adjacency list of this room
        });
    }

}
