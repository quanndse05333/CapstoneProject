package com.koit.capstonproject_version_1.dao;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.koit.capstonproject_version_1.Controller.SharedPreferences.SharedPrefs;
import com.koit.capstonproject_version_1.Model.User;
import com.koit.capstonproject_version_1.View.LoginActivity;

public class UserDAO {

    private static UserDAO mInstance;
    private DatabaseReference databaseReference;



    public UserDAO() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public static UserDAO getInstance() {
        if (mInstance == null) {
            mInstance = new UserDAO();
        }
        return mInstance;
    }

    public String getUserID(){
        String UID = "";
        User user = SharedPrefs.getInstance().getCurrentUser(LoginActivity.CURRENT_USER);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            UID = user.getPhoneNumber();
        } else {
            UID = firebaseUser.getUid();
        }
        return UID;
    }
    public User getUser(){
        User user = new User();
        User currentUser = SharedPrefs.getInstance().getCurrentUser(LoginActivity.CURRENT_USER);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            user = currentUser;
        } else{
            user.setUserID(firebaseUser.getUid());
            user.setFullName(firebaseUser.getDisplayName());
            user.setEmail(firebaseUser.getEmail());
            user.setPhoneNumber("");
            user.setGender(true);
            user.setRoleID("1");
            user.setHasFingerprint(false);
            user.setStoreName("");
            user.setDateOfBirth(null);
        }
        return user;
    }
}
