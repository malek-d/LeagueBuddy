package com.mad.leaguebuddy;

import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "";
    public static String USER_KEY = "user";
    public boolean mBool;
    public EditText emailEditText;
    public EditText passwordEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.ToggleButton);
        final Button authButton = (Button) findViewById(R.id.AuthenticateButton);
        emailEditText =  (EditText) findViewById(R.id.emailEditText);
        passwordEditText =  (EditText) findViewById(R.id.passwordEditText);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mBool = b;
                if(b){authButton.setText(getString(R.string.login));}
                else{authButton.setText(getString(R.string.register));}

            }
        });

        authButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button btn = findViewById(R.id.CancelButton);
                //btn.setText(emailEditText.getText().toString());

                Intent result = new Intent(LoginActivity.this, MainActivity.class);
                Log.d(TAG, emailEditText.getText().toString());
                Log.d(TAG, passwordEditText.getText().toString());
                result.putExtra(MainActivity.EMAIL_KEY, emailEditText.getText().toString());
                result.putExtra(MainActivity.PASSWORD_KEY, passwordEditText.getText().toString());
                if(mBool) {
                    result.putExtra(MainActivity.LOGIN_KEY, mBool);
                }
                startActivity(result);

            }
        });

    }
}
