package com.pathfinder.pathfinding.transports;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.Quest;
import net.runelite.api.coords.WorldPoint;

@AllArgsConstructor
@Getter
public enum FairyRing {
    AIQ("AIQ", new WorldPoint(2996, 3114, 0)),
    AIR("AIR", new WorldPoint(2700, 3247, 0)),
    AJQ("AJQ", new WorldPoint(2735, 5221, 0), Quest.DEATH_TO_THE_DORGESHUUN),
    AJR("AJR", new WorldPoint(2780, 3613, 0)),
    AJS("AJS", new WorldPoint(2500, 3896, 0)),
    AJP("AJP", new WorldPoint(1651, 3010, 0), Quest.CHILDREN_OF_THE_SUN),
    AKP("AKP", new WorldPoint(3284, 2706, 0), Quest.BENEATH_CURSED_SANDS),
    AKQ("AKQ", new WorldPoint(2319, 3619, 0)),
    AKR("AKR", new WorldPoint(1826, 3540, 0)),
    AKS("AKS", new WorldPoint(2571, 2956, 0)),
    ALP("ALP", new WorldPoint(2503, 3636, 0)),
    ALQ("ALQ", new WorldPoint(3597, 3495, 0)),
    ALR("ALR", new WorldPoint(3059, 4875, 0)),
    ALS("ALS", new WorldPoint(2644, 3495, 0)),

    // B
    BIP("BIP", new WorldPoint(3410, 3324, 0)),
    BIQ("BIQ", new WorldPoint(3251, 3095, 0)),
    BIS("BIS", new WorldPoint(2635, 3266, 0)),
    BJP("BJP", new WorldPoint(2267, 2976, 0)),
    BJR("BJR", new WorldPoint(2650, 4730, 0), Quest.HOLY_GRAIL),
    BJS("BJS", new WorldPoint(2150, 3070, 0), Quest.REGICIDE),
    BKP("BKP", new WorldPoint(2385, 3035, 0)),
    BKQ("BKQ", new WorldPoint(3041, 4532, 0)),
    BKR("BKR", new WorldPoint(3469, 3431, 0)),
    BKS("BKS", new WorldPoint(2412, 4434, 0)),
    BLP("BLP", new WorldPoint(2437, 5126, 0)),
    //    BLQ("BLQ", new WorldPoint(3572, 4372, 0), Quest.HOPESPEARS_WILL),
    BLR("BLR", new WorldPoint(2740, 3351, 0)),
    BLS("BLS", new WorldPoint(1295, 3493, 0)),

    // C
    CIP("CIP", new WorldPoint(2513, 3884, 0), Quest.THE_FREMENNIK_TRIALS),
    CIQ("CIQ", new WorldPoint(2528, 3127, 0)),
    CIR("CIR", new WorldPoint(1302, 3762, 0)),
    CIS("CIS", new WorldPoint(1639, 3868, 0)),
    CJR("CJR", new WorldPoint(2705, 3576, 0)),
    CKP("CKP", new WorldPoint(2075, 4848, 0)),
    CKR("CKR", new WorldPoint(2801, 3003, 0)),
    CKS("CKS", new WorldPoint(3447, 3470, 0)),
    CLP("CLP", new WorldPoint(3082, 3206, 0)),
    CLR("CLR", new WorldPoint(2740, 2738, 0), Quest.MONKEY_MADNESS_I),
    CLS("CLS", new WorldPoint(2682, 3081, 0)),

    // D
    DIP("DIP", new WorldPoint(3037, 4763, 0)),
    DIQ("DIQ", new WorldPoint(2027, 5700, 0)),
    DIR("DIR", new WorldPoint(3038, 5348, 0)),
    DIS("DIS", new WorldPoint(3108, 3149, 0)),
    DJP("DJP", new WorldPoint(2658, 3230, 0)),
    DJR("DJR", new WorldPoint(1455, 3658, 0)),
    DKP("DKP", new WorldPoint(2900, 3111, 0)),
    DKR("DKR", new WorldPoint(3129, 3496, 0)),
    DKS("DKS", new WorldPoint(2744, 3719, 0)),
    DLQ("DLQ", new WorldPoint(3423, 3016, 0)),
    DLR("DLR", new WorldPoint(2213, 3099, 0)),
    DLS("DLS", new WorldPoint(3447, 9824, 0), Quest.IN_SEARCH_OF_THE_MYREQUE),
    ;

    private final String code;
    private final WorldPoint worldPoint;
    private final Quest questReq;

    FairyRing(String code, WorldPoint worldPoint) {
        this(code, worldPoint, null);
    }
}
