package lab2;

import com.lazer.lab2.KDTREE.KdTree;
import com.lazer.lab2.KDTREE.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class KdTreeTest {

    private KdTree tree;

    @BeforeEach
    public void setUp() {
        tree = new KdTree();
        tree.insert(new Point(3, 6));
        tree.insert(new Point(17, 15));
        tree.insert(new Point(13, 15));
        tree.insert(new Point(6, 12));
        tree.insert(new Point(9, 1));
        tree.insert(new Point(2, 7));
        tree.insert(new Point(10, 19));
    }

    @Test
    public void testInsert() {
        assertNotNull(tree);
    }

    @Test
    public void testFindNearest() {
        Point target = new Point(10, 10);
        Point nearest = tree.findNearest(target);
        assertNotNull(nearest);
        assertEquals("(13, 15)", nearest.toString(), "Nearest point should be (13, 15)");
    }

    @Test
    public void testNearestToPointOutside() {
        Point target = new Point(100, 100);
        Point nearest = tree.findNearest(target);
        assertNotNull(nearest);
        assertEquals("(17, 15)", nearest.toString(), "Nearest point should be (17, 15)");
    }

    @Test
    public void testFindNearestForEmptyTree() {
        KdTree emptyTree = new KdTree();
        Point target = new Point(5, 5);
        Point nearest = emptyTree.findNearest(target);
        assertNull(nearest, "Nearest point in an empty tree should be null");
    }

    @Test
    public void testInsertAndFind() {
        tree.insert(new Point(7, 3));
        Point target = new Point(7, 3);
        Point nearest = tree.findNearest(target);
        assertNotNull(nearest);
        assertEquals("(7, 3)", nearest.toString(), "Nearest point should be (7, 3)");
    }
}
