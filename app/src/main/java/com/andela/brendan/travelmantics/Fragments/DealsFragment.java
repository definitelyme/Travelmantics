package com.andela.brendan.travelmantics.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andela.brendan.travelmantics.DealsAdapter;
import com.andela.brendan.travelmantics.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentInterface} interface
 * to handle interaction events.
 */
public class DealsFragment extends Fragment {

    private FragmentInterface mListener;
    private AppCompatActivity activity;

    private RecyclerView dealsRecycler;

    public DealsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_deals, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dealsRecycler = view.findViewById(R.id.travel_deals_recycler);

        FloatingActionButton fab = view.findViewById(R.id.addNewDeals);
        fab.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(DealsFragmentDirections.actionDealsFragmentToNewDealFragment(null),
                        new NavOptions.Builder().setPopUpTo(R.id.dealsFragment, false).build()));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        activity = (AppCompatActivity) getActivity();

        if (context instanceof FragmentInterface) mListener = (FragmentInterface) context;
        else throw new RuntimeException(context.toString()
                + " must implement FragmentInterface");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        DealsAdapter dealsAdapter = new DealsAdapter(activity); // Call Deals Adapter
        dealsRecycler.setAdapter(dealsAdapter); // Set Adapter

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, RecyclerView.VERTICAL, false);
        dealsRecycler.setLayoutManager(layoutManager);
    }
}
