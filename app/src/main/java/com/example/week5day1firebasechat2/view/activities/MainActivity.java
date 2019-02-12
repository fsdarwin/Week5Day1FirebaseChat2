package com.example.week5day1firebasechat2.view.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.week5day1firebasechat2.R;
import com.example.week5day1firebasechat2.model.Message;
import com.example.week5day1firebasechat2.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity implements MainActivityContract{
    FirebaseAuth firebaseAuth;
    FirebaseUser fUser;
    EditText etEmail;
    EditText etPassword;
    EditText etMessage;
    TextView tvLogInStatus;
    TextView tvDisplayMessage;
    MainActivityPresenter mainActivityPresenter;
    String etVEmail;
    String etVPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivityPresenter = new MainActivityPresenter(this);

        FirebaseApp.initializeApp(this);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etMessage = findViewById(R.id.etMessage);
        tvDisplayMessage = findViewById(R.id.tvDisplayMessage);
        tvLogInStatus = findViewById(R.id.tvLogResult);

    }

    public void onClick(View view) {
        firebaseAuth = FirebaseAuth.getInstance();
        String email = etEmail.getText() != null ? etEmail.getText().toString() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString() : "";
        switch (view.getId()) {
            case R.id.btnSignIn:
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("TAG", "signInWithEmail:success");
                                    fUser = firebaseAuth.getCurrentUser();
                                    updateUI(fUser);
                                    etVEmail = etEmail.getText().toString();
                                    etVPassword = etPassword.getText().toString();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("TAG", "signInWithEmail:failure", task.getException());

                                    updateUI(null);
                                }

                            }
                        });
                break;
            case R.id.btnSignUp:
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("TAG", "createUserWithEmail:success");
                                    fUser = firebaseAuth.getCurrentUser();
                                    updateUI(fUser);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                    updateUI(null);
                                }
                            }
                        });
                break;
            case R.id.btnSendMessage:
                User user = new User(etVEmail, etVPassword,
                        mainActivityPresenter.getKey(),
                        mainActivityPresenter.getTimestamp(),
                        etMessage.getText().toString());
                mainActivityPresenter.checkFirebaseUserNull(fUser, user);
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            tvLogInStatus.setText("USER " + user.getEmail() + " is logged IN");
        } else {
            tvLogInStatus.setText("USER " + etEmail.getText().toString() + " FAILED TO LOGIN");
        }
    }
    public void makeToast(String toastMessage){
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
    }

    public void postMessage(String message){
        tvDisplayMessage.setText(message);
    }
}