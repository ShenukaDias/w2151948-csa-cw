package com.mycompany.smartcampusapi.mapper;

import com.mycompany.smartcampusapi.exception.LinkedResourceNotFoundException;
import com.mycompany.smartcampusapi.model.ErrorResponse;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class LinkedResourceNotFoundExceptionMapper implements ExceptionMapper<LinkedResourceNotFoundException> {

    @Override
    public Response toResponse(LinkedResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                422,
                "Unprocessable Entity",
                ex.getMessage(),
                System.currentTimeMillis()
        );

        return Response.status(422)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}