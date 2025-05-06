package com.pathfinder.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class LambdaHandler implements RequestHandler<Request, Response> {
    @Override
    public Response handleRequest(Request request, Context context) {
        int sourceX = request.getSourceX();
        String message = String.format("Source X: %d", sourceX);
        return new Response(message);
    }
}
