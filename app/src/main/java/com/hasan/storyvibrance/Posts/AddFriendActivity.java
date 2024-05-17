package com.hasan.storyvibrance.Posts;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
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

        //return back to main page
        binding.backBtn.setOnClickListener(v->{
            getOnBackPressedDispatcher().onBackPressed();
        });

        setupViews();


    }

    private void setupViews() {
        binding.recyclerViewSearchResults.setLayoutManager(new LinearLayoutManager(this));
        searchResults = new ArrayList<>();
        friendSearchAdapter = new FriendSearchAdapter(this, searchResults);
        binding.recyclerViewSearchResults.setAdapter(friendSearchAdapter);

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
                if (searchText.length() < 2) {
                    clearSearchResults();
                } else {
                    searchFriendsByPattern(searchText);
                }
            }

            private void clearSearchResults() {
                searchResults.clear();
                friendSearchAdapter.notifyDataSetChanged();
            }
        });
    }

    private void searchFriendsByPattern(String pattern) {
        db.collection("userdata")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<FriendSearchModel> filteredResults = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        FriendSearchModel friendSearchModel = document.toObject(FriendSearchModel.class);
                        if (matchesPattern(friendSearchModel.getName(), pattern)) {
                            filteredResults.add(friendSearchModel);
                        }
                    }
                    updateRecyclerView(filteredResults);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddFriendActivity.this, "Failed to search for friends", Toast.LENGTH_SHORT).show();
                });
    }

    private boolean matchesPattern(String name, String pattern) {
        if (name == null || name.isEmpty()) {
            return false;
        }
        name = name.toLowerCase();
        pattern = pattern.toLowerCase();
        for (int i = 0; i <= name.length() - pattern.length(); i++) {
            if (name.substring(i, i + pattern.length()).contains(pattern)) {
                return true;
            }
        }
        return false;
    }


    private void updateRecyclerView(List<FriendSearchModel> searchResults) {
        this.searchResults.clear();
        this.searchResults.addAll(searchResults);
        friendSearchAdapter.notifyDataSetChanged();
    }
}
