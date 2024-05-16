package com.hasan.storyvibrance.Posts;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hasan.storyvibrance.Controller.FriendSearchAdapter;
import com.hasan.storyvibrance.Model.FriendSearchModel;
import com.hasan.storyvibrance.R;
import com.hasan.storyvibrance.databinding.ActivityAddFriendBinding;

import java.util.ArrayList;
import java.util.List;

public class AddFriendActivity extends AppCompatActivity {

    private ActivityAddFriendBinding binding;
    private FirebaseFirestore db;
    private FriendSearchAdapter friendSearchAdapter;
    private List<FriendSearchModel> searchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_friend);
        db = FirebaseFirestore.getInstance();

        setupSearchEditText();
        setupRecyclerView();
    }

    private void setupSearchEditText() {
        binding.editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed for this implementation
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not needed for this implementation
            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = s.toString().trim();
                if (searchText.length() >= 2) {
                    searchFriendsByName(searchText.toLowerCase()); // Convert search text to lower case
                }
            }
        });
    }

    private void setupRecyclerView() {
        searchResults = new ArrayList<>();
        friendSearchAdapter = new FriendSearchAdapter(searchResults);
        binding.recyclerViewSearchResults.setAdapter(friendSearchAdapter);
    }

//    private void searchFriendsByName(String searchQuery) {
//        db.collection("userdata").whereEqualTo("name", searchQuery)
//                .get()
//                .addOnSuccessListener(this::handleSearchSuccess)
//                .addOnFailureListener(this::handleSearchFailure);
//    }

//    private void searchFriendsByName(String searchQuery) {
//        db.collection("userdata").whereGreaterThanOrEqualTo("name", searchQuery)
//                .whereLessThan("name", searchQuery + "\uf8ff")
//                .get()
//                .addOnSuccessListener(this::handleSearchSuccess)
//                .addOnFailureListener(this::handleSearchFailure);
//    }

    private void searchFriendsByName(String searchQuery) {
        // Convert the search query to lowercase
        String lowercaseQuery = searchQuery.toLowerCase();

        db.collection("userdata")
                .whereGreaterThanOrEqualTo("name", lowercaseQuery)
                .whereLessThanOrEqualTo("name", lowercaseQuery + "\uf8ff")
                .get()
                .addOnSuccessListener(this::handleSearchSuccess)
                .addOnFailureListener(this::handleSearchFailure);
    }






    private void handleSearchSuccess(QuerySnapshot queryDocumentSnapshots) {
        searchResults.clear();
        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
            FriendSearchModel friendSearchModel = document.toObject(FriendSearchModel.class);
            searchResults.add(friendSearchModel);
            Log.d("Fetch Friend data", "FriendSearchModel: " + friendSearchModel.toString());
        }
        friendSearchAdapter.notifyDataSetChanged();
    }

    private void handleSearchFailure(@NonNull Exception e) {
        Toast.makeText(AddFriendActivity.this, "Failed to search for friends", Toast.LENGTH_SHORT).show();
    }
}
