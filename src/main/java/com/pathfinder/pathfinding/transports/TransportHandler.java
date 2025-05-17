package com.pathfinder.pathfinding.transports;

import com.pathfinder.enums.TransportType;
import com.pathfinder.pathfinding.PlayerProperties;
import com.pathfinder.pathfinding.WorldPointPair;
import com.pathfinder.pathfinding.node.NodeEdge;
import com.pathfinder.pathfinding.requirement.DiaryReq;
import com.pathfinder.pathfinding.requirement.QuestReq;
import com.pathfinder.pathfinding.requirement.SkillReq;
import com.pathfinder.util.Util;
import lombok.Setter;
import net.runelite.api.coords.WorldPoint;

import java.io.InputStream;
import java.util.*;

public class TransportHandler {
    /**
     * File name used for reading in transports (located in resources).
     */
    private static final String TRANSPORTS_DIR = "transports.csv";

    /**
     * Properties of player (e.g., skills, quests, diaries)
     */
    @Setter
    private static PlayerProperties playerProperties;

    /**
     * Transport which are possible, key representing source {@link WorldPoint}
     */
    private static final HashMap<WorldPoint, List<Transport>> transports = new HashMap<>();

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
    public static HashMap<WorldPoint, List<Transport>> loadAllFromResources() {
        addCsvTransports();
        addFairyRings();
        addSpiritTrees();

        return transports;
    }

    /**
     * Add transport to transports arraylist.
     *
     * @param sourceWP      The origin {@link WorldPoint} of the transport
     * @param destinationWP The destination {@link WorldPoint} of the transport
     * @param transport     Contains source & destination {@link WorldPoint}s, as well as transport type
     */
    private static void addTransport(WorldPoint sourceWP, WorldPoint destinationWP, Transport transport) {
        transports.computeIfAbsent(sourceWP, k -> new ArrayList<>()).add(transport);
        worldPointPairs.putIfAbsent(new WorldPointPair(sourceWP, destinationWP),
                new NodeEdge(sourceWP, destinationWP, TransportType.TRANSPORT));
    }

    /**
     * Parses the transports from the internal CSV resource file and populates the given maps.
     */
    private static void addCsvTransports() {
        InputStream inputStream = Transport.class.getClassLoader().getResourceAsStream(TRANSPORTS_DIR);
        if (inputStream == null) {
            return;
        }

        Scanner scanner = new Scanner(inputStream);
        scanner.nextLine(); // Skip first line
        while (scanner.hasNextLine()) {
            String transportLine = scanner.nextLine();
            addCsvTransport(transportLine);
        }
        scanner.close();
    }

    /**
     * Parses a single line from the transport CSV and adds the transport entry to the relevant maps.
     *
     * @param transportLine The raw CSV line describing the transport
     */
    private static void addCsvTransport(String transportLine) {
        if (transportLine.isEmpty() || transportLine.contains("#") || transportLine.equals(",,,,,,,,")) {
            return;
        }

        String[] splitString = transportLine.split(",", -1);

        WorldPoint sourceWP = Util.getWorldPoint(splitString[0]);
        WorldPoint destinationWP = Util.getWorldPoint(splitString[1]);
        String menuOption = splitString[2];
        int objectID = Integer.parseInt(splitString[4]);
        SkillReq skillReq = Util.getSkillReq(splitString[5]);
        QuestReq questReq = Util.getQuestReq(splitString[6]);
        DiaryReq diaryReq = Util.getDiaryReq(splitString[6]);

        // End early if skill reqs not met
        boolean skillReqsMet = skillReq == null || playerProperties.getSkillLevel(skillReq.skill()) < skillReq.level();
        if (!skillReqsMet) {
            return;
        }

        // End early if quest reqs not met
        boolean questReqsMet = questReq == null || playerProperties.getQuestCompleted(questReq.quest());
        if (!questReqsMet) {
            return;
        }

        // End early if diary reqs not met
        boolean diaryReqsMet = diaryReq == null || playerProperties.getDiaryCompleted(diaryReq.diary());
        if (!diaryReqsMet) {
            return;
        }

        Transport transport = new Transport(TransportType.TRANSPORT, destinationWP, sourceWP, objectID, menuOption);
        addTransport(sourceWP, destinationWP, transport);
    }

    /**
     * Add fairy rings to transports
     */
    private static void addFairyRings() {
        // End early if quest reqs not met
        if (!playerProperties.isFairyRingsUnlocked()) {
            return;
        }

        // TODO: Add dramen staff check
        for (FairyRing fairyRingSource : FairyRing.values()) {
            for (FairyRing fairyRingDestination : FairyRing.values()) {
                if (fairyRingSource.equals(fairyRingDestination)) {
                    continue;
                }

                WorldPoint sourceWP = fairyRingSource.getWorldPoint();
                WorldPoint destinationWP = fairyRingDestination.getWorldPoint();
                Transport transport = new Transport(TransportType.FAIRY_RING, destinationWP, sourceWP, null, null);
                addTransport(sourceWP, destinationWP, transport);
            }
        }
    }

    /**
     * Add spirit trees to transports
     */
    private static void addSpiritTrees() {
        // End early if quest reqs not met
        if (!playerProperties.isSpiritTreesUnlocked()) {
            return;
        }

        // TODO: Add POH tree check
        for (SpiritTree spiritTreeSource : SpiritTree.values()) {
            for (SpiritTree spiritTreeDestination : SpiritTree.values()) {
                if (spiritTreeSource.equals(spiritTreeDestination)) {
                    continue;
                }

                WorldPoint sourceWP = spiritTreeSource.getWorldPoint();
                WorldPoint destinationWP = spiritTreeDestination.getWorldPoint();
                Transport transport = new Transport(TransportType.SPIRIT_TREE, destinationWP, sourceWP, spiritTreeSource.getId(), spiritTreeSource.getMenuOption());
                addTransport(sourceWP, destinationWP, transport);
            }
        }
    }


}
