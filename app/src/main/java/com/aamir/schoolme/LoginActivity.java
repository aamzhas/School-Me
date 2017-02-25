package com.aamir.schoolme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends Activity {

    final String AUTHORIZATION_TAG = "Authorization";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;

    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setting up display
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initialising Firebase Auth and Listener Objects
        mAuth = FirebaseAuth.getInstance();
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(AUTHORIZATION_TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(AUTHORIZATION_TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        //initialising email and password editTexts
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

    }

    public void getUserDetails() {
        
    }
    /**
     * Method executed when login button is clicked
     * @param view
     */
    public void login(View view) {
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(AUTHORIZATION_TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            displayDialog("Authentication Failed");

                        }
                    }
                });


    }

    /**
     * Method executed when signup button is clicked
     * @param view
     */
    public void signup(View view) {
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(AUTHORIZATION_TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(AUTHORIZATION_TAG, "signInWithEmail", task.getException());
                            displayDialog("Authentication Failed");

                        }
                    }
                });


    }


    /**
     * Method to generate Alert Dialog showing a message
     * @param message - message to be displayed
     */
    public void displayDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle(R.string.alertDialog);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    /**
     * Method invoked when activity starts. Adding authorization listener from authorization object
     */
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }

    /**
     * Method invoked when activity stops. Removing authorization listener from authorization object
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListner != null) {
            mAuth.removeAuthStateListener(mAuthListner);
        }
    }


}
