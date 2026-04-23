package com.mycompany.smartcampusapi.resource;

import com.mycompany.smartcampusapi.exception.SensorUnavailableException;
import com.mycompany.smartcampusapi.model.Sensor;
import com.mycompany.smartcampusapi.model.SensorReading;
import com.mycompany.smartcampusapi.store.DataStore;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private final String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    public Response getReadings() {
        Sensor sensor = DataStore.sensors.get(sensorId);

        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Sensor not found"))
                    .build();
        }

        List<SensorReading> readings = DataStore.readings.getOrDefault(sensorId, new ArrayList<>());
        return Response.ok(readings).build();
    }

    @POST
    public Response addReading(SensorReading reading, @Context UriInfo uriInfo) {
        Sensor sensor = DataStore.sensors.get(sensorId);

        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Sensor not found"))
                    .build();
        }

        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException("Sensor is under maintenance and cannot accept new readings.");
        }

        DataStore.readings.putIfAbsent(sensorId, new ArrayList<>());
        DataStore.readings.get(sensorId).add(reading);

        sensor.setCurrentValue(reading.getValue());

        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(reading.getId())
                .build();

        return Response.created(uri).entity(reading).build();
    }
}