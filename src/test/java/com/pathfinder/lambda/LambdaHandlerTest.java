package com.pathfinder.lambda;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LambdaHandlerTest {
    @Test
    public void TestLambdaHandler() {
        // Create a sample Request object with mock data
        Request request = new Request();
        request.setSourceX(100);
        request.setSourceY(200);
        request.setSourceZ(300);

        // Instantiate the Lambda handler
        LambdaHandler handler = new LambdaHandler();

        // Invoke the handler
        Response response = handler.handleRequest(request, null);

        // Validate the response
        assertEquals("Source X: 100", response.getMessage());
    }

    @Test
    public void TestA() {
        assertEquals(1, 1);
    }

    @Test
    public void TestB() {
        assertEquals(2, 2);
    }
}