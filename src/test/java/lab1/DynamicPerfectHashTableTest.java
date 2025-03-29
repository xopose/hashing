package lab1;

import com.lazer.lab1.DynamicPerfectHashTable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class DynamicPerfectHashTableTest {
    private static final int DATA_SIZE = 10000;

    @Test
    void testInsertAndContains() {
        DynamicPerfectHashTable hashTable = new DynamicPerfectHashTable(DATA_SIZE);
        Set<String> testData = generateTestData(DATA_SIZE);

        for (String value : testData) {
            hashTable.insert(value);
        }

        for (String value : testData) {
            assertTrue(hashTable.contains(value), "HashTable should contain " + value);
        }

        assertFalse(hashTable.contains("1000000"), "HashTable should not contain a non-inserted value");
    }

    @Test
    void testRemove() {
        DynamicPerfectHashTable hashTable = new DynamicPerfectHashTable(DATA_SIZE);
        Set<String> testData = generateTestData(DATA_SIZE);

        for (String value : testData) {
            hashTable.insert(value);
        }

        int count = 0;
        for (String value : testData) {
            if (count % 2 == 0) {
                hashTable.remove(value);
            }
            count++;
        }

        count = 0;
        for (String value : testData) {
            if (count % 2 == 0) {
                assertFalse(hashTable.contains(value), "HashTable should not contain removed value " + value);
            } else {
                assertTrue(hashTable.contains(value), "HashTable should still contain " + value);
            }
            count++;
        }
    }

    @Test
    void testRemoveNonExistingElement() {
        DynamicPerfectHashTable hashTable = new DynamicPerfectHashTable(DATA_SIZE);
        Set<String> testData = generateTestData(DATA_SIZE);

        for (String value : testData) {
            hashTable.insert(value);
        }

        assertFalse(hashTable.contains("9999999"), "Non-existing value should not be found");

        hashTable.remove("9999999");

        for (String value : testData) {
            assertTrue(hashTable.contains(value), "Existing values should still be in the hash table");
        }
    }

    @Test
    void testEmptyTable() {
        DynamicPerfectHashTable hashTable = new DynamicPerfectHashTable(DATA_SIZE);
        assertFalse(hashTable.contains("10"));
        assertFalse(hashTable.contains("100"));
        assertFalse(hashTable.contains("999"));
    }

    @Test
    void testInsertDuplicateElements() {
        DynamicPerfectHashTable hashTable = new DynamicPerfectHashTable(DATA_SIZE);
        String testValue = "5000";

        hashTable.insert(testValue);
        hashTable.insert(testValue);

        assertTrue(hashTable.contains(testValue), "HashTable should contain inserted value");

        hashTable.remove(testValue);
        assertFalse(hashTable.contains(testValue), "HashTable should not contain removed value");
    }

    @Test
    void testRebuildWithCollisions() {
        DynamicPerfectHashTable hashTable = new DynamicPerfectHashTable(DATA_SIZE);
        Set<String> testData = generateTestData(DATA_SIZE);

        for (String value : testData) {
            hashTable.insert(value);
        }

        int removeCount = 0;
        for (String value : testData) {
            if (removeCount < DATA_SIZE / 10) { // Удаляем 10% элементов
                hashTable.remove(value);
            }
            removeCount++;
        }

        removeCount = 0;
        for (String value : testData) {
            if (removeCount < DATA_SIZE / 10) {
                assertFalse(hashTable.contains(value), "Removed value should not be in the hash table");
            } else {
                assertTrue(hashTable.contains(value), "Remaining values should still be in the hash table");
            }
            removeCount++;
        }
    }

    private Set<String> generateTestData(int size) {
        Set<String> data = new HashSet<>();
        Random random = new Random();
        while (data.size() < size) {
            data.add(String.valueOf(random.nextInt(Integer.MAX_VALUE)));
        }
        return data;
    }
}
