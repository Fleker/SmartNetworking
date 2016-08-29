package org.rowanieee.smartnetworking.constants;

import android.text.InputFilter;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import org.rowanieee.smartnetworking.R;

/**
 * Created by Nick on 5/15/2016.
 */
public enum Networks {
    PHONE("phone", R.drawable.ic_cellphone, EditorInfo.TYPE_CLASS_PHONE, "tel:"),
    COLLABRATEC("IEEE_Collabratec", R.drawable.ic_earth),
    TWITTER("twitter", R.drawable.ic_twitter, "http://twitter.com/"),
    LINKEDIN("linkedin", R.drawable.ic_linkedin, "http://linkedin/in/"),
    GITHUB("github", R.drawable.ic_github, "http://github.com/"),
    MEDIUM("medium", R.drawable.ic_medium, "https://medium.com/@"),
    FACEBOOK("facebook", R.drawable.ic_facebook, "https://www.facebook.com/"),
    INSTAGRAM("instagram", R.drawable.ic_instagram, "http://instagram.com/"),
    GOOGLEPLUS("google_Plus", R.drawable.ic_google_plus, "http://google.com/+"),
    REDDIT("reddit", R.drawable.ic_reddit, "http://reddit.com/u/"),
    SNAPCHAT("snapchat", R.drawable.ic_snapchat, "https://www.snapchat.com/add/"),
    VINE("vine", R.drawable.ic_vine, "https://vine.co/u/"),
    BLOG("blog", R.drawable.ic_rss),
    MISC("other_Website", R.drawable.ic_link);

    private String key;
    private int icon;
    private int textInputType;
    private String protocol;
    /**
     * Constructs a new Networks object
     * @param jsonKey The key that will be used to identify it in the JSON object
     * @param resId The resource id corresponding to the icon for this network
     */
    Networks(String jsonKey, int resId) {
        this.key = jsonKey;
        this.icon = resId;
        this.textInputType = EditorInfo.TYPE_TEXT_VARIATION_URI;
    }
    Networks(String jsonKey, int resId, int inputType) {
        this.key = jsonKey;
        this.icon = resId;
        this.textInputType = inputType;
    }
    Networks(String jsonKey, int resId, String protocol) {
        this.key = jsonKey;
        this.icon = resId;
        this.protocol = protocol;
    }
    Networks(String jsonKey, int resId, int inputType, String protocol) {
        this.key = jsonKey;
        this.icon = resId;
        this.textInputType = inputType;
        this.protocol = protocol;
    }

    public int getTextInputType() {
        return textInputType;
    }

    public String getKey() {
        return key;
    }

    public int getIcon() {
        return icon;
    }

    public String getProtocol() {
        return protocol;
    }

    public static Networks getNetworkFromKey(String s) {
        for(Networks n: Networks.values()) {
            if(n.key.equals(s)) {
                return n;
            }
        }
        return null;
    }
}
