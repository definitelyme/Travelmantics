package com.andela.brendan.travelmantics;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andela.brendan.travelmantics.Foundation.FirebaseUtil;
import com.andela.brendan.travelmantics.Fragments.FragmentInterface;
import com.andela.brendan.travelmantics.Model.TravelDeal;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class DealsAdapter extends RecyclerView.Adapter<DealHolder> implements ChildEventListener {
    private ArrayList<TravelDeal> travelDeals;
    private Activity activity;

    public DealsAdapter(Activity context) {
        this.activity = context;

        DatabaseReference dbRef = FirebaseUtil.getDatabaseNode(FragmentInterface.TRAVEL_DEAL_NODE, context);

        travelDeals = new ArrayList<>(); // This equals --> new ArrayList<>()

        dbRef.addChildEventListener(this); // Add Event Listener
    }

    @NonNull
    @Override
    public DealHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.deal_recycler, parent, false);
        return new DealHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DealHolder holder, int position) {
        TravelDeal deal = travelDeals.get(position);
        holder.bind(travelDeals, deal); // TODO: Make travelDeals static in DealHolder
    }

    @Override
    public int getItemCount() {
        return travelDeals.size();
    }

    // Automatically serialize the Class object with the Data retrieved from Firebase
    // Set the Id of the Object to the Id generated by Firebase
    // This will notify the App Service that the Item hss been inserted so that the user interface will be updated
    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        TravelDeal deal = dataSnapshot.getValue(TravelDeal.class); // Automatically serialize the Class object with the Data retrieved from Firebase
        deal.setId(dataSnapshot.getKey()); // Set the Id of the Object to the Id generated by Firebase
        travelDeals.add(deal);

        notifyItemInserted(travelDeals.size() - 1); // This will notify the App Service that the Item hss been inserted so that the user interface will be updated
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
