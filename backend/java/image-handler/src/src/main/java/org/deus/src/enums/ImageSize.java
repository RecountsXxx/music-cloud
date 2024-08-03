package org.deus.src.enums;

public enum ImageSize {
    SMALL("small"), MEDIUM("medium"), LARGE("large");

    private final String abbreviation;

    ImageSize(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    @Override
    public String toString() {
        return abbreviation;
    }
}