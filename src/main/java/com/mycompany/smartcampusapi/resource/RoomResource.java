package com.mycompany.smartcampusapi.resource;

import com.mycompany.smartcampusapi.exception.DuplicateResourceException;
import com.mycompany.smartcampusapi.exception.RoomNotEmptyException;
import com.mycompany.smartcampusapi.model.Room;
import com.mycompany.smartcampusapi.store.DataStore;
import com.mycompany.smartcampusapi.util.ErrorResponses;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.util.ArrayList;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    @GET
    public Response getAllRooms() {
        return Response.ok(new ArrayList<>(DataStore.rooms.values())).build();
    }

    @POST
    public Response createRoom(Room room, @Context UriInfo uriInfo) {
        if (room == null || room.getId() == null || room.getId().isBlank()) {
            return ErrorResponses.build(Response.Status.BAD_REQUEST, "Room ID is required.");
        }
        if (room.getName() == null || room.getName().isBlank()) {
            return ErrorResponses.build(Response.Status.BAD_REQUEST, "Room name is required.");
        }
        if (room.getCapacity() <= 0) {
            return ErrorResponses.build(Response.Status.BAD_REQUEST, "Room capacity must be greater than zero.");
        }
        if (DataStore.rooms.containsKey(room.getId())) {
            throw new DuplicateResourceException("A room with the same ID already exists.");
        }
        if (room.getSensorIds() == null) {
            room.setSensorIds(new ArrayList<>());
        }

        DataStore.rooms.put(room.getId(), room);

        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(room.getId())
                .build();

        return Response.created(uri).entity(room).build();
    }

    @GET
    @Path("/{roomId}")
    public Response getRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);

        if (room == null) {
            return ErrorResponses.build(Response.Status.NOT_FOUND, "Room not found.");
        }

        return Response.ok(room).build();
    }

    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);

        if (room == null) {
            return ErrorResponses.build(Response.Status.NOT_FOUND, "Room not found.");
        }

        if (room.getSensorIds() != null && !room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException("Room cannot be deleted because sensors are still assigned.");
        }

        DataStore.rooms.remove(roomId);
        return Response.noContent().build();
    }
}
