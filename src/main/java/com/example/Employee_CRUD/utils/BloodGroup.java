package com.example.Employee_CRUD.utils;

public enum BloodGroup {
    A_POSITIVE,
    A_NEGATIVE,
    B_POSITIVE,
    B_NEGATIVE,
    O_POSITIVE,
    O_NEGATIVE,
    AB_POSITIVE,
    AB_NEGATIVE,
    OTHERS;

    public static boolean isValidBloodGroup(String bloodGroup) {
        for (BloodGroup validBloodGroup : BloodGroup.values()) {
            if (validBloodGroup.name().equals(bloodGroup)) {
                return true;
            }
        }
        return false;
    }
}