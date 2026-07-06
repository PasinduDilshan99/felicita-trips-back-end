package com.felicita.model.enums;

public enum MimeTypes {

    PDF("application/pdf"),
    JPEG("image/jpeg"),
    JPG("image/jpg"),
    PNG("image/png"),
    GIF("image/gif"),
    MP4("video/mp4"),
    TXT("text/plain"),
    JSON("application/json"),
    XML("application/xml");

    private final String value;

    MimeTypes(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}