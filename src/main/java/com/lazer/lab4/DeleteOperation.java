package com.lazer.lab4;

public class DeleteOperation extends Operation {
    public final String elementId;

    public DeleteOperation(String replicaId, String opId, String elementId) {
        super(replicaId, opId);
        this.elementId = elementId;
    }
}
