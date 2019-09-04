package com.andela.brendan.travelmantics.Fragments;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public interface FragmentInterface {
    String TRAVEL_DEAL_NODE = "traveldeals";
    String TRAVEL_DEAL_FOLDER = "travel_deals";

    String INTERSTITIAL_AD_1 = "ca-app-pub-5022917256332506/3637505299";
    String INTERSTITIAL_AD_2 = "ca-app-pub-5022917256332506/1818099119";
    String INTERSTITIAL_AD_3 = "ca-app-pub-5022917256332506/9365416642";
    String INTERSTITIAL_AD_4 = "ca-app-pub-5022917256332506/6739253307";
    String INTERSTITIAL_AD_5 = "ca-app-pub-5022917256332506/9173844958";

    String AD_APP_ID = "ca-app-pub-5022917256332506~8925490997";

    default void sendToast(Activity context, String msg) {
        Toast.makeText(context, msg != null && !TextUtils.isEmpty(msg) ? msg : "No Message", Toast.LENGTH_LONG).show();
    }

    default void sendSnackbar(View rootView, String msg) {
        Snackbar sn = Snackbar.make(rootView, msg != null && !TextUtils.isEmpty(msg) ? msg : "No Message", BaseTransientBottomBar.LENGTH_LONG);
        sn.setAction("Okay", v -> sn.dismiss());
        sn.show();
    }
}
