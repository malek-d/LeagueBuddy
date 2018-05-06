package com.mad.leaguebuddy.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mad.leaguebuddy.R;
import com.mad.leaguebuddy.data.urlFactory;
import com.mad.leaguebuddy.model.*;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "";
    public static String USER_KEY = "user";
    private boolean mBool;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button mAuthButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TableRow summonerInfoRow, mRegionTableRow;
    private MaterialSpinner regionSpinner;
    private EditText summonerNameEditText;
    private urlFactory urlFactory = new urlFactory();
    private String mRegion;

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
        //mRegionTableRow = findViewById(R.id.regionTableRow);
        regionSpinner.setItems(getResources().getStringArray(R.array.regions));
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
                // ...
            }
        };

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mBool = b;
                if (b) {
                    mAuthButton.setText(getString(R.string.login));
                    summonerInfoRow.setVisibility(View.GONE);
                    //mRegionTableRow.setVisibility(View.GONE);

                } else {
                    mAuthButton.setText(getString(R.string.register));
                    summonerInfoRow.setVisibility(View.VISIBLE);
                    //mRegionTableRow.setVisibility(View.VISIBLE);
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
    private void tryRegister() {
        if(emailEditText.getText().toString().equals("") || passwordEditText.getText().toString().equals("") || summonerNameEditText.getText().toString().equals("")){
            Toasty.error(this, getString(R.string.emptyFieldsString), Toast.LENGTH_SHORT).show();
        }else{
            validateRegistration();
        }
    }

    private void validateRegistration(){
        mAuth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()) {

                            String summonerName = summonerNameEditText.getText().toString();
                            String region = urlFactory.returnRegion(mRegion);


                            DatabaseReference summonerRef = mDatabase.getReference("users");
                            summonerRef.child(mAuth.getUid()).setValue(new User(summonerName, region));


                            finish();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));

                        } else {
                            Toasty.info(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void tryLogin() {
        if(emailEditText.getText().toString().equals("") || passwordEditText.getText().toString().equals("")){
            Toasty.error(this, getString(R.string.emptyFieldsString), Toast.LENGTH_SHORT).show();
        }else{
            validateLogin();
        }
    }

    private void validateLogin(){
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()) {
                            finish();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else {
                            Toasty.info(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }

}