package com.teama.requestsubsystem;
// not currently used
public enum PriorityLevel {
    LOW(1), MEDIUM(2), HIGH(3);

    private final int value;

    PriorityLevel(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
