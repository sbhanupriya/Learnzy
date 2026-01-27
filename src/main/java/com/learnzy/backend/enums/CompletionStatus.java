package com.learnzy.backend.enums;

public enum CompletionStatus {
    COMPLETED("completed"),
    UN_COMPLETED("un_completed");

    private String value;
    CompletionStatus(String value){
        this.value = value;
    }
}
