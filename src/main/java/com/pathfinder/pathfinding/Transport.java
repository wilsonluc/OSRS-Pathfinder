package com.pathfinder.pathfinding;

import com.pathfinder.pathfinding.node.NodeEdge;
import com.pathfinder.enums.Type;
import com.pathfinder.pathfinding.transports.FairyRing;
import com.pathfinder.pathfinding.transports.SpiritTree;
import com.pathfinder.util.Util;
import lombok.Getter;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;

import java.io.InputStream;
import java.util.*;

/**
 * This class represents a travel point between two WorldPoints.
 */
public class Transport {
    /**
     * The starting point of this transport.
     */
    @Getter
    private final WorldPoint origin;

    /**
     * The ending point of this transport.
     */
    @Getter
    private final WorldPoint destination;

    /**
     * The additional cost to reach the destinationWP.
     */
    @Getter
    public int additionalCost;

    /**
     * {@link WorldArea} representing Ferox's Enclave.
     */
    @Getter
    public static final WorldArea FEROX = new WorldArea(3125, 3622, 27, 18, 0);

    /**
     * {@link WorldArea} representing the wilderness.
     */
    @Getter
    public static final WorldArea WILDY = new WorldArea(2944, 3522, 446, 446, 0);

    /**
     * {@link WorldArea} representing the underground section of the wilderness.
     */
    @Getter
    public static final WorldArea UNDERGROUND_WILDY = new WorldArea(2944, 9918, 320, 442, 0);

    /**
     * File name used for reading in transports (located in resources).
     */
    private static final String TRANSPORTS_DIR = "transports.csv";

    /**
     * Mapping of {@link WorldPointPair} containing source and destination {@link WorldPoint}s against
     * value of {@link NodeEdge} containing source and destination {@link WorldPoint}s with additional cost.
     */
    public static Map<WorldPointPair, NodeEdge> worldPointPairs = new HashMap<>();

    /**
     * Constructs a transport link between two {@link WorldPoint} objects.
     *
     * @param origin      The starting location of the transport
     * @param destination The end location of the transport
     * @param type        Type of transport
     */
    public Transport(WorldPoint origin, WorldPoint destination, Type type) {
        this.origin = origin;
        this.destination = destination;
        this.additionalCost = type.getAdditionalCost();
    }

    /**
     * Constructs a transport link between two {@link WorldPoint} objects.
     *
     * @param origin      The starting location of the transport
     * @param destination The end location of the transport
     */
    public Transport(WorldPoint origin, WorldPoint destination) {
        this(origin, destination, Type.TRANSPORT);
    }

    /**
     * Parses the transports from the internal CSV resource file and populates the given maps.
     *
     * @param transports      A map from origin {@link WorldPoint} to a list of {@link Transport}s originating from there
     * @param worldPointPairs A map from {@link WorldPointPair} to {@link NodeEdge}, used for calculating cost
     */
    private static void addTransports(HashMap<WorldPoint, List<Transport>> transports, Map<WorldPointPair, NodeEdge> worldPointPairs) {
        InputStream inputStream = Transport.class.getClassLoader().getResourceAsStream(TRANSPORTS_DIR);
        if (inputStream == null) {
            return;
        }

        Scanner scanner = new Scanner(inputStream);
        scanner.nextLine(); // Skip first line
        while (scanner.hasNextLine()) {
            String transportLine = scanner.nextLine();
            addTransport(transportLine, transports, worldPointPairs);
        }
        scanner.close();
    }

    /**
     * Parses a single line from the transport CSV and adds the transport entry to the relevant maps.
     *
     * @param transportLine   The raw CSV line describing the transport
     * @param transports      Map from {@link WorldPoint} to transport list
     * @param worldPointPairs Map from {@link WorldPointPair} to {@link NodeEdge}
     */
    private static void addTransport(String transportLine, HashMap<WorldPoint, List<Transport>> transports, Map<WorldPointPair, NodeEdge> worldPointPairs) {
        if (transportLine.isEmpty() || transportLine.contains("#") || transportLine.equals(",,,,,,,,")) {
            return;
        }

        String[] splitString = transportLine.split(",");
        if (splitString.length < 5) {
            return;
        }

        WorldPoint sourceWP = Util.getWorldPoint(splitString[0]);
        WorldPoint destinationWP = Util.getWorldPoint(splitString[1]);
        // TODO: Implement filtering based on player properties
        String menuOption = splitString[2];
        Integer objectID = Integer.parseInt(splitString[4]);

        Transport transport = new Transport(sourceWP, destinationWP);
        transports.computeIfAbsent(sourceWP, k -> new ArrayList<>()).add(transport);
        worldPointPairs.putIfAbsent(new WorldPointPair(sourceWP, destinationWP),
                new NodeEdge(sourceWP, destinationWP, Type.TRANSPORT));
    }

    /**
     * Loads all transport data from the {@code transports.csv} resource and constructs the full transport map.
     *
     * @return A {@link HashMap} containing all transports, keyed by origin {@link WorldPoint}
     */
    public static HashMap<WorldPoint, List<Transport>> loadAllFromResources() {
        HashMap<WorldPoint, List<Transport>> transports = new HashMap<>();

        addTransports(transports, worldPointPairs);
        addSpiritTrees(transports, worldPointPairs);
        addFairyRings(transports, worldPointPairs);

        return transports;
    }

    /**
     * Adds all spirit tree teleports to viable transports.
     *
     * @param transports      Map of transports to populate to
     * @param worldPointPairs Map used to store details of transport
     */
    private static void addSpiritTrees(HashMap<WorldPoint, List<Transport>> transports, Map<WorldPointPair, NodeEdge> worldPointPairs) {
        for (SpiritTree spiritTreeSource : SpiritTree.values()) {
            for (SpiritTree spiritTreeDestination : SpiritTree.values()) {
                if (spiritTreeSource.equals(spiritTreeDestination)) {
                    continue;
                }

                Transport transport = new Transport(spiritTreeSource.getWorldPoint(), spiritTreeDestination.getWorldPoint(), Type.SPIRIT_TREE);
                transports.computeIfAbsent(spiritTreeSource.getWorldPoint(), k -> new ArrayList<>()).add(transport);
            }
        }

        addTransports(transports, worldPointPairs);
    }

    /**
     * Adds all fairy ring teleports to viable transports.
     *
     * @param transports      Map of transports to populate to
     * @param worldPointPairs Map used to store details of transport
     */
    private static void addFairyRings(HashMap<WorldPoint, List<Transport>> transports, Map<WorldPointPair, NodeEdge> worldPointPairs) {
        for (FairyRing fairyRingSource : FairyRing.values()) {
            for (FairyRing fairyRingDestination : FairyRing.values()) {
                if (fairyRingSource.equals(fairyRingDestination)) {
                    continue;
                }

                Transport transport = new Transport(fairyRingSource.getWorldPoint(), fairyRingDestination.getWorldPoint(), Type.FAIRY_RING);
                transports.computeIfAbsent(fairyRingSource.getWorldPoint(), k -> new ArrayList<>()).add(transport);
            }
        }

        addTransports(transports, worldPointPairs);
    }
}
