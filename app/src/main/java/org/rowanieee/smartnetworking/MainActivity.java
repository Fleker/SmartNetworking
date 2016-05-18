package org.rowanieee.smartnetworking;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.felkertech.settingsmanager.SettingsManager;

import net.glxn.qrgen.android.QRCode;

import org.json.JSONException;
import org.json.JSONObject;
import org.rowanieee.smartnetworking.database.PersonQueryDbHelper;
import org.rowanieee.smartnetworking.model.SavedContact;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private String qrurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SettingsManager sm = new SettingsManager(this);
        qrurl = sm.getString(R.string.qrlink);
        ((EditText) findViewById(R.id.qrlink)).setText(qrurl);
        //Search for nearby peeps
        loadQrCode();
        //Load your preferences
        ((EditText) findViewById(R.id.qrlink)).setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.d(TAG, "onkey1");
                /*qrurl = ((EditText) findViewById(R.id.qrlink)).getText().toString();
                Log.d(TAG, "Pre get `"+qrurl+"`");
                loadQrCode();
                sm.setString(R.string.qrlink, qrurl);*/
                return false;
            }
        });
        findViewById(R.id.action_qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onkey3");
                qrurl = ((EditText) findViewById(R.id.qrlink)).getText().toString();
                Log.d(TAG, "Pre get `"+qrurl+"`");
                loadQrCode();
                sm.setString(R.string.qrlink, qrurl);
            }
        });
        /*((EditText) findViewById(R.id.qrlink)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d(TAG, "onkey2");
               *//* qrurl = ((EditText) findViewById(R.id.qrlink)).getText().toString();
                Log.d(TAG, "Pre get `"+qrurl+"`");
                loadQrCode();
                sm.setString(R.string.qrlink, qrurl);*//*
                return false;
            }
        });*/
        redrawContacts();
    }

    public void loadQrCode() {
//        Bitmap myBitmap = QRCode.from("https://about.me/nickfelker").bitmap();
        Log.d(TAG, "QR Code is `"+qrurl+"`");
        if(qrurl == null || qrurl.isEmpty())
            qrurl = "h";
        Bitmap myBitmap = QRCode.from(qrurl).bitmap();
        ImageView myImage = (ImageView) findViewById(R.id.qrcode);
        myImage.setImageBitmap(myBitmap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.alpha, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_add:
                MaterialDialog contactCreator = new MaterialDialog.Builder(this)
                        .title("We are adding a contact!")
                        .customView(R.layout.add_dialog, false)
                        .positiveText("Add")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //TODO Add in user data
                                String name = ((EditText) dialog.getCustomView().findViewById(R.id.name)).getText().toString();
                                String info = ((EditText) dialog.getCustomView().findViewById(R.id.info)).getText().toString();
                                PersonQueryDbHelper pqdh = new PersonQueryDbHelper(MainActivity.this);
                                try {
                                    pqdh.insert(pqdh.getWritableDatabase(), new SavedContact(name, "", "",
                                            "", new JSONObject("{info: "+info+"}")));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                redrawContacts();
                            }
                        })
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void redrawContacts() {
        PersonQueryDbHelper pqdh = new PersonQueryDbHelper(MainActivity.this);
        ArrayList<SavedContact> contacts = pqdh.readAll(pqdh.getReadableDatabase());
        String printable = "";
        for(SavedContact contact: contacts) {
            printable += contact.getName()+" "+contact.getEmail()+"\n"+contact.getAboutme()+"\n"+contact.getConnections()+"\n\n\n";
        }
        ((TextView) findViewById(R.id.printlist)).setText(printable);
    }
}
