package com.pathfinder.pathfinding.transports;

import com.pathfinder.enums.TransportType;
import lombok.Getter;
import net.runelite.api.coords.WorldPoint;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * This class represents a travel point between two WorldPoints.
 */
@Getter
public class Transport {
    private final TransportType transportType;
    private final WorldPoint destination;
    private final WorldPoint origin;
    private final Integer objectID;
    private final String menuOption;

    public Transport(TransportType transportType,
                     WorldPoint destination,
                     WorldPoint origin,
                     @Nullable Integer objectID,
                     @Nullable String menuOption) {
        this.transportType = transportType;
        this.destination = destination;
        this.origin = origin;
        this.objectID = objectID;
        this.menuOption = menuOption;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Transport)) return false;
        Transport other = (Transport) obj;
        return transportType == other.transportType &&
                destination.equals(other.destination) &&
                origin.equals(other.origin) &&
                (Objects.equals(objectID, other.objectID)) &&
                (Objects.equals(menuOption, other.menuOption));
    }

    @Override
    public int hashCode() {
        int result = transportType.hashCode();
        result = 31 * result + destination.hashCode();
        result = 31 * result + origin.hashCode();
        result = 31 * result + (objectID != null ? objectID.hashCode() : 0);
        result = 31 * result + (menuOption != null ? menuOption.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Transport{" +
                "transportType=" + transportType +
                ", destination=" + destination +
                ", origin=" + origin +
                ", objectID=" + objectID +
                ", menuOption='" + menuOption + '\'' +
                '}';
    }
}
