package com.mycompany.smartcampusapi.util;

import com.mycompany.smartcampusapi.model.ErrorResponse;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public final class ErrorResponses {

    private ErrorResponses() {
    }

    public static Response build(Response.Status status, String message) {
        ErrorResponse error = new ErrorResponse(
                status.getStatusCode(),
                status.getReasonPhrase(),
                message,
                System.currentTimeMillis()
        );

        return Response.status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}
