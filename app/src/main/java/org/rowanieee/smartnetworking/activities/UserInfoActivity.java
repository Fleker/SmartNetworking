package org.rowanieee.smartnetworking.activities;

/**
 * Created by Aaron Yangello on 5/17/2016.
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;import org.rowanieee.smartnetworking.R;

public class UserInfoActivity extends AppCompatActivity {
    public static final String EXTRA_PERSON_ID = "dbId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinfo);
    }
}