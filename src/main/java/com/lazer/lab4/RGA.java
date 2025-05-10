package com.lazer.lab4;

import java.util.*;

public class RGA {
    private final String replicaId;
    private int localCounter = 0;
    final Map<String, RGAElement> elements = new HashMap<>();
    private final Map<String, List<String>> childMap = new HashMap<>();
    private final String ROOT_ID = "ROOT";

    public RGA(String replicaId) {
        this.replicaId = replicaId;
        elements.put(ROOT_ID, new RGAElement(ROOT_ID, null, '\0'));
    }

    private String nextId() {
        return replicaId + "-" + (++localCounter);
    }

    public InsertOperation localInsert(String parentId, char value) {
        String elementId = nextId();
        InsertOperation op = new InsertOperation(replicaId, elementId, parentId, elementId, value);
        applyInsert(op);
        return op;
    }

    public DeleteOperation localDelete(String elementId) {
        String opId = nextId();
        DeleteOperation op = new DeleteOperation(replicaId, opId, elementId);
        applyDelete(op);
        return op;
    }

    public void applyInsert(InsertOperation op) {
        if (!elements.containsKey(op.parentId)) return;
        if (elements.containsKey(op.elementId)) return;

        RGAElement elem = new RGAElement(op.elementId, op.parentId, op.value);
        elements.put(op.elementId, elem);

        childMap.computeIfAbsent(op.parentId, k -> new ArrayList<>()).add(op.elementId);
    }

    public void applyDelete(DeleteOperation op) {
        RGAElement elem = elements.get(op.elementId);
        if (elem != null) {
            elem.isDeleted = true;
        }
    }

    public String getText() {
        StringBuilder sb = new StringBuilder();
        traverse(ROOT_ID, sb);
        return sb.toString();
    }

    private void traverse(String parentId, StringBuilder sb) {
        List<String> children = childMap.getOrDefault(parentId, new ArrayList<>());
        children.sort(Comparator.naturalOrder());
        for (String childId : children) {
            RGAElement elem = elements.get(childId);
            if (elem != null && !elem.isDeleted) {
                sb.append(elem.value);
            }
            traverse(childId, sb);
        }
    }
}

