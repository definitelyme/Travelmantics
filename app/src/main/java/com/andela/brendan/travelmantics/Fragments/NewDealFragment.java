package com.andela.brendan.travelmantics.Fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.andela.brendan.travelmantics.Foundation.FirebaseUtil;
import com.andela.brendan.travelmantics.Model.TravelDeal;
import com.andela.brendan.travelmantics.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentInterface} interface
 * to handle interaction events.
 */
public class NewDealFragment extends Fragment {

    private static final int STORAGE_REQUEST_CODE = 121;
    private DatabaseReference dealsReference;
    private FragmentInterface mListener;
    private AppCompatActivity activity;
    private ScrollView scrollView;
    private View container;
    private TravelDeal deal;

    private ImageView cruiseImage;
    private ImageButton selectImage;
    private Button saveCruise, deleteCruise;
    private EditText cruiseTitle, cruiseDescription, cruisePrice;
    private Uri IMAGE_URL;

    public NewDealFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dealsReference = FirebaseUtil.getDatabaseNode(FragmentInterface.TRAVEL_DEAL_NODE, activity);

        if (getArguments() != null)
            deal = NewDealFragmentArgs.fromBundle(getArguments()).getDeal(); // If updating an existing deal

        if (deal == null)
            deal = new TravelDeal();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_deal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        container = view; // Set the Root View

        scrollView = view.findViewById(R.id.fragment_new_deal);
        scrollView.setVerticalScrollBarEnabled(false);

        cruiseImage = view.findViewById(R.id.cruise_image_input);
        selectImage = view.findViewById(R.id.select_cruise_image_btn);
        cruiseTitle = view.findViewById(R.id.cruise_title_input);
        cruiseDescription = view.findViewById(R.id.cruise_description_input);
        cruisePrice = view.findViewById(R.id.cruise_price_input);
        saveCruise = view.findViewById(R.id.save_deal_btn);
        deleteCruise = view.findViewById(R.id.delete_deal_btn);

        if (deal.getId() != null)
            populateFields();

        selectImage.setOnClickListener(v -> imageIntent());

        cruiseImage.setOnClickListener(v -> {
            String dealImageName = deal.getImageName();
            imageIntent();
            deleteImage(dealImageName);
        });

        saveCruise.setOnClickListener(v -> attemptSave());

        deleteCruise.setOnClickListener(v -> attemptDelete());
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == STORAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            StorageReference storageRef = FirebaseUtil.storageReference.child(imageUri.getLastPathSegment());

            storageRef.putFile(imageUri).continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return storageRef.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                task.addOnSuccessListener(taskSnapshot -> {
                    IMAGE_URL = task.getResult();
                    String url = IMAGE_URL.toString();
                    String imageName = task.getResult().getPath();
                    deal.setImageStringUri(url);
                    deal.setImageName(imageName);
                    displayImage();
                });

                task.addOnFailureListener(e -> {
                    Log.i("snapshot", e.getMessage());
                });
            });
        }
    }

    private void attemptSave() {
        boolean cancel = false;
        View focusView = null;

        final String title = cruiseTitle.getText().toString();
        final String description = cruiseDescription.getText().toString();
        final String price = cruisePrice.getText().toString();

        if (TextUtils.isEmpty(title)) {
            cruiseTitle.setError(getString(R.string.required_field));
            focusView = cruiseTitle;
            cancel = true;
        }

        if (TextUtils.isEmpty(price)) {
            cruisePrice.setError(getString(R.string.required_field));
            focusView = cruisePrice;
            cancel = true;
        }

        if (cancel) focusView.requestFocus();
        else {
            saveDeal(title, description, price);
        }
    }

    private void attemptDelete() {
        dealsReference.child(deal.getId()).removeValue();
        String dealImageName = deal.getImageName();
        deleteImage(dealImageName);
        backToList();
    }

    private void deleteImage(String name) {
        if (name != null && !TextUtils.isEmpty(name)) {
            StorageReference picRef = FirebaseUtil.firebaseStorage.getReference().child(name);

            picRef.delete().addOnSuccessListener(aVoid -> {
                Log.i("delete", "File deleted!");
            }).addOnFailureListener(e -> {
                Log.i("delete:failure", e.getMessage());
            });
        }
    }

    private void saveDeal(String title, String desc, String price) {
        deal.setTitle(title);
        deal.setDescription(desc);
        deal.setPrice(price);

        if (deal.getId() == null) { // Create a New Deal
            String key = dealsReference.push().getKey();
            dealsReference.child(key).setValue(deal);
        } else { // Update Existing Deal
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference(FragmentInterface.TRAVEL_DEAL_NODE).child(deal.getId());
            String dealImageName = deal.getImageName();
            deleteImage(dealImageName);
            dbRef.setValue(deal);
        }

        cleanInput();
        backToList();
    }

    private void cleanInput() {
        cruiseTitle.setText("");
        cruisePrice.setText("");
        cruiseDescription.setText("");

        cruiseTitle.requestFocus();
    }

    private void backToList() {
        Navigation.findNavController(container).navigate(R.id.dealsFragment, null,
                new NavOptions.Builder()
                        .setPopUpTo(R.id.dealsFragment, true)
                        .build());
    }

    private void populateFields() {
        cruiseTitle.setText(deal.getTitle());
        cruiseDescription.setText(deal.getDescription());
        cruisePrice.setText(deal.getPrice());
        if (deal.getImageStringUri() != null) {
            IMAGE_URL = Uri.parse(deal.getImageStringUri());
            displayImage();
        }

        saveCruise.setText(getString(R.string.update_cruise_string));
        deleteCruise.setVisibility(View.VISIBLE);
    }

    private void imageIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Please Select Image"), STORAGE_REQUEST_CODE);
    }

    private void displayImage() {
        if (IMAGE_URL != null) {
            selectImage.setVisibility(View.GONE);
            cruiseImage.setVisibility(View.VISIBLE);

            cruiseImage.getLayoutParams().height = 250;
            cruiseImage.getLayoutParams().width = 400;
            cruiseImage.requestLayout();

            Picasso.with(activity)
                    .load(IMAGE_URL)
                    .into(cruiseImage);
        }
    }

    // Get the selected image file Extension from File Path URI.
    public String getFileExtension(Uri uri) {
        ContentResolver contentResolver = activity.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
