package com.mad.leaguebuddy.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mad.leaguebuddy.R;
import com.mad.leaguebuddy.ViewModel.FirebaseFactory;
import com.mad.leaguebuddy.ViewModel.UrlFactory;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;

//Maybe rename to AuthenticationActivity as you also have registration in here
/**
 * This Activity handles user authentication in regards to both logging into an existing account
 * or registering a new account
 */

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "";
    private boolean mBool; //Rename to make more sense i.e. isLogin, isBtnLogin or something
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button mAuthButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TableRow summonerInfoRow;
    private MaterialSpinner regionSpinner;
    private EditText summonerNameEditText;
    private UrlFactory UrlFactory = new UrlFactory();
    private String mRegion;
    private FirebaseFactory mFirebaseFactory = FirebaseFactory.getInstance(this);
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Switch toggleButton = findViewById(R.id.ToggleButton);
        mAuth = FirebaseAuth.getInstance();
        mAuthButton =  findViewById(R.id.AuthenticateButton);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        summonerInfoRow = findViewById(R.id.summonerTablerow);
        regionSpinner = findViewById(R.id.regionSpinner);
        summonerNameEditText = findViewById(R.id.summonerEditText);
        regionSpinner.setItems(getResources().getStringArray(R.array.regions));
        /**
         * You Should put all Firebase stuff in a separate class titled something like DbHandler or DbHelper
         */
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        //Determines if toggle is set for user to login or register
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mBool = b;
                if (b) {
                    mAuthButton.setText(getString(R.string.login));
                    summonerInfoRow.setVisibility(View.GONE);
                } else {
                    mAuthButton.setText(getString(R.string.register));
                    summonerInfoRow.setVisibility(View.VISIBLE);
                }

            }
        });

        mAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBool) {
                    tryLogin();
                } else {
                    tryRegister();
                }
            }
        });
        regionSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                mRegion = item;
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    //what about validation if email is actually in email format ? and also to ensure email doesn't already exist ?
    //I also feel like validation of data can be done in a different class like the object itself or utils not entirely sure :thinking:
    /**
     * Checks if user has entered all required fields and if they have then registers them as a new user
     * Otherwise alerts user that they need to enter all fields
     */

    private void tryRegister() {
        if(emailEditText.getText().toString().equals("") || passwordEditText.getText().toString().equals("") || summonerNameEditText.getText().toString().equals("")){
            Toasty.error(this, getString(R.string.emptyFieldsString), Toast.LENGTH_SHORT).show(); //I changed your strings thing to enter instead of Entire btw
        }else if(mRegion == null || mRegion == "DEFAULT"){
            Toasty.error(this, getString(R.string.selectRegionString), Toast.LENGTH_SHORT).show();
        }else {
            String summonerName = summonerNameEditText.getText().toString();
            String region = UrlFactory.returnRegion(mRegion);
            if(mFirebaseFactory.validateRegistration(this,emailEditText.getText().toString(), passwordEditText.getText().toString(), summonerName, region)){
                Toasty.info(getApplicationContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Checks if user has entered all required fields and if they have then proceeds to log them in
     * Otherwise alerts user they need to enter all fields
     */
    private void tryLogin() {
        if(emailEditText.getText().toString().equals("") || passwordEditText.getText().toString().equals("")){
            Toasty.error(this, getString(R.string.emptyFieldsString), Toast.LENGTH_SHORT).show();
        }else {
            if(mFirebaseFactory.validateLogin(this, emailEditText.getText().toString(), passwordEditText.getText().toString())){
                Toasty.info(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
