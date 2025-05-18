package com.pathfinder.lambda;

import java.util.Map;

/**
 * Wraps a message that can be returned from a Lambda function.
 */
public class Response {
    private Map<String, Object> message;

    public Response(Map<String, Object> message) {
        this.message = message;
    }

    public Map<String, Object> getMessage() {
        return message;
    }

    public void setMessage(Map<String, Object> message) {
        this.message = message;
    }
}