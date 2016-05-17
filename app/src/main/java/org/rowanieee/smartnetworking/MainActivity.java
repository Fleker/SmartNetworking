package org.rowanieee.smartnetworking;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {
    private String qrurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SettingsManager sm = new SettingsManager(this);
        qrurl = sm.getString(R.string.qrlink);
        ((EditText) findViewById(R.id.qrlink)).setText(qrurl);
        //Search for nearby peeps
        loadQrCode();
        //Load your preferences
        ((EditText) findViewById(R.id.qrlink)).setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                qrurl = ((EditText) findViewById(R.id.qrlink)).getText().toString();
                loadQrCode();
                return false;
            }
        });
    }

    public void loadQrCode() {
        Bitmap myBitmap = QRCode.from("https://about.me/nickfelker").bitmap();
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
                            }
                        })
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
