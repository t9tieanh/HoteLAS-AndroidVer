package com.example.hotelas.model.common;

import java.io.Serializable;

public class Images implements Serializable {
    private int imagesId;

    public Images(int imagesId) {
        this.imagesId = imagesId;
    }

    public int getImagesId() {
        return imagesId;
    }

    public void setImagesId(int imagesId) {
        this.imagesId = imagesId;
    }
}