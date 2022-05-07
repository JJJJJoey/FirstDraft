package gr.cityu.firstdraft;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity implements RecyclerViewInterface{
    private Button mButtonNextScreen;
    private FirebaseAuth mAuth;

    private DatabaseReference mDatabaseRef;
    private ImageAdapter mAdapter;

    private RecyclerView mRecyclerViewMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setContentView(R.layout.activity_main);
        mButtonNextScreen = findViewById(R.id.buttonAccount);
        mRecyclerViewMain = findViewById(R.id.recyclerViewMain);
        mRecyclerViewMain.setLayoutManager(new LinearLayoutManager(this));

        //checking if the user is singed in , if not the login register
        //activity will start
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            Intent intent = new Intent(this,LoginRegActivity.class);
            startActivity(intent);
            finish();
        }


        //getting current user
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser=FirebaseAuth.getInstance().getCurrentUser();
        //Toast.makeText(this,"the current user is: "+currentUser,Toast.LENGTH_LONG).show();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        //getting userID
        String currentUserID= FirebaseAuth.getInstance().getUid();
        Toast.makeText(this,"the current user is: "+currentUserID,Toast.LENGTH_LONG).show();


        //Recycler View
        FirebaseRecyclerOptions<ItemImageUpload> options=
                new FirebaseRecyclerOptions.Builder<ItemImageUpload>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("item info all"),ItemImageUpload.class)
                        .build();

        mAdapter = new ImageAdapter(options,this);
        mRecyclerViewMain.setAdapter(mAdapter);


    }

    public void nextActivity(View view){
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
    }
    public void logRegIntent(){
        Intent intent = new Intent(this, LoginRegActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    /*@Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }*/

    @Override
    public void onItemClick(String id, int position) {

    }
}