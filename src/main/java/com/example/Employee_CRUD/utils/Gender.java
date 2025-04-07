package com.example.Employee_CRUD.utils;

public enum Gender {
    MALE, FEMALE, OTHER;

    public static boolean isValidGender(String gender) {
        for (Gender validGender : Gender.values()) {
            if (validGender.name().equals(gender)) {
                return true;
            }
        }
        return false;
    }
}
