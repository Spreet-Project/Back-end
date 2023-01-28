package com.team1.spreet.domain.shorts.model;

public enum Category {
    RAP("RAP"),
    DJ("DJ"),
    BEAT_BOX("BEAT_BOX"),
    STREET_DANCE("STREET_DANCE"),
    GRAFFITI("GRAFFITI"),
    ETC("ETC");

    private final String category;

    Category(String category) {
        this.category = category;
    }

    public String value() {
        return category;
    }
}