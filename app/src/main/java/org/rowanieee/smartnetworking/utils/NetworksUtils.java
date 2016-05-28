package org.rowanieee.smartnetworking.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.felkertech.settingsmanager.SettingsManager;

import org.json.JSONException;
import org.rowanieee.smartnetworking.R;
import org.rowanieee.smartnetworking.constants.Networks;
import org.rowanieee.smartnetworking.model.SavedContact;

/**
 * Created by guest1 on 5/27/2016.
 */
public class NetworksUtils {
    public static final String TAG = "NetworksUtils";

    public static void initializeNetworkFieldsForUser(final SavedContact contact, LinearLayout parentView, final Context mContext) {
        final SettingsManager sm = new SettingsManager(mContext);
        //Add a bunch of Network fields
        for(final Networks n: Networks.values()) {
            LinearLayout networkView = new LinearLayout(mContext);
            ImageView networkImageView = new ImageView(mContext);
            EditText networkField = new EditText(mContext);
            networkField.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            TextInputLayout networkFieldContainer = new TextInputLayout(mContext);
            networkFieldContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            networkImageView.setPadding(8, 0, 0, 0);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 4, 0, 0);
            networkImageView.setLayoutParams(layoutParams);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //Use a tintable icon
                Icon networkIcon = Icon.createWithResource(mContext, n.getIcon());
                networkIcon.setTint(mContext.getResources().getColor(R.color.icon_tint));
                networkImageView.setImageIcon(networkIcon);
            } else {
                //Use a plain vectordrawable
                networkImageView.setImageResource(n.getIcon());
            }
            networkImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if(contact.getConnections().getString(n.getKey()).indexOf(n.getProtocol()) == 0) {
                            //Beginning is already included, don't add it
                            mContext.startActivity(open(contact.getConnections().getString(n.getKey())));
                        } else {
                            mContext.startActivity(open(n.getProtocol() + contact.getConnections().getString(n.getKey())));
                        }
                    } catch (Exception e) {
                        //Catch JSON issues or if that thing can't be clicked
                        e.printStackTrace();
                    }
                }
            });
            networkField.setHint(n.getKey().substring(0,1).toUpperCase()+n.getKey().substring(1).replaceAll("_", " "));
            if(contact.getConnections().has(n.getKey())) {
                try {
                    networkField.setText(contact.getConnections().getString(n.getKey()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
//            networkField.setInputType(n.getTextInputType());
//            networkField.setAutoLinkMask(Linkify.ALL);
//            networkField.setClickable(true);
//            networkField.setLinksClickable(true);
//            networkField.setMovementMethod(LinkMovementMethod.getInstance());
            networkField.setTag(TAG);
            networkField.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        Log.d(TAG, "User is updating "+n.getKey());
                        contact.setConnections(contact.getConnections().put(n.getKey(), s.toString()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            //Add views
            networkFieldContainer.addView(networkField);
            networkView.setOrientation(LinearLayout.HORIZONTAL);
            networkView.addView(networkImageView);
            networkView.addView(networkFieldContainer);
            //Add to master view
            parentView.addView(networkView);
        }
    }
    public static Intent open(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        return i;
    }
}
