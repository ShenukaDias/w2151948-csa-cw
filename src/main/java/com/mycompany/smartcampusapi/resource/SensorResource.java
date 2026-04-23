package com.mycompany.smartcampusapi.resource;

import com.mycompany.smartcampusapi.exception.DuplicateResourceException;
import com.mycompany.smartcampusapi.exception.LinkedResourceNotFoundException;
import com.mycompany.smartcampusapi.model.Room;
import com.mycompany.smartcampusapi.model.Sensor;
import com.mycompany.smartcampusapi.store.DataStore;
import com.mycompany.smartcampusapi.util.ErrorResponses;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    @GET
    public Response getSensors(@QueryParam("type") String type) {

        List<Sensor> sensors = new ArrayList<>(DataStore.sensors.values());

        if (type != null && !type.isBlank()) {
            sensors = sensors.stream()
                    .filter(s -> s.getType().equalsIgnoreCase(type))
                    .collect(Collectors.toList());
        }

        return Response.ok(sensors).build();
    }

    @POST
    public Response createSensor(Sensor sensor, @Context UriInfo uriInfo) {

        if (sensor == null || sensor.getId() == null || sensor.getId().isBlank()) {
            return ErrorResponses.build(Response.Status.BAD_REQUEST, "Sensor ID is required.");
        }
        if (sensor.getType() == null || sensor.getType().isBlank()) {
            return ErrorResponses.build(Response.Status.BAD_REQUEST, "Sensor type is required.");
        }
        if (sensor.getStatus() == null || sensor.getStatus().isBlank()) {
            return ErrorResponses.build(Response.Status.BAD_REQUEST, "Sensor status is required.");
        }
        if (sensor.getRoomId() == null || sensor.getRoomId().isBlank()) {
            return ErrorResponses.build(Response.Status.BAD_REQUEST, "Room ID is required for sensor registration.");
        }
        if (DataStore.sensors.containsKey(sensor.getId())) {
            throw new DuplicateResourceException("A sensor with the same ID already exists.");
        }

        Room room = DataStore.rooms.get(sensor.getRoomId());

        if (room == null) {
            throw new LinkedResourceNotFoundException("Room does not exist");
        }

        DataStore.sensors.put(sensor.getId(), sensor);

        room.getSensorIds().add(sensor.getId());

        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(sensor.getId())
                .build();

        return Response.created(uri).entity(sensor).build();
    }

    @GET
    @Path("/{sensorId}")
    public Response getSensor(@PathParam("sensorId") String sensorId) {

        Sensor sensor = DataStore.sensors.get(sensorId);

        if (sensor == null) {
            return ErrorResponses.build(Response.Status.NOT_FOUND, "Sensor not found.");
        }

        return Response.ok(sensor).build();
    }

    @Path("/{sensorId}/readings")
    public SensorReadingResource getSensorReadingResource(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }
}
