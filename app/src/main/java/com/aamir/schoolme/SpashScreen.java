package com.aamir.schoolme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SpashScreen extends AppCompatActivity {

    SharedPreferences mSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent nextActivity;

        mSharedPreferences = getSharedPreferences(getString(R.string.PreferencesFile), 0);
        boolean loggedIn = mSharedPreferences.getBoolean(getString(R.string.Pref_login), false);

        if (loggedIn) {
            nextActivity = new Intent( this, AllClassesList.class );
        } else {
            nextActivity = new Intent(this, LoginActivity.class);
        }

        startActivity(nextActivity);
        finish();

    }
}
