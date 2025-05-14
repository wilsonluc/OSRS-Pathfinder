package com.pathfinder.pathfinding;

import com.pathfinder.pathfinding.node.NodeEdge;
import com.pathfinder.enums.TransportType;
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
 *
 * @param origin        The starting point of this transport.
 * @param destination   The ending point of this transport.
 * @param transportType The type of this transport.
 */
public record Transport(@Getter WorldPoint origin, @Getter WorldPoint destination,
                        @Getter TransportType transportType) {
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
     * Loads all transport data from the {@code transports.csv} resource and constructs the full transport map.
     *
     * @return A {@link HashMap} containing all transports, keyed by origin {@link WorldPoint}
     */
    public static HashMap<WorldPoint, List<Transport>> loadAllFromResources(PlayerProperties playerProperties) {
        HashMap<WorldPoint, List<Transport>> transports = new HashMap<>();

        addTransports(playerProperties, transports);
        addFairyRings(playerProperties, transports);
        addSpiritTrees(playerProperties, transports);

        return transports;
    }

    /**
     * Add transport to transports arraylist.
     *
     * @param sourceWP      The origin {@link WorldPoint} of the transport
     * @param destinationWP The destination {@link WorldPoint} of the transport
     * @param transport     Contains source & destination {@link WorldPoint}s, as well as transport type
     * @param transports    Map of all transports, using WorldPoint as key
     */
    private static void addTransport(WorldPoint sourceWP, WorldPoint destinationWP, Transport transport, HashMap<WorldPoint, List<Transport>> transports) {
        transports.computeIfAbsent(sourceWP, k -> new ArrayList<>()).add(transport);
        worldPointPairs.putIfAbsent(new WorldPointPair(sourceWP, destinationWP),
                new NodeEdge(sourceWP, destinationWP, TransportType.TRANSPORT));
    }

    /**
     * Parses the transports from the internal CSV resource file and populates the given maps.
     *
     * @param playerProperties Transports unlocked for player
     * @param transports       A map from origin {@link WorldPoint} to a list of {@link Transport}s originating from there
     */
    private static void addTransports(PlayerProperties playerProperties, HashMap<WorldPoint, List<Transport>> transports) {
        InputStream inputStream = Transport.class.getClassLoader().getResourceAsStream(TRANSPORTS_DIR);
        if (inputStream == null) {
            return;
        }

        Scanner scanner = new Scanner(inputStream);
        scanner.nextLine(); // Skip first line
        while (scanner.hasNextLine()) {
            String transportLine = scanner.nextLine();
            addTransport(transportLine, transports);
        }
        scanner.close();
    }

    /**
     * Parses a single line from the transport CSV and adds the transport entry to the relevant maps.
     *
     * @param transportLine The raw CSV line describing the transport
     * @param transports    Map from {@link WorldPoint} to transport list
     */
    private static void addTransport(String transportLine, HashMap<WorldPoint, List<Transport>> transports) {
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

        Transport transport = new Transport(sourceWP, destinationWP, TransportType.TRANSPORT);
        addTransport(sourceWP, destinationWP, transport, transports);
    }

    /**
     * Adds all fairy ring teleports to viable transports.
     *
     * @param playerProperties Transports unlocked for player
     * @param transports       Map of transports to populate to
     */
    private static void addFairyRings(PlayerProperties playerProperties, HashMap<WorldPoint, List<Transport>> transports) {
        if (!playerProperties.isFairyRingsUnlocked()) {
            return;
        }

        for (FairyRing fairyRingSource : FairyRing.values()) {
            for (FairyRing fairyRingDestination : FairyRing.values()) {
                if (fairyRingSource.equals(fairyRingDestination)) {
                    continue;
                }

                Transport transport = new Transport(fairyRingSource.getWorldPoint(), fairyRingDestination.getWorldPoint(), TransportType.FAIRY_RING);
                addTransport(fairyRingSource.getWorldPoint(), fairyRingDestination.getWorldPoint(), transport, transports);
            }
        }
    }

    /**
     * Adds all spirit tree teleports to viable transports.
     *
     * @param playerProperties Transports unlocked for player
     * @param transports       Map of transports to populate to
     */
    private static void addSpiritTrees(PlayerProperties playerProperties, HashMap<WorldPoint, List<Transport>> transports) {
        if (!playerProperties.isSpiritTreesUnlocked()) {
            return;
        }

        for (SpiritTree spiritTreeSource : SpiritTree.values()) {
            for (SpiritTree spiritTreeDestination : SpiritTree.values()) {
                if (spiritTreeSource.equals(spiritTreeDestination)) {
                    continue;
                }

                Transport transport = new Transport(spiritTreeSource.getWorldPoint(), spiritTreeDestination.getWorldPoint(), TransportType.SPIRIT_TREE);
                addTransport(spiritTreeSource.getWorldPoint(), spiritTreeDestination.getWorldPoint(), transport, transports);
            }
        }
    }
}
