package com.hasan.storyvibrance.Utility.PostAdapterUtils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class DeletePostUtils {
    public static void deletePostFromFirebase(String postId) {

         FirebaseFirestore.getInstance().collection("posts").document(postId).delete()
             .addOnSuccessListener(new OnSuccessListener<Void>() {
                 @Override
                 public void onSuccess(Void aVoid) {
                     Log.d("DeletePostUtils", "Post deleted successfully");
                 }
             })
             .addOnFailureListener(new OnFailureListener() {
                 @Override
                 public void onFailure(@NonNull Exception e) {
                     Log.e("DeletePostUtils", "Error deleting post: " + e.getMessage(), e);
                 }
             });
    }
}
