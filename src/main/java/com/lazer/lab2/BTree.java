package com.lazer.lab2;

public class BTree {
    public BTreeNode root;
    int t;

    public BTree(int t) {
        this.t = t;
        this.root = new BTreeNode(t, true);
    }

    public BTreeNode search(BTreeNode node, int key) {
        int idx = node.findKey(key);

        if (idx < node.keys.size() && node.keys.get(idx) == key) {
            return node;
        }

        if (node.leaf) {
            return null;
        }

        return search(node.children.get(idx), key);
    }

    public void insert(int key) {
        BTreeNode root = this.root;

        if (root.keys.size() == 2 * t - 1) {
            BTreeNode newRoot = new BTreeNode(t, false);
            newRoot.children.add(root);
            splitChild(newRoot, 0, root);
            this.root = newRoot;
            insertNonFull(newRoot, key);
        } else {
            insertNonFull(root, key);
        }
    }

    private void insertNonFull(BTreeNode node, int key) {
        int i = node.keys.size() - 1;

        if (node.leaf) {
            node.keys.add(0);
            while (i >= 0 && key < node.keys.get(i)) {
                node.keys.set(i + 1, node.keys.get(i));
                i--;
            }
            node.keys.set(i + 1, key);
        } else {
            while (i >= 0 && key < node.keys.get(i)) {
                i--;
            }
            i++;

            if (node.children.get(i).keys.size() == 2 * t - 1) {
                splitChild(node, i, node.children.get(i));
                if (key > node.keys.get(i)) {
                    i++;
                }
            }
            insertNonFull(node.children.get(i), key);
        }
    }

    private void splitChild(BTreeNode parent, int i, BTreeNode y) {
        BTreeNode z = new BTreeNode(y.t, y.leaf);

        int median = y.keys.get(t - 1);

        for (int j = 0; j < t - 1; j++) {
            z.keys.add(y.keys.get(t + j));
        }

        for (int j = 0; j < t; j++) {
            y.keys.remove(y.keys.size() - 1);
        }

        if (!y.leaf) {
            for (int j = 0; j < t; j++) {
                z.children.add(y.children.get(t));
                y.children.remove(t);
            }
        }

        parent.children.add(i + 1, z);
        parent.keys.add(i, median);
    }

    public void delete(int key) {
        delete(root, key);
        if (root.keys.isEmpty() && !root.leaf) {
            root = root.children.get(0);
        }
    }

    private void delete(BTreeNode node, int key) {
        int idx = node.findKey(key);

        if (idx < node.keys.size() && node.keys.get(idx) == key) {
            if (node.leaf) {
                node.keys.remove(idx);
            } else {
                BTreeNode pred = node.children.get(idx);
                if (pred.keys.size() >= t) {
                    int predKey = getMax(pred);
                    node.keys.set(idx, predKey);
                    delete(pred, predKey);
                } else {
                    BTreeNode succ = node.children.get(idx + 1);
                    if (succ.keys.size() >= t) {
                        int succKey = getMin(succ);
                        node.keys.set(idx, succKey);
                        delete(succ, succKey);
                    } else {
                        mergeChildren(node, idx);
                        delete(pred, key);
                    }
                }
            }
        } else {
            if (node.leaf) {
                return;
            }

            boolean flag = (idx == node.keys.size());
            BTreeNode child = node.children.get(flag ? idx - 1 : idx);

            if (child.keys.size() < t) {
                fill(node, idx);
            }

            if (flag && idx > node.keys.size()) {
                delete(node.children.get(idx - 1), key);
            } else {
                delete(node.children.get(idx), key);
            }
        }
    }

    private void fill(BTreeNode node, int idx) {
        if (idx > 0 && node.children.get(idx - 1).keys.size() >= t) {
            borrowFromPrev(node, idx);
        } else if (idx < node.keys.size() && node.children.get(idx + 1).keys.size() >= t) {
            borrowFromNext(node, idx);
        } else {
            if (idx < node.keys.size()) {
                mergeChildren(node, idx);
            } else {
                mergeChildren(node, idx - 1);
            }
        }
    }

    private int getMin(BTreeNode node) {
        while (!node.leaf) {
            node = node.children.get(0);
        }
        return node.keys.get(0);
    }

    private int getMax(BTreeNode node) {
        while (!node.leaf) {
            node = node.children.get(node.keys.size());
        }
        return node.keys.get(node.keys.size() - 1);
    }

    private void mergeChildren(BTreeNode node, int idx) {
        BTreeNode child = node.children.get(idx);
        BTreeNode sibling = node.children.get(idx + 1);

        child.keys.add(node.keys.get(idx));
        child.keys.addAll(sibling.keys);
        if (!child.leaf) {
            child.children.addAll(sibling.children);
        }

        node.keys.remove(idx);
        node.children.remove(idx + 1);
    }

    private void borrowFromPrev(BTreeNode node, int idx) {
        BTreeNode child = node.children.get(idx);
        BTreeNode sibling = node.children.get(idx - 1);

        child.keys.add(0, node.keys.get(idx - 1));
        node.keys.set(idx - 1, sibling.keys.remove(sibling.keys.size() - 1));

        if (!child.leaf) {
            child.children.add(0, sibling.children.remove(sibling.children.size() - 1));
        }
    }

    private void borrowFromNext(BTreeNode node, int idx) {
        BTreeNode child = node.children.get(idx);
        BTreeNode sibling = node.children.get(idx + 1);

        child.keys.add(node.keys.get(idx));
        node.keys.set(idx, sibling.keys.remove(0));

        if (!child.leaf) {
            child.children.add(sibling.children.remove(0));
        }
    }
}

