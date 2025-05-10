package com.lazer.lab4;

public class InsertOperation extends Operation {
    public final String parentId;
    public final String elementId;
    public final char value;

    public InsertOperation(String replicaId, String opId, String parentId, String elementId, char value) {
        super(replicaId, opId);
        this.parentId = parentId;
        this.elementId = elementId;
        this.value = value;
    }
}

