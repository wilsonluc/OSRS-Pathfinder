package com.pathfinder.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.pathfinder.pathfinding.Pathfinder;
import com.pathfinder.pathfinding.PlayerProperties;
import net.runelite.api.coords.WorldPoint;

import java.util.*;

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
        System.out.println("Request string: " + request.toString());

        WorldPoint startWP = new WorldPoint(request.getSourceX(), request.getSourceY(), request.getSourceZ());
        WorldPoint destinationWP = new WorldPoint(request.getDestinationX(), request.getDestinationY(), request.getDestinationZ());

        // Player properties
        boolean fairyRingsUnlocked = request.isFairyRingsUnlocked();
        boolean spiritTreesUnlocked = request.isSpiritTreesUnlocked();
//        Map<Skill, Integer> skillLevels = request.skillLevels;
//        Set<Quest> questsCompleted = request.questsCompleted;
//        Set<Diary> diariesCompleted = request.diariesCompleted;
        PlayerProperties playerProperties = new PlayerProperties(
                fairyRingsUnlocked,
                spiritTreesUnlocked
//                skillLevels,
//                questsCompleted,
//                diariesCompleted,
        );

        List<WorldPoint> path = Pathfinder.generatePath(startWP, destinationWP, playerProperties);
        Map<String, Object> pathMap = getPathMap(path);
        return new Response(pathMap);
    }

    public Map<String, Object> getPathMap(List<WorldPoint> path) {
        Map<String, Object> responseMap = new HashMap<>();
        List<Map<String, Integer>> pathList = new ArrayList<>();

        for (WorldPoint wp : path) {
            Map<String, Integer> pointMap = new HashMap<>();
            pointMap.put("x", wp.getX());
            pointMap.put("y", wp.getY());
            pointMap.put("z", wp.getPlane());
            pathList.add(pointMap);
        }

        responseMap.put("path", pathList);
        return responseMap;
    }
}
