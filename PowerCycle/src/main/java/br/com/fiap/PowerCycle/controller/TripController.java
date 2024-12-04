package br.com.fiap.PowerCycle.controller;

import java.util.List;

import br.com.fiap.PowerCycle.exception.TripException;

import br.com.fiap.PowerCycle.model.Trip;
import br.com.fiap.PowerCycle.response.ErrorResponse;
import br.com.fiap.PowerCycle.response.TripNotFoundException;
import br.com.fiap.PowerCycle.service.TripService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/v1/trips")
public class TripController {

    TripService tripService = new TripService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTrips() {
        try {
            List<Trip> tripList = tripService.getAllTrips();
            return Response.status(Response.Status.OK)
                    .entity(tripList)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Server Error", "An unexpected error occurred."))
                    .build();
        }
    }

    @GET
    @Path("/{trip}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTripById(@PathParam("trip") String tripId) {
        try {
            Trip trip = tripService.getTripById(tripId);
            return Response.status(Response.Status.OK)
                    .entity(trip)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Server Error", "An unexpected error occurred."))
                    .build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTrip(Trip trip) {
        try {
            tripService.registerTrip(trip);
            return Response.status(Response.Status.CREATED)
                    .entity("Your trip has been successfully created!")
                    .build();
        } catch (TripException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Invalid Input", e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Server Error", "An unexpected error occurred."))
                    .build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTrip(Trip trip) {
        try {
            tripService.updateTrip(trip);
            return Response.status(Response.Status.OK)
                    .entity("Your trip has been successfully updated!")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Server Error", "An unexpected error occurred."))
                    .build();
        }
    }

    @DELETE
    @Path("/{trip}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTrip(@PathParam("trip") String tripId) {
        try {
            tripService.deleteTrip(tripId);
            // Retorna HTTP 204 (No Content) ao invés de 200 em casos de sucesso
            return Response.noContent().build();
        } catch (TripNotFoundException e) {
            // Retorna HTTP 404 caso a trip não seja encontrada
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Not Found", e.getMessage()))
                    .build();
        } catch (Exception e) {
            // Retorna HTTP 500 para erros inesperados
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Server Error", "An unexpected error occurred."))
                    .build();
        }
    }

    
}
