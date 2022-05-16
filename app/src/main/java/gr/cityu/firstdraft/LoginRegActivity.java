package gr.cityu.firstdraft;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;


public class LoginRegActivity extends AppCompatActivity {

    private static final String TAG = "LoginRegActivity";
    int AUTHUI_REQUEST_CODE = 10001;

    Button mButtonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_reg);
        mButtonLogout = findViewById(R.id.buttonLogout);

        //next line hides the action bar
        getSupportActionBar().hide();


        //checking if the user is currently singed in
        // if there is a user signed in we go back to the main activity
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            this.finish(); // the user is not able to go back to the login register activity
        }
    }

    public void handleLoginRegister(View view) {

        //adding the desired providers, google and email
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        Intent intent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTosAndPrivacyPolicyUrls("https://example.com", "https://example.com")

                .setAlwaysShowSignInMethodScreen(true)
                .setIsSmartLockEnabled(false)
                .build();

        startActivityForResult(intent, AUTHUI_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTHUI_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // We have signed in the user or we have a new user
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.d(TAG, "onActivityResult: " + user.toString());
                //Checking for User (New/Old)
                if (user.getMetadata().getCreationTimestamp() == user.getMetadata().getLastSignInTimestamp()) {
                    //This is a New User
                    Intent intent = new Intent(this, NewUserActivity.class);
                    startActivity(intent);
                    this.finish();

                } else {
                    //This is a returning user
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    this.finish();
                }



            } else {
                // Signing in failed
                IdpResponse response = IdpResponse.fromResultIntent(data);
                if (response == null) {
                    Log.d(TAG, "onActivityResult: the user has cancelled the sign in request");
                } else {
                    Log.e(TAG, "onActivityResult: ", response.getError());
                }
            }
        }
    }



}