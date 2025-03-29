package lab2;

import com.lazer.lab2.BTree;
import com.lazer.lab2.BTreeNode;

public class BTreeTest {
    public static void main(String[] args) {
        BTree bTree = new BTree(3);

        int[] keys = {10, 20, 5, 6, 12, 30, 7, 17};
        for (int key : keys) {
            bTree.insert(key);
        }

        for (int key : keys) {
            BTreeNode result = bTree.search(bTree.root, key);
            if (result != null) {
                System.out.println(key + " found");
            } else {
                System.out.println(key + " not found");
            }
        }

        int notFoundKey = 100;
        BTreeNode result = bTree.search(bTree.root, notFoundKey);
        if (result != null) {
            System.out.println(notFoundKey + " found");
        } else {
            System.out.println(notFoundKey + " not found");
        }
    }
}
