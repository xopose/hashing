package com.lazer.lab4;

public class RGAElement {
    public final String id;
    public final String parentId;
    public final char value;
    public boolean isDeleted;

    public RGAElement(String id, String parentId, char value) {
        this.id = id;
        this.parentId = parentId;
        this.value = value;
        this.isDeleted = false;
    }

    @Override
    public String toString() {
        return isDeleted ? "" : Character.toString(value);
    }
}
