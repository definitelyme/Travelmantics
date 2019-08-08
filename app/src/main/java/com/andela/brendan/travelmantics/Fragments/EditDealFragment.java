package com.andela.brendan.travelmantics.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andela.brendan.travelmantics.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentInterface} interface
 * to handle interaction events.
 */
public class EditDealFragment extends Fragment {
    private static final String TRAVEL_DEAL_NODE = "traveldeals";
    private FragmentInterface mListener;
    private AppCompatActivity activity;

    public EditDealFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_deal, container, false);
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
}
