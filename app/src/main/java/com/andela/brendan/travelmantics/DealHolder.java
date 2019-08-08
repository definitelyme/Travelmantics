package com.andela.brendan.travelmantics;

import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andela.brendan.travelmantics.Fragments.DealsFragmentDirections;
import com.andela.brendan.travelmantics.Model.TravelDeal;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class DealHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView dealTitle, dealDescription, dealPrice;
    private ImageView dealImage;
    private ArrayList<TravelDeal> dealArrayList;

    DealHolder(@NonNull View itemView) {
        super(itemView);

        dealTitle = itemView.findViewById(R.id.deal_title);
        dealPrice = itemView.findViewById(R.id.deal_price);
        dealDescription = itemView.findViewById(R.id.deal_description);
        dealImage = itemView.findViewById(R.id.deal_image);
        itemView.setOnClickListener(this);
    }

    void bind(ArrayList<TravelDeal> travelDeals, TravelDeal deal) {
        dealArrayList = travelDeals; // Set the deals Array

        dealTitle.setText(deal.getTitle());
        dealPrice.setText(deal.getPrice());
        dealDescription.setText(deal.getDescription());

        String stringUri = deal.getImageStringUri();
        if (!TextUtils.isEmpty(stringUri))
            displayImage(Uri.parse(stringUri));
    }

    @Override
    public void onClick(View v) {
        int position = getAdapterPosition();

        TravelDeal selectedDeal = dealArrayList.get(position);
        // Onclick, show edit fragment
        Navigation.findNavController(v).navigate(DealsFragmentDirections.actionDealsFragmentToNewDealFragment(selectedDeal),
                new NavOptions.Builder().setPopUpTo(R.id.dealsFragment, false).build());
    }

    private void displayImage(Uri uri) {
        if (uri != null) {
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.with(dealImage.getContext())
                    .load(uri)
                    .into(dealImage);
        }
    }
}
