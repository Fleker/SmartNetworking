package org.rowanieee.smartnetworking.model;

import org.json.JSONObject;

/**
 * Created by Nick on 5/15/2016.
 */
public class SavedContact {
    private String name;
    private String email;
    private String photoBase64;
    /**
     * URL to the user's About.me page, to place in the QR code
     */
    private String aboutme;
    /**
     * A variable that can be expanded and extended to support any type of network in the future
     */
    private JSONObject connections;

    public SavedContact() {
    }

    public SavedContact(String name, String email, String photoBase64, String aboutme, JSONObject connections) {
        this.name = name;
        this.email = email;
        this.photoBase64 = photoBase64;
        this.aboutme = aboutme;
        this.connections = connections;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoBase64() {
        return photoBase64;
    }

    public void setPhotoBase64(String photoBase64) {
        this.photoBase64 = photoBase64;
    }

    public String getAboutme() {
        return aboutme;
    }

    public void setAboutme(String aboutme) {
        this.aboutme = aboutme;
    }

    public JSONObject getConnections() {
        return connections;
    }

    public void setConnections(JSONObject connections) {
        this.connections = connections;
    }
}
