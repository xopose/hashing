package com.lazer.lab2.KDTREE;

public class KdTree {
    private KdNode root;

    public KdTree() {
        this.root = null;
    }

    public void insert(Point point) {
        root = insertRec(root, point, 0);
    }

    private KdNode insertRec(KdNode root, Point point, int depth) {
        if (root == null) {
            return new KdNode(point);
        }

        int axis = depth % 2;

        if (axis == 0) {
            if (point.x < root.point.x) {
                root.left = insertRec(root.left, point, depth + 1);
            } else {
                root.right = insertRec(root.right, point, depth + 1);
            }
        } else {
            if (point.y < root.point.y) {
                root.left = insertRec(root.left, point, depth + 1);
            } else {
                root.right = insertRec(root.right, point, depth + 1);
            }
        }

        return root;
    }

    public Point findNearest(Point target) {
        return findNearestRec(root, target, 0, null, Double.MAX_VALUE);
    }

    private Point findNearestRec(KdNode root, Point target, int depth, Point best, double bestDist) {
        if (root == null) {
            return best;
        }

        double dist = target.distance(root.point);
        if (dist < bestDist) {
            bestDist = dist;
            best = root.point;
        }

        int axis = depth % 2;
        KdNode next, other;

        if (axis == 0) {
            if (target.x < root.point.x) {
                next = root.left;
                other = root.right;
            } else {
                next = root.right;
                other = root.left;
            }
        } else {
            if (target.y < root.point.y) {
                next = root.left;
                other = root.right;
            } else {
                next = root.right;
                other = root.left;
            }
        }

        best = findNearestRec(next, target, depth + 1, best, bestDist);

        if ((axis == 0 && Math.abs(target.x - root.point.x) < bestDist) ||
                (axis == 1 && Math.abs(target.y - root.point.y) < bestDist)) {
            best = findNearestRec(other, target, depth + 1, best, bestDist);
        }

        return best;
    }

    public void printTree() {
        printRec(root, 0);
    }

    private void printRec(KdNode root, int depth) {
        if (root == null) return;

        System.out.println("Depth " + depth + ": " + root.point);
        printRec(root.left, depth + 1);
        printRec(root.right, depth + 1);
    }
}
