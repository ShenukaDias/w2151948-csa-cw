package com.mycompany.smartcampusapi.resource;

import com.mycompany.smartcampusapi.exception.RoomNotEmptyException;
import com.mycompany.smartcampusapi.model.Room;
import com.mycompany.smartcampusapi.store.DataStore;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map;

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
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Room ID is required"))
                    .build();
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
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Room not found"))
                    .build();
        }

        return Response.ok(room).build();
    }

    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);

        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Room not found"))
                    .build();
        }

        if (room.getSensorIds() != null && !room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException("Room cannot be deleted because sensors are still assigned.");
        }

        DataStore.rooms.remove(roomId);
        return Response.noContent().build();
    }
}