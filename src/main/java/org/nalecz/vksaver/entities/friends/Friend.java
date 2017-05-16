package org.nalecz.vksaver.entities.friends;

import lombok.Data;

@Data public class Friend {
    private int id;
    private String firstName;
    private String lastName;
    private String bdate;
    private String city;
    private String country;
    private String photoMaxOrig;
    private String nickname;
    private String screenName;
    private String site;
}
