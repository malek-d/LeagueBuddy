package com.mad.leaguebuddy.ViewModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mad.leaguebuddy.activities.MainActivity;
import com.mad.leaguebuddy.model.User;

/**
 * Java class that handles all FireBase java code in a singleton pattern
 * This class has functions that would handle any kind of firebase
 */
public class FirebaseFactory {
    private static final String TAG = "TAG";
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private DatabaseReference mUsersRef;
    private Activity activity;
    private FirebaseUser mUser;
    private static FirebaseFactory sInstance;
    private String mUserName;
    private String mRegion;

    public String getRegion() {
        return mRegion;
    }

    public void setRegion(String region) {
        mRegion = region;
    }

    /**
     * Private constructor that is only called on by getInstance to ensure it's a singleton pattern
     *
     * @param activity takes in the activity to handle changes between activities/UI
     */
    private FirebaseFactory(Activity activity) {
        this.activity = activity;
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        mUsersRef = mDatabase.getReference("users");
    }

    /**
     * Singleton function that either returns the current instance or creates an instance using the
     * private constructor
     *
     * @param activity
     * @return
     */
    public static synchronized FirebaseFactory getInstance(Activity activity) {
        if (sInstance == null) {
            sInstance = new FirebaseFactory(activity);
        }
        return sInstance;
    }

    public FirebaseAuth getAuth() {
        return mAuth;
    }

    public FirebaseDatabase getDatabase() {
        return mDatabase;
    }

    public DatabaseReference getRef() {
        return mRef;
    }

    public FirebaseUser getUser() {
        return mUser;
    }

    public DatabaseReference getUserRefs() {
        return mUsersRef;
    }

    /**
     * If user has entered all required fields with valid data then their user details are saved into Firebase database
     * Once complete the user is automatically logged in and redirected to MainActivity
     */
    public Boolean validateRegistration(final Context context, final String email, String password, final String summonerName, final String region) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                if (task.isSuccessful()) {
                    mUserName = summonerName;
                    mUsersRef.child(mAuth.getUid()).setValue(new User(summonerName, region, email));
                    mUsersRef.child(mAuth.getUid()).child("email").setValue(email);
                    activity.startActivity(new Intent(context, MainActivity.class));
                }
            }
        });
        return false;
    }

    /**
     * Authenticates user by checking if email and password match and exist in Firebase
     * If user exists then logs them in and redirects them to MainActivity
     * Otherwise a warning is given to user to alert them their details do not match and they need to retry login
     *
     * @param context
     * @param email
     * @param password
     * @return
     */
    public Boolean validateLogin(final Context context, String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInUserWithEmail:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()) {
                            //mUserName = mUsersRef.child(mUser.getUid()).child("summonerName").getKey().toString();
                            activity.startActivity(new Intent(context, MainActivity.class));
                        }
                    }
                });
        return false;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String name) {
        mUserName = name;
    }





    /**
     * Update user info based off information provided by SettingsActivity
     *
     * @param username
     * @param region
     */
    public void updateUserInfo(String username, String region) {
        mUsersRef.child(mUser.getUid()).child("summonerName").setValue(username);
        mUsersRef.child(mUser.getUid()).child("region").setValue(region);
    }

    /**
     * On log out or destroy of app we call this function to end the instance
     */
    public void killInstance(){
        sInstance = null;
    }
}
