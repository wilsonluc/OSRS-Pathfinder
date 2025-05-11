package com.pathfinder.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utility class providing common IO operations.
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
}