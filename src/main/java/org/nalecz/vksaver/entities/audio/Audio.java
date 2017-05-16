package org.nalecz.vksaver.entities.audio;

import lombok.Data;

@Data public class Audio {
    int duration;
    String artist;
    String title;
    String url;
}
