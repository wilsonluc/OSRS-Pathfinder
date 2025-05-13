package com.pathfinder.util;

import net.runelite.api.coords.WorldPoint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utility class providing common operations.
 */
public class Util {
    /**
     * Reads all bytes from the provided {@link InputStream} until end-of-stream is reached.
     *
     * @param in The input stream to read from. Must not be {@code null}.
     * @return A byte array containing all data read from the stream.
     * @throws IOException If an I/O error occurs while reading the stream.
     */
    public static byte[] readAllBytes(InputStream in) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        while (true) {
            int read = in.read(buffer, 0, buffer.length);

            if (read == -1) {
                return result.toByteArray();
            }

            result.write(buffer, 0, read);
        }
    }

    /**
     * Convert to {@link WorldPoint} from String.
     *
     * @param worldPointString String version of {@link WorldPoint}
     * @return {@link WorldPoint} representation of worldPointString
     */
    public static WorldPoint getWorldPoint(String worldPointString) {
        try {
            String[] sourceSplit = worldPointString.split(" ");
            return new WorldPoint(Integer.parseInt(sourceSplit[0]), Integer.parseInt(sourceSplit[1]), Integer.parseInt(sourceSplit[2]));
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error: Not enough values in the source string.");
        } catch (NumberFormatException e) {
            System.err.println("Error: One of the values is not a valid integer.");
        } catch (Exception e) {
            System.err.println("Unexpected error occurred.");
        }
        return null;
    }
}