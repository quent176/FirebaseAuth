package fr.wcs.firebaseauthentication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText name, email, password;
    private Button signin, signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        mAuth = FirebaseAuth.getInstance(); // important call

        signin = (Button) findViewById(R.id.button_signin);
        signup = (Button) findViewById(R.id.button_signup);
        name = (EditText) findViewById(R.id.input_name);
        email = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);

        //Check if User Is Already Logged In

        if (mAuth.getCurrentUser() != null) {
            //User Not Logged In
            finish();
            startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
        }

        //TODO Add email verification
        //TODO Add login with other services
        //TODO Use FirebaseUI

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getEmail = email.getText().toString().trim();
                String getPassword = password.getText().toString().trim();
                callSignIn(getEmail, getPassword);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getEmail = email.getText().toString().trim();
                String getPassword = password.getText().toString().trim();
                callSignUp(getEmail, getPassword);

            }
        });

    }

    //Create Account
    private void callSignUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("TESTING", "Sign Up Sucessful:" + task.isSuccessful());

                        // If sign in fails, display a message to the user.
                        // If Sign in success, the auth state will be notified and logic to handle
                        // the signed in user can be handled in the listener.

                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Signed up failed.",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            userProfile();
                            Toast.makeText(LoginActivity.this, "Account created.",
                                    Toast.LENGTH_SHORT).show();
                            Log.d("TESTING", "Created Account");
                        }
                    }
                });
    }

    //Set UserDisplay Name
    private void userProfile() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name.getText().toString().trim())
                    //.setPhotoUri(Uri.parse("https://google.com/image.png"))
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("TESTING", "User Profile Updated");
                            }
                        }
                    });

        }
    }

    //Now Start SignIn Process
    //Sign In Process
    private void callSignIn(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("TESTING", "Sign In Sucessful:" + task.isSuccessful());

                        // If sign in fails, display a message to the user.
                        // If Sign in success, the auth state will be notified and logic to handle
                        // the signed in user can be handled in the listener.

                        if (!task.isSuccessful()) {
                            Log.v("TESTING", "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed. User/Password incorrect.",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Intent i = new Intent(LoginActivity.this, UserProfileActivity.class);
                            finish();
                            startActivity(i);
                        }

                    }
                });
    }

}
