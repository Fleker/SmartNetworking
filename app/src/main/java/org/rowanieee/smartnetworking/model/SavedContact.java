package org.rowanieee.smartnetworking.model;

import android.content.ContentValues;
import android.content.Context;

import com.felkertech.settingsmanager.SettingsManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.rowanieee.smartnetworking.R;
import org.rowanieee.smartnetworking.database.PersonQueryContract;

/**
 * Created by Nick on 5/15/2016.
 */
public class SavedContact {
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHOTO = "photo";
    public static final String KEY_ABOUTME = "aboutme";
    public static final String KEY_CONNECTIONS = "connections";

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

    public SavedContact(JSONObject inputObject) throws JSONException {
        this(inputObject.getString(KEY_NAME),
                inputObject.getString(KEY_EMAIL),
                inputObject.getString(KEY_PHOTO),
                inputObject.getString(KEY_ABOUTME),
                inputObject.getJSONObject(KEY_CONNECTIONS));
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

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KEY_NAME, getName());
            jsonObject.put(KEY_ABOUTME, getAboutme());
            jsonObject.put(KEY_EMAIL, getEmail());
            jsonObject.put(KEY_PHOTO, getPhotoBase64());
            jsonObject.put(KEY_CONNECTIONS, getConnections());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static SavedContact getMyself(Context c) {
        SettingsManager sm = new SettingsManager(c);
        try {
            return new SavedContact(new JSONObject(sm.getString(c.getString(R.string.sm_contactinfo))));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(PersonQueryContract.PersonQueryEntry.COLUMN_NAME, getName());
        values.put(PersonQueryContract.PersonQueryEntry.COLUMN_EMAIL, getEmail());
        values.put(PersonQueryContract.PersonQueryEntry.COLUMN_PHOTO, getPhotoBase64());
        values.put(PersonQueryContract.PersonQueryEntry.COLUMN_QRURL, getAboutme());
        if(getConnections() != null)
            values.put(PersonQueryContract.PersonQueryEntry.COLUMN_CONNECTIONS, getConnections().toString());
        return values;
    }
}
