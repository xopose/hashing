package lab2;

import com.lazer.lab2.BTree;
import com.lazer.lab2.BTreeNode;
import org.junit.Test;

import static org.junit.Assert.*;

public class BTreeTest {
    @Test
    public void testSearchExistingKey() {
        BTree tree = new BTree(3);
        for (int i = 10; i <= 100; i += 10) {
            tree.insert(i);
        }

        BTreeNode result = tree.search(tree.root, 50);
        assertNotNull("Key should be found", result);
        assertTrue("Key should exist", result.keys.contains(50));
    }

    @Test
    public void testSearchNonExistingKey() {
        BTree tree = new BTree(3);
        for (int i = 10; i <= 100; i += 10) {
            tree.insert(i);
        }

        BTreeNode result = tree.search(tree.root, 55);
        assertNull("Key shouldn`t be found", result);
    }

    @Test
    public void testDeleteKey() {
        BTree tree = new BTree(3);
        for (int i = 1; i <= 20; i++) {
            tree.insert(i);
        }

        tree.delete(10);
        BTreeNode result = tree.search(tree.root, 10);
        assertNull("Key should be deleted", result);

        validateBTreeStructure(tree.root, 3, 0, new int[]{-1}, true);
    }
    @Test
    public void testDeleteRootKey() {
        BTree tree = new BTree(3);
        tree.insert(1);
        tree.insert(2);
        tree.insert(3);
        tree.insert(4);
        tree.insert(5);

        int rootKey = tree.root.keys.get(0);
        tree.delete(rootKey);
        BTreeNode result = tree.search(tree.root, rootKey);
        assertNull("Root key should be deleted", result);

        validateBTreeStructure(tree.root, 3, 0, new int[]{-1}, true);
    }

    @Test
    public void testInsertDeleteReinsert() {
        BTree tree = new BTree(3);

        for (int i = 1; i <= 20; i++) {
            tree.insert(i);
        }
        for (int i = 1; i <= 10; i++) {
            tree.delete(i);
        }
        for (int i = 1; i <= 10; i++) {
            tree.insert(i);
        }
        for (int i = 1; i <= 20; i++) {
            BTreeNode result = tree.search(tree.root, i);
            assertNotNull("Key " + i + " should exist after reinsertion", result);
        }

        validateBTreeStructure(tree.root, 3, 0, new int[]{-1}, true);
    }

    private void validateBTreeStructure(BTreeNode node, int t, int currentLevel, int[] leafLevel, boolean isRoot) {
        int n = node.keys.size();

        if (!isRoot) {
            assertTrue("Node has too few keys", n >= t - 1);
        }
        assertTrue("Node has too many keys", n <= 2 * t - 1);

        for (int i = 0; i < n - 1; i++) {
            assertTrue("Keys not in order", node.keys.get(i) < node.keys.get(i + 1));
        }

        if (node.leaf) {
            if (leafLevel[0] == -1) {
                leafLevel[0] = currentLevel;
            } else {
                assertEquals("Leaves not at the same level", leafLevel[0], currentLevel);
            }
        } else {
            assertEquals("Invalid number of children", node.children.size(), n + 1);
            for (BTreeNode child : node.children) {
                validateBTreeStructure(child, t, currentLevel + 1, leafLevel, false);
            }
        }
    }

    @Test
    public void testBTreeStructureAfterInsertions() {
        int t = 3;
        BTree tree = new BTree(t);

        for (int i = 1; i <= 100; i++) {
            tree.insert(i);
        }

        validateBTreeStructure(tree.root, t, 0, new int[]{-1}, true);
    }
}
