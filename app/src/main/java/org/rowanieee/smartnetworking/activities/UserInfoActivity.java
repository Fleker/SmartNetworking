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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.rowanieee.smartnetworking.R;
import org.rowanieee.smartnetworking.database.PersonQueryDbHelper;
import org.rowanieee.smartnetworking.model.SavedContact;
import org.rowanieee.smartnetworking.utils.ImagePickerUtils;
import org.rowanieee.smartnetworking.utils.NetworksUtils;

import java.util.ArrayList;

public class UserInfoActivity extends AppCompatActivity {
    public static final String EXTRA_PERSON_ID = "dbId";
    private static final String TAG = "UserInfoActivity";
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
        for(SavedContact sc: contacts) {
            if(sc.getDatabaseId() == personId) {
                profile = sc;
            }
        }

//        getSupportActionBar().setTitle(R.string.edit_contact_title);

        //Image first
        updateImage();
        findViewById(R.id.ProfilePic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePickerUtils.openImagePicker(UserInfoActivity.this);
            }
        });

        //Now other fields
        //Load your preferences
        ((EditText) findViewById(R.id.editName)).setText(profile.getName());
        ((EditText) findViewById(R.id.editName)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                profile.setName(s.toString());
            }
        });
        ((EditText) findViewById(R.id.editCompany)).setText(profile.getCompany());
        ((EditText) findViewById(R.id.editCompany)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                profile.setCompany(s.toString());
            }
        });
        ((EditText) findViewById(R.id.editTitle)).setText(profile.getTitle());
        ((EditText) findViewById(R.id.editTitle)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                profile.setTitle(s.toString());
            }
        });
        ((EditText) findViewById(R.id.editPerState)).setText(profile.getPersonalStatement());
        ((EditText) findViewById(R.id.editPerState)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                profile.setPersonalStatement(s.toString());
            }
        });
        ((EditText) findViewById(R.id.editEmail)).setText(profile.getEmail());
        ((EditText) findViewById(R.id.editEmail)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                profile.setEmail(s.toString());
            }
        });
        //TODO Profile pictures
        NetworksUtils.initializeNetworkFieldsForUser(profile, (LinearLayout) findViewById(R.id.contact_container), UserInfoActivity.this);

        final int[] editableFields = new int[] {
                R.id.editCompany,
                R.id.editEmail,
                R.id.editName,
                R.id.editPhone,
                R.id.editTitle,
                R.id.editPerState,
                R.id.editWebsite
        };
        findViewById(R.id.emailIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(open("mailto:"+((EditText) findViewById(R.id.editEmail)).getText().toString()));
            }
        });
        findViewById(R.id.websiteIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(open(((EditText) findViewById(R.id.editWebsite)).getText().toString()));
            }
        });
        //Disable editing by default
        for (int i : editableFields) {
//            findViewById(i).setFocusable(false);
            findViewById(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*if(v.getId() == R.id.editEmail) {
                        startActivity(open("mailto:"+((EditText) v).getText().toString()));
                    } else {
                        startActivity(open(((EditText) v).getText().toString()));
                    }*/
                    if(!v.isEnabled()) {
                        //Open that thing if tapped
                    }
                }
            });
        }
        //Do the same for the dynamic layouts
        for (int i = 0;i < ((LinearLayout) findViewById(R.id.contact_container)).getChildCount();i++) {
            View child = ((LinearLayout) findViewById(R.id.contact_container)).getChildAt(i);
            if(child.getTag() != null && child.getTag().equals(NetworksUtils.TAG)) {
                child.setEnabled(false);
                child.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        startActivity(open(((EditText) v).getText().toString()));
//                        if(!v.isEnabled())
//                            Open that thing if tapped
                    }
                });
            }
        }
        findViewById(R.id.fab_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toggle editing for everything
                /*editingMode = !editingMode;
                if(editingMode) {
                    ((FloatingActionButton) findViewById(R.id.fab_edit)).setImageResource(R.drawable.ic_check);
                } else {
                    ((FloatingActionButton) findViewById(R.id.fab_edit)).setImageResource(R.drawable.ic_lead_pencil);
                    save();
                }
                for (int i : editableFields) {
                    findViewById(i).setFocusable(editingMode);
                }
                //Do same for dynamic fields
                for (int i = 0;i < ((LinearLayout) findViewById(R.id.contact_container)).getChildCount();i++) {
                    View child = ((LinearLayout) findViewById(R.id.contact_container)).getChildAt(i);
                    if(child.getTag() != null && child.getTag().equals(NetworksUtils.TAG)) {
                        child.setFocusable(editingMode);
                    }
                }*/
                save();
                finish();
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
        Log.d(TAG, profile.toString());
        helper.update(helper.getWritableDatabase(), profile, personId);
    }

    public Intent open(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        return i;
    }

    public void updateImage() {
        if(profile.getPhotoBase64().length() > 2) {
            Bitmap bmp = ImagePickerUtils.getBitmapFromBase64(profile.getPhotoBase64());
            ((ImageView) findViewById(R.id.ProfilePic)).setImageBitmap(bmp);

            //Palette the layout
            Palette.from(bmp).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if(palette.getDarkVibrantSwatch() != null) {
                            getWindow().setNavigationBarColor(palette.getDarkVibrantSwatch().getRgb());
                            getWindow().setStatusBarColor(palette.getDarkVibrantSwatch().getRgb());
                        } else if(palette.getDarkMutedSwatch() != null) {
                            getWindow().setNavigationBarColor(palette.getDarkMutedSwatch().getRgb());
                            getWindow().setStatusBarColor(palette.getDarkMutedSwatch().getRgb());
                        }
                    }
                    findViewById(R.id.profile_container).setBackgroundColor(palette.getLightMutedSwatch().getRgb());
                    ((FloatingActionButton) findViewById(R.id.fab_edit)).setBackgroundColor(palette.getVibrantSwatch().getRgb());
                    //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(palette.getVibrantSwatch().getRgb()));
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Is this for ImagePicker?
        ImagePickerUtils.interpretActivityResult(UserInfoActivity.this, requestCode, resultCode, data, new ImagePickerUtils.ImagePickerListener() {
            @Override
            public void onImageSelected(String userPhotoBase64) {
                profile.setPhotoBase64(userPhotoBase64);
                updateImage();
            }
        });
        super.onActivityResult(requestCode, resultCode, data);
    }

}