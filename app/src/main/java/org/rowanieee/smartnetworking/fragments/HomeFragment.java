package org.rowanieee.smartnetworking.fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.felkertech.settingsmanager.SettingsManager;

import net.glxn.qrgen.android.QRCode;

import org.json.JSONException;
import org.rowanieee.smartnetworking.R;
import org.rowanieee.smartnetworking.constants.Networks;
import org.rowanieee.smartnetworking.model.SavedContact;
import org.rowanieee.smartnetworking.utils.NetworksUtils;

/**
 * Will show nearby connections and allow you to change your own settings
 * Created by Nick Felker on 5/26/2016.
 */
public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private String qrurl;
    private SettingsManager sm;
    private View v;
    private SavedContact me;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sm = new SettingsManager(getContext());
        qrurl = sm.getString(R.string.qrlink);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_settings, container, false);
        ((EditText) v.findViewById(R.id.qrlink)).setText(qrurl);
        //Search for nearby peeps
        loadQrCode();
        ((EditText) v.findViewById(R.id.qrlink)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "onkey2");
                qrurl = ((EditText) v.findViewById(R.id.qrlink)).getText().toString();
                Log.d(TAG, "Pre get `"+qrurl+"`");
                loadQrCode();
                sm.setString(R.string.qrlink, qrurl);
            }
        });
        v.findViewById(R.id.action_qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                Log.d(TAG, "onkey3");
                qrurl = ((EditText) v.findViewById(R.id.qrlink)).getText().toString();
                Log.d(TAG, "Pre get `"+qrurl+"`");
                loadQrCode();
                sm.setString(R.string.qrlink, qrurl);
            }
        });


        me = SavedContact.getMyself(getContext());
        //Load your preferences
        ((EditText) v.findViewById(R.id.me_name)).setText(me.getName());
        ((EditText) v.findViewById(R.id.me_name)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                me.setName(s.toString());
                sm.setString(R.string.sm_contactinfo, me.toJSON().toString());
            }
        });
        ((EditText) v.findViewById(R.id.me_company)).setText(me.getCompany());
        ((EditText) v.findViewById(R.id.me_company)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                me.setCompany(s.toString());
                sm.setString(R.string.sm_contactinfo, me.toJSON().toString());
            }
        });
        ((EditText) v.findViewById(R.id.me_title)).setText(me.getTitle());
        ((EditText) v.findViewById(R.id.me_title)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                me.setTitle(s.toString());
                sm.setString(R.string.sm_contactinfo, me.toJSON().toString());
            }
        });
        ((EditText) v.findViewById(R.id.me_personal)).setText(me.getPersonalStatement());
        ((EditText) v.findViewById(R.id.me_personal)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                me.setPersonalStatement(s.toString());
                sm.setString(R.string.sm_contactinfo, me.toJSON().toString());
            }
        });
        ((EditText) v.findViewById(R.id.me_email)).setText(me.getEmail());
        ((EditText) v.findViewById(R.id.me_email)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                me.setEmail(s.toString());
                sm.setString(R.string.sm_contactinfo, me.toJSON().toString());
            }
        });
        //TODO Profile pictures

        NetworksUtils.initializeNetworkFieldsForUser(me, (LinearLayout) v.findViewById(R.id.me_networks), getContext());
        return v;
    }
    public void loadQrCode() {
        Log.d(TAG, "QR Code is `"+qrurl+"`");
        if(qrurl == null || qrurl.isEmpty())
            qrurl = "h";
        Bitmap myBitmap = QRCode.from(qrurl).bitmap();
        ImageView myImage = (ImageView) v.findViewById(R.id.qrcode);
        myImage.setImageBitmap(myBitmap);
    }
}
