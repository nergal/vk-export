package org.nalecz.vksaver.entities.video;

import lombok.Data;

@Data public class Video {
    private String title;
    private String description;
    private int duration;
    private String preview;
    private String player;
    private int views;
    private int comments;
}
