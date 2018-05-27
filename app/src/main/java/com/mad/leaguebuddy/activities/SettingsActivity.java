package com.mad.leaguebuddy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mad.leaguebuddy.R;
import com.mad.leaguebuddy.ViewModel.FirebaseFactory;
import com.mad.leaguebuddy.ViewModel.UrlFactory;

import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import info.hoang8f.widget.FButton;

/**
 * Basic Settings activity which displays current account information to the user and if wanted
 * allow user to change their account settings in FireBase
 */
public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.userNameET)
    EditText mUserNameET;
    @BindView(R.id.regionSpinner)
    MaterialSpinner mRegionSpinner;
    @BindView(R.id.logOutBtn)
    FButton mLogOutBtn;
    @BindView(R.id.updateInfoBtn)
    FButton mUpdateInfoBtn;
    private String mRegion;
    private UrlFactory mUrlFactor = new UrlFactory();
    private FirebaseFactory mFirebaseFactory = FirebaseFactory.getInstance(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        mRegionSpinner.setItems(getResources().getStringArray(R.array.regions));
        mRegionSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                mRegion = mUrlFactor.returnRegion(item);
            }
        });

        mUserNameET.setText(mFirebaseFactory.getUserName());


        mUserNameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mUpdateInfoBtn.setEnabled(true);
                mUpdateInfoBtn.setButtonColor(getResources().getColor(R.color.fbutton_color_turquoise));
                mUpdateInfoBtn.setShadowColor(getResources().getColor(R.color.fbutton_color_green_sea));
            }
        });
    }

    /**
     * When called by a button within the xml authenticates this user if details are valid for change
     * @param view
     */
    public void updateInfo(View view) {
        authenticateChange();
    }

    /**
     * Kills the current firebase instance and logs user out back to the loginActivity
     * @param view
     */
    public void signOut(View view){
        mFirebaseFactory.getAuth().signOut();
        mFirebaseFactory.killInstance();
        Toasty.success(this, "You have been signed out", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
    }

    /**
     * Authenticates any new inputted information by the user before updating info on FireBase
     */
    private void authenticateChange() {
        if(mRegion == null || mRegion == "DEFAULT"){
            Toasty.error(this, getString(R.string.selectRegionString), Toast.LENGTH_SHORT).show();
        }else{
            mFirebaseFactory.updateUserInfo(mUserNameET.getText().toString(), mRegion);
            Toasty.success(this, "Change successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingsActivity.this, MainActivity.class));
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();    //Call the back button's method
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
