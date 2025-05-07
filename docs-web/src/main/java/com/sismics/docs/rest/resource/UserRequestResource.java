package com.sismics.docs.rest.resource;

import com.sismics.docs.core.model.jpa.UserRequest;
import com.sismics.docs.core.service.UserRequestService;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST resource for user registration requests.
 */
@Path("/userRequest")
public class UserRequestResource {

    private final UserRequestService service = new UserRequestService();

    /**
     * Guest sends a registration request.
     */
    @POST
    @Path("/submit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response submitRequest(Map<String, String> payload) {
        String name = payload.get("name");
        String email = payload.get("email");

        if (name == null || email == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Missing name or email").build();
        }

        String requestId = service.createRequest(name, email);
        Map<String, String> result = new HashMap<>();
        result.put("id", requestId);

        return Response.ok(result).build();
    }

    /**
     * Admin fetches all pending requests.
     */
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPendingRequests() {
        List<UserRequest> requests = service.getPendingRequests();
        return Response.ok(requests).build();
    }

    /**
     * Admin approves a request.
     */
    @POST
    @Path("/{id}/approve")
    public Response approveRequest(@PathParam("id") String requestId, @HeaderParam("X-User-Id") String adminUserId) {
        try {
            service.approveRequest(requestId, adminUserId);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Request not found or already processed").build();
        }
    }

    /**
     * Admin rejects a request.
     */
    @POST
    @Path("/{id}/reject")
    public Response rejectRequest(@PathParam("id") String requestId) {
        service.rejectRequest(requestId);
        return Response.ok().build();
    }
}
