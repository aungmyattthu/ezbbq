package com.amt.bbq.model.entity.consts;

public enum OrderStatus {
    PENDING("Pending", "warning"),
    PREPARING("Preparing", "primary"),
    COMPLETED("Completed", "success"),
    CANCELLED("Cancelled", "error"),
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

    public boolean isTerminalState() {
        return this == COMPLETED || this == CANCELLED || this == DELIVERED;
    }
}