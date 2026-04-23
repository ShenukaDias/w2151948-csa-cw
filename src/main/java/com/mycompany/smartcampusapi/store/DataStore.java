package com.mycompany.smartcampusapi.store;

import com.mycompany.smartcampusapi.model.Room;
import com.mycompany.smartcampusapi.model.Sensor;
import com.mycompany.smartcampusapi.model.SensorReading;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStore {
    public static final Map<String, Room> rooms = new HashMap<>();
    public static final Map<String, Sensor> sensors = new HashMap<>();
    public static final Map<String, List<SensorReading>> readings = new HashMap<>();

    static {
        Room room1 = new Room("LIB-301", "Library Quiet Study", 50);
        Room room2 = new Room("LAB-101", "Computer Lab", 40);

        rooms.put(room1.getId(), room1);
        rooms.put(room2.getId(), room2);
    }
}