package com.example.week5day1firebasechat2.view.activities;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.week5day1firebasechat2.model.Message;
import com.example.week5day1firebasechat2.model.MessageReceived;
import com.example.week5day1firebasechat2.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

public class MainActivityPresenter {

    MainActivityContract mainActivityContract;

    public MainActivityPresenter(MainActivityContract mainActivityContract) {
        this.mainActivityContract = mainActivityContract;
    }

    private static final String TAG = "FRANK: ";
    public static final String DATABASE = "week5day1firebasechat";
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    public void checkFirebaseUserNull(FirebaseUser fUser, User user) {
        if (fUser != null) {
            callDatabase(user);
        } else {
            mainActivityContract.makeToast("Please log in to send message.");
        }

    }


    public void callDatabase(final User user) {
        //THIS sets up the Firebase DB
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        //THIS replaces space characters with underscores.
        //This is done a json uses space characters as delimiters.
        user.setMessage(
                replaceSpacesWithUnderScores(user.getMessage()));

        //This adds the massage and user info to the database
        saveMessageToFirebaseDB(user);

        //THE event listener for the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //This will iterate through any responses from the database
                Iterator<DataSnapshot> iter = dataSnapshot.getChildren().iterator();

                while (iter.hasNext()) {
                    //GET the json string
                    String messageJson = iter.next().getValue().toString();
                    //LOG d
                    Log.d(TAG, "onDataChange: " + messageJson);
                    //CONVERT from json to class values
                    MessageReceived messageReceived
                            = new Gson().fromJson(messageJson, MessageReceived.class);
                    //REPLACE the underscores with spaces.
                    messageReceived.setMessage(
                            replaceUnderScoresWithSpaces(
                                    messageReceived.getMessage()
                            )
                    );
                    Log.d(TAG, "onDataChange: " + messageReceived.getMessage());
                    if (user.getKey().equals(messageReceived.getKey())) {
                        constructMessage(messageReceived);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError.toString());
            }
        });
    }

    private void saveMessageToFirebaseDB(User user) {
        String key = getKey();
        myRef.child(key).setValue(user);
        //THIS runs to update when database has changed.
        myRef.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TAG", "onDataChange: " + dataSnapshot.getKey() + " = " + dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //THIS method fro json limitations
    public String replaceSpacesWithUnderScores(String string) {
        return string.replace(" ", "_");
    }

    //THIS method to make readable again
    public String replaceUnderScoresWithSpaces(String string) {
        return string.replace("_", " ");
    }

    //CONSTRUCT the message to print out for the user.
    public void constructMessage(MessageReceived messageReceived) {
        String messageToPost = messageReceived.getMessage()
                + " -Sent by " + messageReceived.geteMail()
                + " at " + messageReceived.getTimeSent();
        Log.d(TAG, "constructMessage: " + messageToPost);
        mainActivityContract.postMessage(messageToPost);
    }


    public String getTimestamp() {
        DateFormat dateFormat = new SimpleDateFormat("HH_mm_ss");
        Date date = new Date();
        return dateFormat.format(date); //2016/11/16 12:08:43
    }

    public String getKey() {
        Random rnd = new Random();
        return rnd.nextInt() + "";
    }


}
