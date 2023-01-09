package com.team1.spreet.entity;

public enum Category {
    RAP("RAP"),
    DJ("DJ"),
    BEAT_BOX("BEAT_BOX"),
    STREET_DANCE("STREET_DANCE"),
    GRAVITY("GRAVITY"),
    ETC("ETC");

    private final String category;

    Category(String category) {
        this.category = category;
    }

    public String value() {
        return category;
    }
}