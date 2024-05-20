package com.hasan.storyvibrance.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PostModel {
    private String postId; //Post Id
    private String authorName; // Name of the author
    private String authorUsername; // Name of the author
    private String authorImg; // URL or path of the author's profile image
    private String postTextContent; // Content of the post
    private String postMedia; // URL or path of the post media (image or video)
//    private List<LikeModel> likes; // List of likes on the post
    private List<Map<String, Object>> likes; // List of likes on the post
    private List<CommentModel> comments; // List of comments on the post
    private String timestamp; // Timestamp of when the post was created
    private List<PostSaveUserIdFireBase> postSaved; // List of postSaved on the post

    // Constructors, getters, and setters



    public PostModel() {

    }



    public PostModel(String postId, String authorName, String authorUsername, String postTextContent,
                     String authorImg, String postMedia, List<Map<String, Object>> likes,
                     List<CommentModel> comments, String timestamp, List<PostSaveUserIdFireBase> postSaved) {
        this.authorUsername = authorUsername;
        this.postId = postId;
        this.authorName = authorName;
        this.postTextContent = postTextContent;
        this.authorImg = authorImg;
        this.postMedia = postMedia;
        this.likes = likes;
        this.comments = comments;
        this.timestamp = timestamp;
        this.postSaved = postSaved;
    }

    public List<PostSaveUserIdFireBase> getPostSaved() {
        return postSaved;
    }

    public void setPostSaved(List<PostSaveUserIdFireBase> postSaved) {
        this.postSaved = postSaved;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }


    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }


    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public String getPostTextContent() {
        return postTextContent;
    }

    public void setPostTextContent(String postTextContent) {
        this.postTextContent = postTextContent;
    }

    public String getAuthorImg() {
        return authorImg;
    }

    public void setAuthorImg(String authorImg) {
        this.authorImg = authorImg;
    }

    public String getPostMedia() {
        return postMedia;
    }

    public void setPostMedia(String postMedia) {
        this.postMedia = postMedia;
    }

//    public List<LikeModel> getLikes() {
//        return likes;
//    }
//    public List<LikeModel> getLikes() {
//        return likes != null ? likes : Collections.emptyList(); // Return an empty list if 'likes' is null
//    }
//    public void setLikes(List<LikeModel> likes) {
//        this.likes = likes;
//    }

    public List<CommentModel> getComments() {
        return comments;
    }

    public void setComments(List<CommentModel> comments) {
        this.comments = comments;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
//
//    /**
//     * Adds a like to the post.
//     * @param like The LikeModel object representing the like to be added.
//     */
//    public void addLike(LikeModel like) {
//        if (likes == null) {
//            likes = new ArrayList<>();
//        }
//        likes.add(like);
//    }
//
//    /**
//     * Removes a like from the post.
//     * @param userId The ID of the user whose like is to be removed.
//     */
//    public void removeLike(String userId) {
//        if (likes != null) {
//            for (Iterator<LikeModel> iterator = likes.iterator(); iterator.hasNext();) {
//                LikeModel like = iterator.next();
//                if (like.getUserId().equals(userId)) {
//                    iterator.remove();
//                    break;
//                }
//            }
//        }
//    }
//
//    /**
//     * Checks if the post is liked by a specific user.
//     * @param userId The ID of the user to check for liking the post.
//     * @return True if the post is liked by the specified user, otherwise false.
//     */
//    public boolean isLikedByUser(String userId) {
//        if (likes != null) {
//            for (LikeModel like : likes) {
//                if (like.getUserId().equals(userId)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }


    public List<Map<String, Object>> getLikes() {
        return likes != null ? likes : new ArrayList<>(); // Return an empty list if 'likes' is null
    }

    public void setLikes(List<Map<String, Object>> likes) {
        this.likes = likes;
    }



    // Add or remove like methods
    public void addLike(LikeModel like) {
        if (this.likes == null) {
            this.likes = new ArrayList<>();
        }
        this.likes.add(like.toMap());
    }

    public void removeLike(String userId) {
        if (this.likes != null) {
            this.likes.removeIf(likeMap -> userId.equals(likeMap.get("userId")));
        }
    }

    public boolean isLikedByUser(String userId) {
        if (this.likes != null) {
            for (Map<String, Object> likeMap : this.likes) {
                if (userId.equals(likeMap.get("userId"))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets the total number of likes on the post.
     * @return The total number of likes on the post.
     */
    public int getLikeCount() {
        return likes != null ? likes.size() : 0;
    }



    // post saved

    public void addSaved(PostSaveUserIdFireBase saved) {
        if (postSaved == null) {
            postSaved = new ArrayList<>();
        }
        postSaved.add(saved);
    }

    public void removeSavePost(String userId) {
        if (postSaved != null) {
            for (Iterator<PostSaveUserIdFireBase> iterator = postSaved.iterator(); iterator.hasNext();) {
                PostSaveUserIdFireBase savedPost = iterator.next();
                if (savedPost.getUserId().equals(userId)) {
                    iterator.remove();
                    break;
                }
            }
        }
    }

    public boolean isPostSavedByUser(String userId) {
        if (postSaved != null) {
            for (PostSaveUserIdFireBase savePost : postSaved) {
                if (savePost.getUserId().equals(userId)) {
                    return true;
                }
            }
        }
        return false;
    }





}
