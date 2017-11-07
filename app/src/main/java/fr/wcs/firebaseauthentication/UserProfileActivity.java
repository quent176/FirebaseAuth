package fr.wcs.firebaseauthentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfileActivity extends AppCompatActivity {

    Button signout;
    private FirebaseAuth mAuth;
    TextView username;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userprofile_activity);

        mAuth = FirebaseAuth.getInstance(); // important call
        signout = (Button) findViewById(R.id.button_signout);
        username = (TextView) findViewById(R.id.display_username);

        //Again, check if user is already logged in or not
        if (mAuth.getCurrentUser() == null) {
            //User Not Logged In
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

        //Fetch the display name of current user
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            username.setText("Welcome, " + user.getDisplayName());
        }

        //TODO Handle Onbackpressed

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                finish();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

    }
}
