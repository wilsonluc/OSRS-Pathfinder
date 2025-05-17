package com.pathfinder.lambda;

import lombok.Getter;

import java.util.Objects;

/**
 * Wraps a message that can be returned from a Lambda function.
 */
@Getter
public class Response {
    private final String message;

    public Response(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Response)) return false;
        Response other = (Response) obj;
        return Objects.equals(message, other.message);
    }

    @Override
    public int hashCode() {
        return message != null ? message.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Response{message='" + message + "'}";
    }
}