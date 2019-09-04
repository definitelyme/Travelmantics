package com.andela.brendan.travelmantics;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.andela.brendan.travelmantics.Foundation.FirebaseUtil;
import com.andela.brendan.travelmantics.Fragments.FragmentInterface;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class DealsActivity extends AppCompatActivity implements FragmentInterface {

    private AppBarConfiguration appBarConfiguration;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deals);

        Toolbar toolbar = findViewById(R.id.toolbar);
        navController = Navigation.findNavController(this, R.id.host_fragment);

        setSupportActionBar(toolbar);
        setupActionBar();

        FirebaseUtil.openDatabaseNode(FragmentInterface.TRAVEL_DEAL_NODE, this);

        MobileAds.initialize(this, initializationStatus -> sendToast(DealsActivity.this, "Ads Initialized!"));
        MobileAds.initialize(this, AD_APP_ID);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUtil.attachAuthListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseUtil.detachAuthListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_deals, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setupActionBar() {
        appBarConfiguration = new AppBarConfiguration.Builder().build();
    }

    private void logout() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i("logout-user", "User Logged out successfully");
                        FirebaseUtil.attachAuthListener(); // This will be called if the User id not Logged in
                    }
                });
        FirebaseUtil.detachAuthListener();
    }
}
