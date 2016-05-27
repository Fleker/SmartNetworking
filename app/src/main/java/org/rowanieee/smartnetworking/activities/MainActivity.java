package org.rowanieee.smartnetworking.activities;

import android.graphics.Bitmap;
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
import org.rowanieee.smartnetworking.adapters.ViewPagerAdapter;
import org.rowanieee.smartnetworking.database.PersonQueryDbHelper;
import org.rowanieee.smartnetworking.fragments.HomeFragment;
import org.rowanieee.smartnetworking.fragments.ListFragment;
import org.rowanieee.smartnetworking.model.SavedContact;

import java.util.ArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gapi = new GoogleApiClient.Builder(this)
                .addApi(Nearby.MESSAGES_API)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .build();
        mMessageListener = new MessageListener() {
            @Override
            public void onFound(Message message) {
                String messageAsString = new String(message.getContent());
                Log.d(TAG, "Found message: " + messageAsString);
                //TODO @(Seamus) Display in contact view
                //When contact is clicked, then we insert into a database
            }

            @Override
            public void onLost(Message message) {
                String messageAsString = new String(message.getContent());
                Log.d(TAG, "Lost sight of message: " + messageAsString);
            }
        };

        //Tabs
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        homeFragment = new HomeFragment();
        listFragment = new ListFragment();
        listFragment.redrawContacts();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_bluetooth_audio);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_account_multiple);
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(homeFragment, "ONE");
        adapter.addFragment(listFragment, "TWO");
        viewPager.setAdapter(adapter);
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
        SubscribeOptions so = new SubscribeOptions.Builder()
                .setFilter(MessageFilter.INCLUDE_ALL_MY_TYPES)
                .setStrategy(new Strategy.Builder()
                        .setTtlSeconds(Strategy.TTL_SECONDS_INFINITE)
                        .setDistanceType(Strategy.DISTANCE_TYPE_EARSHOT) //Only very close people
                        .build())
                .build();
        Nearby.Messages.subscribe(gapi, mMessageListener, so);
    }
    private void unsubscribe() {
        Log.i(TAG, "Unsubscribing.");
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

    }
}
