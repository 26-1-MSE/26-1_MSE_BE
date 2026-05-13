package com.ajou.pettown.pet;

// Maps pet acquisition index (1-based) to a fixed pet name, regardless of pet type.
public class PetNameMapper {

    private static final String[] NAMES = {"Scout", "Clover", "Rusty", "Daisy"};

    public static String getName(Integer petIndex) {
        if (petIndex == null || petIndex < 1 || petIndex > NAMES.length) {
            return "Unknown";
        }
        return NAMES[petIndex - 1]; // 1-based → 0-based
    }
}
