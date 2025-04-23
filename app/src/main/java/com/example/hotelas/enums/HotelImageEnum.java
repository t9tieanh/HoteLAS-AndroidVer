package com.example.hotelas.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum HotelImageEnum {
    LOBBY("Sảnh chờ"),
    OUTSIDE("Bên ngoài"),
    INSIDE("Bên trong"),
    AVARTAR("Hình đại diện"),
    RESTAURANT("Nhà hàng"),
    POOL("Hồ bơi"),
    ROOM("Phòng nghỉ"),
    GYM("Phòng gym"),
    BAR("Quầy bar"),
    RECEPTION("Quầy lễ tân"),
    BATHROOM("Phòng tắm"),
    CONFERENCE("Phòng hội nghị"),
    SPA("Spa"),
    PARKING("Bãi đỗ xe"),
    VIEW("Cảnh quan");

    private final String description;

    HotelImageEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @JsonValue
    public String getValue() {
        return description;
    }

    @JsonCreator
    public static HotelImageEnum fromValue(String value) {
        return Arrays.stream(HotelImageEnum.values())
                .filter(e -> e.description.equalsIgnoreCase(value))
                .findFirst()
                .orElse(HotelImageEnum.AVARTAR); // <-- dùng orElse với giá trị
    }
}

