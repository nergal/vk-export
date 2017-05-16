package org.nalecz.vksaver.entities.notes;

import lombok.Data;

@Data public class NoteObject {
    private String title;
    private String date;
    private String text;
    private String textWiki;
    private String viewUrl;
}
