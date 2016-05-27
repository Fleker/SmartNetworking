package org.rowanieee.smartnetworking.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.felkertech.settingsmanager.SettingsManager;

import net.glxn.qrgen.android.QRCode;

import org.rowanieee.smartnetworking.R;

/**
 * Will show nearby connections and allow you to change your own settings
 * Created by Nick Felker on 5/26/2016.
 */
public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private String qrurl;
    private SettingsManager sm;
    private View v;

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
        //Load your preferences
        v.findViewById(R.id.action_qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onkey3");
                qrurl = ((EditText) v.findViewById(R.id.qrlink)).getText().toString();
                Log.d(TAG, "Pre get `"+qrurl+"`");
                loadQrCode();
                sm.setString(R.string.qrlink, qrurl);
            }
        });
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
