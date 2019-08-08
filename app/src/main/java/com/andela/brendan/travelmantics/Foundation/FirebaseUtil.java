package com.andela.brendan.travelmantics.Foundation;

import android.app.Activity;
import android.widget.Toast;

import com.andela.brendan.travelmantics.Fragments.FragmentInterface;
import com.andela.brendan.travelmantics.Model.TravelDeal;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;

public class FirebaseUtil {
    private static final int RC_SIGN_IN = 123;
    public static FirebaseDatabase firebaseDatabase;
    public static DatabaseReference databaseReference;
    public static FirebaseStorage firebaseStorage;
    public static StorageReference storageReference;
    public static ArrayList<TravelDeal> travelDeals;
    public static FirebaseAuth firebaseAuth;
    public static FirebaseAuth.AuthStateListener authStateListener;
    private static FirebaseUtil util;
    private static Activity context;

    private FirebaseUtil() {
    }

    public static void openDatabaseNode(String reference, Activity activity) {
        context = activity; // Set the Caller Activity

        if (util == null) {
            util = new FirebaseUtil();
            firebaseDatabase = FirebaseDatabase.getInstance(); // FirebaseDatabase Instance
            firebaseAuth = FirebaseAuth.getInstance(); // FirebaseAuth Instance
            authStateListener = new FirebaseAuth.AuthStateListener() { // This will create an onAuthStateChangeListener
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser(); // Current User
                    if (user == null) {
                        FirebaseUtil.authenticate(); // Show Auth screen if user is not authenticated
                        Toast.makeText(context, "Welcome back " + firebaseAuth.getCurrentUser().getDisplayName() + "!", Toast.LENGTH_LONG).show();
                    }
                }
            };

            connectStorage();
        }

        travelDeals = new ArrayList<>();
        databaseReference = firebaseDatabase.getReference().child(reference);
    }

    @org.jetbrains.annotations.NotNull
    public static DatabaseReference getDatabaseNode(String reference, Activity activity) {
        context = activity; // Set the Caller Activity

        if (util == null) {
            util = new FirebaseUtil();
            firebaseDatabase = FirebaseDatabase.getInstance();
            travelDeals = new ArrayList<>();
        }

        return firebaseDatabase.getReference().child(reference);
    }

    private static void authenticate() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        context.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    private static void connectStorage() {
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child(FragmentInterface.TRAVEL_DEAL_FOLDER);
    }

    public static void attachAuthListener() {
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    public static void detachAuthListener() {
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}
