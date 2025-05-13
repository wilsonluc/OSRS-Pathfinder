package com.pathfinder.util;

import java.util.Arrays;

/**
 * A primitive-backed hash map optimised for storing values with {@code int} keys.
 *
 * @param <V> The type of values stored in the map
 */
public class PrimitiveIntHashMap<V> {
    private static final int MINIMUM_SIZE = 8;
    private static final int DEFAULT_BUCKET_SIZE = 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * Internal structure representing a key-value pair stored in the map.
     */
    private static class IntNode<V> {
        private final int key;
        private V value;

        private IntNode(int key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private IntNode<V>[][] buckets;
    private int size;
    private int capacity;
    private int maxSize;
    private int mask;
    private final float loadFactor;

    /**
     * Constructs a new {@code PrimitiveIntHashMap} with the specified initial capacity.
     *
     * @param initialSize Initial size hint for bucket allocation
     */
    public PrimitiveIntHashMap(int initialSize) {
        this(initialSize, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Constructs a new {@code PrimitiveIntHashMap} with the specified capacity and load factor.
     *
     * @param initialSize Initial size hint
     * @param loadFactor  Load factor threshold for resizing
     */
    public PrimitiveIntHashMap(int initialSize, float loadFactor) {
        if (loadFactor < 0.0f || loadFactor > 1.0f) {
            throw new IllegalArgumentException("Load factor must be between 0 and 1");
        }

        this.loadFactor = loadFactor;
        size = 0;
        setNewSize(initialSize);
        recreateArrays();
    }

    /**
     * @return The number of entries currently in the map
     */
    public int size() {
        return size;
    }

    /**
     * Retrieves a value by its integer key.
     *
     * @param key The key to lookup
     * @return The associated value, or {@code null} if not found
     */
    public V get(int key) {
        return getOrDefault(key, null);
    }

    /**
     * Retrieves a value by its integer key, or returns a default value if not found.
     *
     * @param key          The key to lookup
     * @param defaultValue The default value to return if not found
     * @return The found value, or {@code defaultValue} if not found
     */
    public V getOrDefault(int key, V defaultValue) {
        int bucket = getBucket(key);
        int index = bucketIndex(key, bucket);
        if (index == -1) {
            return defaultValue;
        }
        return buckets[bucket][index].value;
    }

    /**
     * Inserts or replaces a value for the given key.
     *
     * @param key   The integer key
     * @param value The value to insert
     * @throws IllegalArgumentException if {@code value} is {@code null}
     */
    public void put(int key, V value) {
        if (value == null) {
            throw new IllegalArgumentException("Cannot insert a null value");
        }

        int bucketIndex = getBucket(key);
        IntNode<V>[] bucket = buckets[bucketIndex];

        if (bucket == null) {
            buckets[bucketIndex] = createBucket(DEFAULT_BUCKET_SIZE);
            buckets[bucketIndex][0] = new IntNode<>(key, value);
            incrementSize();
            return;
        }

        for (int i = 0; i < bucket.length; ++i) {
            if (bucket[i] == null) {
                bucket[i] = new IntNode<>(key, value);
                incrementSize();
                return;
            } else if (bucket[i].key == key) {
                V previous = bucket[i].value;
                bucket[i].value = value;
                return;
            }
        }

        // No space in the bucket, grow it
        growBucket(bucketIndex)[bucket.length] = new IntNode<>(key, value);
        incrementSize();
    }

    /**
     * Clears all entries in the map.
     */
    public void clear() {
        size = 0;
        Arrays.fill(buckets, null);
    }

    /**
     * Custom hash function designed for good distribution with packed {@link net.runelite.api.coords.WorldPoint}s.
     *
     * @param value The integer key
     * @return A hash value
     */
    private static int hash(int value) {
        return value ^ (value >>> 5) ^ (value >>> 25);
    }

    /**
     * Computes the bucket index for the given key.
     */
    private int getBucket(int key) {
        return (hash(key) & 0x7FFFFFFF) & mask;
    }

    /**
     * Searches a bucket for a key and returns its index or -1.
     */
    private int bucketIndex(int key, int bucketIndex) {
        IntNode<V>[] bucket = buckets[bucketIndex];
        if (bucket == null) {
            return -1;
        }

        for (int i = 0; i < bucket.length; ++i) {
            if (bucket[i] == null) {
                break;
            }
            if (bucket[i].key == key) {
                return i;
            }
        }

        // Searched the bucket and found nothing
        return -1;
    }

    /**
     * Increments size and triggers rehashing if needed.
     */
    private void incrementSize() {
        size++;
        if (size >= capacity) {
            rehash();
        }
    }

    /**
     * Doubles the size of a bucket when it becomes full.
     */
    private IntNode<V>[] growBucket(int bucketIndex) {
        IntNode<V>[] oldBucket = buckets[bucketIndex];
        IntNode<V>[] newBucket = createBucket(oldBucket.length * 2);
        System.arraycopy(oldBucket, 0, newBucket, 0, oldBucket.length);
        buckets[bucketIndex] = newBucket;
        return newBucket;
    }

    /**
     * Calculates the next power of two greater than the given size.
     */
    private int getNewMaxSize(int size) {
        int nextPow2 = -1 >>> Integer.numberOfLeadingZeros(size);
        if (nextPow2 >= (Integer.MAX_VALUE >>> 1)) {
            return (Integer.MAX_VALUE >>> 1) + 1;
        }
        return nextPow2 + 1;
    }

    /**
     * Resizes and recalculates internal capacity and mask.
     */
    private void setNewSize(int size) {
        if (size < MINIMUM_SIZE) {
            size = MINIMUM_SIZE - 1;
        }

        maxSize = getNewMaxSize(size);
        mask = maxSize - 1;
        capacity = (int) (maxSize * loadFactor);
    }

    /**
     * Grows capacity during rehashing.
     */
    private void growCapacity() {
        setNewSize(maxSize);
    }

    /**
     * Rehashes the entire map into a larger backing array.
     */
    private void rehash() {
        growCapacity();

        IntNode<V>[][] oldBuckets = buckets;
        recreateArrays();

        for (IntNode<V>[] oldBucket : oldBuckets) {
            if (oldBucket == null) {
                continue;
            }

            for (IntNode<V> vIntNode : oldBucket) {
                if (vIntNode == null) {
                    break;
                }

                int bucketIndex = getBucket(vIntNode.key);
                IntNode<V>[] newBucket = buckets[bucketIndex];
                if (newBucket == null) {
                    newBucket = createBucket(DEFAULT_BUCKET_SIZE);
                    newBucket[0] = vIntNode;
                    buckets[bucketIndex] = newBucket;
                } else {
                    int bInd;
                    for (bInd = 0; bInd < newBucket.length; ++bInd) {
                        if (newBucket[bInd] == null) {
                            newBucket[bInd] = vIntNode;
                            break;
                        }
                    }

                    if (bInd >= newBucket.length) {
                        growBucket(bucketIndex)[newBucket.length] = vIntNode;
                        return;
                    }
                }
            }
        }
    }

    /**
     * Reinitialises the outer array of buckets.
     */
    @SuppressWarnings("unchecked")
    private void recreateArrays() {
        buckets = (IntNode<V>[][]) new IntNode[maxSize][];
    }

    /**
     * Creates a new empty bucket of the given size.
     */
    @SuppressWarnings("unchecked")
    private IntNode<V>[] createBucket(int size) {
        return (IntNode<V>[]) new IntNode[size];
    }
}
