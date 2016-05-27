package org.rowanieee.smartnetworking.constants;

import org.rowanieee.smartnetworking.R;

/**
 * Created by Nick on 5/15/2016.
 */
public enum Networks {
    COLLABRATEC("collabratec", R.drawable.ic_earth),
    TWITTER("twitter", R.drawable.ic_twitter),
    LINKEDIN("linkedin", R.drawable.ic_linkedin),
    GITHUB("github", R.drawable.ic_github),
    MEDIUM("medium", R.drawable.ic_medium),
    FACEBOOK("facebook", R.drawable.ic_facebook),
    INSTAGRAM("instagram", R.drawable.ic_instagram),
    GOOGLEPLUS("gplus", R.drawable.ic_google_plus),
    REDDIT("reddit", R.drawable.ic_reddit),
    SNAPCHAT("snapchat", R.drawable.ic_snapchat),
    VINE("vine", R.drawable.ic_vine),
    BLOG("blog", R.drawable.ic_rss),
    MISC("otherWebsite", R.drawable.ic_link);

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
    public static Networks getNetworkFromKey(String s) {
        for(Networks n: Networks.values()) {
            if(n.key.equals(s)) {
                return n;
            }
        }
        return null;
    }
}
