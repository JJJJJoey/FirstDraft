package gr.cityu.firstdraft;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    Button mButtonNextScreen;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonNextScreen = findViewById(R.id.buttonAccount);

        //checking if the user is singed in , if not the login register
        //activity will start
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            Intent intent = new Intent(this,LoginRegActivity.class);
            startActivity(intent);
            finish();
        }


        //getting user
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser=FirebaseAuth.getInstance().getCurrentUser();
        //Toast.makeText(this,"the current user is: "+currentUser,Toast.LENGTH_LONG).show();

        //getting userID
        String currentUserID= FirebaseAuth.getInstance().getUid();
        Toast.makeText(this,"the current user is: "+currentUserID,Toast.LENGTH_LONG).show();

    }




    public void nextActivity(View view){
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
    }
    public void logRegIntent(){
        Intent intent = new Intent(this, LoginRegActivity.class);
        startActivity(intent);
    }

}