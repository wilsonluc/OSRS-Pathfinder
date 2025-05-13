package com.pathfinder.lambda;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LambdaHandlerTest {
    @Test
    public void testLambdaHandler() {
        // TODO: Change this to be dynamic, is temporary placeholder
        // Create a sample Request object with mock data
        Request request = new Request();
        request.setSourceX(100);
        request.setSourceY(200);
        request.setSourceZ(300);

        // Instantiate the Lambda handler
        LambdaHandler handler = new LambdaHandler();

        // Invoke the handler
        Response response = handler.handleRequest(request, null);

        String expectedResponse = "{\"path\":[{\"sourceWP\":{\"x\":100,\"y\":200,\"plane\":0},\"destinationWP\":{\"x\":110,\"y\":210,\"plane\":0}},{\"sourceWP\":{\"x\":110,\"y\":210,\"plane\":0},\"destinationWP\":{\"x\":120,\"y\":220,\"plane\":0}}]}";
        // Validate the response
        assertEquals(expectedResponse, response.message());
    }
}