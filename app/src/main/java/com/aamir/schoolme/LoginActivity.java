package com.aamir.schoolme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 960;
    final String AUTHORIZATION_TAG = "Authorization";
    final String GOOGLE_SIGNIN_TAG = "Google Signin";
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mSharefPreferencesEditor;
    //Google Services
    private GoogleApiClient mGoogleApiClient;

    //variables for firebase authorization
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private FirebaseUser user;

    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setting up display
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSharedPreferences = getSharedPreferences(getString(R.string.PreferencesFile), 0);
        mSharefPreferencesEditor = mSharedPreferences.edit();

        //Google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();

        //Google Services setup
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        //Initialising Firebase Auth and Listener Objects
        mAuth = FirebaseAuth.getInstance();
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(AUTHORIZATION_TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    user = FirebaseAuth.getInstance().getCurrentUser();

                } else {
                    Log.d(AUTHORIZATION_TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        //initialising email and password editTexts
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

    }

    /**
     * Method invoked when activity starts. Adding authorization listener from authorization object
     */
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener( mAuthListner );
    }

    /**
     * Method invoked when activity stops. Removing authorization listener from authorization
     * object
     */
    @Override
    protected void onStop() {
        super.onStop();
        if ( mAuthListner != null ) {
            mAuth.removeAuthStateListener( mAuthListner );
        }
    }

    /**
     * Method to return the name of current user
     */
    public void getUserName() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
        }
    }

    /**
     * Method executed when login button is clicked
     * @param view - current view
     */
    public void login(View view) {
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(AUTHORIZATION_TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            displayDialog(getString(R.string.alertDialog), getString(R.string.failed));

                        } else {
                            changeLoginState(true);
                        }
                    }
                });
    }

    /**
     * Method to generate Alert Dialog showing a message
     *
     * @param message - message to be displayed
     */
    public void displayDialog( String title, String message ) {
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setMessage( message );
        builder.setTitle( title );
        builder.setPositiveButton( R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick( DialogInterface dialog, int which ) {
                //return;
            }
        } );

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Method to change the Login Attribute in SharedPreferences
     * @param state - new state
     */
    public void changeLoginState(boolean state) {
        mSharefPreferencesEditor.putBoolean(getString(R.string.Pref_login), state);
        mSharefPreferencesEditor.commit();
    }

    /**
     * OnClick for signing in using google
     * @param view -
     */
    public void signInWithGoogle(View view) {
        signIn();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN );
    }

    public void signOut() {
        changeLoginState( false );
        displayDialog( getString( R.string.success ), "Signed out");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                displayDialog(getString(R.string.success), account.getDisplayName());
                changeLoginState(true);
            } else {
                displayDialog(getString(R.string.alertDialog), getString(R.string.failed));
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(AUTHORIZATION_TAG, "firebaseWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(AUTHORIZATION_TAG, "createUserWithCredential:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            displayDialog(getString(R.string.alertDialog), getString(R.string.failed));
                        }
                    }
                });
    }

    /**
     * Method executed when signup button is clicked
     * @param view - current View
     */
    public void signup(View view) {
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(AUTHORIZATION_TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(AUTHORIZATION_TAG, "signInWithEmail", task.getException());
                            displayDialog(getString(R.string.alertDialog), getString(R.string.failed));

                        } else {
                            changeLoginState(true);
                        }
                    }
                });


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(GOOGLE_SIGNIN_TAG, "signInWithGoogle" + connectionResult.getErrorMessage());
    }
}
