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

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mad.leaguebuddy.R;
import com.mad.leaguebuddy.model.FirebaseFactory;
import com.mad.leaguebuddy.ViewModel.UrlFactory;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;

/**
 * This Activity handles user authentication in regards to both logging into an existing account
 * or registering a new account
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "";
    private boolean mBool; //Rename to make more sense i.e. isLogin, isBtnLogin or something
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private Button mAuthButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TableRow mSumInfoRow;
    private MaterialSpinner mRegionSpinner;
    private EditText mSummonerNameEditText;
    private UrlFactory UrlFactory = new UrlFactory();
    private String mRegion;
    private FirebaseFactory mFirebaseFactory = FirebaseFactory.getInstance(this);
    private Switch mToggleBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mToggleBtn = findViewById(R.id.ToggleButton);
        mAuth = FirebaseAuth.getInstance();
        mAuthButton =  findViewById(R.id.AuthenticateButton);
        mEmailEditText = findViewById(R.id.emailEditText);
        mPasswordEditText = findViewById(R.id.passwordEditText);
        mSumInfoRow = findViewById(R.id.summonerTablerow);
        mRegionSpinner = findViewById(R.id.regionSpinner);
        mSummonerNameEditText = findViewById(R.id.summonerEditText);
        mRegionSpinner.setItems(getResources().getStringArray(R.array.regions));

        //TODO: move this to the FirebaseHandler class
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
        mToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mBool = b;
                if (mBool) {
                    mAuthButton.setText(getString(R.string.login));
                    mSumInfoRow.setVisibility(View.GONE);
                } else {
                    mAuthButton.setText(getString(R.string.register));
                    mSumInfoRow.setVisibility(View.VISIBLE);
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
        mRegionSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
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

    /**
     * Checks if user has entered all required fields and if they have then registers them as a new user
     * Otherwise alerts user that they need to enter all fields
     */
    private void tryRegister() {
        if(mEmailEditText.getText().toString().equals("") || mPasswordEditText.getText().toString().equals("") || mSummonerNameEditText.getText().toString().equals("")){
            Toasty.error(this, getString(R.string.emptyFieldsString), Toast.LENGTH_SHORT).show(); //I changed your strings thing to enter instead of Entire btw
        }else if(mRegion == null || mRegion == "DEFAULT"){
            Toasty.error(this, getString(R.string.selectRegionString), Toast.LENGTH_SHORT).show();
        }else {
            String summonerName = mSummonerNameEditText.getText().toString();
            String region = UrlFactory.returnRegion(mRegion);
            if(mFirebaseFactory.validateRegistration(this,mEmailEditText.getText().toString(), mPasswordEditText.getText().toString(), summonerName, region)){
                Toasty.info(getApplicationContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Checks if user has entered all required fields and if they have then proceeds to log them in
     * Otherwise alerts user they need to enter all fields
     */
    private void tryLogin() {
        if(mEmailEditText.getText().toString().equals("") || mPasswordEditText.getText().toString().equals("")){
            Toasty.error(this, getString(R.string.emptyFieldsString), Toast.LENGTH_SHORT).show();
        }else {
            if(mFirebaseFactory.validateLogin(this, mEmailEditText.getText().toString(), mPasswordEditText.getText().toString())){
                Toasty.info(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
