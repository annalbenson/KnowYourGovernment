package com.annabenson.knowyourgovernment;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Anna on 3/19/2018.
 */

// Purpose: Hold Official Data in the RecyclerView
    // Dev Plan 1b)

public class Official implements Serializable {

    // Note: Defaults will be "No Data Provided" for all but Party, "Unknown" for Party
    public static final String NO_DATA = "No Data Provided";
    public static final String UNKNOWN = "Unknown";

    // In Recycler View Entry
    private String name;
    private String office;
    private String party;

    // In Official Detail Activity
    private String address;
    // Note: "There may be more than one -- just use the first entry"
    private String phone;
    private String url;
    private String email;

    private String photoUrl;
    private String googleplus;
    private String facebook;
    private String twitter;
    private String youtube;

    public Official() {
        this.name = NO_DATA;
        this.office = NO_DATA;
        this.party = UNKNOWN;
        this.address = NO_DATA;
        this.phone = NO_DATA;
        this.url = NO_DATA;
        this.email = NO_DATA;
        this.photoUrl = NO_DATA;
        this.googleplus = NO_DATA;
        this.facebook = NO_DATA;
        this.twitter = NO_DATA;
        this.youtube = NO_DATA;
    }

    public Official(String name, String office, String party, String address, String phone, String url, String email, String photoUrl, String googleplus, String facebook, String twitter, String youtube) {
        this.name = name;
        this.office = office;
        this.party = party;
        this.address = address;
        this.phone = phone;
        this.url = url;
        this.email = email;
        this.photoUrl = photoUrl;
        this.googleplus = googleplus;
        this.facebook = facebook;
        this.twitter = twitter;
        this.youtube = youtube;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getGoogleplus() {
        return googleplus;
    }

    public void setGoogleplus(String googleplus) {
        this.googleplus = googleplus;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    @Override
    public String toString() {
        return "Official{" +
                "name='" + name + '\'' +
                ", office='" + office + '\'' +
                ", party='" + party + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", url='" + url + '\'' +
                ", email='" + email + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", googleplus='" + googleplus + '\'' +
                ", facebook='" + facebook + '\'' +
                ", twitter='" + twitter + '\'' +
                ", youtube='" + youtube + '\'' +
                '}';
    }
}
