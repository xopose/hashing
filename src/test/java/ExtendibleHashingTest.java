import com.lazer.ExtendibleHashing;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ExtendibleHashingTest {
    private static final int DATA_SIZE = 1000;

    @Test
    void testInsertAndSearch() {
        ExtendibleHashing hashTable = new ExtendibleHashing(2);
        Set<String> testData = generateTestData(DATA_SIZE);

        for (String value : testData) {
            hashTable.insert(value);
        }

        for (String value : testData) {
            assertTrue(hashTable.search(value), "HashTable should contain " + value);
        }

        assertFalse(hashTable.search("not_existing_value"), "HashTable should not contain a non-inserted value");
    }

    @Test
    void testDelete() {
        ExtendibleHashing hashTable = new ExtendibleHashing(2);
        Set<String> testData = generateTestData(DATA_SIZE);

        for (String value : testData) {
            hashTable.insert(value);
        }

        int count = 0;
        for (String value : testData) {
            if (count % 2 == 0) { // Удаляем половину элементов
                hashTable.delete(value);
            }
            count++;
        }

        count = 0;
        for (String value : testData) {
            if (count % 2 == 0) {
                assertFalse(hashTable.search(value), "Deleted value should not be found: " + value);
            } else {
                assertTrue(hashTable.search(value), "Remaining values should still be found: " + value);
            }
            count++;
        }
    }

    @Test
    void testExpansion() {
        ExtendibleHashing hashTable = new ExtendibleHashing(2);
        Set<String> testData = generateTestData(DATA_SIZE);

        for (String value : testData) {
            hashTable.insert(value);
        }

        for (String value : testData) {
            assertTrue(hashTable.search(value), "All inserted values should be present after expansion: " + value);
        }

        assertFalse(hashTable.search("random_invalid_value"), "HashTable should not contain an invalid value");
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
