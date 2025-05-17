package com.pathfinder.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pathfinder.enums.Diary;
import com.pathfinder.pathfinding.Pathfinder;
import com.pathfinder.pathfinding.PlayerProperties;
import com.pathfinder.pathfinding.node.NodeEdge;
import net.runelite.api.Quest;
import net.runelite.api.Skill;
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
        WorldPoint startWP = new WorldPoint(request.getSourceX(), request.getSourceY(), request.getSourceZ());
        WorldPoint destinationWP = new WorldPoint(request.getDestinationX(), request.getDestinationY(), request.getDestinationZ());

        // Player properties
        Map<Skill, Integer> skillLevels = request.skillLevels;
        Set<Quest> questsCompleted = request.questsCompleted;
        Set<Diary> diariesCompleted = request.diariesCompleted;
        boolean isFairyRingsUnlocked = request.isFairyRingsUnlocked;
        boolean isSpiritTreesUnlocked = request.isSpiritTreesUnlocked;
        PlayerProperties playerProperties = new PlayerProperties(
                skillLevels,
                questsCompleted,
                diariesCompleted,
                isFairyRingsUnlocked,
                isSpiritTreesUnlocked
        );

        List<WorldPoint> path = Pathfinder.generatePath(startWP, destinationWP, playerProperties);
        return new Response(path.toString());
    }

    /**
     * Converts list of CustomWorldPoint objects to a JSON string
     *
     * @param path List of CustomWorldPoint objects that represent a path
     * @return JSON string representation of the path, wrapped under "path"
     */
    public String getPathJson(List<NodeEdge> path) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("path", new Gson().toJsonTree(path));
        return jsonObject.toString();
    }
}
