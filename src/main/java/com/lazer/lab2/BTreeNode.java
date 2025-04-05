package com.lazer.lab2;

import java.util.ArrayList;
import java.util.List;

public class BTreeNode {
    int t;
    public List<Integer> keys;
    public List<BTreeNode> children;
    public boolean leaf;

    BTreeNode(int t, boolean leaf) {
        this.t = t;
        this.leaf = leaf;
        this.keys = new ArrayList<>();
        this.children = new ArrayList<>();
    }

    int findKey(int key) {
        int idx = 0;
        while (idx < keys.size() && keys.get(idx) < key) {
            idx++;
        }
        return idx;
    }
}
