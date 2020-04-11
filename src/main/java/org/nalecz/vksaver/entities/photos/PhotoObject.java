package org.nalecz.vksaver.entities.photos;

import lombok.Data;
import lombok.Setter;

@Data public class PhotoObject {
    private int date;
    private int width;
    private int height;
    private float lat;
    private float lng;
    private String text;
    private String src;

    public void setSrc(String src) {
        this.src = src;
    }
}
