package org.deus.src.enums;

public enum AudioQuality {
    LOW("low"), MEDIUM("medium"), HIGH("high");

    private final String abbreviation;

    AudioQuality(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    @Override
    public String toString() {
        return abbreviation;
    }
}
