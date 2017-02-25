package com.aamir.schoolme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SpashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent loginActivity = new Intent(this, LoginActivity.class);
        startActivity(loginActivity);
        finish();

    }
}
