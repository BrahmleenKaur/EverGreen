package com.example.evergreen.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.evergreen.activities.*
import com.example.evergreen.model.Post
import com.example.evergreen.model.User
import com.example.evergreen.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity : SignUpActivity, userInfo: User) {
        mFireStore.collection(Constants.USERS)
            // Document ID for users fields. Here the document it is the User ID.
            .document(userInfo.uid)
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                // Here call a function of base activity for transferring the result to it.
                activity.userRegisteredSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error writing document",
                    e
                )
            }
    }


    fun loadUserData(activity: Activity, readBoardsList: Boolean = false) {

        // Here we pass the collection name from which we wants the data.
        mFireStore.collection(Constants.USERS)
            // The document id to get the Fields of user.
            .document(FirebaseAuthClass().getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())

                // Here we have received the document snapshot which is converted into the User Data model object.
                val loggedInUser = document.toObject(User::class.java)!!

                // Here call a function of base activity for transferring the result to it.
//                activity.signInSuccess(loggedInUser)

                 when (activity) {
                    is SignInActivity -> {
                        activity.signInSuccess(loggedInUser)
                    }
                    is MainActivity -> {
                        activity.updateNavigationUserDetails(loggedInUser, readBoardsList)
                    }
                    is EditProfileActivity -> {
                        activity.setUserDataInUI(loggedInUser)
                    }
                }
                
            }
            .addOnFailureListener { e ->
                // Here call a function of base activity for transferring the result to it.
//                  activity.hideProgressDialog()
                when (activity) {
                    is SignInActivity -> {
                        activity.hideProgressDialog()
                    }
                    is MainActivity -> {
                        activity.hideProgressDialog()
                    }
                    is EditProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting loggedIn user details",
                    e
                )
            }
    }


    /**
     * A function to update the user profile data into the database.
     */
    fun updateUserProfileData(activity: EditProfileActivity, userHashMap: HashMap<String, Any>) {
        mFireStore.collection(Constants.USERS) // Collection Name
            .document(FirebaseAuthClass().getCurrentUserID()) // Document ID
            .update(userHashMap) // A hashmap of fields which are to be updated.
            .addOnSuccessListener {
                // Profile data is updated successfully.
                Log.i(activity.javaClass.simpleName, "Profile Data updated successfully!")

                Toast.makeText(activity, "Profile updated successfully!", Toast.LENGTH_SHORT).show()

                // Notify the success result.
                activity.profileUpdateSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while creating a board.",
                    e
                )
            }
    }

    fun test(cityOfUser:String,newPost : Post, activity : Activity,createdByUserId : String){

        //add document(post) to collection(auto generated Id)
        mFireStore.collection(Constants.USERS)
                .add(newPost)
                .addOnSuccessListener { documentReference ->
                    //auto generated ID
                    val autoId = documentReference.id
                    Log.d("doc id", "DocumentSnapshot written with ID: ${autoId}")
                }
                .addOnFailureListener { e ->
                    Log.w("doc id", "Error adding document", e)
                }



        //retrieval using city name
        mFireStore.collection(Constants.USERS)
                // A where array query as we want the list of the board in which the user is assigned. So here you can pass the current user id.
                .whereEqualTo("name", cityOfUser)
                .get()
                .addOnSuccessListener { document ->
                    Log.e(activity.javaClass.simpleName, document.documents.toString())
                    var userArrayWithGivenCity : ArrayList<User> = ArrayList()
                    if (document.documents.size > 0) {

                        for (user in document.documents) {
                            val user = document.documents[0].toObject(User::class.java)!!
                            userArrayWithGivenCity.add(user)
                            Log.i("details",user.toString())
                        }

                    } else {

                    }

                }
                .addOnFailureListener { e ->
                    Log.e(
                            activity.javaClass.simpleName,
                            "Error while getting user details",
                            e
                    )
                }



        //retrieval using Doc Id (for updating purposes)
        mFireStore.collection(Constants.USERS)
                .document(createdByUserId)
                .update("city",cityOfUser)
    }



}