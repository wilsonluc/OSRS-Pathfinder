package com.pathfinder.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pathfinder.CustomWorldPoint;
import net.runelite.api.coords.WorldPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Handler for processing requests in an AWS Lambda function.
 * After processing a request, generates a path as a list of CustomWorldPoint objects,
 * and returns the path as a JSON string.
 */
public class LambdaHandler implements RequestHandler<Request, Response> {
    /**
     * Handles incoming request and processes it to generate a path in JSON format
     *
     * @param request Incoming request object with user-related data
     * @param context Context of request such as invocation, function & execution environment
     * @return JSON representation of path
     */
    @Override
    public Response handleRequest(Request request, Context context) {
        // TODO: Change this to be dynamic, is temporary placeholder
        List<CustomWorldPoint> path = new ArrayList<>();
        path.add(new CustomWorldPoint(new WorldPoint(100, 200, 0), new WorldPoint(110, 210, 0)));
        path.add(new CustomWorldPoint(new WorldPoint(110, 210, 0), new WorldPoint(120, 220, 0)));

        String pathJson = getPathJson(path);
        return new Response(pathJson);
    }

    /**
     * Converts list of CustomWorldPoint objects to a JSON string
     *
     * @param path List of CustomWorldPoint objects that represent a path
     * @return JSON string representation of the path, wrapped under "path"
     */
    public String getPathJson(List<CustomWorldPoint> path) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("path", new Gson().toJsonTree(path));
        return jsonObject.toString();
    }
}
