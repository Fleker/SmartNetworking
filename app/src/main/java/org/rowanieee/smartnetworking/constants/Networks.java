package org.rowanieee.smartnetworking.constants;

import org.rowanieee.smartnetworking.R;

/**
 * Created by Nick on 5/15/2016.
 */
public enum Networks {
    FACEBOOK("facebook", R.mipmap.ic_launcher);

    private String key;
    private int icon;
    /**
     * Constructs a new Networks object
     * @param jsonKey The key that will be used to identify it in the JSON object
     * @param resId The resource id corresponding to the icon for this network
     */
    Networks(String jsonKey, int resId) {
        this.key = jsonKey;
        this.icon = resId;
    }
}
