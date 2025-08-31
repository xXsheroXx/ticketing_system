package com.shero.apigateway.route;

import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

@Configuration
public class InventoryServiceRoutes {

    @Bean
    public RouterFunction<ServerResponse> inventoryRoutes() {
        return GatewayRouterFunctions.route("inventory-service")
                // GET /api/v1/inventory/events
                .route(RequestPredicates.GET("/api/v1/inventory/events"),
                        HandlerFunctions.http(URI.create("http://localhost:8080/api/v1/inventory/events")))
                // GET /api/v1/inventory/venue/{venueId}
                .route(RequestPredicates.GET("/api/v1/inventory/venue/{venueId}"),
                        request -> forwardWithPathVariable(request, "venueId",
                                "http://localhost:8080/api/v1/inventory/venue/"))
                // GET /api/v1/inventory/event/{eventId}
                .route(RequestPredicates.GET("/api/v1/inventory/event/{eventId}"),
                        request -> forwardWithPathVariable(request, "eventId",
                                "http://localhost:8080/api/v1/inventory/event/"))
                // PUT /api/v1/inventory/event/{eventId}/capacity/{capacity}
                .route(RequestPredicates.PUT("/api/v1/inventory/event/{eventId}/capacity/{capacity}"),
                        this::forwardCapacityUpdate)
                .build();
    }

    private static ServerResponse forwardWithPathVariable(ServerRequest request,
                                                          String pathVariable,
                                                          String baseUrl) throws Exception {
        String value = request.pathVariable(pathVariable);
        return HandlerFunctions.http(URI.create(baseUrl + value)).handle(request);
    }

    private ServerResponse forwardCapacityUpdate(ServerRequest request) throws Exception {
        String eventId = request.pathVariable("eventId");
        String capacity = request.pathVariable("capacity");
        String url = "http://localhost:8080/api/v1/inventory/event/" + eventId + "/capacity/" + capacity;
        return HandlerFunctions.http(URI.create(url)).handle(request);
    }
}