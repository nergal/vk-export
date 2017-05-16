package org.nalecz.vksaver.entities.photos;

import lombok.Data;

import java.util.List;

@Data public class AlbumObject {
    private String title;
    private int created;
    private String description;
    private int updated;

    private List<PhotoObject> photos;

    public void setPhotos(List<PhotoObject> photos) {
        this.photos = photos;
    }
}
