package com.mycompany.smartcampusapi.mapper;

import com.mycompany.smartcampusapi.exception.RoomNotEmptyException;
import com.mycompany.smartcampusapi.model.ErrorResponse;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {

    @Override
    public Response toResponse(RoomNotEmptyException ex) {
        ErrorResponse error = new ErrorResponse(
                409,
                "Conflict",
                ex.getMessage(),
                System.currentTimeMillis()
        );

        return Response.status(Response.Status.CONFLICT)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}