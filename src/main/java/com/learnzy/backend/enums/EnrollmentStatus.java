package com.learnzy.backend.enums;

public enum EnrollmentStatus {

    ENROLLED("enrolled"),
    HOLD("hold");

    private String value;
    EnrollmentStatus(String value){
        this.value = value;
    }
}
