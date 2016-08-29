package org.rowanieee.smartnetworking.model;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

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
    public static final String KEY_COMPANY = "company";
    public static final String KEY_TITLE = "title";
    public static final String KEY_PERSONAL_STATEMENT = "personalStatement";
    public static final String KEY_CONNECTIONS = "connections";
    private static final String TAG = "SavedContact";

    private String name;
    private String email;
    private String photoBase64;
    private String company;
    private String title;
    private String personalStatement;

    private int databaseId;
    /**
     * URL to the user's About.me page, to place in the QR code
     */
    private String aboutme;
    /**
     * A variable that can be expanded and extended to support any type of network in the future
     */
    private JSONObject connections;

    public SavedContact() {
        //Creates a new SavedContact from scratch
        this("Me", "info@example.com", "", "http://about.me", "N/A", "", "Hi! I'm new!", new JSONObject());
    }
    public SavedContact(int putZeroHere) {
        this("", "", "", "", "", "", "", new JSONObject());
    }
    public SavedContact(JSONObject inputObject) throws JSONException {
        this(inputObject.getString(KEY_NAME),
                inputObject.getString(KEY_EMAIL),
                inputObject.getString(KEY_PHOTO),
                inputObject.getString(KEY_ABOUTME),
                inputObject.getString(KEY_COMPANY),
                inputObject.getString(KEY_TITLE),
                inputObject.getString(KEY_PERSONAL_STATEMENT),
                inputObject.getJSONObject(KEY_CONNECTIONS));
    }

    public SavedContact(String name, String email, String photoBase64, String aboutme, String company, String title, String personalStatement, JSONObject connections) {
        this.name = name;
        this.email = email;
        this.photoBase64 = photoBase64;
        this.aboutme = aboutme;
        this.connections = connections;
        this.company = company;
        this.title = title;
        this.personalStatement = personalStatement;
    }

    public SavedContact setDatabaseId(int databaseId) {
        this.databaseId = databaseId;
        return this;
    }

    public int getDatabaseId() {
        return databaseId;
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

    public String getTitle() {
        return title;
    }

    public String getCompany() {
        return company;
    }

    public String getPersonalStatement() {
        return personalStatement;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPersonalStatement(String personalStatement) {
        this.personalStatement = personalStatement;
    }

    @Override
    public String toString() {
        return "Name: "+getName()+", email: "+getEmail();
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KEY_NAME, getName());
            jsonObject.put(KEY_ABOUTME, getAboutme());
            jsonObject.put(KEY_EMAIL, getEmail());
            jsonObject.put(KEY_PHOTO, getPhotoBase64());
            jsonObject.put(KEY_COMPANY, getCompany());
            jsonObject.put(KEY_TITLE, getTitle());
            jsonObject.put(KEY_PERSONAL_STATEMENT, getPersonalStatement());
            jsonObject.put(KEY_CONNECTIONS, getConnections());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static SavedContact getMyself(Context c) {
        SettingsManager sm = new SettingsManager(c);
        Log.d(TAG, "`"+sm.getString(R.string.sm_contactinfo)+"`");
        if(sm.getString(R.string.sm_contactinfo).isEmpty()) {
            //Create a user
            SavedContact sc = new SavedContact();
            Log.d(TAG, sc.toString());
            sm.setString(R.string.sm_contactinfo, sc.toJSON().toString());
        }
        try {
            return new SavedContact(new JSONObject(sm.getString(c.getString(R.string.sm_contactinfo))));
        } catch (JSONException e) {
            //Reset
            SavedContact sc = new SavedContact();
            sm.setString(R.string.sm_contactinfo, sc.toJSON().toString());
            e.printStackTrace();
            return sc;
        }
        //return null;
    }
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(PersonQueryContract.PersonQueryEntry.COLUMN_NAME, getName());
        values.put(PersonQueryContract.PersonQueryEntry.COLUMN_EMAIL, getEmail());
        values.put(PersonQueryContract.PersonQueryEntry.COLUMN_PHOTO, getPhotoBase64());
        values.put(PersonQueryContract.PersonQueryEntry.COLUMN_QRURL, getAboutme());
        values.put(PersonQueryContract.PersonQueryEntry.COLUMN_COMPANY, getCompany());
        values.put(PersonQueryContract.PersonQueryEntry.COLUMN_TITLE, getTitle());
        values.put(PersonQueryContract.PersonQueryEntry.COLUMN_PERSONAL_STATEMENT, getPersonalStatement());
        if(getConnections() != null)
            values.put(PersonQueryContract.PersonQueryEntry.COLUMN_CONNECTIONS, getConnections().toString());
        return values;
    }
}
