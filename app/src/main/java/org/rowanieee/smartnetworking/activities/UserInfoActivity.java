package org.rowanieee.smartnetworking.activities;

/**
 * Created by Aaron Yangello on 5/17/2016.
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.rowanieee.smartnetworking.R;
import org.rowanieee.smartnetworking.database.PersonQueryDbHelper;
import org.rowanieee.smartnetworking.model.SavedContact;

import java.util.ArrayList;

public class UserInfoActivity extends AppCompatActivity {
    public static final String EXTRA_PERSON_ID = "dbId";
    private int personId = 0;
    private SavedContact profile;
    private boolean editingMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinfo);

        //Query the person from the db
        personId = getIntent().getIntExtra(EXTRA_PERSON_ID, 0);
        PersonQueryDbHelper pqdh = new PersonQueryDbHelper(getApplicationContext());
        ArrayList<SavedContact> contacts = pqdh.readAll(pqdh.getReadableDatabase());
        profile = contacts.get(personId);

        //Image first
        byte[] decodedString = Base64.decode(profile.getPhotoBase64(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        ((ImageView) findViewById(R.id.ProfilePic)).setImageBitmap(decodedByte);

        //Now other fields
        ((EditText) findViewById(R.id.editName)).setText(profile.getName());
        ((EditText) findViewById(R.id.editEmail)).setText(profile.getEmail());
        ((EditText) findViewById(R.id.editWebsite)).setText(profile.getAboutme());
        //TODO Populate the rest

        //Palette the layout
        Palette.from(findViewById(R.id.ProfilePic).getDrawingCache()).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setNavigationBarColor(palette.getDarkVibrantSwatch().getRgb());
                    getWindow().setStatusBarColor(palette.getDarkVibrantSwatch().getRgb());
                }
                findViewById(R.id.profile_container).setBackgroundColor(palette.getLightMutedSwatch().getRgb());
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(palette.getVibrantSwatch().getRgb()));
            }
        });

        final int[] editableFields = new int[] {
                R.id.editCompany,
                R.id.editEmail,
                R.id.editName,
                R.id.editPhone,
                R.id.editText,
                R.id.editWebsite
        };
        //Disable editing by default
        for (int i : editableFields) {
            findViewById(i).setEnabled(false);
            findViewById(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!v.isEnabled()) {
                        //Open that thing if tapped
                        startActivity(open(((EditText) v).getText().toString()));
                    }
                }
            });
        }
        findViewById(R.id.fab_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toggle editing for everything
                editingMode = !editingMode;
                if(editingMode) {
                    ((FloatingActionButton) findViewById(R.id.fab_edit)).setImageResource(R.drawable.ic_check);
                } else {
                    ((FloatingActionButton) findViewById(R.id.fab_edit)).setImageResource(R.drawable.ic_lead_pencil);
                    save();
                }
                for (int i : editableFields) {
                    findViewById(i).setEnabled(editingMode);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Let's save our changes
        save();
    }

    public void save() {
        PersonQueryDbHelper helper = new PersonQueryDbHelper(getApplicationContext());
        helper.update(helper.getWritableDatabase(), profile, personId);
    }

    public Intent open(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        return i;
    }

}