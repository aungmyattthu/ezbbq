package com.amt.bbq.model.entity.consts;

public enum OrderStatus {
    PENDING("Pending", "warning"),
    DELIVERED("Delivered", "info");

    private final String displayName;
    private final String colorCode;

    OrderStatus(String displayName, String colorCode) {
        this.displayName = displayName;
        this.colorCode = colorCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getColorCode() {
        return colorCode;
    }
}