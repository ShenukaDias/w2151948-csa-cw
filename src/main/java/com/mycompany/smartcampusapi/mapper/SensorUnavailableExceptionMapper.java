package com.mycompany.smartcampusapi.mapper;

import com.mycompany.smartcampusapi.exception.SensorUnavailableException;
import com.mycompany.smartcampusapi.model.ErrorResponse;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class SensorUnavailableExceptionMapper implements ExceptionMapper<SensorUnavailableException> {

    @Override
    public Response toResponse(SensorUnavailableException ex) {
        ErrorResponse error = new ErrorResponse(
                403,
                "Forbidden",
                ex.getMessage(),
                System.currentTimeMillis()
        );

        return Response.status(Response.Status.FORBIDDEN)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}