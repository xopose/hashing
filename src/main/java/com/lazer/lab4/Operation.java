package com.lazer.lab4;

// Operation.java
public abstract class Operation {
    public final String replicaId;
    public final String opId;

    protected Operation(String replicaId, String opId) {
        this.replicaId = replicaId;
        this.opId = opId;
    }
}
