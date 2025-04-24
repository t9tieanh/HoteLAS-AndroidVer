package com.example.hotelas.model.common;

public class BillingItem {
    public enum Type { ORIGINAL_PRICE, DISCOUNT }

    private Type type;
    private String label;
    private String value;

    public BillingItem(Type type, String label, String value) {
        this.type = type;
        this.label = label;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }
}

