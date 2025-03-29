package com.lazer.lab2.KDTREE;

public class KdNode {
    Point point;
    KdNode left, right;

    public KdNode(Point point) {
        this.point = point;
        this.left = this.right = null;
    }
}
