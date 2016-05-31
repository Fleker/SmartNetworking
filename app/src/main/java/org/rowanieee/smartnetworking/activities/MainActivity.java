package org.rowanieee.smartnetworking.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.felkertech.settingsmanager.SettingsManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageFilter;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.nearby.messages.SubscribeOptions;

import net.glxn.qrgen.android.QRCode;

import org.json.JSONException;
import org.json.JSONObject;
import org.rowanieee.smartnetworking.R;
import org.rowanieee.smartnetworking.adapters.NearbyContactsAdapter;
import org.rowanieee.smartnetworking.adapters.ViewPagerAdapter;
import org.rowanieee.smartnetworking.database.PersonQueryDbHelper;
import org.rowanieee.smartnetworking.fragments.HomeFragment;
import org.rowanieee.smartnetworking.fragments.ListFragment;
import org.rowanieee.smartnetworking.model.SavedContact;
import org.rowanieee.smartnetworking.utils.ImagePickerUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
    implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "MainActivity";
    private GoogleApiClient gapi;
    private Message mActiveMessage;
    private MessageListener mMessageListener;

    //For our tabbing to work
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    //Control our fragments
    private HomeFragment homeFragment;
    private ListFragment listFragment;

    private List<SavedContact> nearbyContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gapi = new GoogleApiClient.Builder(this)
                .addApi(Nearby.MESSAGES_API)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .build();
        nearbyContacts = new ArrayList<>();
        mMessageListener = new MessageListener() {
            @Override
            public void onFound(Message message) {
                String messageAsString = new String(message.getContent());
                Log.d(TAG, "Found message: " + messageAsString);
                try {
                    SavedContact foundContact = new SavedContact(new JSONObject(messageAsString));
                    nearbyContacts.add(foundContact);
                    homeFragment.updateNearby(nearbyContacts);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //When contact is clicked, then we insert into a database
            }

            @Override
            public void onLost(Message message) {
                String messageAsString = new String(message.getContent());
                Log.d(TAG, "Lost sight of message: " + messageAsString);
                try {
                    SavedContact foundContact = new SavedContact(new JSONObject(messageAsString));
                    nearbyContacts.remove(foundContact);
                    homeFragment.updateNearby(nearbyContacts);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        //Tabs
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        homeFragment = new HomeFragment(new NearbyContactsAdapter.NearbyContactListener() {
            @Override
            public void onContactClicked(SavedContact addedContact) {
                //Make sure contact hasn't been added before
                PersonQueryDbHelper dbHelper = new PersonQueryDbHelper(MainActivity.this);
                boolean shouldAdd = true;
                for(SavedContact sc: dbHelper.readAll(dbHelper.getReadableDatabase())) {
                    shouldAdd = shouldAdd && !sc.getEmail().equals(addedContact.getEmail());
                }
                if(shouldAdd) {
                    listFragment.addNewContact(addedContact);
                    Toast.makeText(MainActivity.this, "Saved " + addedContact.getName(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Contact is already added", Toast.LENGTH_SHORT).show();
                }
            }
        });
        listFragment = new ListFragment();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_bluetooth_audio);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_account_multiple);
    }
    private void setupViewPager(final ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(homeFragment, "ONE");
        adapter.addFragment(listFragment, "TWO");
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0) {
                    listFragment.hideFab();
                } else {
                    new Handler(Looper.getMainLooper()) {
                        @Override
                        public void handleMessage(android.os.Message msg) {
                            super.handleMessage(msg);
                            listFragment.showFab();
                        }
                    }.sendEmptyMessageDelayed(0, 250);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state == ViewPager.SCROLL_STATE_DRAGGING) {
                    listFragment.hideFab();
                } else if(state == ViewPager.SCROLL_STATE_IDLE && viewPager.getCurrentItem() == 1) {
                    listFragment.showFab();
                }
            }
        });
    }

    @Override
    public void onConnected(Bundle connectionHint) {
//        publish("Hello World");
        publish(SavedContact.getMyself(MainActivity.this).toJSON().toString());
        subscribe();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onStop() {
        unpublish();
        unsubscribe();
        super.onStop();
    }
    private void publish(String message) {
        Log.i(TAG, "Publishing message: " + message);
        mActiveMessage = new Message(message.getBytes());
        Nearby.Messages.publish(gapi, mActiveMessage);
    }
    private void unpublish() {
        Log.i(TAG, "Unpublishing.");
        if (mActiveMessage != null) {
            Nearby.Messages.unpublish(gapi, mActiveMessage);
            mActiveMessage = null;
        }
    }
    // Subscribe to receive messages.
    private void subscribe() {
        Log.i(TAG, "Subscribing.");
        /*SubscribeOptions so = new SubscribeOptions.Builder()
                .setFilter(MessageFilter.INCLUDE_ALL_MY_TYPES)
                .setStrategy(new Strategy.Builder()
                        .setTtlSeconds(Strategy.TTL_SECONDS_INFINITE)
                        .setDistanceType(Strategy.DISTANCE_TYPE_DEFAULT) //Only very close people
                        .build())
                .build();*/
        Nearby.Messages.subscribe(gapi, mMessageListener);
    }
    private void unsubscribe() {
        Log.i(TAG, "Unsubscribing.");
        if(gapi.isConnected())
            Nearby.Messages.unsubscribe(gapi, mMessageListener);
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
                viewPager.setCurrentItem(1); //Opens the list
                listFragment.getView().findViewById(R.id.fab_add).performClick();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(MainActivity.this, "Cannot connect", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Is this for ImagePicker?
        ImagePickerUtils.interpretActivityResult(MainActivity.this, requestCode, resultCode, data, new ImagePickerUtils.ImagePickerListener() {
            @Override
            public void onImageSelected(String userPhotoBase64) {
                //Set to user's profile
                homeFragment.updateImage(userPhotoBase64);
            }
        });
        super.onActivityResult(requestCode, resultCode, data);
    }
}
