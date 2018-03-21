package com.annabenson.knowyourgovernment;

import java.util.Arrays;

/**
 * Created by Anna on 3/19/2018.
 */

// Purpose: Hold Official Data in the RecyclerView
    // Dev Plan 1b)

public class Official {

    // Note: Defaults with be "No Data Provided"

    // In Recycler View Entry
    private String name;
    private String office;
    private String party;

    // In Official Detail Activity
    private String address;
    private String [] phones;
    private String [] urls;
    private String [] emails;

    private String photoUrl;
    private String [] channels; // social media type and id

    public Official(String name, String office, String party, String address, String[] phones, String[] urls, String[] emails, String photoUrl, String[] channels) {
        this.name = name;
        this.office = office;
        this.party = party;
        this.address = address;
        this.phones = phones;
        this.urls = urls;
        this.emails = emails;
        this.photoUrl = photoUrl;
        this.channels = channels;
    }

    public  Official(String name, String office, String party){
            setName(name); setOffice(office); setParty(party);
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String[] getPhones() {
        return phones;
    }

    public void setPhones(String[] phones) {
        this.phones = phones;
    }

    public String[] getUrls() {
        return urls;
    }

    public void setUrls(String[] urls) {
        this.urls = urls;
    }

    public String[] getEmails() {
        return emails;
    }

    public void setEmails(String[] emails) {
        this.emails = emails;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String[] getChannels() {
        return channels;
    }

    public void setChannels(String[] channels) {
        this.channels = channels;
    }

    @Override
    public String toString() {
        return "Official{" +
                "name='" + name + '\'' +
                ", office='" + office + '\'' +
                ", address='" + address + '\'' +
                ", party='" + party + '\'' +
                ", phones=" + Arrays.toString(phones) +
                ", urls=" + Arrays.toString(urls) +
                ", emails=" + Arrays.toString(emails) +
                ", photoUrl='" + photoUrl + '\'' +
                ", channels=" + Arrays.toString(channels) +
                '}';
    }
}
