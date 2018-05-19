package com.mad.leaguebuddy.ViewModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
 * Created by Maleks on 19-May-18.
 */

public class FirebaseFactory {
    private static final String TAG = "TAG";
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private Activity activity;
    private FirebaseUser mUser;
    private String mRegion;
    private String mUserName;
    private static FirebaseFactory sInstance; //SINGLETON INSTANCE SAVES THE DAY

    private FirebaseFactory(Activity activity){
        this.activity = activity;
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        mRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public static synchronized FirebaseFactory getInstance(Activity activity){
        if(sInstance == null){
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

    public String getRegion() {
        return mRegion;
    }

    public String getUserName() {
        return mUserName;
    }

    /**
     * Method handles finding the correct user based on the provided snapshot of my Firebase Database
     * when found we save the username and region of the given user to be used in other activities
     * when needed
     * @param dataSnapshot
     */
    public void showData(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            if(ds.getKey().equals(mUser.getUid())){
                mUserName = ds.child("summonerName").getValue().toString();
                mRegion = ds.child("region").getValue().toString();
            }
        }
    }

    /**
     * If user has entered all required fields with valid data then their user details are saved into Firebase database
     * Once complete the user is automatically logged in and redirected to MainActivity
     */
    public Boolean validateRegistration(final Context context, String email, String password, final String summonerName, final String region) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                if(task.isSuccessful()){
                    DatabaseReference summonerRef = mDatabase.getReference("users");
                    summonerRef.child(mAuth.getUid()).setValue(new User(summonerName, region));
                    activity.startActivity(new Intent(context, MainActivity.class));
                }
            }
        });
        return false;
    }

    /**
     *  Authenticates user by checking if email and password match and exist in Firebase
     *  If user exists then logs them in and redirects them to MainActivity
     *  Otherwise a warning is given to user to alert them their details do not match and they need to retry login
     * @param context
     * @param email
     * @param password
     * @return
     */
    public Boolean validateLogin(final Context context, String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInUserWithEmail:onComplete:" + task.isSuccessful());
                        if(task.isSuccessful()){
                            activity.startActivity(new Intent(context, MainActivity.class));
                        }
                    }
                });
        return false;
    }
}
